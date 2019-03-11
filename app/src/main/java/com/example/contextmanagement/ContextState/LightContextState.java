package com.example.contextmanagement.ContextState;

public class LightContextState {


    public LightContextState(int id, int level, String status, int roomId) {
        super();
        this.id = id;
        this.level = level;
        this.status = status;
        this.roomId = roomId;
    }

    private int id;
    private int level;
    private String status;
    private int roomId;

    public String getStatus() {

        return this.status;
    }

    public void setStatus(String status) {

        this.status = status;
    }


    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getLevel() {

        return level;
    }

    public void setLevel(int level) {

        this.level = level;
    }

    public int getRoomId() {

        return roomId;
    }

    public void setRoomId(int roomId) {

        this.roomId = roomId;
    }
}
