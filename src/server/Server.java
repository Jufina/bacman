package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julia Matrokhina on 24.05.2015.
 */
public class Server {
    private GameEngine gameEngine;
    public static void main(String[] args) {
        Server server=new Server();
        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   public void Server() {
       gameEngine=new GameEngine(1);
   }
    public void run() throws IOException {
        Map<Integer, Integer> idPlayers=new HashMap<Integer, Integer>();
        int pacSize = 20*20+2;
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
                System.out.println("КЛИЕНТ ПОДКЛЮЧИЛСЯ - "+pacs[pac].getPort());
                idPlayers.put(pacs[pac].getPort(),pac);
            }

            for (int pac = 0; pac < 4; pac++) {
                pacs[pac].setData(gameEngine.getResult());
                s.send(pacs[pac]);
            }

            s.setSoTimeout(600);
            while (true) {
                byte[] movepl=new byte[4];
                for (int pac = 0; pac < 4; pac++) {
                    s.receive(pacs[pac]);
                    movepl[idPlayers.get(pacs[pac].getPort())]=pacs[pac].getData()[0];
                }
                byte[] result;
                result=gameEngine.rebuildingMap(movepl);

                for (int pac = 0; pac < 4; pac++) {
                    pacs[pac].setData(result);
                    s.send(pacs[pac]);
                }
            }
        } catch (SocketTimeoutException e) {
            // если время ожидания вышло
            System.out.println("Истекло время ожидания, прием данных закончен");
        }
    }

}
