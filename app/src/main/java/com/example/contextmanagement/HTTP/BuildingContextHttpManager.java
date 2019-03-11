package com.example.contextmanagement.HTTP;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.contextmanagement.ContextManagementActivity;
import com.example.contextmanagement.ContextState.BuildingContextState;
import com.example.contextmanagement.ContextState.RoomContextState;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuildingContextHttpManager {

    private ContextManagementActivity contextManagementActivity;
    public BuildingContextHttpManager (ContextManagementActivity contextManagementActivity){
        this.contextManagementActivity = contextManagementActivity;
    }

    public void retrieveAllBuildingsContextState(){

        String url =  "https://hugues-chanteloup-faircorp.cleverapps.io/api/buildings/";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextManagementActivity);

        //get room sensed context
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<BuildingContextState> building_list = new ArrayList<BuildingContextState>();
                            for(int i =0;i<response.length();i++){
                                JSONObject building_json = response.getJSONObject(i);
                                int id = building_json.getInt("id");
                                String name = building_json.getString("name");
                                BuildingContextState building = new BuildingContextState(id,name);
                                building_list.add(building);
                            }

                            contextManagementActivity.buildingList = building_list;
                            contextManagementActivity.fill_spinner_with_buildings(building_list);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Creation of popup message
                        Context context = contextManagementActivity;
                        CharSequence text = "Error : buildings not imported";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });

        queue.add(jsonArrayRequest);


    }



    public void retrieveAllRoomsContextState(final int id) {

        String url =  "https://hugues-chanteloup-faircorp.cleverapps.io/api/rooms/";
        RequestQueue queue = Volley.newRequestQueue(contextManagementActivity);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            ArrayList<RoomContextState> rooms = new ArrayList<RoomContextState>();
                            for(int i =0;i<response.length();i++){
                                JSONObject room = response.getJSONObject(i);

                                if(room.getInt("buildingId") == (id)){

                                    int id = room.getInt("id");
                                    int floor = room.getInt("floor");
                                    String name = room.getString("name");
                                    String buildingId = room.getString("buildingId");
                                    RoomContextState aRoom = new RoomContextState(id, floor,name,buildingId);
                                    rooms.add(aRoom);
                                }
                            }

                            contextManagementActivity.roomList = rooms;

                            contextManagementActivity.fill_spinner_with_rooms(rooms);

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

}
