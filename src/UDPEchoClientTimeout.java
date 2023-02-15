import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class UDPEchoClientTimeout {
    private static final int TIMEOUT = 3000;
    private static final int MAXTRIES = 5;
    public static void main(String[] args) throws IOException {
//        if((args.length < 2) || (args.length > 3)){
//            throw new IllegalArgumentException("Parameters");
//        }

        Scanner tec = new Scanner(System.in);

        System.out.println("Introduce la direccion del server");
        InetAddress serverAddress = InetAddress.getByName(tec.nextLine());

        System.out.println("Introduce el mensaje a enviar");
        byte[] bytesToSend = tec.nextLine().getBytes();


        System.out.println("Introduce el puerto");
        int servPort = tec.nextInt();

        DatagramSocket socket = new DatagramSocket();

        socket.setSoTimeout(TIMEOUT);

        DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, servPort);
        DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

        int tries = 0;
        boolean receivedResponse = false;

        do{
            socket.send(sendPacket);
            try {
                socket.receive(receivePacket);
                if(!receivePacket.getAddress().equals(serverAddress)){
                    throw new IOException("Received packet from an unknown source");
                }
                receivedResponse = true;
            }catch (InterruptedIOException e){
                tries +=1;
                System.out.println("Time out, " + (MAXTRIES - tries) + " more tries...");
            }
        }while((!receivedResponse) && (tries < MAXTRIES));

        if(receivedResponse){
            System.out.println("Received: " + new String(receivePacket.getData()));
        }else{
            System.out.println("No response -- giving up.");
        }
        socket.close();
    }
}