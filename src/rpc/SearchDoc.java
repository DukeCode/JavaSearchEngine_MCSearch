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
import entity.MyDoc;

/**
 * Servlet implementation class SearchDoc
 */
@WebServlet("/SearchDoc")
public class SearchDoc extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchDoc() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("user_id");
		String movieTitleQuery = request.getParameter("movieTitleQuery");
		String authorQuery = request.getParameter("authorQuery");
		String contentQuery = request.getParameter("contentQuery");
		long userTime = Long.parseLong(request.getParameter("userTime"));
		//List<Event> events = conn.searchEvents(userId, lat, lon, term);
		DBConnection conn = DBConnectionFactory.getDBConnection();
		Set<String> favoriteDocIds = conn.getFavoriteDocIds(userId);
		try {
			// append "favorite" if user have it in favorite list
			for (MyDoc doc : docs) {
				JSONObject obj = doc.toJSONObject();
				if (favoriteDocIds != null) {
					obj.put("favorite", favoriteDocIds.contains(doc.getDocId()));
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
