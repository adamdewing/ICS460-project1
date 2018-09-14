/**
 * 
 */
package com.metrostate.ics460.project1;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author adamv
 *
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4107877854517030923L;

	//Unique id for a message conversation
	private UUID conversationUuid;
	
	// The type of UDP message
	private MessageType messageType;	
	
	// Total number of Messages of this conversationUuid to be sent.
	// Should be the same for all Messages of a conversation
	private int size;	
	
	// Relative order, zero based, of Messages to be sent 
	private int sequence;	
	
	//Data to be sent
	private byte[] data;

	//A human readable message
	private String text;

	
	
	public UUID getConversationUuid() {
		return conversationUuid;
	}

	public void setConversationUuid(UUID conversationUuid) {
		this.conversationUuid = conversationUuid;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Message [conversationUuid=" + conversationUuid + ", messageType=" + messageType + ", size=" + size
				+ ", sequence=" + sequence + ", data=" + Arrays.toString(data) + ", text=" + text + "]";
	}
	
}
