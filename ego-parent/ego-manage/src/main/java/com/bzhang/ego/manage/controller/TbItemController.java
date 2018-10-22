package com.bzhang.ego.manage.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.commons.constvalue.EgoResultConst.EgoResultReason;
import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.commons.utils.IDUtils;
import com.bzhang.ego.manage.service.TbItemDescService;
import com.bzhang.ego.manage.service.TbItemService;
import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.pojo.TbItemDesc;

@Controller
public class TbItemController {
	@Resource
	private TbItemService tbItemServiceImpl;
	
	/**
	 * 分页显示商品
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("item/list")
	@ResponseBody
	public EasyUIDataGrid showItem(@RequestParam(value="page",defaultValue="1")Integer pageNum,
			@RequestParam(value="rows",defaultValue="10")Integer pageSize) {
		return tbItemServiceImpl.show(pageNum, pageSize);
	}
	
	/**
	 * 商品删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/delete")
	@ResponseBody
	public EgoResult deleteItem(String ids) {
		EgoResult egoResult=new EgoResult();
		int res = tbItemServiceImpl.updateItemStatus(ids, (byte) 3);
		if (res==1) {
			egoResult.setStatus(EgoResultReason.OK_UPDATE_STATUS.getCode());
			egoResult.setReason(EgoResultReason.OK_UPDATE_STATUS.getValue());
			
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_UPDATE_STATUS.getCode());
			egoResult.setReason(EgoResultReason.ERROR_UPDATE_STATUS.getValue());
		}
		
		return egoResult;
	}
	
	/**
	 * 商品上架
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/reshelf")
	@ResponseBody
	public EgoResult reshelfItem(String ids) {
		EgoResult egoResult=new EgoResult();
		int res = tbItemServiceImpl.updateItemStatus(ids, (byte) 1);
		if (res==1) {
			egoResult.setStatus(EgoResultReason.OK_UPDATE_STATUS.getCode());
			egoResult.setReason(EgoResultReason.OK_UPDATE_STATUS.getValue());
			
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_UPDATE_STATUS.getCode());
			egoResult.setReason(EgoResultReason.ERROR_UPDATE_STATUS.getValue());
		}
		return egoResult;
	}
	
	/**
	 * 商品下架
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/instock")
	@ResponseBody
	public EgoResult instockItem(String ids) {
		EgoResult egoResult=new EgoResult();
		int res = tbItemServiceImpl.updateItemStatus(ids, (byte) 2);
		if (res==1) {
			egoResult.setStatus(EgoResultReason.OK_UPDATE_STATUS.getCode());
			egoResult.setReason(EgoResultReason.OK_UPDATE_STATUS.getValue());
			
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_UPDATE_STATUS.getCode());
			egoResult.setReason(EgoResultReason.ERROR_UPDATE_STATUS.getValue());
		}
		
		return egoResult;
	}
	
	@RequestMapping("item/save")
	@ResponseBody
	public EgoResult addItem(TbItem tbItem,String desc,String itemParams) {
		EgoResult egoResult=new EgoResult();
		//库存数量必须大于0
		if (tbItem.getCid()==null) {
			egoResult.setStatus(EgoResultReason.ERROR_NUM.getCode());
			egoResult.setReason(EgoResultReason.ERROR_NUM.getValue());
			return egoResult;
		}
		//商品类目不能为空
		if (StringUtils.isBlank(tbItem.getTitle())) {
			egoResult.setStatus(EgoResultReason.ERROR_CID.getCode());
			egoResult.setReason(EgoResultReason.ERROR_CID.getValue());
			return egoResult;
		}
		//库存数量必须大于0
		if (tbItem.getNum()==null||tbItem.getNum()<=0) {
			egoResult.setStatus(EgoResultReason.ERROR_NUM.getCode());
			egoResult.setReason(EgoResultReason.ERROR_NUM.getValue());
			return egoResult;
		}
		//价格必须大于0
		if (tbItem.getPrice()==null||tbItem.getPrice()<=0) {
			egoResult.setStatus(EgoResultReason.ERROR_PRICE.getCode());
			egoResult.setReason(EgoResultReason.ERROR_PRICE.getValue());
			return egoResult;
		}
		
		int index=0;
		try {
			index = tbItemServiceImpl.insertItem(tbItem, desc,itemParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (index==1) {
			egoResult.setStatus(EgoResultReason.OK_INSERT.getCode());
			egoResult.setReason(EgoResultReason.OK_INSERT.getValue());
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_INSERT.getCode());
			egoResult.setReason(EgoResultReason.ERROR_INSERT.getValue());
		}
		
		return egoResult;
	}
	
	
}
