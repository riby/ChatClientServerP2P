/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
Ravneet Singh Sidhu---------1001106510
Brunda Padmaraju------------1001095203
AnuroopSai Potthuru----------1001095202
Suhaib Siraj Shaik-------------1001106493
 */
package chatclientserverp2p;


import static chatclientserverp2p.RegisterPeerOnNetwork.PORT;
import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.PeerServerDBConnect;

/**
 *
 * make connection using p2p network with sockets communication
 * @author Riby
 */
public class ConnectionP2P implements Runnable {

     
    Socket SOCK;
    Scanner INPUT;
    Scanner SEND = new Scanner(System.in);
    PrintWriter OUT;
    private volatile boolean isRunning = true;
     private PeerServerDBConnect ser = new PeerServerDBConnect();

    public ConnectionP2P(Socket X) {

        this.SOCK = X;
    }

    public void run() {

        try {
            
            try {
                INPUT = new Scanner(SOCK.getInputStream());
                OUT = new PrintWriter(SOCK.getOutputStream());
                OUT.flush();
                CheckStream();

            } finally {
                SOCK.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(ConnectionP2P.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void DISCONNECT() throws IOError, IOException, SQLException {
       
        System.out.println(SOCK.getPort()); 
        
        ser.updateServerStatus(SOCK.getPort(), 1);
        SEND("Disconnected, refresh the list");
       
        JOptionPane.showMessageDialog(null,ChatGui.UserName+"Disconnected, refresh the list");
        System.exit(0);
    }

    public void CheckStream() {

        while (isRunning) {
            RECEIVE();
        }
    }

    public void kill() {

        isRunning = false;
    }

    public void RECEIVE() {
        if (INPUT.hasNext()) {
            String MESSAGE = INPUT.nextLine();

           if (MESSAGE.contains("#?!")) {
                System.out.println(MESSAGE);
                String tmp = MESSAGE.substring(3);
                tmp = tmp.replace("[", "");
                tmp = tmp.replace("]", "");

                String[] CurrentUsers = tmp.split(", ");
                System.out.println(CurrentUsers);


            } else {
            
                ChatGui.chatTA.append(MESSAGE + "\n");
            
           }
        }
    }

    //send data to all connected peers
    public void SEND(String X) throws IOException {

      Map<Integer, Boolean> PORTNUMS = new HashMap<Integer, Boolean>();
      HashSet<Socket> ConnectionPeer = new HashSet();
        //get ports
       
        PORTNUMS=ser.getServerList();
           final String HOST = "localhost";
           
        Set<Integer> key = PORTNUMS.keySet();
        for (Integer k : key) {
            if (!PORTNUMS.get(k))// if not equal to true
            {
                Socket SOCK = new Socket(HOST, k);
                ConnectionPeer.add(SOCK);
               
            }
        }
        
          Iterator itr =ConnectionPeer.iterator();
          
                while (itr.hasNext()) {
                System.out.println(itr);

                Socket TEMP_SOCK = (Socket) itr.next();
                PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
                TEMP_OUT.println(ChatGui.UserName +" : "+X);
                TEMP_OUT.flush();

                System.out.println( X);

            }
        
        ChatGui.messageTF.setText("");
        

    }

}
