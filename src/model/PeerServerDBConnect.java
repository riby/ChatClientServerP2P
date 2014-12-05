package model;

/**
 *
 * @author Riby
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import chatclientserverp2p.ConnectionP2P;
import com.mysql.jdbc.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Riby
 */
public class PeerServerDBConnect {

    private Connection con;
    private Statement st;
    private ResultSet rs;

    public PeerServerDBConnect() {

        try {
    //connection to mysql using jdbc
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/server_list", "root", "");
            st = (Statement) con.createStatement();

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public Map<Integer, Boolean> getServerList() {
    
        //get peer list from server
        Map<Integer, Boolean> PORT = new HashMap<Integer, Boolean>();
        try {

            String query = "select * from `server_info`";
            rs = st.executeQuery(query);
            System.out.println("Records from database");

            while (rs.next()) {
                Integer server_port = rs.getInt("server_port");
                Boolean status = rs.getBoolean("status");
                System.out.println("Port" + server_port + " Status" + status);

                PORT.put(server_port, status);

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return PORT;
    }

    //get name list
    public Map<Integer, String> getOnlinePeerNameList() {
        Map<Integer, String> PORT = new HashMap<Integer, String>();
        try {

            String query = "select * from `server_info`";
            rs = st.executeQuery(query);
            System.out.println("Records from database");

            while (rs.next()) {
                Integer server_port = rs.getInt("server_port");
                String user_name = rs.getString("user_name");
                Boolean status = rs.getBoolean("status");
                System.out.println("Port" + server_port + " Status" + user_name);

                if (!status) {
                    PORT.put(server_port, user_name);
                }

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return PORT;

    }
    
    //getPort number from username
    public Integer getPortNumber(String user) throws SQLException {

        String query = "select * from `server_info` where user_name ='" + user + "'";
        rs = st.executeQuery(query);
        System.out.println("Records from database");
        Integer server_port = 0;
        while (rs.next()) {
            server_port = rs.getInt("server_port");
        }
        return server_port;
    }

    public void updateServerStatus(Integer server_port, int status, String user_name) throws SQLException {

        String q = "UPDATE `server_list`.`server_info` SET `status` = '" + status + "',`user_name` ='" + user_name + "' WHERE `server_info`.`server_port` =" + server_port;
        st.executeUpdate(q);
        System.out.println("updated records");

    }

    public void updateServerStatus(Integer server_port, int status) throws SQLException {

        String q = "UPDATE `server_list`.`server_info` SET `status` = '" + status + "' WHERE `server_info`.`server_port` =" + server_port;
        st.executeUpdate(q);
        System.out.println("updated records");

    }

}
