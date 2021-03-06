package com.sockets.server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Clase encargada de manejar los datos de I/O de los clientes.
 */
class ClientHandler implements Runnable
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
    private String ip;

    /**
     * Constructor de la clase.
     * @param s
     * @param ip
     * @param name
     * @param dis
     * @param dos
     */
    public ClientHandler(Socket s, String ip, String name, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
        this.ip = ip;
    }

    @Override
    public void run() {

        String received;

        /**
         * Ciclo que se encarga de recivir y enviar los mensajes a los respectivos clientes.
         */
        while (true) {
            try {
                // receive the string
                received = dis.readUTF();

                System.out.println(received);

                if(received.equals("logout")){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }

                // break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();
                String nameC = st.nextToken();

                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users

                for (ClientHandler mc : Server.ar) {
                    // if the recipient is found, write on its
                    // output stream
                    if(!mc.name.equals(nameC)){
                        if (mc.ip.equals(recipient) && mc.isloggedin==true) {
                            mc.dos.writeUTF(this.name+" : "+MsgToSend);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}