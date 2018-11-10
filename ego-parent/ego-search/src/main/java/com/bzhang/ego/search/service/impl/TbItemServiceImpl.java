package com.bzhang.ego.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.dubbo.service.TbItemCatDubboService;
import com.bzhang.ego.dubbo.service.TbItemDescDubboService;
import com.bzhang.ego.dubbo.service.TbItemDubboService;
import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.pojo.TbItemCat;
import com.bzhang.ego.pojo.TbItemDesc;
import com.bzhang.ego.search.service.TbItemService;
import com.bzhang.ego.vo.TbItemVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class TbItemServiceImpl implements TbItemService{

	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;
	
	@Reference 
	private TbItemCatDubboService tbItemCatDubboServiceImpl;
	
	@Resource
	private CloudSolrClient solrClient;
	
	@Override
	public void init() throws SolrServerException, IOException {
		List<TbItem> itemList = tbItemDubboServiceImpl.selectAllByStatus((byte)1);
		for (TbItem tbItem : itemList) {
			TbItemCat tbItemCat = tbItemCatDubboServiceImpl.selectById(tbItem.getCid());
			TbItemDesc tbItemDesc = tbItemDescDubboServiceImpl.selectByItemId(tbItem.getId());
			
			//向solr中新增数据
			/*<field name="item_title" type="text_ik" indexed="true" stored="true"/>
				<field name="item_sell_point" type="text_ik" indexed="true" stored="true"/>
				<field name="item_price"  type="long" indexed="true" stored="true"/>
				<field name="item_image" type="string" indexed="false" stored="true" />
				<field name="item_category_name" type="string" indexed="true" stored="true" />
				<field name="item_desc" type="text_ik" indexed="true" stored="false" />
			 */
			SolrInputDocument doc=new SolrInputDocument();
			doc.setField("id", tbItem.getId());
			doc.setField("item_title", tbItem.getTitle());
			doc.setField("item_sell_point", tbItem.getSellPoint());
			doc.setField("item_price", tbItem.getPrice());
			doc.setField("item_image", tbItem.getImage());
			doc.setField("item_category_name",tbItemCat.getName() );
			doc.setField("item_desc", tbItemDesc.getItemDesc());
			doc.setField("item_updated", tbItem.getUpdated());
			solrClient.add(doc);
			
		}
		solrClient.commit();
	}

	@Override
	public Map<String, Object> searchItem(String q, Integer pageNum, Integer pageSize) throws SolrServerException, IOException {
		List<TbItemVo> tbItemVoList=Lists.newArrayList();
		
		SolrQuery params=new SolrQuery();
		//查询条件
		params.setQuery("item_keywords:"+q);
		//分页
		params.setStart((pageNum-1)*pageSize);
		params.setRows(pageSize);
		//设置高亮属性
		params.setHighlight(true);
		params.addHighlightField("item_title");
		params.setHighlightSimplePre("<span style='color:green;font-weight:bold'>");
		params.setHighlightSimplePost("</span>");
		
		//设置排序
		params.setSort("item_updated", ORDER.desc);
		
		QueryResponse queryResponse = solrClient.query(params);
		SolrDocumentList resultList = queryResponse.getResults();
		Map<String, Map<String, List<String>>> hhMap = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : resultList) {
			TbItemVo tbItemVo=new TbItemVo();
			//组装tbitemvo对象
			tbItemVo.setId(Long.parseLong(solrDocument.getFieldValue("id").toString()));
			tbItemVo.setSellPoint(solrDocument.getFieldValue("item_sell_point").toString());
			tbItemVo.setPrice(Long.parseLong(solrDocument.getFieldValue("item_price").toString()));
			
			Map<String, List<String>> map = hhMap.get(solrDocument.getFieldValue("id"));
			List<String> list = map.get("item_title");
			if (CollectionUtils.isNotEmpty(list)) {
				tbItemVo.setTitle(list.get(0));
			}else {

				tbItemVo.setTitle(solrDocument.getFieldValue("item_title").toString());
			}
			
			Object images = solrDocument.getFieldValue("item_image");
			if (images==null||"".equals(images)) {
				tbItemVo.setImages(new String[1]);
			}else {
				tbItemVo.setImages(StringUtils.split(images.toString(), ","));
			}
			System.out.println(solrDocument.getFieldValue("item_title"));
			tbItemVoList.add(tbItemVo);
			
			
		}
		Map<String, Object> mapResult=Maps.newHashMap();
		mapResult.put("itemList", tbItemVoList);
		mapResult.put("totalPages", (resultList.getNumFound()-1)/pageSize+1);
		return mapResult;
	}

	@Override
	public int insertOrUpdateToSolr(TbItem tbItem) throws SolrServerException, IOException {

		SolrInputDocument doc=new SolrInputDocument();
		doc.addField("id", tbItem.getId());
		doc.addField("item_title", tbItem.getTitle());
		doc.addField("item_sell_point", tbItem.getSellPoint());
		doc.addField("item_price", tbItem.getPrice());
		doc.addField("item_image", tbItem.getImage());
		doc.addField("item_category_name", tbItemCatDubboServiceImpl.selectById(tbItem.getCid()).getName());
		doc.addField("item_desc", tbItemDescDubboServiceImpl.selectByItemId(tbItem.getId()).getItemDesc());
		
		UpdateResponse response = solrClient.add(doc);
		solrClient.commit();
		if (response.getStatus()==0) {
			return 1;
		}
		return 0;
	}

	@Override
	public int deleteSolrById(String  ids) throws SolrServerException, IOException {
		
		String[] split = StringUtils.split(ids, ",");
		List<String> list = Arrays.asList(split);
		System.out.println(list);
		UpdateResponse response = solrClient.deleteById(list);
		
		solrClient.commit();
		if (response.getStatus()==0) {
			return 1;
		}
		return 0;
	}

	@Override
	public int insertById(String ids) throws SolrServerException, IOException {
		String[] split = StringUtils.split(ids, ",");
		int res = 0;
		for (String idStr : split) {
			TbItem item = tbItemDubboServiceImpl.selectById(Long.parseLong(idStr));
			res+=insertOrUpdateToSolr(item);
		}
		if (res==split.length) {
			return 1;
		}
		return 0;
	}

}
