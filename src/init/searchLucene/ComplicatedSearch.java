package init.searchLucene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.EngDoc;
import init.indexingLucene.MyIndexReader;
import init.tools.IdScorePair;
import init.tools.MinHeap;

public class ComplicatedSearch {
	public List<EngDoc> retrieveQuery(String titleQ, String ctitleQ, String contentQ, String authorQ, int TopN) throws Exception {
		
		MyIndexReader titleIndexReader = new MyIndexReader("title");
		PseudoRFRetrievalModel titlePRFmodel = new PseudoRFRetrievalModel(titleIndexReader);
		List<EngDoc> titleResults = titlePRFmodel.RetrieveQuery(titleQ, 8260, 8260, 0.4);
		titleIndexReader.close();
		
		MyIndexReader ctitleIndexReader = new MyIndexReader("ctitle");
		PseudoRFRetrievalModel ctitlePRFmodel = new PseudoRFRetrievalModel(ctitleIndexReader);
		List<EngDoc> ctitleResults = ctitlePRFmodel.RetrieveQuery(ctitleQ, 8260, 8260, 0.4);
		ctitleIndexReader.close();
		
		MyIndexReader contentIndexReader = new MyIndexReader("content");
		PseudoRFRetrievalModel contentPRFmodel = new PseudoRFRetrievalModel(contentIndexReader);
		List<EngDoc> contentResults = contentPRFmodel.RetrieveQuery(contentQ, 8260, 8260, 0.4);
		contentIndexReader.close();
		
		MyIndexReader authorIndexReader = new MyIndexReader("author");
		PseudoRFRetrievalModel authorPRFmodel = new PseudoRFRetrievalModel(authorIndexReader);
		List<EngDoc> authorResults = authorPRFmodel.RetrieveQuery(authorQ, 8260, 8260, 0.4);
		
		HashMap<String, Double> mapOne = new HashMap<>();
		for (EngDoc doc : titleResults) {
			mapOne.put(doc.docid(), doc.score());
		}
		
		HashMap<String, Double> mapTwo = new HashMap<>();
		for (EngDoc doc : ctitleResults) {
			if (mapOne.containsKey(doc.docid())) {
				double tempScore = mapOne.get(doc.docid()) * 10 + doc.score() * 5;
				mapTwo.put(doc.docid(), tempScore);
			} else if (mapOne.isEmpty()) {
				mapTwo.put(doc.docid(), doc.score() * 5);
			}
		}
		if (ctitleResults.isEmpty()) {
			mapTwo = mapOne;
		}
		
		HashMap<String, Double> mapThree = new HashMap<>();
		for (EngDoc doc : contentResults) {
			if (mapTwo.containsKey(doc.docid())) {
				double tempScore = mapTwo.get(doc.docid()) + doc.score();
				mapThree.put(doc.docid(), tempScore);
			} else if (mapTwo.isEmpty()) {
				mapThree.put(doc.docid(), doc.score());
			}
		}
		if (contentResults.isEmpty()) {
			mapThree = mapTwo;
		}
		
		HashMap<String, Double> mapFour = new HashMap<>();
		for (EngDoc doc : authorResults) {
			if (mapThree.containsKey(doc.docid())) {
				double tempScore = mapThree.get(doc.docid()) + doc.score() * 2;
				mapFour.put(doc.docid(), tempScore);
			} else if (mapThree.isEmpty()) {
				mapFour.put(doc.docid(), doc.score() * 2);
			}
		}
		if (authorResults.isEmpty()) {
			mapFour = mapThree;
		}
		
		MinHeap minHeap = new MinHeap(TopN);
		for (Map.Entry<String, Double> entry : mapFour.entrySet()) {
			int docid = Integer.parseInt(entry.getKey());
			IdScorePair element = new IdScorePair(docid, entry.getValue());
			minHeap.offer(element);
		}
		
		List<EngDoc> results = new ArrayList<>();
		while (!minHeap.isEmpty()) {
			IdScorePair tempPair = minHeap.poll();
			String tempDocID = String.valueOf(tempPair.docid);
			String tempDocNo = authorIndexReader.getDocno(tempPair.docid);
			results.add(new EngDoc(tempDocID, tempDocNo, tempPair.score));
		}
		Collections.reverse(results);
		authorIndexReader.close();
		return results;
	}
}
