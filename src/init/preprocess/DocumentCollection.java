package init.preprocess;

import java.io.IOException;
import java.util.Map;

/**
 * DocumentCollection is an interface for reading individual document files from 
 * a collection file.
 */
public interface DocumentCollection {
	
	/**
	 * 
	 * @return The next document stored in the collection; or null if it is the end of the collection file.
	 */
	public abstract Map<String,Object> nextDocument() throws IOException;
	
}
