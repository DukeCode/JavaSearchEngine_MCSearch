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

/*
 *  Class for general search
 *  both pseudo relevance feedback and smoothing method is used
 */
public class GeneralSearch {
	// equation: 10*titleScore + 5*ctitleScore + 1*contentScore + 2*authorScore
	// why use + rather than *? because we treat them as different model and actually they are
	// tested with actual dataset, + relationship gives the best results
	public List<EngDoc> retrieveQuery(String queryContent, int TopN) throws Exception {
		List<EngDoc> results = new ArrayList<>();
		// corner case
		if (queryContent.equals("")) {
			return results;
		}
		
		MyIndexReader titleIndexReader = new MyIndexReader("title");
		PseudoRFRetrievalModel titlePRFmodel = new PseudoRFRetrievalModel(titleIndexReader);
		List<EngDoc> titleResults = titlePRFmodel.RetrieveQuery(queryContent, 1000, 4000, 0.4);
		titleIndexReader.close();
		
		MyIndexReader ctitleIndexReader = new MyIndexReader("ctitle");
		PseudoRFRetrievalModel ctitlePRFmodel = new PseudoRFRetrievalModel(ctitleIndexReader);
		List<EngDoc> ctitleResults = ctitlePRFmodel.RetrieveQuery(queryContent, 1000, 4000, 0.4);
		ctitleIndexReader.close();
		
		MyIndexReader contentIndexReader = new MyIndexReader("content");
		PseudoRFRetrievalModel contentPRFmodel = new PseudoRFRetrievalModel(contentIndexReader);
		List<EngDoc> contentResults = contentPRFmodel.RetrieveQuery(queryContent, 1000, 4000, 0.4);
		contentIndexReader.close();
		
		MyIndexReader authorIndexReader = new MyIndexReader("author");
		PseudoRFRetrievalModel authorPRFmodel = new PseudoRFRetrievalModel(authorIndexReader);
		List<EngDoc> authorResults = authorPRFmodel.RetrieveQuery(queryContent, 1000, 4000, 0.4);
		
		HashMap<String, Double> map = new HashMap<>();
		
		for (EngDoc doc : titleResults) {
			map.put(doc.docid(), doc.score());
		}
		
		for (EngDoc doc : ctitleResults) {
			if (!map.containsKey(doc.docid())) {
				map.put(doc.docid(), doc.score());
				continue;
			}
			double tempScore = map.get(doc.docid());
			tempScore = tempScore * 10 + doc.score() * 5;
			map.put(doc.docid(), tempScore);
		}
		
		for (EngDoc doc : contentResults) {
			if (!map.containsKey(doc.docid())) {
				map.put(doc.docid(), doc.score());
				continue;
			}
			double tempScore = map.get(doc.docid());
			tempScore = tempScore + doc.score();
			map.put(doc.docid(), tempScore);
		}
		
		for (EngDoc doc : authorResults) {
			if (!map.containsKey(doc.docid())) {
				map.put(doc.docid(), doc.score());
				continue;
			}
			double tempScore = map.get(doc.docid());
			tempScore = tempScore + doc.score() * 2;
			map.put(doc.docid(), tempScore);
		}
		
		MinHeap minHeap = new MinHeap(TopN);
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			int docid = Integer.parseInt(entry.getKey());
			IdScorePair element = new IdScorePair(docid, entry.getValue());
			minHeap.offer(element);
		}
		
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
