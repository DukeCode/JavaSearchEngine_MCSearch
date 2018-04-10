package initialize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetURLs {
	private static final String API_HOST = "api.nytimes.com";
	private static final String SEARCH_PATH = "/svc/movies/v2/reviews/search.json";
	private static final String API_KEY = " ";
	private static BufferedWriter urlWriter;
	private static BufferedWriter resourceWriter;
	int startOffset;
	int endOffset;
	
	public GetURLs(int startOffset, int endOffset) throws IOException {
		// only consider posts after 2004, endOffset <= 8260
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		
		// For url file
		File urlFile = new File(InitPath.urlPath + startOffset + "to" + endOffset + "url");
		if (!urlFile.exists()) {
			urlFile.createNewFile();
		}
		urlWriter = new BufferedWriter(new FileWriter(urlFile));
		urlWriter.write("<!doctype html>");
		urlWriter.newLine();
		urlWriter.write("<html>");
		urlWriter.newLine();
		urlWriter.write("<body>");
		urlWriter.newLine();
		
		// For response file
		File resourceFile = new File(InitPath.resourcePath + startOffset + "to" + endOffset + "resource");
		if (!resourceFile.exists()) {
			resourceFile.createNewFile();
		}
		resourceWriter = new BufferedWriter(new FileWriter(resourceFile));
	}
	
	// get critics in a 20-critics page
	private void get(int offset) {
		String url = "http://" + API_HOST + SEARCH_PATH;
		String query = String.format("apikey=%s&offset=%s", API_KEY, offset);
		try {
			// Create a HTTP connection based on url
			HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();
			// Set requrest method to GET
			connection.setRequestMethod("GET");
			// Send request to NYT and get response, response code could be returned directly
			// response body is saved in InputStream of connection.
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url + "?" + query);
			System.out.println("Response Code : " + responseCode);
			// read response body 
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject responseJson = new JSONObject(response.toString());
            JSONArray critics= (JSONArray) responseJson.get("results");
            // get critics object
            for (int i = 0; i < critics.length(); i++) {
            		JSONObject critic = critics.getJSONObject(i);
            		if (critic != null) {
            			// get property data
            			String movieTitle = getStringFieldOrNull(critic, "display_title");
            			String mpaaRate = getStringFieldOrNull(critic, "mpaa_rating");
            			if (mpaaRate == null || mpaaRate.equals(" ") || mpaaRate.equals("")) {
            				mpaaRate = "NotRated";
            			}
            			String nytPick = "n";
            			if (getIntFieldOrNull(critic, "critics_pick") == 1) {
            				nytPick = "y";
            			}
            			String author = getStringFieldOrNull(critic, "byline");
            			String criticTitle = getStringFieldOrNull(critic, "headline");
            			String summary = getStringFieldOrNull(critic, "summary_short");
            			String publicationDate = getStringFieldOrNull(critic, "publication_date");
            			// get post url
            			JSONObject link = critic.getJSONObject("link");
            			String linkURL = null;
            			if (link != null) {
            				linkURL = getStringFieldOrNull(link, "url");
            			} 
            			// in case post does not have url
            			if (linkURL == null) {
            				throw new NoURLException("Fetal Error: Post Does Not Have URL! number:" + offset);
            			}
            			// write to url file
            			StringBuilder linkURLSB = new StringBuilder();
        				linkURLSB.append("<a href=\"").append(linkURL).append("\" >").append(linkURL).append("</a>");
        				urlWriter.write(linkURLSB.toString());
//        				System.out.println(linkURLSB.toString());
        				linkURLSB = null;
        				urlWriter.newLine();
        				// get post img
        				String imgSRC = null;
        				if (!critic.isNull("multimedia")) { // to walk around json exception
        					JSONObject multiMedia = critic.getJSONObject("multimedia");
                			imgSRC = getStringFieldOrNull(multiMedia, "src");
        				} else {
        					// some post may not have img, use default
        					imgSRC = "default";
        				}
            			// write to resouce file
            			resourceWriter.write(linkURL);
            			resourceWriter.newLine();
            			resourceWriter.write(imgSRC);
            			resourceWriter.newLine();
            			resourceWriter.write(movieTitle);
            			resourceWriter.newLine();
            			resourceWriter.write(mpaaRate);
            			resourceWriter.newLine();
            			resourceWriter.write(nytPick);
            			resourceWriter.newLine();
            			resourceWriter.write(author);
            			resourceWriter.newLine();
            			resourceWriter.write(criticTitle);
            			resourceWriter.newLine();
            			resourceWriter.write(summary);
            			resourceWriter.newLine();
            			resourceWriter.write(publicationDate);
            			resourceWriter.newLine();
            		}
            }
		} catch (NoURLException e1) {
			e1.getMessage();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	// Get String Field or Null
	private String getStringFieldOrNull(JSONObject event, String field) throws JSONException {
		return event.isNull(field) ? null : event.getString(field);
	}
	
	// Get Int Field or Null
	private Integer getIntFieldOrNull(JSONObject event, String field) throws JSONException {
		return event.isNull(field) ? null : event.getInt(field);
	}
	
	// Get different part of url file
	private void getURLFile() throws InterruptedException {
		for(int i = startOffset; i <= endOffset; i += 20) {
			get(i);
//			TimeUnit.MILLISECONDS.sleep(6000);
		}
	}
	
	private void closeWriter() throws IOException {
		resourceWriter.close();
		urlWriter.write("</body>");
        urlWriter.newLine();
        urlWriter.write("</html>");
        urlWriter.close();
	}
	// author: Jin Dai 04102018
	public static void main(String[] args) {
		try {
			// only consider posts after 2004, endOffset <= 8260
			// split into 3 part: 0to2760;2780to5540;5560to8260
			// be careful for response code 429
			GetURLs getFile = new GetURLs(8020, 8260);
			getFile.getURLFile();
			getFile.closeWriter();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}		
	}
}
