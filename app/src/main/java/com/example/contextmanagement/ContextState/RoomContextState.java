package com.example.contextmanagement.ContextState;

public class RoomContextState {


    public RoomContextState(int id, int floor,String name, String buildingId) {
        super();
        this.id = id;
        this.name = name;
        this.floor = floor;
        this.buildingId = buildingId;
    }


    private int id;
    private String name;
    private int floor;
    private String buildingId;


    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getFloor() {

        return floor;
    }

    public void setFloor(int floor) {

        this.floor = floor;
    }

    public String getBuildingId()
    {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {

        this.buildingId = buildingId;
    }


}
