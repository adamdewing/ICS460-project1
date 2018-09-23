/**
 * 
 */
package com.metrostate.ics460.project1.receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.metrostate.ics460.project1.Constants;

/**
 * @author adamv
 *
 */
public class UDPDataReceiver implements DataReceiver {

	@Override
	public byte[] receiveData() {
		List<byte []> byteList = new ArrayList<>();
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(Constants.PORT);
			while(true) {
				DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
				socket.receive(packet);
				byteList.add(packet.getData());
			}
		} catch(SocketException e) {
			System.out.println("Error creating socket on port " + Constants.PORT);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error trying to receive data.");
			e.printStackTrace();
		}finally {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
			
		return mergeBytes(byteList);
	}
	
	private byte[] mergeBytes(List<byte[]> byteList) {
		// Find the total length of all the data combined
		int totalLength = 0;
		for (int i = 0; i < byteList.size(); i++) {
			totalLength += byteList.get(i).length;
		}

		byte[] bytes = new byte[totalLength];
		int currentPosition = 0;
		System.out.println("totalLength: " + totalLength);
		for (int i = 0; i < byteList.size(); i++) {
			System.out.println("Adding the following bytes:" +byteList.get(i));
			System.arraycopy(byteList.get(i), 0, bytes, currentPosition, byteList.get(i).length);
			currentPosition += byteList.get(i).length;
		}
		return bytes;
	}

}
