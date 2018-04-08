package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class MyDoc {
	private String docID;
	private String criticTitle;
	private String movieTitle;
	private String author;
	private String url;
	private String imageUrl;
	private String mpaaRate;
	private String nytPick;
	private String summary;
	
	@Override
	// for de-duplicate
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docID == null) ? 0 : docID.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}	
		MyDoc other = (MyDoc) obj;
		if (docID == null) {
			if (other.docID != null) {
				return false;
			}
		} else if (!docID.equals(other.docID)) {
			return false;
		}
		return true;
	}
	
	/**
	 * This is a builder pattern in Java.
	 */
	// Constructor
	private MyDoc(MyDocBuilder builder) {
		this.docID = builder.docID;
		this.criticTitle = builder.criticTitle;
		this.movieTitle = builder.movieTitle;
		this.author = builder.author;
		this.url = builder.url;
		this.imageUrl = builder.imageUrl;
		this.mpaaRate = builder.mpaaRate;
		this.nytPick = builder.nytPick;
		this.summary = builder.summary;
	}
	
	// Builder Pattern
	public static class MyDocBuilder {
		private String docID;
		private String criticTitle;
		private String movieTitle;
		private String author;
		private String url;
		private String imageUrl;
		private String mpaaRate;
		private String nytPick;
		private String summary;

		public MyDocBuilder setDocID(String docID) {
			this.docID = docID;
			return this;
		}

		public MyDocBuilder setCriticTitle(String criticTitle) {
			this.criticTitle = criticTitle;
			return this;
		}

		public MyDocBuilder setMovieTitle(String movieTitle) {
			this.movieTitle = movieTitle;
			return this;
		}

		public MyDocBuilder setAuthor(String author) {
			this.author = author;
			return this;
		}

		public MyDocBuilder setUrl(String url) {
			this.url = url;
			return this;
		}

		public MyDocBuilder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		public MyDocBuilder setMpaaRate(String mpaaRate) {
			this.mpaaRate = mpaaRate;
			return this;
		}

		public MyDocBuilder setNytPick(String nytPick) {
			this.nytPick = nytPick;
			return this;
		}

		public MyDocBuilder setSummary(String summary) {
			this.summary = summary;
			return this;
		}
		
		public MyDoc build() {
			return new MyDoc(this);
		}
	}
	
	// Getter
	// only getter is needed because docs remain unchanged after inserted into the database
	public String getDocID() {
		return docID;
	}
	public String getCriticTitle() {
		return criticTitle;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public String getAuthor() {
		return author;
	}
	public String getUrl() {
		return url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getMpaaRate() {
		return mpaaRate;
	}
	public String getNytPick() {
		return nytPick;
	}
	public String getSummary() {
		return summary;
	}
	
	// Convert to JSON
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("doc_id", docID);
			obj.put("criticTitle", criticTitle);
			obj.put("movieTitle", movieTitle);
			obj.put("author", author);
			obj.put("url", url);
			obj.put("imageUrl", imageUrl);
			obj.put("mpaaRate", mpaaRate);
			obj.put("nytPick", nytPick);
			obj.put("summary", summary);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
