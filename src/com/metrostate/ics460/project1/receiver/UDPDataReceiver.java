/**
 * 
 */
package com.metrostate.ics460.project1.receiver;

import static com.metrostate.ics460.project1.Constants.PORT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.metrostate.ics460.project1.Message;
import com.metrostate.ics460.project1.MessageType;

/**
 * @author adamv
 *
 */
public class UDPDataReceiver implements DataReceiver {

	private static final int TIMEOUT = 5000;
	private static final int MAX_RETRIES = 2;

	Map<UUID, Map<Integer, Message>> conversations = new HashMap<UUID, Map<Integer, Message>>();
	Map<UUID, Integer> timeouts = new HashMap<UUID, Integer>();

	/**
	 * Receives a series of UPD DatagramPackets and then returns an array of bytes
	 * that were contained in the messages.
	 */
	@Override
	public byte[] receiveData() {
		UUID uuid = null;
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(PORT);
			DatagramPacket datagramPacket = new DatagramPacket(new byte[socket.getReceiveBufferSize()],
					(socket.getReceiveBufferSize()));
			socket.setSoTimeout(TIMEOUT);
			do {
				try {
					socket.receive(datagramPacket);
					uuid = processDatagramPacket(datagramPacket);
				} catch (SocketTimeoutException e) {
					if (timeouts.containsKey(uuid)) {
						if (timeouts.get(uuid) > MAX_RETRIES) {
							// We already timed before and waited for this conversation so now exit
							System.out.println("Timed out waiting for conversation with UUID of " + uuid);
							return null;
						} else {
							// Timed out, but have not exceedeed MAX_RETRIES
							System.out.println("Conversation with UUID of " + uuid + " has timed out.");
							timeouts.put(uuid, timeouts.get(uuid) + 1);
							requestMissingMessages(uuid, socket);
						}
					} else {
						// First time we have timed out waiting for this conversation
						System.out.println("Conversation with UUID of " + uuid + " has timed out for the first time.");
						timeouts.put(uuid, 1);
						requestMissingMessages(uuid, socket);
					}
				} catch (IOException e) {
					System.err.println("Unable to receive data from the socket.");
					e.printStackTrace();
				}
			} while (!isConversationComplete(uuid));
			sendConversationACK(uuid, socket, datagramPacket.getSocketAddress());

		} catch (SocketException e) {
			System.err.println("Could not create an UDP socket on PORT " + PORT);
			e.printStackTrace();
		} finally {
			socket.close();
		}

		return uuid == null ? null : getByteData(uuid);
	}

	/**
	 * Process the DatagramPacket and return the conversation UUID associated with
	 * the received message.
	 * <p>
	 * For how the deserialization works see the linked url.
	 * 
	 * @param datagramPacket
	 * @return
	 * @see <a
	 *      href=" https://stackoverflow.com/questions/4252294/sending-objects-across-network-using-udp-in-java">sending-objects-across-network-using-udp-in-java</a>
	 */
	private UUID processDatagramPacket(DatagramPacket datagramPacket) {
		UUID uuid = null;
		byte[] bytes = datagramPacket.getData();
		try {
			// Deserialize objects
			ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes));
			Message message = (Message) input.readObject();

			if (!isValidMessage(message)) {
				System.out.println("The following invalid message was received:");
				System.out.println(message);
				throw new IOException("Invalid Message object received.");
			}

			uuid = message.getConversationUuid();
			Map<Integer, Message> messages = conversations.get(uuid);

			// first time we have heard from a conversation with this uuid so create needed
			// Maps
			if (messages == null) {
				messages = new HashMap<Integer, Message>();
				conversations.put(uuid, messages);
			}

			if (message != null && message.getMessageType() == MessageType.DATA) {
				System.out.println("Received the following message:");
				System.out.println(message);
				messages.put(message.getSequence(), message);
				timeouts.put(uuid, 0); // reset timeouts for this conversation
			} else {
				// Currently we are only handling data messages
			}
		} catch (IOException e) {
			System.err.println("Unable to create input stream from DatagramPacket data.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Unable to deserialize Message from DatagramPacket data.");
			e.printStackTrace();
		}

		return uuid;
	}

	/**
	 * Finds out if the conversation of a specified UUID is complete and has
	 * received all the parts.
	 * 
	 * @param uuid
	 * @return
	 */
	private boolean isConversationComplete(UUID uuid) {
		Map<Integer, Message> messages = conversations.get(uuid);
		if (messages == null || messages.isEmpty()) {
			return false;
		}
		// Get any message as the size value *should* be the same for all messages in a
		// conversation.
		Message someMessage = messages.entrySet().iterator().next().getValue();

		// Have we received all the messages for this conversation?
		if (someMessage.getSize() != messages.size()) {
			return false;
		}

		for (int i = 0; i < someMessage.getSize(); i++) {
			if (!messages.containsKey(i)) {
				return false;
			}
		}
		return true;
	}

	private void requestMissingMessages(UUID uuid, DatagramSocket socket) {
		// TODO
	}

	/**
	 * Checks to make sure the message is a valid format
	 * 
	 * @param message
	 * @return
	 */
	private boolean isValidMessage(Message message) {
		if (message.getConversationUuid() == null) {
			return false;
		} else if (message.getMessageType() == null) {
			return false;
		}
		return true;
	}

	private void sendConversationACK(UUID uuid, DatagramSocket socket, SocketAddress socketAddress) {
		Map<Integer, Message> messages = conversations.get(uuid);
		Message requestMessage = messages.entrySet().iterator().next().getValue();
		
		Message ackMessage = new Message();
		ackMessage.setConversationUuid(requestMessage.getConversationUuid());
		ackMessage.setMessageType(MessageType.ACK);
		byte[] bytes = convertObjectToBytes(ackMessage);
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, socketAddress);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Error sending back ACK message for conversation " + uuid);
			e.printStackTrace();
		}
	}

	/**
	 * This gets all the byte data for a conversation. Assumes that the conversation
	 * is complete.
	 * 
	 * @param uuid
	 * @return
	 */
	private byte[] getByteData(UUID uuid) {
		Map<Integer, Message> messages = conversations.get(uuid);

		// Get any message as the size value *should* be the same for all messages in a
		// conversation.
		Message someMessage = messages.entrySet().iterator().next().getValue();

		// Find the total length of all the data combined
		int totalLength = 0;
		for (int i = 0; i < someMessage.getSize(); i++) {
			Message message = messages.get(i);
			totalLength += message.getData().length;
		}

		byte[] bytes = new byte[totalLength];
		int currentPosition = 0;
		System.out.println("totalLength: " + totalLength);
		for (int i = 0; i < someMessage.getSize(); i++) {
			Message message = messages.get(i);
			System.out.println("Adding the following bytes:" + message.getData());
			System.arraycopy(message.getData(), 0, bytes, currentPosition, message.getData().length);
			currentPosition += messages.get(i).getData().length;
		}
		return bytes;
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
}
