package db;

import java.util.List;
import java.util.Set;

import entity.MyDoc;
import entity.Query;
import entity.User;
// author: Jin Dai 04102018
public interface DBConnection {
	/**
	 * Close the connection.
	 */
	public void close();

	// favorite docs
	/**
	 * add the favorite documents for a user.
	 * 
	 * @param userId
	 * @param docIds
	 */
	public void addFavoriteDocs(String userId, List<String> docIds);

	/**
	 * delete the favorite documents for a user.
	 * 
	 * @param userId
	 * @param docIds
	 */
	public void deleteFavoriteDocs(String userId, List<String> docIds);

	/**
	 * Get the favorite document Ids for a user.
	 * 
	 * @param userId
	 * @return docIds
	 */
	public Set<String> getFavoriteDocIds(String userId);

	/**
	 * Get the favorite documents for a user.
	 * 
	 * @param userId
	 * @return documents
	 */
	public Set<MyDoc> getFavoriteDocs(String userId);

	// query logs
	/**
	 * Get the query Ids for a user.
	 * 
	 * @param userId
	 * @return docIds
	 */
	public Set<String> getQueryIds(String userId);
	
	/**
	 * Gets query log of a user
	 * 
	 * @param userId
	 * @return queries
	 */
	public Set<Query> getQueryLog(String userId);
	
	/**
	 * add query into query log of a user
	 * 
	 * @param userId
	 * @param queries
	 */
	public void addQuery(Query query);
	
	/**
	 * delete query log of a user
	 * 
	 * @param userId
	 * @param queries
	 */
	public void deleteQuery(String userId, List<String> queryIds);
	
	/**
	 * Save document into db.
	 * 
	 * @param doc
	 */
	public void addDoc(MyDoc doc);
	

	/**
	 * Save document into db.
	 * 
	 * @param doc
	 */
	public void addUser(User user);
	
	/**
	 * Get full name of a user. (This is not needed for main course, just for demo
	 * and extension).
	 * 
	 * @param userId
	 * @return full name of the user
	 */
	public String getFullname(String userId);

	/**
	 * Return whether the credential is correct. (This is not needed for main
	 * course, just for demo and extension)
	 * 
	 * @param userId
	 * @param password
	 * @return boolean
	 */
	public boolean verifyLogin(String userId, String password);
}
