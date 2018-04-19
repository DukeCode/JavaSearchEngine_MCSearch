package init.preprocess;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StopWordRemover {
	Set<String> stopWords;
	public StopWordRemover( ) {
		// load and store the stop words from the fileinputstream with appropriate data structure
		// that you believe is suitable for matching stop words.
		// address of stopword.txt should be Path.StopwordDir
		stopWords = new HashSet<>();
        String path = PrepPath.StopwordDir;
        File file = new File(path);

        try (BufferedReader inputStream = new BufferedReader(new FileReader(file))) {
            String line = inputStream.readLine();
            if (line == null) {
                System.err.println("stop-word file is empty");
            }
            while (line != null) {
                line = line.trim();
                stopWords.add(line);
                line = inputStream.readLine();
            }
        } catch (IOException e) {
            System.err.println("Caught IOException:" + e.getMessage());
        }
	}
	
	public boolean isStopword(char[] word) {
		// return true if the input word is a stopword, or false if not
        String wordStr = new String(word);
        if (stopWords.contains(wordStr)) {
            return true;
        }
		return false;
	}
}
