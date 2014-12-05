package model;


/*
*/
import chatclientserverp2p.ConnectionP2P;
import com.mysql.jdbc.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.text.html.parser.DTDConstants;

public class PeerRegisterDBConnect {

    private Connection con;
    private Statement st;
    private ResultSet rs;

    //connect to database with jdbc driver
    public PeerRegisterDBConnect() {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");
            st = (Statement) con.createStatement();

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    //add user with password
    //md5 authentication is perfomed using salt
    public void registerUser(String uname, String pass) throws SQLException, NoSuchAlgorithmException {

        String salt = "QASJLDAJSLJ$SaltValue#WAKSHDHAHKters12@$@4&#%^$*";
        String hash = md5(pass + salt);

        String query = "INSERT INTO `login`.`login_info` (`username`, `password`) VALUES ('" + uname + "', '" + hash + "')";

        st.executeUpdate(query);
        System.out.println("updated records");

    }

    //check user from db
    public boolean checkUser(String uname) throws SQLException {
        String q = "SELECT * FROM `login_info` WHERE username='" + uname + "'";

        rs = st.executeQuery(q);

        if (rs.next() == true) {
            return true;
        } else {
            return false;
        }
    }

    // check login from db
    public boolean loginCheck(String name, String password) throws SQLException {

        rs = null;
        String salt = "QASJLDAJSLJ$SaltValue#WAKSHDHAHKters12@$@4&#%^$*";
        String hash = md5(password + salt);
        String q = "SELECT * FROM `login_info` WHERE username='" + name + "' and password='" + hash + "'";
        rs = st.executeQuery(q);

        if (rs.next() == true) {
            return true;
        } else {
            return false;
        }
    }

    public void getData() {
        try {

            String query = "select * from login_info";
            rs = st.executeQuery(query);
            System.out.println("Records from database");

            while (rs.next()) {
                String name = rs.getString("username");
                String password = rs.getString("password");
                System.out.println("Name" + name + " Password" + password);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    //perform MD5 
    public static String md5(String input) {

        String md5 = null;

        if (null == input) {
            return null;
        }

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }
}
