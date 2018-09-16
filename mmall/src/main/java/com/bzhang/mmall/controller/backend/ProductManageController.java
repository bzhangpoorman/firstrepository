package com.bzhang.mmall.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ResponseCode;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.Product;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.FileService;
import com.bzhang.mmall.service.ProductService;
import com.bzhang.mmall.service.UserService;
import com.bzhang.mmall.util.PropertiesUtil;
import com.bzhang.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/manager/product")
public class ProductManageController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping("save_product.do")
	@ResponseBody
	public ServerResponse saveProduct(HttpSession session,Product product) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return productService.saveOrUpdateProduct(product);
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}
	}
	
	@RequestMapping("set_sale_status.do")
	@ResponseBody
	public ServerResponse setSaleStatus(HttpSession session,Product product) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return productService.setProductStatus(product.getId(), product.getStatus());
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}
	}
	
	@RequestMapping("product_detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVo> getProductDetail(HttpSession session,Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return productService.manageProductDetail(productId);
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}
	}
	
	@RequestMapping("get_list.do")
	@ResponseBody
	public ServerResponse<PageInfo> getList(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,@RequestParam(value="pageSize",defaultValue="10")Integer pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return productService.getProductList(pageNum, pageSize);
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}
	}
	
	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse<PageInfo> seacrhProduct(HttpSession session,String productName,Integer productId,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,@RequestParam(value="pageSize",defaultValue="10")Integer pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return productService.searchProduct(productName, productId, pageNum, pageSize);
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}
	}
	
	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse upload(HttpServletRequest request,@RequestParam(value="upload_file",required=false)MultipartFile file ) {
		User user =(User) request.getSession().getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			String path = request.getSession().getServletContext().getRealPath("upload");
			System.out.println(path);
			String targetFileName=fileService.upload(path, file);
			String url=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
			Map fileMap=Maps.newHashMap();
			fileMap.put("uri", targetFileName);
			fileMap.put("url", url);
			return ServerResponse.createBySuccess(fileMap);
			
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}
	}
	
	@RequestMapping("richtext_img_upload.do")
	@ResponseBody
	public Map richtextImgUpload(HttpServletResponse response,HttpServletRequest request,@RequestParam(value="upload_file",required=false)MultipartFile file ) {
		Map map=Maps.newHashMap();
		
		User user =(User) request.getSession().getAttribute(Const.CURRENT_USER);
		if (user==null) {
			map.put("success", false);
			map.put("msg", "用户未登录，请先登录！");
			return map;
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName=fileService.upload(path, file);
			if (StringUtils.isBlank(targetFileName)) {
				map.put("success", false);
				map.put("msg", "文件上传失败！");
				return map;
			}
			String url=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
			
			map.put("success", true);
			map.put("msg", "上传成功！");
			map.put("file-path", url);
			response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
			return map;
			
		}else {
			map.put("success", false);
			map.put("msg", "该用户没有管理员权限，无法操作！");
			return map;
		}
	}
	
	
}
