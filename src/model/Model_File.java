
package model;

public class Model_File {

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Model_File(int fileID, String fileExtension, String fileName) {
        this.fileID = fileID;
        this.fileExtension = fileExtension;
        this.fileName = fileName;
    }

    public Model_File() {
    }

    private int fileID;
    private String fileName; //
    private String fileExtension;
}