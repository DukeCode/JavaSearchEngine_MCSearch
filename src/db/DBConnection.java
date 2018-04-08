package db;

import java.util.List;
import java.util.Set;

import entity.Document;
import entity.MyDoc;
import entity.Query;

public interface DBConnection {
	/**
	 * Close the connection.
	 */
	public void close();

	// favorite docs
	/**
	 * Insert the favorite documents for a user.
	 * 
	 * @param userID
	 * @param docIDs
	 */
	public void setFavoriteDocs(String userID, List<String> docIDs);

	/**
	 * Delete the favorite documents for a user.
	 * 
	 * @param userID
	 * @param docIDs
	 */
	public void unsetFavoriteDocs(String userID, List<String> docIDs);

	/**
	 * Get the favorite document ids for a user.
	 * 
	 * @param userID
	 * @return docIDs
	 */
	public Set<String> getFavoriteDocIds(String userID);

	/**
	 * Get the favorite documents for a user.
	 * 
	 * @param userID
	 * @return documents
	 */
	public Set<MyDoc> getFavoriteDocs(String userID);

	// query logs
	/**
	 * Gets query log of a user
	 * 
	 * @param userID
	 * @return queries
	 */
	public Set<Query> getQueryLog(String userID);
	
	/**
	 * save query log of a user
	 * 
	 * @param userID
	 * @param queries
	 */
	public void saveQuery(String userID, String queryContent, String userTime, String queryType);
	
	/**
	 * delete query log of a user
	 * 
	 * @param userID
	 * @param queries
	 */
	public void deleteQueryLog(String userID, List<String> queryIDs);
	
	// search
	/**
	 *
	 * @param userID
	 * @param queryContent
	 * @param queryType
	 * @param userTime
	 *            (Nullable)
	 * @return list of documents
	 */
	public List<MyDoc> searchDocuments(String userID, String queryContent, String queryType, String userTime);

	/**
	 * Save document into db.
	 * 
	 * @param doc
	 */
	public void saveDoc(MyDoc doc);
}
