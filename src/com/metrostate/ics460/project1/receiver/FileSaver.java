package com.metrostate.ics460.project1.receiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.metrostate.ics460.project1.FileProcessor;

public class FileSaver implements FileProcessor {

	private List<byte[]> lines = new ArrayList<byte[]>();

	public void saveFile(List<byte[]> byteList) {
		File file = new File("output.txt");
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter);
			for(byte[] bytes : byteList) {
				printWriter.println(new String(bytes, "UTF-8"));
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<byte[]> loadFile() {
		// TODO Auto-generated method stub
		return null;
	}
}
