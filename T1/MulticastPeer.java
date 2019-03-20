import java.net.*;
import java.io.*;
import java.util.Scanner;

public class MulticastPeer{
    public static void main(String args[]){
		// args give message contents and destination multicast group (e.g. "228.5.6.7")
    MulticastSocket s =null;

		try {
      s = new MulticastSocket(6789);

      // Cria thread ouvinte
      Connection ouvinte = new Connection(s);

      InetAddress group = InetAddress.getByName(args[0]);
      s.joinGroup(group);

      Scanner keyboard = new Scanner(System.in);
      String text = "init";

      while(true){
        text= keyboard.nextLine();

        System.out.println(text);

        byte [] m = text.getBytes();
  			DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);

        s.send(messageOut);
        }

        //s.leaveGroup(group);
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("IO: " + e.getMessage());
		}finally {if(s != null) s.close();}
	}
}

class Connection extends Thread {
	MulticastSocket clientSocket;

	public Connection (MulticastSocket aClientSocket) {
			clientSocket = aClientSocket;
			this.start();
	}
	public void run(){
		try {			                 // an echo server
      byte[] buffer = new byte[1000];
      while(true){
      DatagramPacket in = new DatagramPacket(buffer, buffer.length);
      clientSocket.receive(in);
      System.out.println("Received:" + new String(in.getData()));
    }
		} catch(IOException e) {System.out.println("readline:"+e.getMessage());}
	}
}

