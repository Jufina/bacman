package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Julia on 24.05.2015.
 */
public class ClientService extends  Thread {
    private Socket fromclient;
    public ClientService(Socket fromclient) {
        this.fromclient=fromclient;
    }
    public void run() {
        try {
            BufferedReader in = null;
            PrintWriter out= null;
            in = new BufferedReader(new
                    InputStreamReader(fromclient.getInputStream()));
            out = new PrintWriter(fromclient.getOutputStream(), true);
            String input, output;
            System.out.println("Wait for messages");
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")) break;
                out.println("S ::: " + input);
                System.out.println(input);
                //TODO: Main Logic
            }
            out.close();
            in.close();
            fromclient.close();

        }
        catch (IOException e) {
            System.out.println("Can't read");
            System.exit(-1);
        }
    }
}
