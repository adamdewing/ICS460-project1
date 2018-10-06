/**
 * 
 */
package com.metrostate.ics460.project1.receiver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.metrostate.ics460.project1.Constants;

/**
 * @author adamv
 *
 */
public class UDPDataReceiver implements DataReceiver {

	public static final int TIMEOUT_INITIAL = 30000;
	public static final int TIMEOUT_RECEIVING = 3000;

	@Override
	public byte[] receiveData() {
		List<byte[]> byteList = new ArrayList<>();
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(Constants.PORT);
			socket.setSoTimeout(TIMEOUT_INITIAL);
			boolean isTimeoutLowered = false;
			int packetsReceived = 0;
			int bytesReceived = 0;
			while (true) {
				DatagramPacket packet = new DatagramPacket(new byte[Constants.PACKET_SIZE], Constants.PACKET_SIZE);
				socket.receive(packet);
				if (!isTimeoutLowered) {
					// Once we receive the first packet of data, we shouldn't have to wait long for
					// the next ones.
					socket.setSoTimeout(TIMEOUT_RECEIVING);
					isTimeoutLowered = true;
				}
				packetsReceived++;
				System.out.println("Receiving packet number: " + packetsReceived + ", bytes: " + bytesReceived + " - " + (bytesReceived + packet.getLength() - 1));
				bytesReceived += packet.getLength();
				
				byte[] bytes = new byte[packet.getLength()];
				System.arraycopy(packet.getData(), 0, bytes, 0, packet.getLength());
				byteList.add(bytes);
			}
		} catch (SocketException e) {
			System.out.println("Error creating socket on port " + Constants.PORT);
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			if (byteList.isEmpty()) {
				System.err.println("Socket timed out and there was no data received!");
			} else {
				System.out.println(
						"Socket timed out.  This might be due to the fact that the sender might be done sending data.");
			}
		} catch (IOException e) {
			System.out.println("Error trying to receive data.");
			e.printStackTrace();
		} finally {
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
		System.out.println("Total number of packets received: " + byteList.size());
		return mergeBytes(byteList);
	}

	/**
	 * This method takes a List of byte arrays and merges them into a single byte
	 * array. This method is no longer used.
	 * 
	 * @param byteList
	 * @return
	 */
	@SuppressWarnings("unused")
	private byte[] mergeBytes(List<byte[]> byteList) {
		// Find the total length of all the data combined
		int totalLength = 0;
		for (int i = 0; i < byteList.size(); i++) {
			totalLength += byteList.get(i).length;
		}

		byte[] bytes = new byte[totalLength];
		int currentPosition = 0;
		for (int i = 0; i < byteList.size(); i++) {
			System.arraycopy(byteList.get(i), 0, bytes, currentPosition, byteList.get(i).length);
			currentPosition += byteList.get(i).length;
		}
		return bytes;
	}

}
