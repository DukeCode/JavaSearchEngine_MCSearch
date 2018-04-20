package init.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.EngDoc;
import entity.MyDoc;
import init.searchLucene.ExtractQuery;

public class TestDb {
	public static void main(String[] args) {
		DBConnection conn = DBConnectionFactory.getDBConnection();
		init.searchLucene.GeneralSearch gs = new init.searchLucene.GeneralSearch();
		ExtractQuery eq = new ExtractQuery();
		String query = eq.process("ben"); // parse query
		List<JSONObject> list = new ArrayList<>();
		try {
			List<EngDoc> results = gs.retrieveQuery(query, 50); // show top 50 results
			Set<String> favorite = conn.getFavoriteDocIds("1111");
			for (EngDoc doc : results) {
				MyDoc tempDoc = conn.getDoc(doc.docno());
				JSONObject obj = tempDoc.toJSONObject();
				if (favorite != null) {
					obj.put("favorite", favorite.contains(doc.docno()));
				}
				list.add(obj);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		JSONArray array = new JSONArray(list); // return JSON to front-end
		System.out.println(array);
		conn.close();
	}
}
