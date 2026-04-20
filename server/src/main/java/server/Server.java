package server;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    Server.java
*/

import java.util.*;
import java.io.*;
import java.net.*;

public class Server {
    public static final int PORT = 5000;

    private int MaxUsers;
    private Socket [] users;
    private UserThread [] threads;
    private int numUsers;

    public Server(int m) {
        MaxUsers = m;
        users = new Socket[MaxUsers];
        threads = new UserThread[MaxUsers];   // Set things up and start
        numUsers = 0; 
        
        try {
            runServer();
        }
        catch (Exception e) {
        System.out.println("Problem with server");
        }
    }

    private void runServer() throws IOException
    {
       ServerSocket s = new ServerSocket(PORT);
	   System.out.println("Started: " + s);
          
	   try {
          while(true) {
         
            if (numUsers < MaxUsers) {
              try {
                // wait for client
                Socket newSocket = s.accept();
                // set up streams and thread for client    
                synchronized (this) {
     	           users[numUsers] = newSocket;
                   ObjectInputStream in = new ObjectInputStream(newSocket.getInputStream());         
	               String newName = (String)in.readObject();

                   threads[numUsers] = new UserThread(newSocket, newName, numUsers, in);
                   threads[numUsers].start();
                   System.out.println("Connection " + newName + ", " + numUsers + users[numUsers]);
                   numUsers++;
                }
             }
             catch (Exception e) {
                System.out.println("Problem with connection...terminating");
             }
            }  // end if

          }  // end while
      }   // end try   
      finally {
         System.out.println("Server shutting down");  
         s.close();     
      }
    }

    public synchronized void removeClient(int id) {
        try                          
        {                             
            users[id].close();    
        }                            
        catch (IOException e)
        {
            System.out.println("Already closed");
        }

        System.out.println("removing: " + threads[id].username);

        users[id] = null;
        threads[id] = null;
        // fill up "gap" in arrays
        for (int i = id; i < numUsers-1; i++)           
        {                             
            users[i] = users[i+1];
            threads[i] = threads[i+1];
            threads[i].setId(i);
        }
        numUsers--;  
    }

    private class UserThread extends Thread {
         @SuppressWarnings("unused")
        private Socket mySocket;
         private ObjectInputStream myInputStream;
         private ObjectOutputStream myOutputStream;
         private int myId;
         public String username = "none";
         
         private UserThread(Socket newSocket, String user, int id, ObjectInputStream in) throws IOException {
            mySocket = newSocket;
            myId = id;
            username = user;
            myInputStream = in;
            myOutputStream = new ObjectOutputStream(newSocket.getOutputStream());
        }

        @SuppressWarnings("unused")
        public ObjectInputStream getInputStream()
        {
            return myInputStream;
        }

        @SuppressWarnings("unused")
        public ObjectOutputStream getOutputStream() {
            return myOutputStream;
        }

        public synchronized void setId(int newId) {
            myId = newId;   
        }

        private void respond(String msg) { // Use for sending confirmation and/or error messages
            try {
                myOutputStream.writeUTF(msg);

                myOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {  
            boolean alive = true;

            while (alive) // Where you want to track client data
            {
                try {

                }
                catch (Exception e) {
                    System.out.println("Error listening to client: "+ e.getMessage());
                    alive = false;
                }
            }
            removeClient(myId);
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        System.out.println("\n\nStarting server...\n\n");
        Server Server = new Server(2);
    }
}