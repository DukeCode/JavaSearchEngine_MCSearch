package init.pseudoRF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entity.EngDoc;
import init.indexingLucene.MyIndexReader;
import init.searchLucene.QueryRetrievalModel;
import init.tools.DirichletSmooth;
import init.tools.IdScorePair;
import init.tools.MinHeap;

public class PseudoRFRetrievalModel {

	MyIndexReader ixreader;
	QueryRetrievalModel original;
	Map<String, Integer> topDocs;
	
	public PseudoRFRetrievalModel(MyIndexReader ixreader)
	{
		this.ixreader=ixreader;
		original = new QueryRetrievalModel(ixreader);
		topDocs = new HashMap<>();
	}
	
	/**
	 * Search for the topic with pseudo relevance feedback in 2017 spring assignment 4. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @param TopK The count of feedback documents
	 * @param alpha parameter of relevance feedback model
	 * @return TopN most relevant document, in List structure
	 */
	public List<EngDoc> RetrieveQuery( String queryContent, int TopN, int TopK, double alpha) throws Exception {	
		// this method will return the retrieval result of the given Query, and this result is enhanced with pseudo relevance feedback

		//get P(token|feedback documents)
		HashMap<String, Double> TokenRFScore = GetTokenRFScore(queryContent, TopK);
		
		// sort all retrieved documents from most relevant to least, and return TopN
		List<EngDoc> results = new ArrayList<EngDoc>();
		
		String[] contentArr = queryContent.split(" ");
		List<EngDoc> topK = original.retrieveQuery(queryContent, TopK);
		long cLength = ixreader.colLength();
		MinHeap minHeap = new MinHeap(TopN);
		
		for (EngDoc doc :  topK) {
			double score = 1;
			int docId = Integer.parseInt(doc.docid());
			int docLength = ixreader.docLength(docId);
			for (int i = 0; i < contentArr.length; i++) {
				long cFreq = ixreader.CollectionFreq(contentArr[i]);
				// if the query term not existed in the collection, just skip it
				if (cFreq == 0) {
					continue;
				}
				
				int termFreq = 0;
				String tempKey = doc.docid().trim() + contentArr[i];
				if (topDocs.containsKey(tempKey)) {
					termFreq = topDocs.get(tempKey);
				}

				/*
				 * Smoothing Method
				 * miu = 3000, same as the original model
				 */
				double tempScore = DirichletSmooth.calculateScore(termFreq, cFreq, 3000, docLength, cLength);
				tempScore = alpha * tempScore + (1 - alpha) * TokenRFScore.get(contentArr[i]);
				score = score * tempScore;
			}
			IdScorePair element = new IdScorePair(docId, score);
			minHeap.offer(element);
		}
		
		while (!minHeap.isEmpty()) {
			IdScorePair tempPair = minHeap.poll();
			String tempDocID = String.valueOf(tempPair.docid);
			String tempDocNo = ixreader.getDocno(tempPair.docid);
			results.add(new EngDoc(tempDocID, tempDocNo, tempPair.score));
		}
		Collections.reverse(results);
		
		return results;
	}
	
	public HashMap<String,Double> GetTokenRFScore(String queryContent,  int TopK) throws Exception {
		// for each token in the query, you should calculate token's score in feedback documents: P(token|feedback documents)
		// use Dirichlet smoothing
		// save <token, score> in HashMap TokenRFScore, and return it
		HashMap<String, Double> TokenRFScore = new HashMap<String, Double>();
		
		List<EngDoc> topK = original.retrieveQuery(queryContent, TopK);
		Set<Integer> docidSet = new HashSet<>();
		if (topK == null) {
			System.err.println("No results found.");
			return null;
		}
		
		int rfDocLength = 0;
		for (EngDoc doc : topK) {
			int docId = Integer.parseInt(doc.docid());
			docidSet.add(docId);
			rfDocLength += ixreader.docLength(docId);
		}
		
		String[] contentArr = queryContent.split(" ");
		long cLength = ixreader.colLength();
		for (int i = 0; i < contentArr.length; i++) {
			long cFreq = ixreader.CollectionFreq(contentArr[i]);
			// if the query term not existed in the collection, just skip it
			if (cFreq == 0) {
				continue;
			}
			
			int rfTermFreq = 0;
			int[][] tempList = ixreader.getPostingList(contentArr[i]);
			for (int j = 0; j < tempList.length; j++) {
				if (docidSet.contains(tempList[j][0])) {
					rfTermFreq += tempList[j][1];
					String tempKey = String.valueOf(tempList[j][0]) + contentArr[i];
					topDocs.put(tempKey, tempList[j][1]); //store top documentId, term, and freq in a hashMap
				}
			}
			/*
			 * Smoothing Method
			 * miu = 3000, same as the original model
			 */
			double score = DirichletSmooth.calculateScore(rfTermFreq, cFreq, 3000, rfDocLength, cLength); 
			TokenRFScore.put(contentArr[i], score);
		}
		return TokenRFScore;
	}
}