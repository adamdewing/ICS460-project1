/**
 * 
 */
package com.metrostate.ics460.project1.receiver;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.metrostate.ics460.project1.Constants;

/**
 * Integration tests using JUnit and testing the UDPDataReceiver
 * 
 * @author adamv
 *
 */
class UDPDataReceiverTest {

	private DataReceiver dataReceiver;

	private Thread senderThread;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		dataReceiver = new UDPDataReceiver();
		delay(3000);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		dataReceiver = null;
		senderThread = null;

	}

	/**
	 * Test method for
	 * {@link com.metrostate.ics460.project1.receiver.UDPDataReceiver#receiveData()}.
	 */
	@Test
	void testReceiveDataHappyPathString() {
		String[] data = {"123456"};
		
		// Start sending test data
		senderThread = createTestDataSender(data);
		senderThread.start();
		
		// Receive test data
		List<byte[]> byteList = dataReceiver.receiveData();
		
		String receivedData = null;
		for(byte[] bytes : byteList) {
			try {
					
				receivedData = new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				assertTrue(false, "Could not encode receiving data from bytes back to a String.");
			}
			assertTrue(bytes != null, "UDPDataReceiver did not receive any data.");
			assertTrue(data[0].equals(receivedData), "Was expecting " + data[0] + ", but received the String " + receivedData + " intead.");
		}
	}

	/**
	 * Test method for
	 * {@link com.metrostate.ics460.project1.receiver.UDPDataReceiver#receiveData()}.
	 */
	@Test
	void testReceiveDataHappyPathMultipleStrings() {
		String[] data = {"123456", "7", "", "8", "9", "0"};
		
		Map<String, String> dataMap = new HashMap<>();
		for(String value : data) {
			dataMap.put(value, null);
		}
		
		// Start sending test data
		senderThread = createTestDataSender(data);
		senderThread.start();
		
		// Receive test data
		List<byte[]> byteList = dataReceiver.receiveData();
		
		String receivedData = null;
		for(byte[] bytes : byteList) {
			try {
					
				receivedData = new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				assertTrue(false, "Could not encode receiving data from bytes back to a String.");
			}
			assertTrue(bytes != null, "UDPDataReceiver did not receive any data.");
			assertTrue(dataMap.containsKey(receivedData), "The value " + receivedData + " was received, but was not part of the test data sent.");
		}
	}

	private Thread createTestDataSender(String[] data) {
		return new Thread(new Runnable() {
			public void run() {
				delay(3000);
				DatagramSocket socket = null;
				try {
					socket = new DatagramSocket(0);
	                socket.setSoTimeout(10000);
	                InetAddress host = InetAddress.getByName("127.0.0.1");
					byte[] bytes;
					for(int i = 0; i < data.length; i++) {
						bytes = data[i].getBytes("US-ASCII");
						DatagramPacket sending = new DatagramPacket(bytes, bytes.length, host, Constants.PORT);
						socket.send(sending);
					}
				} catch (SocketException e) {
					e.printStackTrace();
					assertTrue(false, "Could not create socket.");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					assertTrue(false, "Error trying to convert String to bytes.");
				} catch (IOException e) {
					e.printStackTrace();
					assertTrue(false, "Error trying to send data.");
				}finally {
					if(socket != null && !socket.isClosed()) {
						socket.close();
					}
				}
			}
		});

	}

	/**
	 * 
	 * @param waitTime time to wait in milliseconds
	 */
	private void delay(int waitTime) {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			// just exit block
		}
	}
}
