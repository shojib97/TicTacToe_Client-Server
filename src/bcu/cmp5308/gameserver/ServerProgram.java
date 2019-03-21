
package bcu.cmp5308.gameserver;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;

/**
 * This class provides start the console's thread and 
 * starts game server listening 
 * @author Andrew Kay
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class ServerProgram {
    private static final int DEFAULT_PORT = 9089;
    
    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if(args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        
        String ip = Inet4Address.getLocalHost().getHostAddress();
        System.out.println("Opening socket on " + ip + " port " + port + ".");
        ServerSocket socket = new ServerSocket(port);
        GameServer gameServer = new GameServer(socket);
        
        System.out.println("Starting console.");
        new Thread(gameServer.getConsole()).start();
        
        System.out.println("Waiting for connections.");
        gameServer.startListening();
    }
}
