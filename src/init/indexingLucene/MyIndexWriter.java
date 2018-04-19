package init.indexingLucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import path.AllPath;

public class MyIndexWriter {
	
	protected File dir;
	private Directory directory;
	private IndexWriter ixwriter;
	private FieldType type;
	
	public MyIndexWriter( String dataType ) throws IOException {
		if (dataType.equals("title")) {
			directory = FSDirectory.open(Paths.get(AllPath.titleIndex));  
		} else if (dataType.equals("author")) {
			directory = FSDirectory.open(Paths.get(AllPath.authorIndex)); 
		} else if (dataType.equals("ctitle")) {
			directory = FSDirectory.open(Paths.get(AllPath.ctitleIndex)); 
		} else if (dataType.equals("content")){
			directory = FSDirectory.open(Paths.get(AllPath.contentIndex)); 
		} else {
			System.err.println("undefined data type");
		}
				
		IndexWriterConfig indexConfig=new IndexWriterConfig(new WhitespaceAnalyzer());
		indexConfig.setMaxBufferedDocs(10000);
		ixwriter = new IndexWriter( directory, indexConfig);
		type = new FieldType();
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(false);
		type.setStoreTermVectors(true);
	}
	
	/**
	 * 
	 * @param docno 
	 * @param content
	 * @throws IOException
	 */
	public void index( String docno, char[] content) throws IOException {
		// you should implement this method to build index for each document
		Document doc = new Document();
		doc.add(new StoredField("DOCNO", docno));		
		doc.add(new Field("CONTENT", new String(content), type));
		ixwriter.addDocument(doc);
	}
	
	/**
	 * @throws IOException
	 */
	public void close() throws IOException {
		// you should implement this method if necessary
		ixwriter.close();
		directory.close();
	}
	
}
