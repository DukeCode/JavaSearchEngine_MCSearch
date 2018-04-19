package init.preprocess;

/**
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	char[] texts;
	int start;
	public WordTokenizer( char[] texts ) {
		// this constructor will tokenize the input texts (usually it is a char array for a whole document)
		this.texts = texts;
		start = 0;
	}
	
	public char[] nextWord() {
		// read and return the next word of the document
		// or return null if it is the end of the document

        // section 1. skip spaces before word
        while (start < texts.length && texts[start] == ' ') {
            start++;
        }
        if (start >= texts.length) {
            return null;
        }
        // section 2. find end point of the word
        int end = start;
        while (end < texts.length && texts[end] != ' ') {
            end++;
        }
        // section 3. return word
        char[] next = new char[end - start];
        for (int i = start; i < end; i++) {
            next[i - start] = texts[i];
        }
        start = end; 
		return next;
	}
	
}
