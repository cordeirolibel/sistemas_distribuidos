import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.DatagramSocket;

public class MulticastPeer{
    static long ajusteTempo;
    static SimpleDateFormat sdf;

    public static void main(String args[]) throws UnknownHostException {
		// args give message contents and destination multicast group (e.g. "228.5.6.7")
    MulticastSocket s =null;
    sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    ajusteTempo = 0;

    InetAddress address = InetAddress.getLocalHost();
    String hostIP = address.getHostAddress() ;
    String hostName = address.getHostName();
    System.out.println( "IP: " + hostIP + "\n" + "Name: " + hostName);

		try {
      s = new MulticastSocket(6789);

      System.out.println(long2sting(timePC()));
      ajusteTempo = 1234;
      System.out.println(long2sting(timePC()));


      // Cria thread ouvinte
      Connection ouvinte = new Connection(s);
      //entra no grupo
      InetAddress group = InetAddress.getByName(args[0]);
      s.joinGroup(group);

      //le teclado
      Scanner keyboard = new Scanner(System.in);
      String text = "init";

      while(true){
        text= keyboard.nextLine();

        System.out.println(text);

        //envia mensagem
        byte [] m = text.getBytes();
  			DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);

        s.send(messageOut);
      }
        //s.leaveGroup(group);
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("IO: " + e.getMessage());
		}finally {if(s != null) s.close();}
	}

  public static String long2sting(long time){
    return sdf.format(new Date(time));
  }
  public static long timePC(){
    //time do pc em fomato long
    Date now = new Date();
    long nowL = now.getTime();

    //add ajuste
    long timeProcessoL = nowL + ajusteTempo;
    Date timeProcesso =  new Date(timeProcessoL);
    return timeProcessoL;
  }
}

//Thread para leitura do Multicast
class Connection extends Thread {
	MulticastSocket multiSocket;

	public Connection (MulticastSocket amultiSocket) {
			multiSocket = amultiSocket;
			this.start();
	}

	public void run(){
		try {			                 // an echo server
      byte[] buffer = new byte[1000];
      while(true){
        //leitura
        DatagramPacket in = new DatagramPacket(buffer, buffer.length);
        multiSocket.receive(in);
        System.out.println("Received:" + new String(in.getData()));
      }
		}
    catch(IOException e) {System.out.println("readline:"+e.getMessage());}
	}
}
