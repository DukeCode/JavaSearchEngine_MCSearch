package init.preprocess;

import java.io.FileWriter;
import java.util.Map;

public class StartProcess {
	public static void main(String[] args) throws Exception {
		StartProcess sarter = new StartProcess();
		String[] rawFilePath = new String[] {PrepPath.parsedTitle, PrepPath.parsedContent, PrepPath.parsedAuthor, PrepPath.parsedCTitle};
		String[] destPath = new String[] {"title", "content", "author", "ctitle"};
		for (int i = 0; i < rawFilePath.length; i++) {
			sarter.preProcess(rawFilePath[i], PrepPath.Preprocessed + destPath[i]);
		}
	}

	public void preProcess(String filePath, String destPath) throws Exception {
		// Loading the collection file and initiate the DocumentCollection class
		DocumentCollection corpus = new TextDocCollection(filePath);

		// loading stopword list and initiate the StopWordRemover and WordNormalizer class
		StopWordRemover stopwordRemover = new StopWordRemover();
		WordNormalizer normalizer = new WordNormalizer();

		// initiate the BufferedWriter to output result
		FileWriter wr = new FileWriter(destPath);

		// initiate a doc object, which can hold document number and document content of a document
		Map<String, Object> doc = null;

		// process the corpus, document by document, iteractively
		int count=0;
		while ((doc = corpus.nextDocument()) != null) {
			// load document number of the document
			String docno = doc.keySet().iterator().next();

			// load document content
			char[] content = doc.get(docno).toString().toCharArray();

			// write docno into the result file
			wr.append(docno + "\n");

			// initiate the WordTokenizer class
			WordTokenizer tokenizer = new WordTokenizer(content);

			// initiate a word object, which can hold a word
			char[] word = null;

			// process the document word by word iteratively
			while ((word = tokenizer.nextWord()) != null) {
				// each word is transformed into lowercase
				word = normalizer.lowercase(word);

				// filter out stopword, and only non-stopword will be written
				// into result file
				if (!stopwordRemover.isStopword(word)) {
					wr.append(normalizer.stem(word) + " ");
				}
				//stemmed format of each word is written into result file
			}
			wr.append("\n");// finish processing one document
			count++;
		}
		System.out.println("totaly document count:  "+count);
		wr.close();
	}
}
