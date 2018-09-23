package com.metrostate.ics460.project1.sender;

import java.io.*;
import java.net.*;

import javax.swing.*;

public class Sender {

    private final static  int PORT = 320;
    private static final String HOSTNAME ="127.0.0.1";
    private  static int pukNum=0;

    public static void main(String[] args) throws IOException {

        String inputFileName=" ";
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("."));
        //Chooses a file from the current directory where the sender accepts a file from that directory
        if (jFileChooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
            inputFileName = jFileChooser.getSelectedFile().getName();
            System.out.println("Selected fileName is : " + inputFileName);
            System.out.println("Selected file path is : " + jFileChooser.getSelectedFile().getAbsolutePath());

            }
            //Reads the file using bufferReader
            BufferedReader bufferedReader ;
            FileReader fileReader = new FileReader(jFileChooser.getSelectedFile());
            bufferedReader =  new BufferedReader(fileReader);
            String line;


            try (DatagramSocket socket =  new DatagramSocket (0)){

                socket.setSoTimeout(10000);
                InetAddress host = InetAddress.getByName(HOSTNAME);

                while((line = bufferedReader.readLine()) != null){

                    byte[] sendData;
                    sendData=line.getBytes("US-ASCII");
                    DatagramPacket sending = new DatagramPacket(sendData, sendData.length, host, PORT);
                    socket.send(sending);

                    byte startByte= sendData[0];
                    byte endByte= sendData[ sendData.length - 1 ];
                    pukNum++;


                    String input = new String(sending.getData(), 0,sending.getLength(), "US-ASCII");
//For each Datagram sent the sender will write the text sent, packer #, startbyte and endbyte.
                    System.out.println("*************************************************");
                    System.out.println(" The sender is sending the following text\n"  +
                    "----------------------------------------------\n "+input);
                    System.out.println(" The number of packet sending is:"+ sending.getLength());
                    System.out.println(" This is  packet number : " +pukNum);
                    System.out.println(" The Start byte is:"+" "+ startByte );
                    System.out.println(" The end byte is:"+" "+ endByte );
                    System.out.println("*************************************************");
                    System.out.println();

               }

            bufferedReader.close();
           }catch(IOException ex){
                ex.printStackTrace();
            }

    }
}

