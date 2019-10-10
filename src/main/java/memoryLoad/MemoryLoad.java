package memoryLoad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class MemoryLoad {

	private List<byte[]> memory;
	private int nrOfMegabytes;
	private byte[] validationData;
	
	public MemoryLoad(int size) {
		nrOfMegabytes = size;
		memory = new LinkedList<>();
		validationData = null;
	}

	public void enableValidation() {
		validationData = loadFile();
	}

	public static void main(String...args) {
		if (args.length == 0) {
			System.out.println("This should be called with at least one argument the number of megabytes");
			System.out.println("Second optional argument is 1 if you want to validate the reading from memory");
			System.exit(-1);
		}
		MemoryLoad memory = new MemoryLoad(Integer.parseInt(args[0]));
		if(Integer.parseInt(args[1]) == 1) {
			memory.enableValidation();
		}
		memory.startLoading();
		if (Integer.parseInt(args[1]) == 1) {
			memory.validateData();
		}
		System.out.println("End Test!");
	}

	public void validateData() {
		System.out.println("Validating the write data ...");
		for (int i = 0 ; i < memory.size(); i++) {
			validateElement(memory.get(i), i);
		}
	}

	private void validateElement(byte[] data, int segment) {
		for (int i = 0 ;i < validationData.length; i ++) {
			if (validationData[i] != data[i]) {
				System.err.println("Reading error at segment " + segment + " byte = " + i + " it should be " + validationData[i] + " but is " + data[i]);
			}
		}
	}

	public void startLoading() {
		System.out.println("Loading data into memory ...");
		for (int i = 0 ;i < nrOfMegabytes; i++) {
			byte[] readData =loadFile();
			memory.add(readData);
		}
	}
	
	private byte[] loadFile() {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		try (InputStream is = getClass().getClassLoader().getResourceAsStream("file.txt");) {
			int nRead;
			byte[] data = new byte[1024];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer.toByteArray();
	}
}
