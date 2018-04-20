package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class MyDoc {
	private String docId;
	private String criticTitle;
	private String movieTitle;
	private String author;
	private String url;
	private String imageUrl;
	private String mpaaRate;
	private String nytPick;
	private String summary;
	private String publicationDate;
	
	@Override
	// for de-duplicate
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docId == null) ? 0 : docId.hashCode());
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
		if (docId == null) {
			if (other.docId != null) {
				return false;
			}
		} else if (!docId.equals(other.docId)) {
			return false;
		}
		return true;
	}
	
	/**
	 * This is a builder pattern in Java.
	 */
	// author: Jin Dai 04102018
	// Constructor
	private MyDoc(MyDocBuilder builder) {
		this.docId = builder.docId;
		this.criticTitle = builder.criticTitle;
		this.movieTitle = builder.movieTitle;
		this.author = builder.author;
		this.url = builder.url;
		this.imageUrl = builder.imageUrl;
		this.mpaaRate = builder.mpaaRate;
		this.nytPick = builder.nytPick;
		this.summary = builder.summary;
		this.publicationDate = builder.publicationDate;
	}
	
	// Builder Pattern
	public static class MyDocBuilder {
		private String docId;
		private String criticTitle;
		private String movieTitle;
		private String author;
		private String url;
		private String imageUrl;
		private String mpaaRate;
		private String nytPick;
		private String summary;
		private String publicationDate;

		public MyDocBuilder setDocId(String docId) {
			this.docId = docId;
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
		
		public MyDocBuilder setPublicationDate(String publicationDate) {
			this.publicationDate = publicationDate;
			return this;
		}
		
		public MyDoc build() {
			return new MyDoc(this);
		}
	}
	
	// Getter
	// only getter is needed because docs remain unchanged after inserted into the database
	public String getDocId() {
		return docId;
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
	public String getPublicationDate() {
		return publicationDate;
	}
	
	// Convert to JSON
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("doc_id", docId);
			obj.put("criticTitle", criticTitle);
			obj.put("movieTitle", movieTitle);
			obj.put("author", author);
			obj.put("url", url);
			obj.put("imageUrl", imageUrl);
			obj.put("mpaaRate", mpaaRate);
			obj.put("nytPick", nytPick);
			obj.put("summary", summary);
			obj.put("publicationDate", publicationDate);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
