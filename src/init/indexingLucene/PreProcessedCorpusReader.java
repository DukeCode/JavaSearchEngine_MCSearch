package init.indexingLucene;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import path.AllPath;

public class PreProcessedCorpusReader {


	private BufferedReader br;
	private FileInputStream instream_collection;
	private InputStreamReader is;
	public PreProcessedCorpusReader(String type) throws IOException {
		instream_collection = new FileInputStream(AllPath.Preprocessed + type);
		is = new InputStreamReader(instream_collection);
		br = new BufferedReader(is);   
	}


	public Map<String, Object> nextDocument() throws IOException {
		String docno=br.readLine();
		if(docno==null) {
			instream_collection.close();
			is.close();
			br.close();
			return null;
		}
		String content =br.readLine();
		Map<String, Object> doc = new HashMap<>();
		doc.put(docno, content.toCharArray());
		return doc;
	}
}
