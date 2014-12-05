/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

 */
package chatclientserverp2p;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Riby
 */
public class PeerEventListner implements Runnable {

    public static HashSet<Socket> ConnectionArray = new HashSet();//can resize them array
    public static HashSet<String> CurrentUsers = new HashSet();
    public static int PORT;
    public static int rec;

    public PeerEventListner(int PORT) {
        this.PORT = PORT;
        this.rec=1;
    }

    public void run() {

        
            try {

                ServerSocket SERVER = new ServerSocket(PORT);
                System.out.println("Waiting for clients..");
                while (true) {
                    Socket SOCK = SERVER.accept();
                    if(rec==1)
                    {
                    ConnectionArray.add(SOCK);
                    rec++;
                    AddUserName(SOCK);
                    }
                    else{
                    System.out.println("Client Connected from " + SOCK.getLocalAddress().getHostName());
                    
                    printInput(SOCK);
                    }
                   
                }

            } catch (Exception x) {
                System.out.print(x);
            }
        
    }
    
    //send input to all peers
    public static void printInput(Socket X) throws IOException
    {
    Scanner INPUT = new Scanner(X.getInputStream());
    String MESSAGE = INPUT.nextLine();
      Iterator itr = PeerEventListner.ConnectionArray.iterator();
        while (itr.hasNext()) {
            System.out.println(itr);

            Socket TEMP_SOCK = (Socket) itr.next();
            PrintWriter OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
           
            OUT.println(MESSAGE);
            OUT.flush();
        }
    
    }

    //add user
    public static void AddUserName(Socket X) throws IOException {
        Scanner INPUT = new Scanner(X.getInputStream());
        String UserName = INPUT.nextLine();
        CurrentUsers.add(UserName);

        Iterator itr = PeerEventListner.ConnectionArray.iterator();
        while (itr.hasNext()) {
            System.out.println(itr);

            Socket TEMP_SOCK = (Socket) itr.next();
            PrintWriter OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
            System.out.println("add user" + CurrentUsers);
            OUT.println("#?!" + CurrentUsers);
            OUT.flush();
        }

    }
    //remove user
    public static void RemoveUserName(String UserName, Socket SOCK) throws IOException {

        System.out.println(PeerEventListner.ConnectionArray + "before");
        System.out.println(PeerEventListner.CurrentUsers + "Before");
        PeerEventListner.ConnectionArray.remove(SOCK);
        PeerEventListner.CurrentUsers.remove(UserName);

        System.out.println("Afer" + PeerEventListner.CurrentUsers);
        System.out.println("asdasd" + PeerEventListner.ConnectionArray);

        Iterator itr = PeerEventListner.ConnectionArray.iterator();
        while (itr.hasNext()) {
            System.out.println(itr);
            Socket TEMP_SOCK = (Socket) itr.next();
            PrintWriter OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
            OUT.println(CurrentUsers);
            OUT.flush();
        }

    }

}
