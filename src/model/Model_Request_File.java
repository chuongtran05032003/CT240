
package model;

public class Model_Request_File {

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }


    public Model_Request_File(int fileID) {
        this.fileID = fileID;
    }

    public Model_Request_File() {
    }

    private int fileID;
    private String fileName;
}
