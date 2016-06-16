package com.hxsn.iot.util;

import android.util.Log;

import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.data.SensorScreen;
import com.hxsn.iot.model.VideoData;
import com.hxsn.iot.model.ControlDevices;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.DetailQuestion;
import com.hxsn.iot.model.DevicesModel;
import com.hxsn.iot.model.FrameWork;
import com.hxsn.iot.model.GrowthCycle;
import com.hxsn.iot.model.JiDi;
import com.hxsn.iot.model.QxEntity;
import com.hxsn.iot.model.Sensor;
import com.hxsn.iot.model.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLParse{
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
	
	/**
	 * 解析登录
	 * @ param nodelist
	 */

	public User parserLogin(NodeList nodelist) {
		User user = new User();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if (node.getNodeName().equals("code")) {
				String code = node.getTextContent();
				user.setStatus(code);
			}
			if (node.getNodeName().equals("func")) {
				String func = node.getTextContent();
			}
			if (node.getNodeName().equals("data")) {
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for (int j = 0; j < childs.getLength(); j++) {
						Node child = childs.item(j);
						if(child.getNodeName().equals("sessionid")){
							String sessionid = child.getTextContent();
							user.setSessionid(sessionid);
						}
						if(child.getNodeName().equals("pass")){
							String controlPwd = child.getTextContent();
							user.setControlPwd(controlPwd);
						}
					}
				}
			}
		}
		return user;
	}
	
	/**
	 * 解析登出
	 * @param
	 */
	public void parserLogout(NodeList nodelist){
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				System.out.println("code:"+code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				System.out.println("func:"+func);
			}
			if(node.getNodeName().equals("data")){
				String data = node.getTextContent();
				System.out.println("data:"+data);
			}
		}
	}
	
	/**
	 * 解析修改密码
	 * @param
	 */
	public HashMap<String, String> parserRestPass(NodeList nodelist){
		HashMap<String, String> map =new HashMap<String, String>();
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
			}
		}
		return map;
	}
	//解析修改控制密码
	public HashMap<String, String> parserControlRestPass(NodeList nodelist){
		HashMap<String, String> map =new HashMap<String, String>();
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
			}
		}
		return map;
	}
	
	//解析软件更新
	public HashMap<String,String> parserUpdate(NodeList nodelist){
		HashMap<String,String> map = new HashMap<String,String>();
		for(int i=0;i<nodelist.getLength();i++){
			Node node = nodelist.item(i);
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
					for (int j = 0; j < childs.getLength(); j++) {
						Node child = childs.item(j);
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
	
	/**
	 * 解析气象哨
	 */
	public ParseDatas parserWeatherPost(NodeList nodelist){
		ParseDatas pard = new ParseDatas();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				pard.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				pard.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					SensorScreen sensren =new SensorScreen();
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("time")){
							String time = child.getTextContent();
							sensren.setDate(time);
						}//end if time
						if(child.getNodeName().equals("growth")){
							String growth = child.getTextContent();
							sensren.setGrowth(growth);
						}
						if(child.getNodeName().equals("group")){
							NodeList childs2 = child.getChildNodes();
							for(int k=0; k<childs2.getLength();k++){
								Node child2 = childs2.item(k);
								if(child2.getNodeName().equals("item")){
									Sensor sensor = new Sensor();
									String jc_name = child2.getAttributes().
									getNamedItem("name").getNodeValue();
									String jc_type = child2.getAttributes().
									getNamedItem("type").getNodeValue();
									String jc_val = child2.getAttributes().
									getNamedItem("value").getNodeValue();
									String jc_unit = child2.getAttributes().
											getNamedItem("unit").getNodeValue();
									sensor.setUnit(jc_unit);
									sensor.setName(jc_name);
									sensor.setType(jc_type);
									sensor.setValue(jc_val);
									sensren.addSensor(sensor);
								}
							}// end for group
						}
					}//end for data
					pard.setObject(sensren);
				}
			}//end if data
		}
		return pard;
	}
	
	/**
	 * 解析基地大棚,增加单元解析
	 * @param
	 */
	public ParseDatas parserJidiDape(NodeList nodelist){
		ParseDatas pard = new ParseDatas();
		ArrayList<JiDi> list =new ArrayList<JiDi>();
		pard.setObject(list);
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				pard.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				pard.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("jd")){
							String jd_id = child.getAttributes().
								getNamedItem("id").getNodeValue();
							String jd_name = child.getAttributes().
								getNamedItem("name").getNodeValue();
							JiDi jidi = new JiDi();
							jidi.setId(jd_id);
							jidi.setName(jd_name);
							if(child.hasChildNodes()){
								NodeList childs2 = child.getChildNodes();
								for(int k=0; k<childs2.getLength();k++){
									Node child2 = childs2.item(k);
									if(child2.getNodeName().equals("dp")){
										String dp_id = child2.getAttributes().
											getNamedItem("id").getNodeValue();
										String dp_name = child2.getAttributes().
											getNamedItem("name").getNodeValue();
										String dp_img = child2.getAttributes().
											getNamedItem("img").getNodeValue();
										Dapeng dp = new Dapeng();
										dp.setId(dp_id);
										dp.setName(dp_name);
										dp.setImg(dp_img);
										dp.setJidiname(jd_name);
										dp.setJidiid(jd_id);
										if(child2.hasChildNodes()) {
											NodeList childs3 = child2.getChildNodes();
											for (int l = 0; l < childs3.getLength(); l++) {
												Node child3 = childs3.item(l);
												if(child3.getNodeName().equals("dy")){
													String dy_id = child3.getAttributes().
															getNamedItem("id").getNodeValue() ;
													String dy_name=child3.getAttributes().
															getNamedItem("name").getNodeValue();
													String dy_crop = child3.getAttributes().
															getNamedItem("crop").getNodeValue() ;
													String dy_img=child3.getAttributes().
															getNamedItem("img").getNodeValue();
													Danyuan dy = new Danyuan();
													dy.setId(dy_id);
													dy.setName(dy_name);
													dy.setImage(dy_img);
													dy.setCrop(dy_crop);
													dy.setDapengId(dp_id);
													dy.setDapengName(dp_name);
													dy.setJidiId(jd_id);
													dy.setJidiName(jd_name);
													dp.addDanyuan(dy);
												}
											}
										}
										jidi.addDapeng(dp);
									}
								}
							}
							list.add(jidi);
						}
					}
				}
			}
		}
		return pard;
	}
	
	
	/**
	 * 解析监测数据    返回数据以SensorScreen封装
	 * @ param
	 */
	public ParseDatas parserDapeData(NodeList nodelist){
		ParseDatas pard = new ParseDatas();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
//				map.put("code", code);
				pard.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
//				map.put("func", func);
				pard.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
//				String data = node.getTextContent();
//				map.put("data", data);
				if(node.hasChildNodes()){
					SensorScreen sensren =new SensorScreen();
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("time")){
							String time = child.getTextContent();
							sensren.setDate(time);
						}//end if time
						if(child.getNodeName().equals("group")){
							NodeList childs2 = child.getChildNodes();
							for(int k=0; k<childs2.getLength();k++){
								Node child2 = childs2.item(k);
								if(child2.getNodeName().equals("item")){
									Sensor sensor = new Sensor();
									String jc_name = child2.getAttributes().
									getNamedItem("name").getNodeValue();
									String jc_type = child2.getAttributes().
									getNamedItem("type").getNodeValue();
									String jc_val = child2.getAttributes().
									getNamedItem("value").getNodeValue();
									String jc_normal = child2.getAttributes().
									getNamedItem("normal").getNodeValue();
									String jc_unit = child2.getAttributes().
											getNamedItem("unit").getNodeValue();
									sensor.setUnit(jc_unit);
									sensor.setNormal(jc_normal);
									sensor.setName(jc_name);
									sensor.setType(jc_type);
									sensor.setValue(jc_val);
									sensren.addSensor(sensor);
								}
							}// end for group
						}
					}//end for data
					pard.setObject(sensren);
				}
			}//end if data
		}
		return pard;
	}
	
	/**
	 * 解析曲线皮肤
	 * @param
	 */
	public HashMap<String,Object> parserLineDatas(NodeList nodelist){
		HashMap<String,Object> map = new HashMap<String,Object>();
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
				List<QxEntity> list = new ArrayList<QxEntity>();
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						QxEntity qxData = new QxEntity();
						Node child = childs.item(j);
						if(child.getNodeName().equals("time")){
							map.put("time",child.getTextContent());
						}
						if(child.getNodeName().equals("growth")){
							map.put("growth", child.getTextContent());
						}
						if(child.getNodeName().equals("qx")){
							if(child.hasChildNodes() ){
								String type = child.getAttributes().
										getNamedItem("type").getNodeValue();
								Log.i("type","type:"+type);
								qxData.setType(type);		
								//map.put("type", type);
								NodeList childs2 = child.getChildNodes();
								for(int k=0; k<childs2.getLength();k++){
									Node child2 = childs2.item(k);
									if(child2.getNodeName().equals("ymax")){
										String ymax = child2.getTextContent();
										double maxy = Double.parseDouble(ymax);
										qxData.setYmax(Integer.parseInt(new java.text.DecimalFormat("0").format(maxy)));
										//map.put("ymax", ymax);
									}
									if(child2.getNodeName().equals("ymin")){
										String ymin = child2.getTextContent();
										double miny = Double.parseDouble(ymin);
										qxData.setYmin(Integer.parseInt(new java.text.DecimalFormat("0").format(miny)));
										//map.put("ymin", ymin);
									}

									if(child2.getNodeName().equals("interval")){
										String interval = child2.getTextContent();
										qxData.setInterval(Integer.parseInt(interval));
										//map.put("interval", interval);
									}
									if(child2.getNodeName().equals("ydesc")){
										String ydesc = child2.getTextContent();
										qxData.setYdesc(ydesc);
										//map.put("ydesc", ydesc);
									}
//
									if(child2.getNodeName().equals("line")&&child2.getTextContent()!=null&&child2.getTextContent().length()>0){
										String line = child2.getTextContent();
										Log.i("line","line:"+line);
										String[] strLine = line.split(";");
										float[] floatLine = new float[strLine.length];
										for(int h= 0;h<strLine.length;h++){
											try{
												floatLine[h]= Float.parseFloat(strLine[h]);
											}catch(Exception ex){
												floatLine[h] = 0;
											}
										}
										qxData.setLine(floatLine);
										//map.put("line", line);
									}
									
									if(child2.getNodeName().equals("colours")&&child2.getTextContent()!=null&&child2.getTextContent().length()>0){
										String colours = child2.getTextContent();
										qxData.setColours(colours);
										//map.put("colours", colours);
									}
									if(child2.getNodeName().equals("unit")&&child2.getTextContent()!=null&&child2.getTextContent().length()>0){
										String unit = child2.getTextContent();
										qxData.setUnit(unit);
										//map.put("unit", unit);
									}
								}
							}
							//list.add(qxData);
						}//end qx
						list.add(qxData);	
							
					}//end for
					map.put("list", list);
				}
			}//end if data
		}
		return map;
	}
	
	/**
	 * 解析设备控制
	 * @param
	 */
	public HashMap<String, String> parserDeviceCtrl(NodeList nodelist){
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
				String data = node.getTextContent();
				map.put("data", data);
			}
		}
		return map;
	}
	
	/**
	 * 解析设备密码验证
	 * @param
	 */
	public HashMap<String, String> parserVerifyPwd(NodeList nodelist){
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
				String data = node.getTextContent();
				map.put("data", data);
			}
		}
		return map;
	}
	
	/**
	 * 解析控制设备类型个数
	 * @param nodelist
	 * @return 
	 */
	public ControlDevices parserDevices(NodeList nodelist){//
		ControlDevices device = new ControlDevices();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
//				map.put("code", code);
				device.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
			}
			if(node.getNodeName().equals("data")){
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
				
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String,Object> map = new HashMap<String,Object>();
							String deviceName = child.getAttributes().getNamedItem("name").getNodeValue();
							String deviceId = child.getAttributes().getNamedItem("id").getNodeValue();
							String deviceStatus = child.getAttributes().getNamedItem("status").getNodeValue();
							String deviceKind = child.getAttributes().getNamedItem("kind").getNodeValue();
							map.put("deviceName", deviceName);
							map.put("deviceId", deviceId);
							map.put("deviceStatus", deviceStatus);
							map.put("deviceKind", deviceKind);
							if(child.hasChildNodes()){
								NodeList childs1 = child.getChildNodes();
								ArrayList<HashMap<String,String>> list1 = new ArrayList<HashMap<String,String>>();
								for (int k = 0; k < childs1.getLength(); k++) {
									Node child1 = childs1.item(k);
									if(child1.getNodeName().equals("status")){
										HashMap<String,String> map1 = new HashMap<String,String>();
										String staId = child1.getAttributes().getNamedItem("id").getNodeValue();
										String staName = child1.getAttributes().getNamedItem("name").getNodeValue();
										map1.put("id", staId);
										map1.put("name", staName);
										list1.add(map1);
									}
								}
								map.put("status", list1);
							}
							list.add(map);
						}
					}
					
				}//end if has nodes
				device.setList(list);
			}
		}
		return device;
	}
	
	/**
	 * 解析群棚设备列表
	 */
	public ControlDevices parserShedsDevicesList(NodeList nodelist){//
		ControlDevices device = new ControlDevices();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
//				map.put("code", code);
				device.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
			}
			if(node.getNodeName().equals("data")){
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
				
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String,Object> map = new HashMap<String,Object>();
							String deviceName = child.getAttributes().getNamedItem("name").getNodeValue();
							String deviceKind = child.getAttributes().getNamedItem("kind").getNodeValue();
							map.put("deviceName", deviceName);
							map.put("deviceKind", deviceKind);
							if(child.hasChildNodes()){
								NodeList childs1 = child.getChildNodes();
								ArrayList<HashMap<String,String>> list1 = new ArrayList<HashMap<String,String>>();
								for (int k = 0; k < childs1.getLength(); k++) {
									Node child1 = childs1.item(k);
									if(child1.getNodeName().equals("status")){
										HashMap<String,String> map1 = new HashMap<String,String>();
										String staId = child1.getAttributes().getNamedItem("id").getNodeValue();
										String staName = child1.getAttributes().getNamedItem("name").getNodeValue();
										map1.put("id", staId);
										map1.put("name", staName);
										list1.add(map1);
									}
								}
								map.put("status", list1);
							}
							list.add(map);
						}
					}
					
				}//end if has nodes
				device.setList(list);
			}
		}
		return device;
	}
	
	//解析群棚根据设备获得大棚列表
	public ParseDatas parserShedsJidiDape(NodeList nodelist){
		ParseDatas pard = new ParseDatas();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				pard.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				pard.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					ArrayList<JiDi> list = new ArrayList<JiDi>();
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("jd")){
							String jd_id = child.getAttributes().
								getNamedItem("id").getNodeValue();
							String jd_name = child.getAttributes().
								getNamedItem("name").getNodeValue();
							JiDi jidi = new JiDi();
							jidi.setId(jd_id);
							jidi.setName(jd_name);
							if(child.hasChildNodes()){
								NodeList childs2 = child.getChildNodes();
								for(int k=0; k<childs2.getLength();k++){
									Node child2 = childs2.item(k);
									if(child2.getNodeName().equals("dp")){
										String dp_id = child2.getAttributes().
											getNamedItem("id").getNodeValue();
										String dp_name = child2.getAttributes().
											getNamedItem("name").getNodeValue();
										Dapeng dp = new Dapeng();
										dp.setId(dp_id);
										dp.setName(dp_name);
										dp.setJidiname(jd_name);
										dp.setJidiid(jd_id);
										if(child2.hasChildNodes()) {
											NodeList childs3 = child2.getChildNodes();
											for (int l = 0; l < childs3.getLength(); l++) {
												Node child3 = childs3.item(l);
												if(child3.getNodeName().equals("dy")){
													String dy_id = child3.getAttributes().
															getNamedItem("id").getNodeValue() ;
													String dy_name=child3.getAttributes().
															getNamedItem("name").getNodeValue();
													Danyuan dy = new Danyuan();
													dy.setId(dy_id);
													dy.setName(dy_name);
													dy.setDapengId(dp_id);
													dy.setDapengName(dp_name);
													dy.setJidiId(jd_id);
													dy.setJidiName(jd_name);
													dp.addDanyuan(dy);
													if(child3.hasChildNodes()){
														NodeList childs4 = child3.getChildNodes();
														for (int m = 0; m < childs4.getLength(); m++) {
															Node child4 = childs4.item(m);
															if(child4.getNodeName().equals("sb")){
																String sb_id = child4.getAttributes(). 
																		getNamedItem("id").getNodeValue();
																String sb_name = child4.getAttributes(). 
																		getNamedItem("name").getNodeValue();
																DevicesModel device = new DevicesModel();
																device.setId(sb_id);
																device.setName(sb_name);
																device.setDanYuanId(dy_id);
																device.setDanYuanName(dy_name);
																device.setDapengId(dp_id);
																device.setDapengName(dp_name);
																device.setJidiId(jd_id);
																device.setJidiName(jd_name);
																dy.addDeviceModel(device);
															}
														}
													}
												}
											}
										}
										jidi.addDapeng(dp);
									}
								}
							}
							list.add(jidi);
						}
					}
					pard.setObject(list);
				}
			}
		}
		return pard;
	}
	
	/**
	 * 解析群棚控制
	 * @param
	 */
	public ParseDatas parserShedsControl(NodeList nodelist){
		ParseDatas parse = new ParseDatas();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				parse.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				parse.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String,String> map = new HashMap<String,String>();
							String id = child.getAttributes().
								getNamedItem("sbid").getNodeValue();
							String status = child.getAttributes().
									getNamedItem("status").getNodeValue();
							map.put("sbid", id);
							map.put("status", status);
							list.add(map);
						}
					}
					parse.setList(list);
				}
			}
		}
		return parse;
	}
	
	/**
	 * 解析报警
	 * @param
	 */
	public ParseDatas parserAlarmList(NodeList nodelist){
		ParseDatas parse = new ParseDatas();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				parse.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				parse.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String, String> map = new HashMap<String, String>();
							String bjtext = child.getTextContent();
							map.put("bjtext", bjtext);
							String time = child.getAttributes().
								getNamedItem("alarm_time").getNodeValue();
							map.put("alarm_time", time);
							String level = child.getAttributes().
								getNamedItem("alarm_level").getNodeValue();
							map.put("alarm_level", level);
							String title = child.getAttributes().
								getNamedItem("alarm_title").getNodeValue();
							map.put("alarm_title", title);
							String dpname = child.getAttributes().
								getNamedItem("dp_name").getNodeValue();
							map.put("dp_name", dpname);
							String jdname = child.getAttributes().
									getNamedItem("jd_name").getNodeValue();
								map.put("jd_name", jdname);
							String dpId = child.getAttributes().
										getNamedItem("dpid").getNodeValue();
									map.put("dpid", dpId);
							String messageid = child.getAttributes().
								getNamedItem("alarm_msgid").getNodeValue();
							map.put("alarm_msgid", messageid);
							String timemark = child.getAttributes().
								getNamedItem("alarm_timemark").getNodeValue();
							map.put("alarm_timemark", timemark);
							list.add(map);
						}
					}//end for
					parse.setList(list);
				}
			}//end if data
		}
		return parse;
	}
	
	/**]
	 * 解析报警详情
	 * @param
	 */
	public HashMap<String, String> parserAlarmContent(NodeList nodelist){
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
						if(child.getNodeName().equals("time")){
							String time = child.getTextContent();
							map.put("time", time);
						}
						if(child.getNodeName().equals("jdname")){
							String jdname = child.getTextContent();
							map.put("jdname", jdname);
						}
						if(child.getNodeName().equals("dpname")){
							String dpname = child.getTextContent();
							map.put("dpname", dpname);
						}
						if(child.getNodeName().equals("content")){
							String content = child.getTextContent();
							map.put("content", content);
						}
						if(child.getNodeName().equals("level")){
							String level = child.getTextContent();
							map.put("level", level);
						}
						if(child.getNodeName().equals("title")){
							String title = child.getTextContent();
							map.put("title", title);
						}
						if(child.getNodeName().equals("receiver")){
							String receiver = child.getTextContent();
							map.put("receiver", receiver);
						}
						if(child.getNodeName().equals("receivePhone")){
							String receivePhone = child.getTextContent();
							map.put("receivePhone", receivePhone);
						}
//						if(child.getNodeName().equals("msgstatus")){
//							String msgstatus = child.getTextContent();
//							map.put("msgstatus", msgstatus);
//						}
					}//end for
				}
			}//end if data
		}
		return map;
	}
	
	/**
	 * 解析历史数据
	 * @param
	 */
	public void parserHistroyList(NodeList nodelist){
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				System.out.println("code:"+code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				System.out.println("func:"+func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							String item = child.getTextContent();
							String count = child.getAttributes().
								getNamedItem("count").getNodeValue();
						}
					}//end for
				}
			}//end if data
		}
	}
	
	/**
	 * 解析作物的生育周期
	 */
	public GrowthCycle parserGrowthCycle(NodeList nodelist){
		GrowthCycle growth = new GrowthCycle();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				growth.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				System.out.println("func:"+func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("name")){
							String name = child.getTextContent();
							growth.setName(name);
						}
						if(child.getNodeName().equals("totaldays")){
							String days = child.getTextContent();
							growth.setTotalDays(days);
						}
						if(child.getNodeName().equals("currentday")){
							String currentDay = child.getTextContent();
							growth.setCurrentDay(currentDay);
						}
						
						if(child.getNodeName().equals("group")){
							if(child.hasChildNodes()){
								NodeList childs1 = child.getChildNodes();
								List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
								for (int k = 0; k < childs1.getLength(); k++) {
									Node child1 = childs1.item(k);
									if(child1.getNodeName().equals("item")){
										HashMap<String,String> map = new HashMap<String,String>();
										String name = child1.getAttributes().
												getNamedItem("name").getNodeValue();
										String type = child1.getAttributes().
												getNamedItem("type").getNodeValue();
										String value = child1.getAttributes().
												getNamedItem("value").getNodeValue();
										map.put("name", name);
										map.put("type", type);
										map.put("value", value);
										list.add(map);
									}
								}
								growth.setGroupList(list);
							}
						}
					}
				}
			}
		}
		return growth;
	}
	
	//农事活动列表
	public ParseDatas parserGrowthList(NodeList nodelist){
		ParseDatas parse = new ParseDatas();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				parse.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				parse.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String,String> map = new HashMap<String,String>();
							String type = child.getAttributes().
								getNamedItem("type").getNodeValue();
							String name = child.getAttributes().
									getNamedItem("name").getNodeValue();
							String time = child.getAttributes().
									getNamedItem("time").getNodeValue();
							String mark = child.getTextContent();
							map.put("type", type);
							map.put("name", name);
							map.put("time", time);
							map.put("mark", mark);
							list.add(map);
						}
					}//end for
					parse.setList(list);
				}
			}//end if data
		}
		return parse;
	}
	
	//农事活动项
	public ArrayList<String> parserGrowthTerm(NodeList nodelist){
		ArrayList<String> list = new ArrayList<String>();
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
							System.out.println(child.getAttributes().getNamedItem("id").getNodeValue());;
							String item = child.getTextContent();
							list.add(item);
						}
					}
				}
			}
		}
		return list;
	}
	
	//解析农事洗活动
	public ArrayList<FrameWork> parserGrowthTermObject(NodeList nodelist){
		ArrayList<FrameWork> list = new ArrayList<FrameWork>();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							String id = child.getAttributes().getNamedItem("id").getNodeValue();;
							String item = child.getTextContent();
							list.add(new FrameWork(id, item));
						}
					}
				}
			}
		}
		return list;
	}
	
	
	//添加农事活动项
	public HashMap<String, String> parserAddGrowth(NodeList nodelist){
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
				String data = node.getTextContent();
				map.put("data", data);
			}
		}
		return map;
	}
	
	//专家信息解析
	public ParseDatas parserexpertList(NodeList nodelist){//
		ParseDatas pard = new ParseDatas();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
//				map.put("code", code);
				pard.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
//				map.put("func", func);
				pard.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
				
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String, String> map = new HashMap<String, String>();
							String value = child.getTextContent();
							String name = child.getAttributes().getNamedItem("name").getNodeValue();
							String id = child.getAttributes().getNamedItem("id").getNodeValue();
							String type = child.getAttributes().getNamedItem("type").getNodeValue();
							String img = child.getAttributes().getNamedItem("img").getNodeValue();
							map.put("value", value);
							map.put("name", name);
							map.put("id", id);
							map.put("type", type);
							map.put("img", img);
							list.add(map);
						}
					}
				}
				pard.setList(list);
			}
		}
		return pard;
	}
	
	//专家信息是否有变动
	public HashMap<String, String> getInfIsChange(NodeList nodelist){
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
				String data = node.getTextContent();
				map.put("data", data);
			}
		}
		return map;
	}
	
	//专家我的问题列表
	public ParseDatas parserQuestionList(NodeList nodelist){
		ParseDatas parse = new ParseDatas();
		
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				parse.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				parse.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String,String> map = new HashMap<String,String>();
							String num = child.getAttributes().
								getNamedItem("num").getNodeValue();
							String name = child.getAttributes().
									getNamedItem("name").getNodeValue();
							String time = child.getAttributes().
									getNamedItem("time").getNodeValue();
							String id = child.getAttributes().getNamedItem("id").getNodeValue();
							String value = child.getTextContent();
							map.put("id", id);
							map.put("name", name);
							map.put("time", time);
							map.put("num", num);
							map.put("value", value);
							list.add(map);
						}
					}
					parse.setList(list);
				}
			}
		}
		return parse;
	}
	
	//问题详细内容
	public DetailQuestion parserDetailQuestion(NodeList nodelist){
		DetailQuestion question = new DetailQuestion();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				question.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				System.out.println("func:"+func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("name")){
							String name = child.getTextContent();
							question.setName(name);
						}
						if(child.getNodeName().equals("content")){
							String content = child.getTextContent();
							question.setContent(content);
						}
						if(child.getNodeName().equals("group")){
							if(child.hasChildNodes()){
								NodeList childs1 = child.getChildNodes();
								ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
								for (int k = 0; k < childs1.getLength(); k++) {
									Node child1 = childs1.item(k);
									if(child1.getNodeName().equals("item")){
										HashMap<String,String> map = new HashMap<String,String>();
										String name = child1.getAttributes().
												getNamedItem("name").getNodeValue();
										String img = child1.getAttributes().
												getNamedItem("img").getNodeValue();
										String type = child1.getAttributes().
												getNamedItem("type").getNodeValue();
										String time = child1.getAttributes().
												getNamedItem("time").getNodeValue();
										String value = child1.getTextContent();
										map.put("name", name);
										map.put("img", img);
										map.put("type", type);
										map.put("time", time);
										map.put("value", value);
										list.add(map);
									}
								}
								question.setGroupList(list);
							}
						}
						if(child.getNodeName().equals("image")){
							if(child.hasChildNodes()){
								NodeList childs1 = child.getChildNodes();
								List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
								for (int k = 0; k < childs1.getLength(); k++) {
									Node child1 = childs1.item(k);
									if(child1.getNodeName().equals("item")){
										HashMap<String,String> map = new HashMap<String,String>();
										String url = child1.getAttributes().
												getNamedItem("url").getNodeValue();
										map.put("url", url);
										list.add(map);
									}
								}
								question.setImgList(list);
							}
						}
					}
				}
			}
		}
		return question;
	}
	
	//我的提问
	public HashMap<String, String> parserMyQuestion(NodeList nodelist){
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
				String data = node.getTextContent();
				map.put("data", data);
			}
		}
		return map;
	}
	
	//病害查询农产品种类,病害种类，问题列表
	public ParseDatas parserDiseaseType(NodeList nodelist){
		ParseDatas parse = new ParseDatas();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				parse.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				parse.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("item")){
							HashMap<String,String> map = new HashMap<String,String>();
							String id = child.getAttributes().
								getNamedItem("id").getNodeValue();
							String value = child.getTextContent();
							map.put("id", id);
							map.put("value", value);
							list.add(map);
						}
					}
					parse.setList(list);
				}
				
			}
		}
		return parse;
	}
	
	//病害问题内容
	public HashMap<String,String> parserDiseaseQuestion(NodeList nodelist){
		HashMap<String,String> map = new HashMap<String,String>();
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
						if(child.getNodeName().equals("name")){
							String value = child.getTextContent();
							map.put("value", value);
						}
						if(child.getNodeName().equals("reason")){
							String value = child.getTextContent();
							map.put("reason", value);
						}
						if(child.getNodeName().equals("method")){
							String value = child.getTextContent();
							map.put("method", value);
						}
					}
				}
			}
		}
		return map;
	}
	
	//专家主页热点问题列表
	public ArrayList<HashMap<String,String>> parserHotQuestion(NodeList nodelist){
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<nodelist.getLength();i++){
			Node node =nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				System.out.println("code:"+code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				System.out.println("func:"+func);
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
							String img = child.getAttributes().
									getNamedItem("img").getNodeValue();
							String value = child.getTextContent();
							map.put("id", id);
							map.put("name", name);
							map.put("img", img);
							map.put("value", value);
							list.add(map);
						}
					}
				}
			}
		}
		return list;
	}
	//系统设置，意见反馈
	public HashMap<String, String> parserFeedback(NodeList nodelist){
		HashMap<String, String> map =new HashMap<String, String>();
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
			}
		}
		return map;
	}
	
	//视频监控
	public VideoData parserVideoData(NodeList nodelist){
		VideoData data = new VideoData();
		for(int i=0;i<nodelist.getLength();i++){
			Node node = nodelist.item(i);
			if(node.getNodeName().equals("code")){
				String code = node.getTextContent();
				data.setCode(code);
			}
			if(node.getNodeName().equals("func")){
				String func = node.getTextContent();
				data.setFunc(func);
			}
			if(node.getNodeName().equals("data")){
				if(node.hasChildNodes()){
					NodeList childs = node.getChildNodes();
					for(int j=0; j<childs.getLength();j++){
						Node child = childs.item(j);
						if(child.getNodeName().equals("ip")){
							String ip = child.getTextContent();
							data.setIp(ip);
						}
						if(child.getNodeName().equals("port")){
							String port = child.getTextContent();
							data.setPort(port);
						}
						if(child.getNodeName().equals("user")){
							String user = child.getTextContent();
							data.setUser(user);		
						}
						if(child.getNodeName().equals("password")){
							String password = child.getTextContent();
							data.setPassword(password);
						}
						if(child.getNodeName().equals("channel")){
							if(child.hasChildNodes()){
								NodeList childs1 = child.getChildNodes();
								ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
								for (int k = 0; k < childs1.getLength(); k++) {
									Node child1 = childs1.item(k);
									HashMap<String,String> map = new HashMap<String,String>();
									if(child1.getNodeName().equals("item")){
										String note = child1.getAttributes().getNamedItem("note").getNodeValue();
										String channel = child1.getTextContent();
										map.put("note", note);
										map.put("channel", channel);
										list.add(map);
									}
								}
								data.setList(list);
							}
						}
					}
				}
			}
		}
		return data;
	}
}
