package com.bzhang.ego.search.service;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.bzhang.ego.pojo.TbItem;

public interface TbItemService {
	/**
	 * 初始化solr
	 * @throws SolrServerException
	 * @throws IOException
	 */
	void init() throws SolrServerException, IOException;
	
	/**
	 * 在solr中查找，并分页
	 * @param q 查询字段
	 * @param pageNum 当前页
	 * @param pageSize 每页数量
	 * @return
	 * @throws SolrServerException
	 * @throws IOException
	 */
	Map<String , Object> searchItem(String q,Integer pageNum,Integer pageSize) throws SolrServerException, IOException;
	
	/**
	 * 新增或修改solr中的数据
	 * @param tbItem
	 * @return
	 */
	int insertOrUpdateToSolr(TbItem tbItem) throws SolrServerException, IOException ;
	
	/**
	 * 根据id删除solr中信息
	 * @param id
	 * @return
	 */
	int deleteSolrById(String ids)throws SolrServerException, IOException;

	/**
	 * 通过id新增solr中数据
	 * @param ids
	 * @return
	 */
	int insertById(String ids) throws SolrServerException, IOException ;
	
	
	
}
