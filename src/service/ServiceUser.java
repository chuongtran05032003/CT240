/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;


import connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Model_Client;
import model.Model_History_Message;

import model.Model_Login;
import model.Model_Message;
import model.Model_Register;
import model.Model_User_Account;

public class ServiceUser {
    
    public ServiceUser() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public Model_Message register(Model_Register data) {
        // Check user exists
        Model_Message message = new Model_Message();
        try {
            PreparedStatement p = con.prepareStatement(CHECK_USER);
            p.setString(1, data.getUserName());
            ResultSet r = p.executeQuery();
            if (r.next()) { // Move to the first row
                int count = r.getInt(1);
                if (count > 0) {
                    message.setAction(false);
                    message.setMessage("User Already Exists");
                } else {
                    message.setAction(true);
                }
            } else {
                // No rows returned by the query
                message.setAction(true); // Proceed with user registration
            }

            r.close();
            p.close();

            if (message.isAction()) {
                //  Insert User Register
                con.setAutoCommit(false);
                p = con.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
                p.setString(1, data.getUserName());
                p.setString(2, data.getPassword());
                p.execute();
                r = p.getGeneratedKeys();
                r.first();
                int userID = r.getInt(1);
                r.close();
                p.close();
                //  Create user account
                p = con.prepareStatement(INSERT_USER_ACCOUNT);
                p.setInt(1, userID);
                p.setString(2, data.getUserName());
                p.setInt(3, 1);
                p.execute();
                p.close();
                con.commit();
                con.setAutoCommit(true);
                message.setAction(true);
                message.setMessage("Insert successfully");
                message.setData(new Model_User_Account(userID, data.getUserName(), 1));
            }
        } catch (SQLException e) {
            System.out.println(e);
            message.setAction(false);
            message.setMessage("Server Error");
            try {
                if (!con.getAutoCommit()) {
                    con.rollback();
                    con.setAutoCommit(true);
                }
            } catch (SQLException e1) {
                // Handle rollback exception
                e1.printStackTrace();
            }
        }
        return message;
    }

    public Model_Message login(Model_Login login) {
        Model_Message message = new Model_Message();
        try {
            PreparedStatement p = con.prepareStatement(LOGIN);
            p.setString(1, login.getUserName());
            p.setString(2, login.getPassword());
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    int userID = r.getInt(1);
                    String userName = r.getString(2);
                    int status = r.getInt(3);
                    message.setData(new Model_User_Account(userID, userName, status));
                    message.setAction(true);
                    message.setMessage("Login success");
                } else {
                    message.setAction(false);
                    message.setMessage("Wrong username or password");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            message.setAction(false);
            message.setMessage("Login fail");
        }
        return message;
    }
    
    public List<Model_User_Account> getUser(int exitUser) throws SQLException {
        List<Model_User_Account> list = new ArrayList<>();
        PreparedStatement p = con.prepareStatement(SELECT_USER_ACCOUNT);
        p.setInt(1, exitUser);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            int userID = r.getInt(1);
            String userName = r.getString(2);
            list.add(new Model_User_Account(userID, userName, checkUserStatus(userID)));
        }
        r.close();
        p.close();
        return list;
    }
    
    
    private int checkUserStatus(int userID) {
        List<Model_Client> clients = Service.getInstance(null).getListClient();
        for (Model_Client c : clients) {
            if (c.getUser().getUserID() == userID) {
                return 1;
            }
        }
        return 0;
    }
    
    public void saveMess(Model_History_Message data){
        try {
            PreparedStatement p = con.prepareStatement(INSERT_MESS);
            p.setInt(1, data.getFromUserID());
            p.setInt(2, data.getToUserID());
            p.setString(3, data.getName());
            p.setInt(4, data.getMessageType());
            p.setString(5, data.getMess());
            p.execute();
            p.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public List<Model_History_Message> getMessages(int userID) throws SQLException {

        List<Model_History_Message> messages = new ArrayList<>();
        PreparedStatement p = con.prepareStatement(SELECT_MESS);
        p.setInt(1, userID);
        p.setInt(2, userID);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            int idFrom = r.getInt(2);
            int idTo = r.getInt(3);
            String name = r.getString(4);
            int type  = r.getInt(5);
            String mess = r.getString(6);
            messages.add(new Model_History_Message(idFrom, idTo, name, type, mess));
        }
        r.close();
        p.close();
        return messages;
    }
    //  SQL

    private final String LOGIN = "SELECT user.userID, user_account.userName, user_account.status FROM user JOIN user_account ON user.userID = user_account.userID WHERE user.userName = BINARY ? AND user.pass = BINARY ?";
    private final String SELECT_USER_ACCOUNT = "SELECT * FROM user_account WHERE userID <> ?";
    private final String INSERT_USER = "insert into user (userName, pass) values (?,?)";
    private final String INSERT_USER_ACCOUNT = "insert into user_account (userID, userName, status) values (?,?,?)";
    private final String CHECK_USER = "SELECT 1 FROM user WHERE username = ? LIMIT 1";
    private final String INSERT_MESS = "insert into history_message (idFrom, idTo, name, type, mess) values (?,?,?,?,?)";
    private final String SELECT_MESS = "SELECT * FROM history_message WHERE idFrom = ? or idTo = ?";
    //  Instance
    private final Connection con;
}

