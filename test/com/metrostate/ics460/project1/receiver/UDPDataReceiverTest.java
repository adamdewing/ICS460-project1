/**
 * 
 */
package com.metrostate.ics460.project1.receiver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.metrostate.ics460.project1.Constants;
import com.metrostate.ics460.project1.Message;
import com.metrostate.ics460.project1.MessageType;

/**
 * Integration tests using JUnit and testing the UDPDataReceiver
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
	 * Test method for {@link com.metrostate.ics460.project1.receiver.UDPDataReceiver#receiveData()}.
	 */
	@Test
	void testReceiveDataHappyPathString() {
		String data = "1234567890";
		senderThread = createTestDataSender(data); 
		senderThread.start();
		byte[] bytes = dataReceiver.receiveData();
		Object obj = convertBytesToObject(bytes);
		assertTrue(bytes != null, "UDPDataReceiver did not receive any data.");
		assertTrue(bytes.length > 0, "UDPDataReceiver sent back an empty array of data.");
		assertTrue(data.equals(obj), "Expected " + data + ", but got " + obj);
	}
	
	/**
	 * Test method for {@link com.metrostate.ics460.project1.receiver.UDPDataReceiver#receiveData()}.
	 */
	@Test
	void testReceiveDataHappyPathInt() {
		int data = 123456;
		senderThread = createTestDataSender(data); 
		senderThread.start();
		byte[] bytes = dataReceiver.receiveData();
		Object obj = convertBytesToObject(bytes);
		System.out.println("DataReceiver received: " + convertBytesToObject(bytes));
		assertTrue(bytes != null, "UDPDataReceiver did not receive any data.");
		assertTrue(bytes.length > 0, "UDPDataReceiver sent back an empty array of data.");
		assertTrue(data == (int)obj, "Expected " + data + ", but got " + obj);
	}
	
	private Thread createTestDataSender(Object obj) {
		return new Thread(new Runnable() {
			public void run() {
				Object testData = obj;
				delay(3000);
				DatagramSocket socket = null;
				try{
					socket = new DatagramSocket(Constants.PORT + 1);
					
				}catch (SocketException e) {
					e.printStackTrace();
					assertTrue(false, "Could not create socket.");
				}
				
				byte[] bytes = convertObjectToBytes(testData);
				UUID uuid = UUID.randomUUID();
				for(int i = 0; i < bytes.length; i++) {
					Message message = new Message();
					message.setConversationUuid(uuid);
					message.setMessageType(MessageType.DATA);
					message.setSize(bytes.length);
					message.setSequence(i);
					byte[] dataToSend = new byte[1];
					dataToSend[0] = bytes[i];
					message.setData(dataToSend);
					DatagramPacket packet = null;
					try {
						byte[] serializedMessage = convertObjectToBytes(message);
						packet = new DatagramPacket(serializedMessage, serializedMessage.length, InetAddress.getByName("127.0.0.1"), Constants.PORT);
						socket.send(packet);
					} catch (UnknownHostException e) {
						System.err.println("Could not find host.");
						e.printStackTrace();
					} catch (IOException e) {
						System.err.println("Error sending packet data.");
						e.printStackTrace();
					}
				}
				DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
				try {
					socket.receive(response);
				} catch (IOException e) {
					System.err.println("Exception received waiting for a response.");
					e.printStackTrace();
				}finally {
					socket.close();
				}
				System.out.println("Received the following response:");
				System.out.println(response.getAddress() + ":" + response.getPort());
				System.out.println(convertObjectToBytes(response.getData()).toString());
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
	
	private byte[] convertObjectToBytes(Object obj) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] bytes = null;
		try {
			ObjectOutput out = new ObjectOutputStream(output);
			out.writeObject(obj);
			out.flush();
			bytes = output.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				output.close();
			} catch (IOException e) {
				//Do nothing
			}
		}
		return bytes;
	}
	
	private Object convertBytesToObject(byte[] bytes) {
		Object obj = null;
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		ObjectInput objectInput = null;
		try {
			objectInput = new ObjectInputStream(input);
			obj = objectInput.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(objectInput != null) {
				try {
					objectInput.close();
				} catch (IOException e) {
					// ignore this error
				}
			}
		}
		return obj;
	}
	
}
