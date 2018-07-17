package com.live.socketproject;

/**
 * Created by Administrator on 2018/6/13.
 */

public class AlertInformation {
    private String idOfBuilding;
    private String cellOfBuilding;
    private String idOfRoom;
    private String alertCategory;
    private String alertLevel;
    private String alretTime;
    private String alertStatus;



    public String getIdOfBuilding() {
        return idOfBuilding;
    }

    public void setIdOfBuilding(String idOfBuilding) {
        this.idOfBuilding = idOfBuilding;
    }

    public String getCellOfBuilding() {
        return cellOfBuilding;
    }

    public void setCellOfBuilding(String cellOfBuilding) {
        this.cellOfBuilding = cellOfBuilding;
    }


    public String getIdOfRoom() {
        return idOfRoom;
    }

    public void setIdOfRoom(String idOfRoom) {
        this.idOfRoom = idOfRoom;
    }

    public String getAlertCategory() {
        return alertCategory;
    }

    public void setAlertCategory(String alertCategory) {
        this.alertCategory = alertCategory;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getAlretTime() {
        return alretTime;
    }

    public void setAlretTime(String alretTime) {
        this.alretTime = alretTime;
    }

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }
}
