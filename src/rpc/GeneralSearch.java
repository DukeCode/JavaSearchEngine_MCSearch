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
 * Servlet implementation class GeneralSearch
 */
@WebServlet("/GeneralSearch")
public class GeneralSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeneralSearch() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("user_id");
		String query = request.getParameter("query");
		DBConnection conn = DBConnectionFactory.getDBConnection();
		init.searchLucene.GeneralSearch gs = new init.searchLucene.GeneralSearch();
		ExtractQuery eq = new ExtractQuery();
		List<JSONObject> list = new ArrayList<>();
		JSONArray array = null;
		query = eq.process(query); // parse query
		try {
			List<EngDoc> results = gs.retrieveQuery(query, 50); // show top 50 results
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
			System.out.println("bug");
		}
		array = new JSONArray(list); // return JSON to front-end
		RpcHelper.writeJsonArray(response, array);
		conn.close();
	}
}
