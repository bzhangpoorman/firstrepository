package com.bzhang.ego.manage.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.bzhang.ego.commons.constvalue.EgoResultConst.EgoResultReason;
import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.manage.service.TbItemParamService;
import com.bzhang.ego.pojo.TbItemParam;

@Controller
public class TbItemParamController {
	@Resource
	private TbItemParamService tbItemParamServiceImpl;
	
	/**
	 * 规格参数分页查询
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("item/param/list")
	@ResponseBody
	public EasyUIDataGrid showItemParam(@RequestParam(value="page",defaultValue="1")Integer pageNum,
			@RequestParam(value="rows",defaultValue="10")Integer pageSize) {
		return tbItemParamServiceImpl.show(pageNum, pageSize);
	}
	
	/**
	 * 删除规格参数信息，真实删除数据
	 * @param ids
	 * @return
	 */
	@RequestMapping("item/param/delete")
	@ResponseBody
	public EgoResult deleteItemParam(String ids) {
		EgoResult egoResult=new EgoResult();
		if (StringUtils.isBlank(ids)) {
			egoResult.setStatus(EgoResultReason.ERROR_DELETE_PARAMS_ID_NULL.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_DELETE_PARAMS_ID_NULL.getValue());
			return egoResult;
		}
		
		int res = tbItemParamServiceImpl.deleteParams(ids);
		if (res>=1) {
			egoResult.setStatus(EgoResultReason.OK_DELETE_PARAMS.getCode());
			egoResult.setMsg(EgoResultReason.OK_DELETE_PARAMS.getValue());
			
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_DELETE_PARAMS.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_DELETE_PARAMS.getValue());
		}
		return egoResult;
	}
	
	/**
	 * 根据类目id查询规格参数
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping("item/param/query/itemcatid/{itemCatId}")
	@ResponseBody
	public EgoResult showItemParamData(@PathVariable Long itemCatId) {
		EgoResult egoResult=new EgoResult();
		if (itemCatId==null||itemCatId<0) {
			egoResult.setStatus(EgoResultReason.ERROR_CID.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_CID.getValue());
			return egoResult;
		}
		TbItemParam itemParam = tbItemParamServiceImpl.selectByItemCatId(itemCatId);
		if (itemParam!=null) {
			
			egoResult.setStatus(EgoResultReason.OK_SELECT_PARAMS.getCode());
			egoResult.setMsg(EgoResultReason.OK_SELECT_PARAMS.getValue());
			egoResult.setData(itemParam);
			
			return egoResult;
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_SELECT_PARAMS_NOTFOUND.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_SELECT_PARAMS_NOTFOUND.getValue());
			return egoResult;
		}
	}
	
	/**
	 * 新增规格参数详情信息
	 * @param itemCatId
	 * @param paramData
	 * @return
	 */
	@RequestMapping("item/param/save/{itemCatId}")
	@ResponseBody
	public EgoResult addItemParam(@PathVariable Long itemCatId,String paramData) {
		EgoResult egoResult=new EgoResult();
		if (StringUtils.isBlank(paramData)) {
			egoResult.setStatus(EgoResultReason.ERROR_EMPTY_PARAMDATA.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_EMPTY_PARAMDATA.getValue());
			return egoResult;
		}
		int index = tbItemParamServiceImpl.insertItemParam(itemCatId, paramData);
		if (index==1) {
			
			egoResult.setStatus(EgoResultReason.OK_INSERT_PARAMS.getCode());
			egoResult.setMsg(EgoResultReason.OK_INSERT_PARAMS.getValue());
			return egoResult;
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_INSERT_PARAMS.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_INSERT_PARAMS.getValue());
			return egoResult;
		}
	}
}
