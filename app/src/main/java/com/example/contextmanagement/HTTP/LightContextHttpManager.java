package com.example.contextmanagement.HTTP;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contextmanagement.ContextManagementActivity;
import com.example.contextmanagement.ContextState.LightContextState;
import com.example.contextmanagement.ContextState.RoomContextState;
import com.example.contextmanagement.Recycler.MyAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LightContextHttpManager {

    private ContextManagementActivity contextManagementActivity;

    public LightContextHttpManager(ContextManagementActivity contextManagementActivity){
        this.contextManagementActivity = contextManagementActivity;
    }


    public void retrieveLightContextState(int id){


        String url =  "https://hugues-chanteloup-faircorp.cleverapps.io/api/lights"+ "/" + id ;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextManagementActivity);

        //change Light status
        JsonObjectRequest contextRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                try {

                    int id = response.getInt("id");
                    int lightLevel = response.getInt("level");
                    String lightStatus = response.getString("status");
                    int roomId = response.getInt("roomId");

                    contextManagementActivity.chosen_light = new LightContextState(id, lightLevel,lightStatus,roomId);

                    contextManagementActivity.ManageSelectedLight();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Creation of popup message
                Context context = contextManagementActivity;
                CharSequence text = "Error : light not retrieved";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                // Some error to access URL : Room may not exists...
                }
            });

        queue.add(contextRequest);

    }



    public void switchLight(final int id){

        String url = "https://hugues-chanteloup-faircorp.cleverapps.io/api/lights/"+id+"/switch";

        RequestQueue queue = Volley.newRequestQueue(contextManagementActivity);

        StringRequest putRequest = new StringRequest
                (Request.Method.PUT, url, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", String.valueOf(error));
                    }
                }
        );

        queue.add(putRequest);

    }

    public void setLightLevel(int id, int level){

        String url = "https://hugues-chanteloup-faircorp.cleverapps.io/api/lights/"+id+"/switch_level/"+level ;

        RequestQueue queue = Volley.newRequestQueue(contextManagementActivity);

        StringRequest putRequest = new StringRequest
                (Request.Method.PUT, url, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", String.valueOf(error));
                            }
                        }
                );

        queue.add(putRequest);

    }


}

