/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


/**
 *
 * @author Chuong Tran
 */
public class Model_History_Message {

    /**
     * @return the messageType
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the fromUserID
     */
    public int getFromUserID() {
        return fromUserID;
    }

    /**
     * @param fromUserID the fromUserID to set
     */
    public void setFromUserID(int fromUserID) {
        this.fromUserID = fromUserID;
    }

    /**
     * @return the toUserID
     */
    public int getToUserID() {
        return toUserID;
    }

    /**
     * @param toUserID the toUserID to set
     */
    public void setToUserID(int toUserID) {
        this.toUserID = toUserID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mess
     */
    public String getMess() {
        return mess;
    }

    /**
     * @param mess the mess to set
     */
    public void setMess(String mess) {
        this.mess = mess;
    }
    
    public Model_History_Message( int fromUserID, int toUserID, String userName,int messageType, String text) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.name = userName;
        this.toUserID = toUserID;
        this.mess = text;
    }

    public Model_History_Message() {
    }

    private int messageType;
    private int fromUserID;
    private int toUserID;
    private String name;
    private String mess;

}
