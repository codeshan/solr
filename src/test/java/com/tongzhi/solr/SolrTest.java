package com.tongzhi.solr;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import com.tongzhi.vo.SolrItem;

/**
 * solr的添加修改
 * @author shan
 *
 */
public class SolrTest {
	private HttpSolrServer httpSolrServer;
	
	@Before
	public void setUp(){
		// 初始化httpSolrServer；参数为solr的访问地址
		httpSolrServer = new HttpSolrServer("http://localhost:8080/solr");
	}
	
	/**
	 * 查询索引方式一
	 * @throws Exception 
	 */
	@Test
	public void testSearch1() throws Exception{
		//创建查询对象
		SolrQuery solrQuery = new SolrQuery();
		//搜索标题中包含手机并且状态为1的文档
		solrQuery.setQuery("title:手机 AND status:1");
		//设置过滤条件，在上面的过滤条件之后再加下面的条件
		//返回的数据价格在300000-800000（不包含800000）
		solrQuery.setFilterQueries("price:[300000 TO 800000]");
		
		//设置排序，根据价格降序排序
		solrQuery.setSort("price", ORDER.desc);
		
		//设置分页
		solrQuery.setStart(0);	//设置起始索引号
		solrQuery.setRows(10);	//页大小
		
		//设置高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("title");	//添加需要高亮显示的域
		solrQuery.setHighlightSimplePre("<em>");	//高亮前缀标签
		solrQuery.setHighlightSimplePost("</em>");	//高亮后缀标签]
		
		//查询
		QueryResponse response = httpSolrServer.query(solrQuery);
		
		//获取总记录数
		long total = response.getResults().getNumFound();
		
		System.out.println("本次查询到的总记录数为：" + total);
		
		//获取结果列表
		List<SolrItem> list = response.getBeans(SolrItem.class);
		
		//处理高亮标题
		if (list != null) {
			//获得高亮数据
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			//遍历所有数据
			for (SolrItem item : list) {
				System.out.println("-------------------------------------");
				System.out.println("id = " + item.getId());
				System.out.println("原标题title = " + item.getTitle());
				System.out.println("高亮标题title = " + highlighting.get(item.getId().toString()).get("title").get(0));
				System.out.println("price = " + item.getPrice());
				System.out.println("image = " + item.getImage());
				System.out.println("sellPoint = " + item.getSellPoint());
				//因为在schema.xml时没有设置该域是存储的所以为null，但是设置了要索引，所以可以进行搜索
				System.out.println("status = " + item.getStatus());
			}
		}
	}
	
	/**
	 * 查询索引方式二
	 * @throws Exception 
	 */
	@Test
	public void testSearch2() throws Exception{
		//创建查询对象
		SolrQuery solrQuery = new SolrQuery();
		//搜索标题中包含手机并且状态为1的文档
		solrQuery.setQuery("title:手机 AND status:1");
		//设置过滤条件，在上面的过滤条件之后再加下面的条件
		//返回的数据价格在300000-800000（不包含800000）
		solrQuery.setFilterQueries("price:[300000 TO 800000]");
		
		//设置排序，根据价格降序排序
		solrQuery.setSort("price", ORDER.desc);
		
		//设置分页
		solrQuery.setStart(0);	//设置起始索引号
		solrQuery.setRows(10);	//页大小
		
		//设置高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("title");	//添加需要高亮显示的域
		solrQuery.setHighlightSimplePre("<em>");	//高亮前缀标签
		solrQuery.setHighlightSimplePost("</em>");	//高亮后缀标签]
		
		//查询
		QueryResponse response = httpSolrServer.query(solrQuery);
		
		//获取总记录数
		long total = response.getResults().getNumFound();
		
		System.out.println("本次查询到的总记录数为：" + total);
		
		//获取结果列表
		SolrDocumentList results = response.getResults();
		
		//处理高亮标题
		if (results != null) {
			//获得高亮数据
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			//遍历所有数据
			for (SolrDocument solrDocument : results) {
				System.out.println("-------------------------------------");
				System.out.println("id = " + solrDocument.get("id"));
				System.out.println("原标题title = " + solrDocument.get("title"));
				System.out.println("高亮标题title = " + highlighting.get(solrDocument.get("id").toString()).get("title").get(0));
				System.out.println("price = " + solrDocument.get("price"));
				System.out.println("image = " + solrDocument.get("image"));
				System.out.println("sellPoint = " + solrDocument.get("sellPoint"));
				//因为在schema.xml时没有设置该域是存储的所以为null，但是设置了要索引，所以可以进行搜索
				System.out.println("status = " + solrDocument.get("status"));
			}
		}
	}
	
	
	/**
	 * 添加方式一：SolrInputDocument
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddDocument() throws Exception{
		// 创建SolrInputDocument对象，通过它来添加一个个的域
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		// 参数1：域的名称，域的名称必须是在schema.xml中定义的；
		// 参数2：域的值
		// 注意：id的域不能少
		solrInputDocument.addField("id", 123L);
		solrInputDocument.addField("title", "通过代码添加--solrInputDocument方式");
		solrInputDocument.addField("price", 2000L);
		solrInputDocument.addField("sellPoint", "我会Java编程");
		solrInputDocument.addField("image", "http://image.taotao.com/jd/b43e6da16b414d7eb372863f502034b1.jpg");
		solrInputDocument.addField("status", 1);
		// 将文档添加到索引库索引化
		httpSolrServer.add(solrInputDocument);
		// 提交，在更新solr后要记得提交
		httpSolrServer.commit();	
	}
	
	/**
	 * 方式一：Bean
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddBean() throws Exception{
		//创建提交的对象
		SolrItem solrItem = new SolrItem();
		solrItem.setId(124L);
		solrItem.setTitle("通过代码添加--bean方式");
		solrItem.setImage("http://image.taotao.com/jd/b43e6da16b414d7eb372863f502034b1.jpg");
		solrItem.setPrice(2000L);
		solrItem.setSellPoint("我会Java编程");
		solrItem.setStatus(1);
		
		// 将文档添加到索引库索引化
		httpSolrServer.addBean(solrItem);
		// 提交，在更新solr后要记得提交
		httpSolrServer.commit();	
	}
	
	/**
	 * 根据ID删除索引文档
	 * @throws Exception 
	 */
	@Test
	public void testDeleteById() throws Exception{
		// 指定要删除的文档id域对应的值
		httpSolrServer.deleteById("123");
		//提交删除
		httpSolrServer.commit();
	}
	
	/**
	 * 根据条件删除索引文档
	 * 删除所有
	 * @throws Exception 
	 */
	@Test
	public void testDeleteByQuery() throws Exception{
		// 根据条件删除索引文档
		httpSolrServer.deleteByQuery("title:代码");
		
		//删除所有的索引文档
		//httpSolrServer.deleteByQuery("*:*");
		
		//提交删除
		httpSolrServer.commit();
	}
	
}
