package com.techmahindra.vehicletelemetry.rest.bean;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Test {

    static String str = "{ "+ 
            "\"client\":\"127.0.0.1\"," + 
            "\"servers\":[" + 
            "    \"8.8.8.8\"," + 
            "    \"8.8.4.4\"," + 
            "    \"156.154.70.1\"," + 
            "    \"156.154.71.1\" " + 
            "    ]" + 
            "}";

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {

            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject)jsonParser.parse(str);
            JsonArray jsonArr = jo.getAsJsonArray("servers");
            //jsonArr.
            Gson googleJson = new Gson();
            ArrayList jsonObjList = googleJson.fromJson(jsonArr, ArrayList.class);
            System.out.println("List size is : "+jsonObjList.size());
                    System.out.println("List Elements are  : "+jsonObjList.toString());


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

