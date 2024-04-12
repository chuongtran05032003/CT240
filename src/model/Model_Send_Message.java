
package model;

public class Model_Send_Message {


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Model_Send_Message(int messageType, int fromUserID, String userName, int toUserID, String text, String fileName) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.userName = userName;
        this.toUserID = toUserID;
        this.text = text;
        this.fileName = fileName;
    }

    public Model_Send_Message() {
    }

    private int messageType;
    private int fromUserID;
    private String userName;
    private int toUserID;
    private String text;
    private String fileName;
}