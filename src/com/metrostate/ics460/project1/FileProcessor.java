package com.metrostate.ics460.project1;

import java.util.List;

public interface FileProcessor {

	public List<byte[]> loadFile();
	
	public void saveFile(List<byte[]> byteList);

}
