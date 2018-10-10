package com.bzhang.mmall.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.TradeStatus;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.Utils;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.dao.CartMapper;
import com.bzhang.mmall.dao.OrderItemMapper;
import com.bzhang.mmall.dao.OrderMapper;
import com.bzhang.mmall.dao.PayInfoMapper;
import com.bzhang.mmall.dao.ProductMapper;
import com.bzhang.mmall.dao.ShippingMapper;
import com.bzhang.mmall.pojo.Cart;
import com.bzhang.mmall.pojo.Order;
import com.bzhang.mmall.pojo.OrderItem;
import com.bzhang.mmall.pojo.PayInfo;
import com.bzhang.mmall.pojo.Product;
import com.bzhang.mmall.pojo.Shipping;
import com.bzhang.mmall.service.CartService;
import com.bzhang.mmall.service.OrderService;
import com.bzhang.mmall.util.BigDecimalUtil;
import com.bzhang.mmall.util.DateTimeUtil;
import com.bzhang.mmall.util.FtpUtil;
import com.bzhang.mmall.util.PropertiesUtil;
import com.bzhang.mmall.vo.CartProductVo;
import com.bzhang.mmall.vo.CartVo;
import com.bzhang.mmall.vo.OrderItemVo;
import com.bzhang.mmall.vo.OrderProductVo;
import com.bzhang.mmall.vo.OrderVo;
import com.bzhang.mmall.vo.ShippingVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.zxing.common.detector.MathUtils;

import ch.qos.logback.core.encoder.ObjectStreamEncoder;


@Service("orderService")
public class OrderServiceImpl implements OrderService{
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ShippingMapper shippingMapper;
	
