package init.test;

import init.indexingLucene.MyIndexReader;
import init.searchLucene.ComplicatedSearch;
import init.searchLucene.ExtractQuery;
import init.searchLucene.GeneralSearch;
import init.searchLucene.PseudoRFRetrievalModel;
import init.searchLucene.QueryRetrievalModel;

import java.util.List;

import entity.EngDoc;
/*
 * for test use only
 */
public class SearchTest {
	public static void main(String[] args) throws Exception{
		MyIndexReader ixreader = new MyIndexReader("title");
		PseudoRFRetrievalModel PRFSearchModel = new PseudoRFRetrievalModel(ixreader);
		ExtractQuery query = new ExtractQuery();
		String queryContent = query.process("ben");
		// begin search
		List<EngDoc> results = PRFSearchModel.RetrieveQuery(queryContent, 10, 100, 0.4);
		if (results != null) {
			int rank = 1;
			for (EngDoc result : results) {
				System.out.print(queryContent);
				System.out.println(" Q0 " + result.docno() + " " + rank + " " + result.score() + " MYRUN");
				rank++;
			}
		}
		
		System.out.println("2");
		GeneralSearch agg = new GeneralSearch();
		List<EngDoc> results2 = agg.retrieveQuery(queryContent, 10);
		if (results2 != null) {
			int rank = 1;
			for (EngDoc result : results2) {
				System.out.print(queryContent);
				System.out.println(" Q0 " + result.docno() + " " + rank + " " + result.score() + " MYRUN");
				rank++;
			}
		}
		System.out.println("3");
		ComplicatedSearch c = new ComplicatedSearch();
		List<EngDoc> results3 = c.retrieveQuery(queryContent,"",queryContent,queryContent, 10);
		if (results3 != null) {
			int rank = 1;
			for (EngDoc result : results3) {
				System.out.print(queryContent);
				System.out.println(" Q0 " + result.docno() + " " + rank + " " + result.score() + " MYRUN");
				rank++;
			}
		}
		System.out.println();
		QueryRetrievalModel qr = new QueryRetrievalModel(ixreader);
		List<EngDoc> results4 = qr.retrieveQuery(queryContent, 10);
		if (results4 != null) {
			int rank = 1;
			for (EngDoc result : results3) {
				System.out.print(queryContent);
				System.out.println(" Q0 " + result.docno() + " " + rank + " " + result.score() + " MYRUN");
				rank++;
			}
		}
		ixreader.close();
	}
}