package server;

import java.io.*;
import java.net.*;

/**
 * Created by Julia on 24.05.2015.
 */
public class Server {
    public static void main(String[] args) {
        Server server=new Server();
        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() throws IOException {
        int pacSize = 1000;
        int port = 4444;
        byte data[] = new byte[pacSize];
        System.out.println("Server starts...");
        DatagramPacket[] pacs = new DatagramPacket[4];
        for (int pac = 0; pac < 4; pac++) {
            pacs[pac] = new DatagramPacket(data, data.length);
        }
        DatagramSocket s = new DatagramSocket(port);
        try {
            s.setSoTimeout(300000);
            for (int pac = 0; pac < 4; pac++) {
                s.receive(pacs[pac]);
            }

            for (int pac = 0; pac < 4; pac++) {
                s.send(pacs[pac]);
            }

            s.setSoTimeout(600);
            while (true) {
                for (int pac = 0; pac < 4; pac++) {
                    s.receive(pacs[pac]);
                }
                //TODO: run Engine
                //TODO: pacs = ...

                for (int pac = 0; pac < 4; pac++) {
                    s.send(pacs[pac]);
                }
            }
        } catch (SocketTimeoutException e) {
            // если время ожидания вышло
            System.out.println("Истекло время ожидания, прием данных закончен");
        }
    }

}
