package com.example.contextmanagement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView;
import com.example.contextmanagement.ContextState.BuildingContextState;
import com.example.contextmanagement.ContextState.LightContextState;
import com.example.contextmanagement.ContextState.RoomContextState;
import com.example.contextmanagement.HTTP.BuildingContextHttpManager;
import com.example.contextmanagement.HTTP.LightContextHttpManager;
import com.example.contextmanagement.HTTP.RoomContextHttpManager;
import com.example.contextmanagement.Recycler.MyAdapter;

import java.util.ArrayList;

public class ContextManagementActivity extends Activity {

    RoomContextHttpManager roomContextHttpManager = new RoomContextHttpManager(this);
    LightContextHttpManager lightContextHttpManager = new LightContextHttpManager(this);
    BuildingContextHttpManager buildingContextHttpManager = new BuildingContextHttpManager(this);
    MyAdapter myAdapter = new MyAdapter(this);


    public ArrayList<LightContextState> lightList;
    public ArrayList<RoomContextState> roomList;
    public ArrayList<BuildingContextState> buildingList;
    public int numberOfLights;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public RelativeLayout light_management_layout;
    public View light_management_view;
    public Spinner building_Spinner;
    public Spinner room_Spinner;
    public Spinner light_Spinner;
    public Switch on_off_switch;
    public SeekBar level_seekBar;
    public RoomContextState chosen_room;
    public BuildingContextState chosen_building;
    public LightContextState chosen_light;

    public Integer first_trigger_light = 0;
    public Integer first_trigger_room = 0;
    public Integer first_trigger_building = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.welcome_page);
        building_Spinner = (Spinner) findViewById(R.id.building_Spinner);
        room_Spinner = (Spinner) findViewById(R.id.room_Spinner);
        light_Spinner = (Spinner) findViewById(R.id.light_Spinner);

        RelativeLayout light_management_layout = (RelativeLayout) findViewById(R.id.light_management_layout);
        View light_manager = light_management_layout.findViewById(R.id.light_manager);
        on_off_switch = light_manager.findViewById(R.id.on_off_switch);
        level_seekBar = light_manager.findViewById(R.id.level_seekbar);


        final Context context = this;



        building_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String buildingName = adapterView.getItemAtPosition(i).toString();
                if (first_trigger_building != 0 && buildingName != "Nothing Selected") {

                    int id = 0;
                    for (BuildingContextState tempBuilding : buildingList) {

                        if (buildingName.equals(tempBuilding.getName())) {
                            id = tempBuilding.getId();
                            ContextManagementActivity.this.chosen_building = tempBuilding;
                        }
                    }
                    buildingContextHttpManager.retrieveAllRoomsContextState(id);
                }
                first_trigger_building = 1;
                first_trigger_light = 0;
                first_trigger_room = 0;

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        room_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String roomName = adapterView.getItemAtPosition(i).toString();
                if (first_trigger_room != 0 && roomName != "Nothing Selected") {

                    int id = 0;
                    for (RoomContextState tempRoom : roomList) {
                        if (roomName.equals(tempRoom.getName())) {
                            id = tempRoom.getId();
                            ContextManagementActivity.this.chosen_room = tempRoom;
                        }
                    }

                    roomContextHttpManager.retrieveAllLightsContextState(id);
                }

                first_trigger_room = 1;
                first_trigger_light = 0;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        light_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id_string = adapterView.getItemAtPosition(i).toString();
                if (first_trigger_light != 0 && id_string != "Nothing Selected") {

                    int id = Integer.parseInt(id_string);

                    lightContextHttpManager.retrieveLightContextState(id);
                }

                first_trigger_light = 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        level_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                if(fromUser) {
                    progress = progressValue;
                    lightContextHttpManager.setLightLevel(chosen_light.getId(), progressValue);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        on_off_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lightContextHttpManager.switchLight(chosen_light.getId());
                } else {

                }
            }

        });

        GetBuildings(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void GetRooms(View view) {
        roomContextHttpManager.retrieveAllRoomsContextState();
    }

    public void GetBuildings(View view) {
        buildingContextHttpManager.retrieveAllBuildingsContextState();
    }


    public void fill_spinner_with_rooms(ArrayList<RoomContextState> rooms) {

        this.roomList = rooms;

        ArrayList<String> roomNames = new ArrayList<String>();
        roomNames.add("Nothing Selected");
        for (RoomContextState tempRoom : roomList){
            roomNames.add(tempRoom.getName());

        }

        ArrayAdapter<String>adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomNames);
        this.room_Spinner.setAdapter(adapter);



    }


    public void fill_spinner_with_buildings(ArrayList<BuildingContextState> buildings) {

        this.buildingList = buildings;

        ArrayList<String> buildingNames = new ArrayList<String>();
        buildingNames.add("Nothing Selected");
        for (BuildingContextState tempBuilding : buildingList){
            buildingNames.add(tempBuilding.getName());
        }



        ArrayAdapter<String>adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, buildingNames);
        this.building_Spinner.setAdapter(adapter);

    }


    public void fill_spinner_with_lights(ArrayList<LightContextState> lights) {

        this.lightList = lights;

        ArrayList<String> lightIds = new ArrayList<String>();
        lightIds.add("Nothing Selected");
        for (LightContextState tempLight : lightList){
            lightIds.add(Integer.toString(tempLight.getId()));
        }


        ArrayAdapter<String>adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lightIds);
        this.light_Spinner.setAdapter(adapter);

    }




    public void ManageSelectedLight() {

        RelativeLayout light_management_layout = (RelativeLayout) findViewById(R.id.light_management_layout);
        View light_manager = light_management_layout.findViewById(R.id.light_manager);
        on_off_switch = light_manager.findViewById(R.id.on_off_switch);
        level_seekBar = light_manager.findViewById(R.id.level_seekbar);

        light_management_layout.setVisibility(View.VISIBLE);

        on_off_switch.setChecked(chosen_light.getStatus().equals("ON"));

        level_seekBar.setProgress(chosen_light.getLevel());
    }


}