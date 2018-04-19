package init.searchLucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import init.preprocess.*;

public class ExtractQuery {
	private Set<String> stopWords;

	public ExtractQuery() {
		stopWords = new HashSet<>();
		try {
			// Build Stop Word Set
			String stopWordPath = PrepPath.StopwordDir;
			File stopWordFile = new File(stopWordPath);
			BufferedReader stopWordReader = new BufferedReader(new FileReader(stopWordFile));
			String line = null;
			while ((line = stopWordReader.readLine()) != null) {
				line = line.trim();
				stopWords.add(line);
			}
			stopWordReader.close();
		} catch (IOException e) {
			e.getMessage();
		}
	}

	// aggregated content pre-process
	public String process(String queryContent) {
		// remove punctuation
		queryContent = queryContent.replaceAll("[^a-zA-Z]", " ").trim(); 

		// process content
		char[] contentArr = queryContent.toCharArray();
		StringBuilder result = new StringBuilder();
		int[] contentPoint = {0}; // to remember where the previous word stopped
		while (contentPoint[0] < contentArr.length) {
			String nextWord = nextProcessed(contentArr, contentPoint);// pass object reference contentPoint[] into sub-method
			if (nextWord != null) {
				result.append(nextWord).append(" ");
			}
		}
		return result.toString().trim();
	}

	// pre-process single term
	private String nextProcessed(char[] contentArr, int[] contentPoint) {
		/*
		 * tokenization
		 */
		// section 1. skip spaces before word
		while (contentPoint[0] < contentArr.length && contentArr[contentPoint[0]] == ' ') {
			contentPoint[0]++;
		}
		if (contentPoint[0] >= contentArr.length) {
			return null;
		}
		// section 2. find end point of the word
		int end = contentPoint[0];
		while (end < contentArr.length && contentArr[end] != ' ') {
			end++;
		}
		// section 3. return word
		char[] next = new char[end - contentPoint[0]];
		for (int i = contentPoint[0]; i < end; i++) {
			next[i - contentPoint[0]] = contentArr[i];
		}
		contentPoint[0] = end;

		/*
		 * to lowercase
		 */
		for (int i = 0; i < next.length; i++) {
			next[i] = Character.toLowerCase(next[i]);
		}

		/*
		 * remove stopword & stemming
		 */
		String word = new String(next);
		if (stopWords.contains(word)) {
			return null;
		} else {
			Stemmer stemmer = new Stemmer();
			stemmer.add(next, next.length);
			stemmer.stem();
			String resultWord = stemmer.toString();
			return resultWord;
		}
	}
}
