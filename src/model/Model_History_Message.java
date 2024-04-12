
package model;

public class Model_History_Message {

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(int fromUserID) {
        this.fromUserID = fromUserID;
    }

    public int getToUserID() {
        return toUserID;
    }

    public void setToUserID(int toUserID) {
        this.toUserID = toUserID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
    
    public Model_History_Message( int fromUserID, int toUserID, String userName,int messageType, String text, int side) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.name = userName;
        this.toUserID = toUserID;
        this.mess = text;
        this.side = side;
    }

    public Model_History_Message() {
    }

    private int messageType;
    private int fromUserID;
    private int toUserID;
    private String name;
    private String mess;
    private int side;

}