	@Autowired
	private PayInfoMapper payInfoMapper;
	
	
	private static AlipayTradeService   tradeService;
	static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

       
    }
	
	
	@Override
	public ServerResponse pay(Integer userId, String path, Long orderNo) {
		
		Map<String , String> map=Maps.newHashMap();
		Order order = orderMapper.selectOrderByOrderNoAndUserId(userId, orderNo);
		if (order==null) {
			
			return ServerResponse.createByErrorMsg("用户不存在该订单");
		}
		map.put("orderNo", order.getOrderNo().toString());
		
		 // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("happy_mmall扫码支付，订单号：").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单：").append(outTradeNo).append("中的商品价格共计：").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        
        List<OrderItem> orderItemList = orderItemMapper.selectOrderItemByUserIdAndOrderNo(userId, orderNo);
        for (OrderItem orderItem : orderItemList) {
        	 // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getOrderNo().toString(), orderItem.getProductName(), BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), 100.00).longValue(), orderItem.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
		}
        
       

       /* // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        goodsDetailList.add(goods2);*/

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
            .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
            .setGoodsDetailList(goodsDetailList);
        
        
       
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                File nfile=new File(path);
                if (!nfile.exists()) {
					nfile.setWritable(true);
					nfile.mkdirs();
				}

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path+"/qr-%s.png",response.getOutTradeNo());
                String qrFileName=String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                
                File targetFile=new File(path, qrFileName);
                try {
                	FtpUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
 
                	logger.error("二维码上传异常",e);
                }
                
                logger.info("qrPath:" + qrPath);
                String qrUrl=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                map.put("qrUrl", qrUrl);
                return ServerResponse.createBySuccess(map);
                //                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

            case FAILED:
            	logger.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMsg("支付宝预下单失败!!!");

            case UNKNOWN:
            	logger.error("系统异常，预下单状态未知!!!");
            	return ServerResponse.createByErrorMsg("系统异常，预下单状态未知!!!");

            default:
            	logger.error("不支持的交易状态，交易返回异常!!!");
            	return ServerResponse.createByErrorMsg("不支持的交易状态，交易返回异常!!!");
        }
		
	}
	
	// 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

	@Override
	public ServerResponse alipayCallBack(Map<String, String> params) {

		Long orderNo = Long.parseLong(params.get("out_trade_no"));
		String tradeNo=params.get("trade_no");
		String tradeStatus=params.get("trade_status");
		Order order = orderMapper.selectOrderByOrderNo(orderNo);
		if (order==null) {
			return ServerResponse.createByErrorMsg("订单不存在，非本站订单，无需回复");
		}
		
		if (order.getStatus()>=Const.OrderStatusEnum.PAID.getCode()) {
			return ServerResponse.createBySuccessMsg("支付宝重复调用");
			
		}
		if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
			order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
			order.setStatus(Const.OrderStatusEnum.PAID.getCode());
			orderMapper.updateByPrimaryKey(order);
		}
		PayInfo payInfo=new PayInfo();
		payInfo.setOrderNo(order.getOrderNo());
		payInfo.setPlatformStatus(tradeStatus);
		payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
		payInfo.setPlatformNumber(tradeNo);
		
		int res = payInfoMapper.insert(payInfo);
		if (res>0) {
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByErrorMsg("生成支付信息失败！");
		
	}

	@Override
	public ServerResponse<Boolean> queryOrderPayStatus(Integer userId, Long orderNo) {
		Order order = orderMapper.selectOrderByOrderNoAndUserId(userId, orderNo);
		if (order==null) {
			return ServerResponse.createByErrorMsg("用户没有该订单");
		}
		if (order.getStatus()>=Const.OrderStatusEnum.PAID.getCode()) {
			return ServerResponse.createBySuccess("已支付",true);
		}else {
			
			// (必填) 商户订单号，通过此商户订单号查询当面付的交易状态
	        String outTradeNo = order.getOrderNo().toString();

	        // 创建查询请求builder，设置请求参数
	        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
	            .setOutTradeNo(outTradeNo);

	        AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
	        switch (result.getTradeStatus()) {
	            case SUCCESS:
	                logger.info("查询返回该订单支付成功: )");

	                AlipayTradeQueryResponse response = result.getResponse();
	                dumpResponse(response);
	                
	                String tradeNo = response.getTradeNo();
	                String tradeStatus = response.getTradeStatus();
	                
	                
	    			order.setStatus(Const.OrderStatusEnum.PAID.getCode());
	    			orderMapper.updateByPrimaryKey(order);
	                
	    			PayInfo payInfo=new PayInfo();
	    			payInfo.setOrderNo(order.getOrderNo());
	    			payInfo.setPlatformStatus(tradeStatus);
	    			payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
	    			payInfo.setPlatformNumber(tradeNo);
	    			
	    			int res = payInfoMapper.insert(payInfo);

	                logger.info(response.getTradeStatus());
	                if (Utils.isListNotEmpty(response.getFundBillList())) {
	                    for (TradeFundBill bill : response.getFundBillList()) {
	                    	logger.info(bill.getFundChannel() + ":" + bill.getAmount());
	                    }
	                }
	                return ServerResponse.createBySuccess("查询返回该订单支付成功: )",true);

	            case FAILED:
	            	logger.error("查询返回该订单支付失败或被关闭!!!");
	            	return ServerResponse.createByErrorMsg("查询返回该订单支付失败或被关闭!!!");

	            case UNKNOWN:
	            	logger.error("系统异常，订单支付状态未知!!!");
	            	return ServerResponse.createByErrorMsg("系统异常，订单支付状态未知!!!");


	            default:
	            	logger.error("不支持的交易状态，交易返回异常!!!");
	            	return ServerResponse.createByErrorMsg("不支持的交易状态，交易返回异常!!!");

	        }
			
			
		}
	}

	@Override
	public ServerResponse createOrder(Integer userId, Integer shippingId) {
		if (userId==null||shippingId==null) {
			return ServerResponse.createByErrorMsg("参数错误");
		}
		//List<Cart> cartList=cartMapper.selectByuserIdAndChecked(userId, Const.Cart.CART_CHECKED);
		List<CartProductVo> cartProductVolist = cartService.listCart(userId).getData().getCartProductVolist();
		
		
		Long createOrderNo = createOrderNo();
		ServerResponse<List<OrderItem>> createOrderItem = createOrderItem(cartProductVolist,createOrderNo);
		if (!createOrderItem.isSuccess()) {
			return createOrderItem;
		}
		BigDecimal payment=getOrderPayment(createOrderItem.getData());
		Order order=assembleOrder(userId,shippingId,payment,createOrderNo);
		if (order==null) {
			return ServerResponse.createByErrorMsg("生成订单错误");
		}
		List<OrderItem> orderItemList = createOrderItem.getData();
		orderItemMapper.batchInsert(orderItemList);
		
		//修改库存
		changeProductStock(orderItemList,Const.ChangeStock.REDUCE_STOCK);
		
		//清空购物车中已下单
		removeCartOrder(userId,orderItemList);
		
		OrderVo orderVo = assembleOrderVo(order, orderItemList);
		
		return ServerResponse.createBySuccess(orderVo);
	}
	
	private OrderVo assembleOrderVo(Order order,List<OrderItem> orderItemList) {
		OrderVo orderVo=new OrderVo();
		orderVo.setOrderNo(order.getOrderNo());
		orderVo.setPayment(order.getPayment());
		orderVo.setPaymentType(order.getPaymentType());
		if (order.getPaymentType()==Const.PayPlatformEnum.ALIPAY.getCode()) {
			orderVo.setPaymentTypeDesc(Const.PayPlatformEnum.ALIPAY.getValue());
		}else {
			orderVo.setPaymentTypeDesc(Const.PayPlatformEnum.WECHATPAY.getValue());
		}
		
		orderVo.setPostage(order.getPostage());
		orderVo.setStatus(order.getStatus());
		orderVo.setStatusDesc(Const.OrderStatusEnum.getDesc(order.getStatus()));
		orderVo.setShippingId(order.getShippingId());
		
		Shipping shipping=shippingMapper.selectByPrimaryKey(order.getShippingId());
		
		if (shipping!=null) {
			ShippingVo shippingVo = getShippingVo(shipping);
			
			orderVo.setShippingVo(shippingVo);
			
			orderVo.setReceiverName(shipping.getReceiverName());
		}
		
		orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
		orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));
		orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
		orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
		orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
		
		orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		
		
		
		List<OrderItemVo> orderItemVoList = getOrderItemVoList(orderItemList);
		
		orderVo.setOrderItemVoList(orderItemVoList);
		
		return orderVo;
		
	}

	private List<OrderItemVo> getOrderItemVoList(List<OrderItem> orderItemList) {
		List<OrderItemVo> orderItemVoList=Lists.newArrayList();
		
		for (OrderItem orderItem : orderItemList) {
			OrderItemVo orderItemVo=new OrderItemVo();
			orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
			orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
			orderItemVo.setOrderNo(orderItem.getOrderNo());
			orderItemVo.setProductId(orderItem.getProductId());
			orderItemVo.setProductImage(orderItem.getProductImage());
			orderItemVo.setProductName(orderItem.getProductName());
			orderItemVo.setQuantity(orderItem.getQuantity());
			orderItemVo.setTotalPrice(orderItem.getTotalPrice());
			orderItemVoList.add(orderItemVo);
		}
		return orderItemVoList;
		
	}

	private ShippingVo getShippingVo(Shipping shipping) {
		ShippingVo shippingVo=new ShippingVo();
		shippingVo.setReceiverAddress(shipping.getReceiverAddress());
		shippingVo.setReceiverCity(shipping.getReceiverCity());
		shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
		shippingVo.setReceiverMobile(shipping.getReceiverMobile());
		shippingVo.setReceiverName(shipping.getReceiverName());
		shippingVo.setReceiverPhone(shipping.getReceiverPhone());
		shippingVo.setReceiverProvince(shipping.getReceiverProvince());
		shippingVo.setReceiverZip(shipping.getReceiverZip());
		return shippingVo;
	}

	private void removeCartOrder(Integer userId,List<OrderItem> orderItemList) {
		List<String> pIdsList=Lists.newArrayList();
		for (OrderItem orderItem : orderItemList) {
			pIdsList.add(orderItem.getProductId().toString());
		}
		cartMapper.deleteByUserIdAndProductId(userId, pIdsList);
	}

	private void changeProductStock(List<OrderItem> orderItemList,String type) {
		for (OrderItem orderItem : orderItemList) {
			
			Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
			if (Const.ChangeStock.ADD_STOCK.equals(type)) {
				product.setStock(product.getStock()+orderItem.getQuantity());
				System.out.println(product.getStock()+"**************");
			}else if (Const.ChangeStock.REDUCE_STOCK.equals(type)) {
				product.setStock(product.getStock()-orderItem.getQuantity());

			}
			productMapper.updateByPrimaryKey(product);
		}
	}
	
	private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment,Long createOrderNo) {
		Order order=new Order();
		order.setPayment(payment);
		order.setPaymentType(Const.PayPlatformEnum.ALIPAY.getCode());
		order.setOrderNo(createOrderNo);
		order.setShippingId(shippingId);
		order.setPostage(0);
		order.setUserId(userId);
		order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
		int insert = orderMapper.insert(order);
		if (insert>0) {
			return order;
		}
		return null;
	}

	private Long createOrderNo() {
		long currentTime=System.currentTimeMillis()*100;
		return currentTime+RandomUtils.nextInt(0, 99);
	}

	private BigDecimal getOrderPayment(List<OrderItem> data) {
		BigDecimal payment=new BigDecimal("0");
		for (OrderItem orderItem : data) {
			payment=BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
			
		}
		return payment;
	}

	private ServerResponse<List<OrderItem>> createOrderItem(List<CartProductVo> cartProductVolist,Long createOrderNo){
		if (CollectionUtils.isNotEmpty(cartProductVolist)) {
			for (CartProductVo cartProductVo : cartProductVolist) {
				if (cartProductVo.getChecked()==Const.Cart.CART_UNCHECKED) {
					cartProductVolist.remove(cartProductVo);
				}
			}
		
		}
		
		if (CollectionUtils.isEmpty(cartProductVolist)) {
			return ServerResponse.createByErrorMsg("未勾选商品或购物车为空");
		}
		
		List<OrderItem> orderItemList=Lists.newArrayList();
		for (CartProductVo cartProductVo : cartProductVolist) {
			OrderItem orderItem = new OrderItem();
			if (cartProductVo.getProductStatus().equals(Const.PRODUCT_UNSALE)) {
				return ServerResponse.createByErrorMsg("商品"+cartProductVo.getProductName()+"已下架");
				
			}
			if (cartProductVo.getQuantity()>cartProductVo.getProductStock()) {
				return ServerResponse.createByErrorMsg("商品"+cartProductVo.getProductName()+"库存不足");
			}
			orderItem.setUserId(cartProductVo.getUserId());
			orderItem.setProductId(cartProductVo.getProductId());
			orderItem.setProductName(cartProductVo.getProductName());
			orderItem.setProductImage(cartProductVo.getProductMainImage());
			orderItem.setQuantity(cartProductVo.getQuantity());
			orderItem.setCurrentUnitPrice(cartProductVo.getProductPrice());
			
			orderItem.setTotalPrice(BigDecimalUtil.mul(cartProductVo.getQuantity().doubleValue(), cartProductVo.getProductPrice().doubleValue()));
			orderItem.setOrderNo(createOrderNo);
			orderItemList.add(orderItem);
		}
		return ServerResponse.createBySuccess(orderItemList);
	}

	@Override
	public ServerResponse cancelOrder(Integer userId, Long orderNo) {
		if (orderNo==null) {
			return ServerResponse.createByErrorMsg("订单号不能为空");
		}
		Order order = orderMapper.selectOrderByOrderNoAndUserId(userId, orderNo);
		if (order==null) {
			return ServerResponse.createByErrorMsg("用户不存在该订单");
		}
		if (order.getStatus()==Const.OrderStatusEnum.NO_PAY.getCode()) {
			order.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
			int res = orderMapper.updateByPrimaryKey(order);
			if (res>0) {
				List<OrderItem> orderItemList = orderItemMapper.selectOrderItemByUserIdAndOrderNo(userId, orderNo);

				changeProductStock(orderItemList,Const.ChangeStock.ADD_STOCK);
				return ServerResponse.createBySuccessMsg("取消订单成功");
				
			}
			return ServerResponse.createByErrorMsg("取消订单失败");

		}
		
		return ServerResponse.createByErrorMsg("订单已取消或不能取消");
	}
	
	@Override
	public ServerResponse getOrderCartProduct(Integer userId) {
		OrderProductVo orderProductVo=new OrderProductVo();
		List<Cart> cartList = cartMapper.selectByuserIdAndChecked(userId, Const.Cart.CART_CHECKED);
		
		List<CartProductVo> cartProductVolist = cartService.listCart(userId).getData().getCartProductVolist();
		
		ServerResponse<List<OrderItem>> createOrderItem = createOrderItem(cartProductVolist,null);
		
		List<OrderItem> orderItemList = createOrderItem.getData();
		
		if (CollectionUtils.isEmpty(orderItemList)) {
			return ServerResponse.createByErrorMsg("订单错误！");
		}
		List<OrderItemVo> orderItemVoList = this.getOrderItemVoList(orderItemList);
		orderProductVo.setOrderItemVoList(orderItemVoList);
		BigDecimal payment = this.getOrderPayment(orderItemList);
		orderProductVo.setProductTotalPrice(payment);
		orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		return ServerResponse.createBySuccess(orderProductVo);
		
	}

	private List<OrderItem> getCartOrderItem(List<Cart> cartList, Integer userId) {
		List<OrderItem> orderItemList=Lists.newArrayList();
		for (Cart cart : cartList) {
			OrderItem orderItem=orderItemMapper.selectByUserIdAndProductId(userId, cart.getProductId());
			if (orderItem!=null) {
				orderItemList.add(orderItem);
			}
			
		}
		return orderItemList;
	}

	@Override
	public ServerResponse<OrderVo> getDetail(Integer userId, Long orderNo) {

		Order order = orderMapper.selectOrderByOrderNoAndUserId(userId, orderNo);
		if (order==null) {
			return ServerResponse.createByErrorMsg("用户不存在该订单");
		}
		List<OrderItem> orderItemList = orderItemMapper.selectOrderItemByUserIdAndOrderNo(userId, orderNo);
		OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
		return ServerResponse.createBySuccess(orderVo);
	}

	@Override
	public ServerResponse<PageInfo> getOrderList(Integer userId,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		List<Order> orderList = orderMapper.selectByuserId(userId);
		List<OrderVo> orderVoList=assembleOrderVoList(orderList,userId);
		PageInfo pageInfo=new PageInfo<>(orderList);
		pageInfo.setList(orderVoList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	

	private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId) {
		if (CollectionUtils.isEmpty(orderList)) {
			return null;
		}
		List<OrderVo> orderVoList=Lists.newArrayList();
		for (Order order : orderList) {
			List<OrderItem> orderItemList=Lists.newArrayList();
			if (userId==null) {
				orderItemList=orderItemMapper.selectByOrderNo(order.getOrderNo());
			}else {
				orderItemList=orderItemMapper.selectOrderItemByUserIdAndOrderNo(userId, order.getOrderNo());
				
			}
			OrderVo orderVo = assembleOrderVo(order, orderItemList);
			orderVoList.add(orderVo);
		}
		return orderVoList;
	}
	
	//manage业务
	
	@Override
	public ServerResponse<PageInfo> getManageOrderList(Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		List<Order> orderList = orderMapper.selectAll();
		List<OrderVo> orderVoList=assembleOrderVoList(orderList,null);
		PageInfo pageInfo=new PageInfo<>(orderVoList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse<OrderVo> getManageOrderDetail(Long orderNo) {
		Order order = orderMapper.selectOrderByOrderNo(orderNo);
		if (order==null) {
			return ServerResponse.createByErrorMsg("不存在该订单");
		}
		List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
		OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
		return ServerResponse.createBySuccess(orderVo);
	}

	@Override
	public ServerResponse<PageInfo> searchOrder(Long orderNo,Integer pageNum, Integer pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		Order order = orderMapper.selectOrderByOrderNo(orderNo);
		if (order==null) {
			return ServerResponse.createByErrorMsg("订单不存在！");
			
		}
		List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
		OrderVo orderVo= assembleOrderVo(order, orderItemList);
		PageInfo pageInfo=new PageInfo<>(Lists.newArrayList(orderVo));
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse<String> sendGoods(Long orderNo) {
		Order order = orderMapper.selectOrderByOrderNo(orderNo);
		if (order==null) {
			return ServerResponse.createByErrorMsg("订单不存在！");
		}
		if (order.getStatus()==Const.OrderStatusEnum.PAID.getCode()) {
			order.setShippingId(Const.OrderStatusEnum.SHIPPING.getCode());
			order.setSendTime(new Date());
			orderMapper.updateByPrimaryKeySelective(order);
			return ServerResponse.createBySuccess("发货成功");
		}
		if (order.getStatus()<=Const.OrderStatusEnum.NO_PAY.getCode()) {
			return ServerResponse.createByErrorMsg("订单未付款！");
		}
		
		return ServerResponse.createByErrorMsg("订单已发货！");
		
		
	}

}
