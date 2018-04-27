package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.EngDoc;
import entity.MyDoc;
import init.searchLucene.ExtractQuery;

/**
 * Servlet implementation class ComplicatedSearch
 */
@WebServlet("/ComplicatedSearch")
public class ComplicatedSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ComplicatedSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("user_id");
		String movieTitleQ = request.getParameter("movieTitle");
		if (movieTitleQ == null) {
			movieTitleQ = "";
		}
		String criticTitleQ = request.getParameter("criticTitle");
		if (criticTitleQ == null) {
			criticTitleQ = "";
		}
		String authorQ = request.getParameter("author");
		if (authorQ == null) {
			authorQ = "";
		}
		String contentQ = request.getParameter("content");
		if (contentQ == null) {
			contentQ = "";
		}
		DBConnection conn = DBConnectionFactory.getDBConnection();
		init.searchLucene.ComplicatedSearch gs = new init.searchLucene.ComplicatedSearch();
		ExtractQuery eq = new ExtractQuery();
		movieTitleQ = eq.process(movieTitleQ); 
		criticTitleQ = eq.process(criticTitleQ); 
		authorQ = eq.process(authorQ); 
		contentQ = eq.process(contentQ); 
		List<JSONObject> list = new ArrayList<>();
		JSONArray array = null;
		try {
			List<EngDoc> results = gs.retrieveQuery(movieTitleQ, criticTitleQ, contentQ, authorQ, 50); // show top 50 results
			Set<String> favorite = conn.getFavoriteDocIds(userId);
			for (EngDoc doc : results) {
				MyDoc tempDoc = conn.getDoc(doc.docno());
				JSONObject obj = tempDoc.toJSONObject();
				if (favorite != null) {
					obj.put("favorite", favorite.contains(doc.docno()));
				}
				list.add(obj);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		if (list.isEmpty()) {
			array = new JSONArray();
		} else {
			array = new JSONArray(list); // return JSON to front-end
		}
		RpcHelper.writeJsonArray(response, array);
		conn.close();
	}
}
