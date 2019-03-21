
package bcu.cmp5308.gameserver;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class game server thread and 
 * @author Andrew Kay & Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class GameServer {
    public class Console implements Runnable {
        @Override
        public void run() {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(isr);
            try {
                while(!isClosed()) {
                    String cmd = in.readLine();
                    switch(cmd) {
                        case "CLOSE":
                            System.out.println("Server shutting down.");
                            close();
                            break;
                        
                        case "USERS":
                            System.out.println(connections.size() + " users.");
                            for(GameConnection conn : connections.values()) {
                                System.out.println(conn.getUsername());
                            }
                            break;
                        
                        case "WAITING":
                            System.out.println(channelsWaiting.size() + " users waiting.");
                            for(Map.Entry<String, GameConnection> e : channelsWaiting.entrySet()) {
                                String channelName = e.getKey();
                                GameConnection conn = e.getValue();
                                System.out.println(conn.getUsername() + " waiting in " + channelName);
                            }
                            break;
                        
                        default:
                            System.out.println("Server console commands: CLOSE, USERS, WAITING");
                            break;
                    }
                }
            } catch(IOException e) {
                System.err.println("Console error: " + e);
            }
        }
    }
    
    private ServerSocket socket;
    
    private Map<String, GameConnection> connections = new HashMap<>();
    private Map<String, GameConnection> channelsWaiting = new HashMap<>();
    private Random rng = new Random();
    
    public GameServer(ServerSocket socket) {
        this.socket = socket;
    }
    
    public boolean isClosed() {
        return socket.isClosed();
    }
    
    public void close() {
        if(!isClosed()) {
            for(GameConnection conn : connections.values()) {
                if(!conn.isClosed()) {
                    conn.send("500 >> ERROR Server shutdown.");
                    conn.close();
                }
            }
            
            try {
                socket.close();
            } catch(IOException e2) {
                System.err.println("While closing, server caused " + e2);
            }
        }
    }
    
    public void startListening() {
        try {
            while(!isClosed()) {
                GameConnection conn = new GameConnection(this, socket.accept());
                new Thread(conn).start();
            }
        } catch(IOException e) {
            System.err.println("Server error while listening: " + e);
        }
    }
    
    public Console getConsole() {
        return new Console();
    }
    
    boolean usernameIsAvailable(String username) {
        return !connections.containsKey(username);
    }
    boolean markIsAvailable(String mark) {
        return !connections.containsKey(mark);
    }
    void registerMark(GameConnection conn) throws GameProtocolException {
        String mark = conn.get_Mark();
        connections.put(mark, conn);
    }
    
     
    void registerUsername(GameConnection conn) throws GameProtocolException {
        String username = conn.getUsername();
        connections.put(username, conn);
    }
    
    void deregisterConnection(GameConnection conn) {
        String username = conn.getUsername();
        if(username != null) {
            connections.remove(username);
        }
        
        String channelName = conn.getChannelName();
        if(channelName != null) {
            channelsWaiting.remove(channelName);
        }
        String mark = conn.get_Mark();
        if(mark != null) {
            connections.remove(mark);
        }
    }
    
    void requestMatchup(GameConnection conn, String channelName) throws GameProtocolException {
        
        // remove the key from the map, returning the value if any
        GameConnection other = channelsWaiting.remove(channelName);
        
        if(other == null) {
            channelsWaiting.put(channelName, conn);
            conn.send(GameProtocol.OK + " >> "+ "WAITING %s", channelName);
        } else {
            other.setOpponent(conn);
            conn.setOpponent(other);
            
            // flip a coin to decide who goes first
            if(rng.nextBoolean()) {
                GameConnection tmp = other;
                other = conn;
                conn = tmp;
            }
            
            String msg = String.format(GameProtocol.OK + " >> " + "READY %s %s %s", channelName, conn.getUsername(), other.getUsername());
            conn.send(msg);
            other.send(msg);
        }
    }
    
 
}
