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
	public void sendData(List<byte[]> byteList) {
		try (DatagramSocket socket = new DatagramSocket(0)) {

			socket.setSoTimeout(10000);
			InetAddress host = InetAddress.getByName(HOSTNAME);

			for(byte[] bytes : byteList){
				DatagramPacket sending = new DatagramPacket(bytes, bytes.length, host, Constants.PORT);
				socket.send(sending);
				pukNum++;
				if(bytes == null || bytes.length <= 0) {
					System.out.println("Packet number " + pukNum + " contains no data.");
					continue;
				}
				byte startByte = bytes[0];
				byte endByte = bytes[bytes.length - 1];

//For each Datagram sent the sender will write the text sent, packer #, startbyte and endbyte.
				System.out.println("*************************************************");
				System.out.println(" The number of packet sending is:" + byteList.size());
				System.out.println(" This is  packet number : " + pukNum);
				System.out.println(" The Start byte is:" + " " + startByte);
				System.out.println(" The end byte is:" + " " + endByte);
				System.out.println("*************************************************");
				System.out.println();

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
