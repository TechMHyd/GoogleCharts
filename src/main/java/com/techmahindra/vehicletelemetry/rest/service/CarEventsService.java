package com.techmahindra.vehicletelemetry.rest.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.techmahindra.vehicletelemetry.rest.bean.CarAlert;
import com.techmahindra.vehicletelemetry.rest.bean.CarData;
import com.techmahindra.vehicletelemetry.rest.dao.CarDataDao;

/*
 * It is just a helper class which should be replaced by database implementation.
 * It is not very well written class, it is just used for demonstration.
 */
public class CarEventsService {

@Autowired
 private CarDataDao dao;

public static Set<String> modelData=new HashSet<>();

public static Set<String> cityData=new HashSet<>();

 public CarEventsService() {
  super();
  dao=new CarDataDao();
 }

 public List<Integer> getAggressiveDrivers() throws SQLException {
	
	
	 List<CarData> carData=dao.getAggressiveDrivers();
	
	modelData=new HashSet<>();
	cityData=new HashSet<>();
	
	 /*List<CarData> data=carData;
	 for(CarData car :data)
	 {
		 if(car!=null && car.getModel()!=null){
			 modelData.add(car.getModel());
		 }
		 if(car!=null && car.getCity()!=null)
		 {
			 cityData.add(car.getCity());
		 }
	 }*/
	 
	Gson gson=new Gson();
	String jsonString=gson.toJson(carData);
	
	System.out.println("JSOnString:"+jsonString);
	
	JSONParser parser = new JSONParser();
	ArrayList finallist = new ArrayList();
	Object obj = null;
	try {
		obj = parser.parse(jsonString);
	
	//JSONObject jsonObject = (JSONObject) obj;
		
	
		JSONArray monthslistjsonArray = (JSONArray) obj/*.get(model)*/;
		System.out.println("monthslist array:" + monthslistjsonArray);
		int size = monthslistjsonArray.size();
		Map<String,Integer> map=new HashMap<>();

		for (int i = 0; i < size; i++) {
			ArrayList<String> valuelist = new ArrayList<String>();
			ArrayList singleList = new ArrayList();
			JSONObject singleObject = (JSONObject) monthslistjsonArray
					.get(i);
			System.out.println("single Json Object at index " + i + " is "
					+ singleObject);
			if (singleObject != null) {
				
				valuelist.add(singleObject.get("count").toString());
				Integer count=Integer.parseInt(singleObject.get("count").toString());
				String model=singleObject.get("model").toString();
				
				String city=singleObject.get("city").toString();
				//if(map.containsKey(set)){
					map.put(city+"_"+model, count);
				//}
				if(!(cityData.contains(city))){
					cityData.add(city);
				}
				if(!(modelData.contains(model))){
					modelData.add(model);
				}
				
				/*if(cityData.contains(singleObject.get("city").toString())){
					if(modelData.contains(singleObject.get("model").toString())){
						valuelist.add(singleObject.get("count").toString());
					}
				}*/
				
				/*for(String model:modelData){
					if(model.equalsIgnoreCase(model)){
						valuelist.add(singleObject.get("count").toString());
					}
				}*/
				
				/*valuelist.add(singleObject.get("mar").toString());
				valuelist.add(singleObject.get("apr").toString());
				valuelist.add(singleObject.get("may").toString());
				valuelist.add(singleObject.get("jun").toString());
				valuelist.add(singleObject.get("jul").toString());*/

				singleList.addAll(valuelist);
				System.out.println("Single List   is :::" + singleList);
				finallist.add(singleList);

			}
		}
		
	System.out.println("Final value List   is :::" + finallist);
	System.out.println("City Data:"+cityData);
	System.out.println("Model Data:"+modelData);
	System.out.println("Map Data:"+map);
	
	
	
	
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return finallist;
	
	
}
 
 public Set<String> getAggressiveDriversModal() throws SQLException {
	
	 if(modelData == null || modelData.size() ==0){
		 List<CarData> data=dao.getAggressiveDrivers();
		 for(CarData car :data)
		 {
			 if(car!=null && car.getModel()!=null){
				 modelData.add(car.getModel());
			 }
		 }
	 }else {
		System.out.println("fething memory models");
	}
	 
		return modelData;
	}

 public Set<String> getAggressiveDriversCity() throws SQLException {

	 if(cityData ==null || cityData.size()==0){
		 List<CarData> data=dao.getAggressiveDrivers();
		 for(CarData car :data)
		 {
			 if(car!=null && car.getCity()!=null){
				 cityData.add(car.getCity());
			 }
		 }
	 }else {
			System.out.println("fething memory cities");
		}
	 return cityData;
 }

public List<CarData> getFuelEffcntDrivers() throws SQLException {
	
	return dao.getFuelEffcntDrivers();
}

public List<CarAlert> getCarAlerts() throws SQLException {
	
	return dao.getCarAlerts();
}


public static void main(String[] args) {
	
	Map<String,Integer> map=new HashMap<>();
	map.put("Seattle_sports car", 276);
	map.put("Seattle_Large SUV", 2415);
	map.put("Bellevue_Small SUV", 895);
	map.put("Redmond_Family Saloon", 1535);
	
	
	Map<String,Map<String,Integer>> map2=new HashMap<>();
	
	Map<String,Integer>cmMap=new HashMap<>();
	cmMap.put("model",100);
	
	Map<String,Integer>cmMap2=new HashMap<>();
	cmMap2.put("model2",200);
	
	Map<String,Integer>cmMap3=new HashMap<>();
	cmMap3.put("model3",300);
	
	Map<String,Integer>cmMap4=new HashMap<>();
	cmMap4.put("model4",400);
	
	map2.put("city",cmMap);
	map2.put("city2",cmMap2);
	map2.put("city3",cmMap3);
	map2.put("city4",cmMap4);
	
	System.out.println(map2);
	
	Map<String,Integer>cmMap31=new HashMap<>();
	cmMap31.put("model3",301);
	map2.put("city3",cmMap31);
	
	System.out.println(map2);
	
	
	
	/*Set set=map.keySet();
	System.out.println(map);
	System.out.println(set);
	map.put("Seattle_Large SUV1", 2416);
	//set.add("test");
	System.out.println(map);
	System.out.println(set);
	map.remove("Bellevue_Small SUV");
	set.remove("Seattle_sports car");
	System.out.println(map);
	System.out.println(set);*/
	
	//set.iterator()
	
}


}

