package com.techmahindra.vehicletelemetry.rest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.techmahindra.vehicletelemetry.rest.bean.ChartData;
import com.techmahindra.vehicletelemetry.rest.service.CarEventsService;

@RestController
public class ChartDataController {

	CarEventsService CarEventService = new CarEventsService();

	@RequestMapping(value = "/data", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<List<Integer>> getData() {
		List<ChartData> chartData = new ArrayList<>();
		//28, 48, 40, 19, 6, 27, 90
		ChartData dat1=new ChartData(28, 48, 40, 19, 6, 27, 90);
		ChartData dat2=new ChartData(22, 28, 40, 19, 6, 27, 90);
		ChartData dat3=new ChartData(23, 38, 20, 49, 8, 27, 90);
		ChartData dat4=new ChartData(20, 48, 40, 39, 10, 27, 90);
		ChartData dat5=new ChartData(24, 58, 30, 69, 22, 27, 90);
		ChartData dat6=new ChartData(25, 48, 40, 79, 16, 27, 90);
		ChartData dat7=new ChartData(20, 18, 50, 19, 26, 27, 90);
		
		/*List<Long> toIdsList(List<ViewValue> viewValues) {

			   List<Long> ids = new ArrayList<Long>();

			   for (ViewValue viewValue : viewValues) {
			      ids.add(viewValue.getId());
			   }

			   return ids;
		}*/
		
		Gson gson=new Gson();
		/*chartData.add(gson.toJson(dat1));
		chartData.add(gson.toJson(dat2));
		chartData.add(gson.toJson(dat3));
		chartData.add(gson.toJson(dat4));
		chartData.add(gson.toJson(dat5));
		chartData.add(gson.toJson(dat6));
		chartData.add(gson.toJson(dat7));*/
		
		chartData.add(dat1);
		chartData.add(dat2);
		chartData.add(dat3);
		chartData.add(dat4);
		chartData.add(dat5);
		chartData.add(dat6);
		chartData.add(dat7);
		
		System.out.println("ChartData:"+chartData);
		
		String jsonString=gson.toJson(chartData);
		
		System.out.println("JSOnString:"+jsonString);
		
		JSONParser parser = new JSONParser();
		ArrayList finallist = new ArrayList();
		Object obj = null;
		try {
			obj = parser.parse(jsonString);
		
		//JSONObject jsonObject = (JSONObject) ;
		
		JSONArray monthslistjsonArray = (JSONArray) obj;
				/*.get("monthslist");*/
		System.out.println("monthslist array:" + monthslistjsonArray);
		int size = monthslistjsonArray.size();
		for (int i = 0; i < size; i++) {
			ArrayList<String> valuelist = new ArrayList<String>();
			ArrayList singleList = new ArrayList();
			JSONObject singleObject = (JSONObject) monthslistjsonArray
					.get(i);
			System.out.println("single Json Object at index " + i + " is "
					+ singleObject);
			if (singleObject != null) {
				// System.out.println(singleObject.size());
				/*Set keyset = singleObject.keySet();
				HashSet<String> newset = new HashSet<String>(keyset);
				System.out.println(newset);
				Iterator iterator = newset.iterator();
				while (iterator.hasNext()) {
					Object jsonkey = iterator.next();
					Object keyvalue = singleObject.get(jsonkey);
					System.out.println("key: " + jsonkey + " and value: "
							+ keyvalue.toString());
					valuelist.add(keyvalue.toString());

				}*/
				
				valuelist.add(singleObject.get("jan").toString());
				valuelist.add(singleObject.get("feb").toString());
				valuelist.add(singleObject.get("mar").toString());
				valuelist.add(singleObject.get("apr").toString());
				valuelist.add(singleObject.get("may").toString());
				valuelist.add(singleObject.get("jun").toString());
				valuelist.add(singleObject.get("jul").toString());
				
				singleList.addAll(valuelist);
				System.out.println("Single List   is :::" + singleList);
				finallist.add(singleList);

			}

		}
		
		System.out.println("Final value List   is :::" + finallist);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return finallist;
	}
	
	@RequestMapping(value = "/sample", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<List<Integer>> getSample() {
		
		
		ChartData dat1=new ChartData(28, 48, 40, 19, 6, 27, 90);
		ChartData dat2=new ChartData(22, 28, 40, 19, 6, 27, 90);
		ChartData dat3=new ChartData(23, 38, 20, 49, 8, 27, 90);
		ChartData dat4=new ChartData(20, 48, 40, 39, 10, 27, 90);
		ChartData dat5=new ChartData(24, 58, 30, 69, 22, 27, 90);
		ChartData dat6=new ChartData(25, 48, 40, 79, 16, 27, 90);
		ChartData dat7=new ChartData(20, 18, 50, 19, 26, 27, 90);
		
		List<ChartData> list=new ArrayList<>();
		list.add(dat1);
		list.add(dat2);
		list.add(dat3);
		list.add(dat4);
		list.add(dat5);
		list.add(dat6);
		list.add(dat7);
		
		List<Integer> ids = list.stream().map(ChartData::getJan).collect(Collectors.toList());
		
		
		
		List<List<Integer>> twoDim=new ArrayList<List<Integer>>();
		twoDim.add(Arrays.asList(28, 48, 40, 19, 6, 27, 90));
		twoDim.add(Arrays.asList(22, 28, 40, 19, 6, 27, 90));
		twoDim.add(Arrays.asList(20, 48, 40, 39, 10, 27, 90));
		
		System.out.println("Array :"+twoDim);
		return twoDim;
	}
	
	
	/*@RequestMapping(value = "/CarEvent/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public CarEvent getCarEventById(@PathVariable int id) {
		return CarEventService.getCarEvent(id);
	}

	@RequestMapping(value = "/countries", method = RequestMethod.POST, headers = "Accept=application/json")
	public CarEvent addCarEvent(@RequestBody CarEvent CarEvent) {
		return CarEventService.addCarEvent(CarEvent);
	}

	@RequestMapping(value = "/countries", method = RequestMethod.PUT, headers = "Accept=application/json")
	public CarEvent updateCarEvent(@RequestBody CarEvent CarEvent) {
		return CarEventService.updateCarEvent(CarEvent);

	}

	@RequestMapping(value = "/CarEvent/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public void deleteCarEvent(@PathVariable("id") int id) {
		CarEventService.deleteCarEvent(id);

	}*/	
	
	
	public static void main(String[] args) {
		
		
		
		ChartData dat1=new ChartData(28, 48, 40, 19, 6, 27, 90);
		ChartData dat2=new ChartData(22, 28, 40, 19, 6, 27, 90);
		ChartData dat3=new ChartData(23, 38, 20, 49, 8, 27, 90);
		ChartData dat4=new ChartData(20, 48, 40, 39, 10, 27, 90);
		ChartData dat5=new ChartData(24, 58, 30, 69, 22, 27, 90);
		ChartData dat6=new ChartData(25, 48, 40, 79, 16, 27, 90);
		ChartData dat7=new ChartData(20, 18, 50, 19, 26, 27, 90);
		
		List<ChartData> list=new ArrayList<>();
		list.add(dat1);
		list.add(dat2);
		list.add(dat3);
		list.add(dat4);
		list.add(dat5);
		list.add(dat6);
		list.add(dat7);
		
		List<Integer> ids = list.stream().map(ChartData::getJan).collect(Collectors.toList());
		
		System.out.println("Jan list:"+ids);
		
		List<List<Float>> twoDim=new ArrayList<List<Float>>();
		twoDim.add(Arrays.asList(0f, 1f, 0f, 0f, 1f));
		twoDim.add(Arrays.asList(0f, 1f, 1f, 0f, 1f));
		/*twoDim.add(Arrays.asList(0, 0, 0, 1, 0));
		System.out.println("hello user"+twoDim);
		twoDim.add(Arrays.asList(0, 1, 0, 1, 0));
		twoDim.add(Arrays.asList(0, 1, 1, 0, 1));
		twoDim.add(Arrays.asList(0, 0, 0, 1, 0));*/
		
		System.out.println("hello user"+twoDim);
		

		
	}
}
