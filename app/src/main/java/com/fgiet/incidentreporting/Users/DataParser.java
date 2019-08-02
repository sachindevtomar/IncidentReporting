package com.fgiet.incidentreporting.Users;

/**
 * Created by Admin on 3/30/2018.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Admin on 3/23/2018.
 */

public class DataParser {
    private HashMap<String,String> getPlace(JSONObject googlePlaceJson){
        HashMap<String, String> googlePlaceMap=new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude="";
        String longitude="";
        String reference="";

        Log.d("finding error","Got error26");
        try {
            if(!googlePlaceJson.isNull("name")){
                placeName=googlePlaceJson.getString("name");

                Log.d("finding error","Got error27");
            }
            if(!googlePlaceJson.isNull("vicinity")){
                vicinity=googlePlaceJson.getString("vicinity");
            }
            Log.d("finding error","Got error23");
            latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            Log.d("finding error","Got error25");
            reference=googlePlaceJson.getString("reference");

            googlePlaceMap.put("place_name",placeName);
            googlePlaceMap.put("vicinity",vicinity);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lng",longitude);
            googlePlaceMap.put("reference",reference);
        } catch (JSONException e) {

            Log.d("finding error","Got error24");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }

    private List<HashMap<String, String >> getPlaces(JSONArray jsonArray){
        int count=jsonArray.length();

        Log.d("finding error","Got errorHello"+count+"f");
        List<HashMap<String,String>> placeList=new ArrayList<>();
        HashMap<String ,String > placeMap=null;
        for(int i=0;i<count;i++){
            try {
                placeMap=getPlace((JSONObject)jsonArray.get(i));
                placeList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placeList;
    }

    public List<HashMap<String ,String >> parse(String jsonData){
        JSONArray jsonArray=null;
        JSONObject jsonObject;
        try {

            Log.d("finding error","Got errorHello");
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("results");

            Log.d("finding error","Got errorHello1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}
