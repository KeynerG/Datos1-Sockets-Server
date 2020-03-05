package com.sockets.server;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 * Clase encargada de aceptar a los clientes y almacenarlos en hilos.
 */
public class Server
{

    static Vector<ClientHandler> ar = new Vector<>();

    // count of clients
    static int i = 1;

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(1234);
        Socket s;

        /**
         * Ciclo encargado de aceptar clientes y crear los hilos correspondientes
         */
        while (true) {

            s = ss.accept();

            InetAddress address = s.getInetAddress();
            String hostIP = address.getHostAddress();
            System.out.println(hostIP);

            System.out.println("New client request received : " + s);

            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Creating a new handler for this client...");

            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(s, hostIP,"client " + i, dis, dos);

            // Create a new Thread with this object.
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");

            // add this client to active clients list
            ar.add(mtch);

            // start the thread.
            t.start();

            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;

        }
    }
}

