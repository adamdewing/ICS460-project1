package com.metrostate.ics460.project1.sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import com.metrostate.ics460.project1.Constants;

public class UDPDataSender implements DataSender {
	
    private static final String HOSTNAME ="127.0.0.1";
    private  static int pukNum=0;
	
    @Override
	public void sendData(byte[] bytes) {
		try (DatagramSocket socket = new DatagramSocket(0)) {

			socket.setSoTimeout(10000);
			InetAddress host = InetAddress.getByName(HOSTNAME);
			
			List<byte[]> byteList = splitBytes(bytes);
			
			for(byte[] packetBytes : byteList){
				DatagramPacket sending = new DatagramPacket(packetBytes, packetBytes.length, host, Constants.PORT);
				socket.send(sending);
				pukNum++;
				if(packetBytes == null || packetBytes.length <= 0) {
					System.out.println("Packet number " + pukNum + " contains no data.");
					continue;
				}
				byte startByte = packetBytes[0];
				byte endByte = packetBytes[packetBytes.length - 1];

				//TODO output to log

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
    
    private List<byte[]> splitBytes(byte[] bytes){
    	//TODO magic
    	return null;
    }

}
