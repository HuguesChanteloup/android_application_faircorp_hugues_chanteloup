package com.example.contextmanagement.HTTP;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.example.contextmanagement.ContextManagementActivity;
import com.example.contextmanagement.ContextState.LightContextState;
import com.example.contextmanagement.ContextState.RoomContextState;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomContextHttpManager {

    private ContextManagementActivity contextManagementActivity;
    public RoomContextHttpManager (ContextManagementActivity contextManagementActivity){
        this.contextManagementActivity = contextManagementActivity;
    }

    //Get all the rooms for the spinner view
    public void retrieveAllRoomsContextState(){

        String url =  "https://hugues-chanteloup-faircorp.cleverapps.io/api/rooms/";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextManagementActivity);

        //get room sensed context
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //ArrayList<String> rooms = new ArrayList<String>();
                            ArrayList<RoomContextState> RoomList = new ArrayList<RoomContextState>();
                            //JSONArray Rooms= new JSONArray(response);
                            for(int i =0;i<response.length();i++){
                                //Get the room from the JSONArray
                                JSONObject room = response.getJSONObject(i);

                                int id = room.getInt("id");
                                int floor = room.getInt("floor");
                                String name = room.getString("name");
                                String buildingId = room.getString("buildingId");
                                RoomContextState aRoom = new RoomContextState(id, floor,name,buildingId);
                                RoomList.add(aRoom);
                            }

                            contextManagementActivity.fill_spinner_with_rooms(RoomList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Creation of popup message
                        Context context = contextManagementActivity;
                        CharSequence text = "Error : rooms not imported";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });

        queue.add(jsonArrayRequest);

    }


    public void retrieveAllLightsContextState(final int id) {

        String url =  "https://hugues-chanteloup-faircorp.cleverapps.io/api/lights/";
        RequestQueue queue = Volley.newRequestQueue(contextManagementActivity);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            ArrayList<LightContextState> lights = new ArrayList<LightContextState>();
                            for(int i =0;i<response.length();i++){
                                JSONObject light = response.getJSONObject(i);

                                if(light.getInt("roomId") == (id)){

                                    int id = light.getInt("id");
                                    int level = light.getInt("level");
                                    String status = light.getString("status");
                                    int roomId = light.getInt("roomId");
                                    LightContextState aLight = new LightContextState(id,level,status,roomId);
                                    lights.add(aLight);
                                }
                            }

                            contextManagementActivity.lightList = lights;
                            contextManagementActivity.fill_spinner_with_lights(lights);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Creation of popup message
                        Context context = contextManagementActivity;
                        CharSequence text = "Error : lights not imported";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });

        queue.add(jsonArrayRequest);

    }





    public void switchRoom(final int roomId){

        String url = "https://hugues-chanteloup-faircorp.cleverapps.io/api/rooms/"+ roomId +"/switch";

        RequestQueue queue = Volley.newRequestQueue(contextManagementActivity);

        StringRequest putRequest = new StringRequest
                (Request.Method.PUT, url, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //TODO : Update the info of the lights... refaire un GET
                        //retrieveLightContextState(lightId);
                        Log.d("Response : ", response);
                    }
                },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                CharSequence text = "Error : room could not be switched";
                                int duration = Toast.LENGTH_SHORT;
                                Context context = contextManagementActivity;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();

                                Log.d("Error.Response", String.valueOf(error));
                            }
                        }
                );

        queue.add(putRequest);

    }

}
