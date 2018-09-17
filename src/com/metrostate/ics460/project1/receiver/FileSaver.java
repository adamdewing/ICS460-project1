package com.metrostate.ics460.project1.receiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileSaver {

	private File file;
	private StringBuilder string_builder;
	private FileWriter file_writer;
	private BufferedWriter buffered_writer;
	private PrintWriter print_writer;

	public void saveFile(String str) {
		file = new File( "output.txt" );
		string_builder.append(str);
		String data = string_builder.toString();
		
		try {
			file_writer = new FileWriter(file, true);
			buffered_writer = new BufferedWriter(file_writer);
			print_writer = new PrintWriter(buffered_writer);
			print_writer.println(data);
			print_writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
