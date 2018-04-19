package init.test;

import init.indexingLucene.MyIndexReader;
import init.pseudoRF.PseudoRFRetrievalModel;
import init.searchLucene.ExtractQuery;

import java.util.List;

import entity.EngDoc;
/*
 * for test use only
 */
public class SearchTest {
	public static void main(String[] args) throws Exception{
		MyIndexReader ixreader = new MyIndexReader("author");
		PseudoRFRetrievalModel PRFSearchModel = new PseudoRFRetrievalModel(ixreader);
		ExtractQuery query = new ExtractQuery();
		String queryContent = query.process("CATSOULIS");
		// begin search
		List<EngDoc> results = PRFSearchModel.RetrieveQuery(queryContent, 20, 100, 0.4);
		if (results != null) {
			int rank = 1;
			for (EngDoc result : results) {
				System.out.print(queryContent);
				System.out.println(" Q0 " + result.docno() + " " + rank + " " + result.score() + " MYRUN");
				rank++;
			}
		}
		ixreader.close();
	}
}