package init.preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextDocCollection implements DocumentCollection {
	BufferedReader inputStream;
	String filePath;
	public TextDocCollection(String filePath) throws IOException {
		this.filePath = filePath;
		File file = new File(filePath);
        inputStream = new BufferedReader(new FileReader(file));
        System.out.println("Start reading, text file opened.");
	}
	
	public Map<String, Object> nextDocument() throws IOException {
		Map<String, Object> textCollection = new HashMap<>();
		String key = inputStream.readLine();
		if (key == null) {
			return null;
		}
		inputStream.readLine();
		String value = inputStream.readLine().replaceAll("[^a-zA-Z]", " ").trim();
		if (filePath.equals(PrepPath.parsedCTitle)) {
			value = value.replaceAll("^Review", " ");  // \\b gives you word boundary
		} 
		textCollection.put(key, value);
		if (!textCollection.isEmpty()) {
			return textCollection;
		}
		return null;
	}
}
