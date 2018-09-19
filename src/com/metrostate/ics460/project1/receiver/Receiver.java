package com.metrostate.ics460.project1.receiver;

public class Receiver {

	private DataReceiver dataReceiver = new UDPDataReceiver();
	private FileSaver fileSaver = new FileSaver();
	
	public static void main(String[] args) {
		Receiver receiver = new Receiver();
		receiver.receiveFile();

	}

	public void receiveFile() {
		byte[] bytes = dataReceiver.receiveData();
//		fileSaver.saveFile(bytes);
	}
	
}
