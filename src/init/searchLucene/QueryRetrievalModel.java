package init.searchLucene;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;

import init.indexingLucene.MyIndexReader;
import entity.EngDoc;;

public class QueryRetrievalModel {

	protected MyIndexReader indexReader;

	private Directory directory;
	private DirectoryReader ireader;
	private IndexSearcher indexSearcher;

	public QueryRetrievalModel(MyIndexReader ixreader) {
		try {
			directory = ixreader.directory;
			ireader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(ireader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<EngDoc> retrieveQuery(String queryContent, int TopN) throws Exception {
		List<EngDoc> results = new ArrayList<EngDoc>();
		// corner case
		if (queryContent.equals("")) {
			return results;
		}
		Query theQ = new QueryParser("CONTENT", new WhitespaceAnalyzer()).parse(queryContent);
		ScoreDoc[] scoreDoc = indexSearcher.search(theQ, TopN).scoreDocs;
		for (ScoreDoc score : scoreDoc) {
			results.add(new EngDoc(score.doc + "", ireader.document(score.doc).get("DOCNO"), score.score));
		}
		return results;
	}
}