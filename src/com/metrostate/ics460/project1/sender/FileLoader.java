package com.metrostate.ics460.project1.sender;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

public class FileLoader implements Loader{

	private String fileName;
	private String line;
	private List<byte[]> lines = new ArrayList<byte[]>();

	public List<byte[]> loadFile() {
		JFileChooser file_Chooser = new JFileChooser();
		file_Chooser.setCurrentDirectory(new File("."));
		if (file_Chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fileName = file_Chooser.getSelectedFile().getName();

			try {
				FileInputStream fis = new FileInputStream((new File(fileName)));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				try {

					for (int readNum; (readNum = fis.read(buf)) != -1;) {
						//Writes to this byte array output stream
						bos.write(buf, 0, readNum); 
						System.out.println("read " + readNum + " bytes,");
					}
				} catch (IOException ex) {

				}

				fis.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lines;

	}


	/**
	 * @return the lines
	 */
	public List<byte[]> getLines() {
		return lines;
	}

	/**
	 * @param line the lines to set
	 */
	public void setLines(List<byte[]> line) {
		this.lines = line;
	}

}
