
package service;

import app.MessageType;
import connection.DatabaseConnection;
import model.Model_File;
import model.Model_File_Receiver;
import model.Model_Package_Sender;
import model.Model_Receive_Image;
import model.Model_Send_Message;
import swing.blurHash.BlurHash;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import model.Model_File_Sender;

public class ServiceFile {

    public ServiceFile() {
        this.con = DatabaseConnection.getInstance().getConnection();
        this.fileReceivers = new HashMap<>();
        this.fileSenders = new HashMap<>();
    }

    public Model_File addFileReceiver(String fileExtension, String fileName) throws SQLException { //
        Model_File data;
        PreparedStatement p = con.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
        p.setString(1, fileExtension);
        p.setString(2, fileName);
        p.execute();
        ResultSet r = p.getGeneratedKeys();
        r.first();
        int fileID = r.getInt(1);
        data = new Model_File(fileID, fileExtension, fileName); //
        r.close();
        p.close();
        return data;
    }

    public void updateBlurHashDone(int fileID, String blurhash) throws SQLException {
        PreparedStatement p = con.prepareStatement(UPDATE_BLUR_HASH_DONE);
        p.setString(1, blurhash);
        p.setInt(2, fileID);
        p.execute();
        p.close();
    }

    public void updateDone(int fileID) throws SQLException {
        PreparedStatement p = con.prepareStatement(UPDATE_DONE);
        p.setInt(1, fileID);
        p.execute();
        p.close();
    }

    public void initFile(Model_File file, Model_Send_Message message) throws IOException {
        fileReceivers.put(file.getFileID(), new Model_File_Receiver(message, toFileObject(file)));
    }

    public Model_File getFile(int fileID) throws SQLException {
        PreparedStatement p = null;
        ResultSet r = null;
        try {
            p = con.prepareStatement(GET_FILE_EXTENSION);
            p.setInt(1, fileID);
            r = p.executeQuery();
            if (r.next()) {
                String fileExtension = r.getString(1);
                String fileName = r.getString(2); //
                return new Model_File(fileID, fileExtension, fileName); //
            } else {
                System.out.println("error getfile");
                return null;
            }
        } finally {
            // Close resources in a finally block to ensure they are always closed
            if (r != null) {
                r.close();
            }
            if (p != null) {
                p.close();
            }
        }
    }

    public synchronized Model_File initFile(int fileID) throws IOException, SQLException {
        Model_File file;
        if (!fileSenders.containsKey(fileID)) {
            file = getFile(fileID);
            String fn = file.getFileID() + "_" + file.getFileName();
            fileSenders.put(fileID, new Model_File_Sender(file, new File(PATH_FILE + fn)));
        } else {
            file = fileSenders.get(fileID).getData();
        }
        return file;
    }

    public synchronized byte[] getFileData(int fileID) throws IOException, SQLException {
        initFile(fileID);
        return fileSenders.get(fileID).read();
    }
    



    public long getFileSize(int fileID) {
        return fileSenders.get(fileID).getFileSize();
    }

    public void receiveFile(Model_Package_Sender dataPackage) throws IOException {
        if (!dataPackage.isFinish()) {
            fileReceivers.get(dataPackage.getFileID()).writeFile(dataPackage.getData());
        } else {
            fileReceivers.get(dataPackage.getFileID()).close();
        }
    }

    public Model_Send_Message closeFile(Model_Receive_Image dataImage) throws IOException, SQLException {
        Model_File_Receiver file = fileReceivers.get(dataImage.getFileID());
        if (file.getMessage().getMessageType() == MessageType.IMAGE.getValue()) {
            //  Image file
            //  So create blurhash image string
            file.getMessage().setText("");
            String blurhash = convertFileToBlurHash(file.getFile(), dataImage);
            updateBlurHashDone(dataImage.getFileID(), blurhash);
        } else {
            updateDone(dataImage.getFileID());
        }
        fileReceivers.remove(dataImage.getFileID());
        //  Get message to send to target client when file receive finish
        return file.getMessage();
    }

    private String convertFileToBlurHash(File file, Model_Receive_Image dataImage) throws IOException {
        BufferedImage img = ImageIO.read(file);
        Dimension size = getAutoSize(new Dimension(img.getWidth(), img.getHeight()), new Dimension(200, 200));
        //  Convert image to small size
        BufferedImage newImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(img, 0, 0, size.width, size.height, null);
        String blurhash = BlurHash.encode(newImage);
        dataImage.setWidth(size.width);
        dataImage.setHeight(size.height);
        dataImage.setImage(blurhash);
        return blurhash;
    }

    private Dimension getAutoSize(Dimension fromSize, Dimension toSize) {
        int w = toSize.width;
        int h = toSize.height;
        int iw = fromSize.width;
        int ih = fromSize.height;
        double xScale = (double) w / iw;
        double yScale = (double) h / ih;
        double scale = Math.min(xScale, yScale);
        int width = (int) (scale * iw);
        int height = (int) (scale * ih);
        return new Dimension(width, height);
    }

    private File toFileObject(Model_File file) {
        String fn = file.getFileID() + "_" + file.getFileName();
        return new File(PATH_FILE + fn);
    }

    //  SQL
    private final String PATH_FILE = "server_data/";
    private final String INSERT = "insert into files (fileExtension, fileName) values (?, ?)"; //
    private final String UPDATE_BLUR_HASH_DONE = "update files set BlurHash=?, `Status`='1' where fileID=? limit 1";
    private final String UPDATE_DONE = "update files set `Status`='1' where FileID=? limit 1";
    private final String GET_FILE_EXTENSION = "select fileExtension, fileName from files where fileID= ? limit 1;"; //
    //  Instance
    private final Connection con;
    private final Map<Integer, Model_File_Receiver> fileReceivers;
    private final Map<Integer, Model_File_Sender> fileSenders;
}
