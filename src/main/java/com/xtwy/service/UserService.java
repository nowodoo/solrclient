package com.xtwy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.stereotype.Service;

import com.xtwy.model.User;

@Service
public class UserService {

	@Resource
	private CloudSolrClient solrClient;
	
	public Object get(Integer id) throws Exception {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", "id:"+id);
		QueryResponse response = solrClient.query("mycollection",solrQuery);
		
		return response.getResults();
	}

	
	public Object save(User user) throws SolrServerException, IOException {
//		SolrInputDocument solrInputDocument = new SolrInputDocument();
//		solrInputDocument.addField("userName", user.getUserName());
//		solrInputDocument.addField("id", user.getId());
//		solrInputDocument.addField("age", user.getAge());
//		solrInputDocument.addField("sex", user.getSex());
//		solrInputDocument.addField("description", user.getDescription());
//		solrInputDocument.addField("hobby", user.getHobby());
		solrClient.setDefaultCollection("mycollection");
//		solrClient.add("mycollection",solrInputDocument);
		solrClient.addBean(user);
		solrClient.commit();
		return "ok add success";
	}


	public Object delete(Integer id) throws SolrServerException, IOException {
		solrClient.setDefaultCollection("mycollection");
		solrClient.deleteById(String.valueOf(id));
		solrClient.commit();
		return "ok del success";
	}


	public Object search(String key) throws SolrServerException, IOException {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", key);
		solrQuery.set("defType","edismax");
		solrQuery.set("qf","userName^3 hobby^2 description^1");
		QueryResponse response = solrClient.query("mycollection",solrQuery);
		
		return response.getResults();
	}


	public Object searchHighLight(String key) throws SolrServerException, IOException {
   
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", key);
		solrQuery.set("defType","edismax");
		solrQuery.set("qf","userName^3 hobby^2 description^1");
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		
		solrQuery.setFacet(true);
		solrQuery.addFacetField("id");
		solrQuery.setFacetMinCount(1);
		QueryResponse response = solrClient.query("mycollection",solrQuery);
		
		FacetField ff = response.getFacetFields().get(0);
		for(Count count : ff.getValues()){
			System.out.println(count.getName()+":"+count.getCount());
		}
		
		Iterator<SolrDocument> results =  response.getResults().iterator();
	
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		while(results.hasNext()){
			List<String> data = new ArrayList<String>();
			SolrDocument doc = results.next();
			String id = String.valueOf(doc.getFieldValue("id"));
			List<String> userName =  response.getHighlighting().get(id).get("userName");
			List<String> hobby =  response.getHighlighting().get(id).get("hobby");
			List<String> description =  response.getHighlighting().get(id).get("description");
			if(userName!=null && !userName.isEmpty())data.addAll(userName);
			if(hobby!=null && !hobby.isEmpty())data.addAll(hobby);
			if(description!=null && !description.isEmpty())data.addAll(description);
			map.put(id, data);
		}
		
	List<String> datas = new ArrayList<String>();
	for(String k : map.keySet()){
		datas.addAll(map.get(k));
	}
	
	return datas;
	}


}
