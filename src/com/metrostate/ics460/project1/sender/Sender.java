package com.metrostate.ics460.project1.sender;

public class Sender {

	private FileLoader fileLoader = null;
	private DataSender sendData = null;

	public static void main(String[] args) {
		Sender sender = new Sender();
		sender.sendFile();
	}

	public void sendFile() {
		byte[] bytes = fileLoader.loadFile();
		sendData.sendData(bytes);
	}

}
