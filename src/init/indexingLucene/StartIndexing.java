package init.indexingLucene;

import java.util.Map;

public class StartIndexing {
	public static void main(String[] args) throws Exception {
		// main entrance
		StartIndexing hm2 = new StartIndexing();
		hm2.WriteIndex("title");
		hm2.WriteIndex("content");
		hm2.WriteIndex("author");
		hm2.WriteIndex("ctitle");
		// for test use only hm2.ReadIndex("content", "war");
	}
	
	public void WriteIndex(String dataType) throws Exception {
		// Initiate pre-processed collection file reader
		PreProcessedCorpusReader corpus=new PreProcessedCorpusReader(dataType);
		
		// initiate the output object
		MyIndexWriter output=new MyIndexWriter(dataType);
		
		// initiate a doc object, which can hold document number and document content of a document
		Map<String, Object> doc = null;

		int count=0;
		// build index of corpus document by document
		while ((doc = corpus.nextDocument()) != null) {
			// load document number and content of the document
			String docno = doc.keySet().iterator().next();
			char[] content = (char[]) doc.get(docno);
			// index this document
			output.index(docno, content); 
			count++;
		}
		System.out.println("totaly document count:  "+count);
		output.close();
	}
	
	public void ReadIndex(String dataType, String token) throws Exception {
		// Initiate the index file reader
		MyIndexReader ixreader=new MyIndexReader(dataType);
		
		// do retrieval
		int df = ixreader.DocFreq(token);
		long ctf = ixreader.CollectionFreq(token);
		System.out.println(" >> the token \""+token+"\" appeared in "+df+" documents and "+ctf+" times in total");
		if(df>0){
			int[][] posting = ixreader.getPostingList(token);
			for(int ix=0;ix<posting.length;ix++){
				int docid = posting[ix][0];
				int freq = posting[ix][1];
				String docno = ixreader.getDocno(docid);
				System.out.printf("    %20s    %6d    %6d\n", docno, docid, freq);
			}
		}
		ixreader.close();
	}
}
