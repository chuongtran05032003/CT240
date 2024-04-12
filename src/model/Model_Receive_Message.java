
package model;

public class Model_Receive_Message {

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Model_File_Receiver getDataFile() {
        return dataFile;
    }

    public void setDataFile(Model_File_Receiver dataFile) {
        this.dataFile = dataFile;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Model_Receive_Image getDataImage() {
        return dataImage;
    }

    public void setDataImage(Model_Receive_Image dataImage) {
        this.dataImage = dataImage;
    }

    public Model_Receive_Message(int messageType, int fromUserID ,String userName, int userID, String text, Model_Receive_Image dataImage) {
        this.messageType = messageType;
        this.fromUserID = fromUserID;
        this.userName = userName;
        this.userID = userID;
        this.text = text;
        this.dataImage = dataImage;
    }

    public Model_Receive_Message() {
    }

    private int messageType;
    private int fromUserID;
    private String userName;
    private int userID;
    private String text;
    private Model_Receive_Image dataImage;
    private Model_File_Receiver dataFile;
}