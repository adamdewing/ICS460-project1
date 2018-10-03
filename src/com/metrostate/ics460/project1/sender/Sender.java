package com.metrostate.ics460.project1.sender;

import java.io.*;
import java.net.*;
import java.util.List;

import javax.swing.*;

public class Sender {

    private Loader loader = new FileLoader();
    private DataSender dataSender = new UDPDataSender();

    public static void main(String[] args) throws IOException {
    	Sender sender = new Sender();
    	sender.start();
    }
    
    public void start() {
    	byte[] bytes = loader.loadFile();
    	dataSender.sendData(bytes);
    }
}

