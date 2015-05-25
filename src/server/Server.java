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
    public void run() throws IOException {
        gameEngine=new GameEngine(1);
        Map<Integer, Integer> idPlayers=new HashMap<Integer, Integer>();
        int pacSize = 20*20+6;
        int port = 4444;
        byte data[] = new byte[pacSize];
        System.out.println("Server starts...");
        DatagramPacket[] pacs = new DatagramPacket[4];
        for (int pac = 0; pac < 4; pac++) {
            pacs[pac] = new DatagramPacket(data, data.length);
        }
        DatagramSocket s = new DatagramSocket(port);

        byte[] idpl=new byte[4];
        try {
            s.setSoTimeout(300000);
            for (int pac = 0; pac < 4; pac++) {
                s.receive(pacs[pac]);
                System.out.println("КЛИЕНТ ПОДКЛЮЧИЛСЯ - "+pacs[pac].getPort());
                idPlayers.put(pacs[pac].getPort(),pac);
                // JULIA//

                idpl[pac]=(byte)pacs[pac].getPort();
                System.out.println(idpl[pac]+" - "+pac);
                pacs[pac].setData(idpl);
                System.out.println(pacs[pac].getData()[0]);
                System.out.println(pacs[pac].getData()[1]);
                System.out.println(pacs[pac].getData()[2]);
                System.out.println(pacs[pac].getData()[3]);

            }

            for (int pac = 0; pac < 4; pac++) {

                s.send(pacs[pac]);
                byte[] tr = gameEngine.getResult();
                pacs[pac].setData(tr);
                s.send(pacs[pac]);
            }

            s.setSoTimeout(60000);
            while (true) {
                byte[] movepl=new byte[4];
                for (int pac = 0; pac < 4; pac++) {
                    s.receive(pacs[pac]);
                    movepl[idPlayers.get(pacs[pac].getPort())]=pacs[pac].getData()[0];
                }
                byte[] result;
                result=gameEngine.rebuildingMap(movepl);
                try {
                    Thread.sleep(300);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
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
