package com.metrostate.ics460.project1.sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.metrostate.ics460.project1.Constants;

public class UDPDataSender implements DataSender {
	
    private static final String HOSTNAME ="127.0.0.1";
	
    @Override
	public void sendData(byte[] bytes) {
    	List<byte[]> byteList = splitBytes(bytes);
    	try (DatagramSocket socket = new DatagramSocket(0)) {
			socket.setSoTimeout(10000);
			InetAddress host = InetAddress.getByName(HOSTNAME);
			
			
			int packetsSent = 0;
			int bytesSent = 0;
			for(byte[] packetBytes : byteList){
				DatagramPacket sending = new DatagramPacket(packetBytes, packetBytes.length, host, Constants.PORT);
				socket.send(sending);
				packetsSent++;
				System.out.println("Sending packet number: " + packetsSent + ", bytes: " + bytesSent + " - " + (bytesSent + packetBytes.length - 1));
				bytesSent += packetBytes.length;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    	System.out.println("Total number of packets received: " + byteList.size());
	}
    
    private List<byte[]> splitBytes(byte[] bytes){
    	List<byte[]> byteList = new ArrayList<byte[]>();
    	for(int i = 0; i < bytes.length; i += Constants.PACKET_SIZE) {
    		byte[] b = Arrays.copyOfRange(bytes, i, i + Constants.PACKET_SIZE); 		
    		byteList.add(b);
    	}
    	return byteList;
    }

}
