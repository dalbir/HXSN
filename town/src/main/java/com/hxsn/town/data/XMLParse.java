package com.hxsn.town.data;

import com.hxsn.town.model.ConditionListModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/*
 * 解析从服务器传来的xml文件
 */
public class XMLParse {
	
	private static XMLParse xmlParse;
	
	private DocumentBuilderFactory factory;
	
	private XMLParse(){
		factory = DocumentBuilderFactory.newInstance();
	}
	
	public static XMLParse getInstance(){
		if(null != xmlParse){
			return xmlParse;
		}
		return new XMLParse();
	}
	
	public Document parser(InputStream input) throws Exception{
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(input);
		return document;
	}
	
	public NodeList parserToList(Document d) {
		Element root = d.getDocumentElement();
		NodeList nodelist = root.getChildNodes();
		return nodelist;
	}
	 
	//解析周边园区列表
	public ArrayList<HashMap<String, String>> parserRimParkList(NodeList nodelist){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String,String> map = new HashMap<String,String>();
							String id = child.getAttributes().
								getNamedItem("id").getNodeValue();
							String name = child.getAttributes().
									getNamedItem("name").getNodeValue();
							String about = child.getAttributes().
									getNamedItem("about").getNodeValue();
							String image = child.getAttributes().
									getNamedItem("img").getNodeValue();
							String lat = child.getAttributes().
									getNamedItem("lat").getNodeValue();
							String lon = child.getAttributes().
									getNamedItem("lon").getNodeValue();
							map.put("id", id);
							map.put("name", name);
							map.put("about", about);
							map.put("img", image);
							map.put("lat", lat);
							map.put("lon", lon);
							list.add(map);
						}
					}
				}
			}
		}
		return list;
	}
	
	//周边模块搜索条件列表
	public ConditionListModel parserConditionList(NodeList nodelist){
		ConditionListModel mModel = new ConditionListModel();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				mModel.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				mModel.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					ArrayList<String> categoryList = new ArrayList<String>();
					ArrayList<ArrayList<HashMap<String,String>>> list = new ArrayList<ArrayList<HashMap<String,String>>>();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("group")){
							String name = child.getAttributes().
									getNamedItem("name").getNodeValue();
							categoryList.add(name);
							ArrayList<HashMap<String,String>> list1 = new ArrayList<HashMap<String,String>>();
							if(child.hasChildNodes()){
								NodeList childs1 = child.getChildNodes();
								for (int k = 0; k < childs1.getLength(); k++) {
									Node child1 = childs1.item(k);
									if(child1.getNodeName().equals("item")){
										HashMap<String,String> map = new HashMap<String,String>();
										String name1 = child1.getAttributes().
												getNamedItem("name").getNodeValue();
										String id = child1.getAttributes().
												getNamedItem("id").getNodeValue();
										map.put("name", name1);
										map.put("id", id);
										list1.add(map);
									}
								}
							}
							list.add(list1);
						}
					}
					mModel.setCategoryList(categoryList);
					mModel.setEventList(list);
				}
			}
		}
		return mModel;
	}
	
	//消息推送
	public HashMap<String, String> parserPushData(NodeList nodelist){
		HashMap<String, String> map = new HashMap<String, String>();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				map.put("code", code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				map.put("func", func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("title")){
							String title = child.getTextContent();
							map.put("title", title);
						}
						if(child.getNodeName().equals("about")){
							String about = child.getTextContent();
							map.put("about", about);
						}
						if(child.getNodeName().equals("url")){
							String url = child.getTextContent();
							map.put("url", url);
						}
					}
				}
			}
		}
		return map;
	}
}
