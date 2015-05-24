package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Julia on 24.05.2015.
 */
public class Server {
    public static void main(String[] args) {
        Server server=new Server();
        server.run();
    }
    public void run() {
        System.out.println("Добро пожаловать на тёмную сторону!");
        BufferedReader in = null;
        PrintWriter out= null;

        ServerSocket servers = null;
        Socket fromclient = null;

        // create server socket
        try {
            servers = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 4444");
            System.exit(-1);
        }
        int cntClients=0;
        while(cntClients != 4) {
            try {
                System.out.print("Waiting for a client...");
                fromclient = servers.accept();
                System.out.println("Client connected");
                cntClients++;
                ClientService cs=new ClientService(fromclient);
                cs.start();
            } catch (IOException e) {
                System.out.println("Can't accept");
                System.exit(-1);
            }
        }



        try {
            servers.close();
        }
        catch (IOException e) {
            System.out.println("Can't accept");
            System.exit(-1);
        }

    }

}
