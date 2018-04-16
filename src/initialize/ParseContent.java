package initialize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class ParseContent {
	
	private static void parse() {
		int[] starter = new int[] {0, 840, 1680, 2520, 3360, 4200, 5040, 5880, 6420, 6920, 8020};
		int[] ender = new int[] {820, 1660, 2500, 3340, 4180, 5020, 5860, 6400, 6900, 8000, 8260};
		int length = starter.length;
		for (int i = 0; i < length; i++) {
			read(starter[i], ender[i]);
		}		
	}
	
	private static HashSet<String> existed(int starter, int ender) {
		HashSet<String> exist = new HashSet<>();
		StringBuffer bf = new StringBuffer();
		bf.append(InitPath.contentPath).append(starter).
			append("to").append(ender).append("content").append(".csv");
		String contentPath = bf.toString();
		BufferedReader br = new BufferedReader(new FileReader(contentPath));
		br.readLine();
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] arr = line.split("\",\"");
			exist.add(arr[3].trim()); 
		}
		return exist;
	}
	
	private static void read(int starter, int ender) {
		StringBuffer bf1 = new StringBuffer();
		bf1.append(InitPath.contentPath).append(starter).
			append("to").append(ender).append("content").append(".csv");
		StringBuffer bf2 = new StringBuffer();
		bf2.append(InitPath.resourcePath).append(starter).
			append("to").append(ender).append("resource");
		StringBuffer bf4 = new StringBuffer();
		bf4.append(InitPath.parsedTitle).append(starter).
			append("to").append(ender).append("parsedTitle");
		StringBuffer bf5 = new StringBuffer();
		bf5.append(InitPath.parsedContent).append("parsedContent").append(starter).
			append("to").append(ender);
		StringBuffer bf6 = new StringBuffer();
		bf6.append(InitPath.parsedAuthor).append("parsedAuthor").append(starter).
			append("to").append(ender);
		StringBuffer bf7 = new StringBuffer();
		bf7.append(InitPath.parsedResource).append("parsedResource").append(starter).
			append("to").append(ender);
		String contentPath = bf1.toString();
		String resourcePath = bf2.toString();
		String parsedTitlePath = bf4.toString();
		String parsedContentPath = bf5.toString();
		String parsedAuthorPath = bf6.toString();
		String parsedResoPath = bf7.toString();
		try {
			File contFile = new File(contentPath);
			File resoFile = new File(resourcePath);
			File pTitleFile = new File(parsedTitlePath);
			if (!pTitleFile.exists()) {
				pTitleFile.createNewFile();
			}
			File pContFile = new File(parsedContentPath);
			if (!pContFile.exists()) {
				pContFile.createNewFile();
			File pAuthorFile = new File(parsedAuthorPath);
			if (!pAuthorFile.exists()) {
				pAuthorFile.createNewFile();
			}
			File pResoFile = new File(parsedResoPath);
			if (!pResoFile.exists()) {
				pResoFile.createNewFile();
			}
			
			BufferedReader brCont = new BufferedReader(new FileReader(contFile));
			BufferedReader brReso = new BufferedReader(new FileReader(resoFile));
			
			BufferedWriter brpTitle = new BufferedWriter(new FileWriter(pTitleFile));
			BufferedWriter brpCont = new BufferedWriter(new FileWriter(pContFile));
			BufferedWriter brpAuthor = new BufferedWriter(new FileWriter(pAuthorFile));
			BufferedWriter brpReso = new BufferedWriter(new FileWriter(pResoFile));
			
			brCont.readLine();
			String line1 = brCont.readLine();
			while (line1  != null) {
				String[] arr1 = line1.split("\",\"");
				String urlOne = arr1[3].trim();
				String urlTwo = brReso.readLine().trim();
				// if not exist, go to next record
				if (!urlOne.equals(urlTwo)) {
					brReso.readLine();
					brReso.readLine();
					brReso.readLine();
					brReso.readLine();
					brReso.readLine();
					brReso.readLine();
					brReso.readLine();
					brReso.readLine();
					continue;
				}
				// url
				brpReso.write(urlTwo);
				// img url
				brpReso.write(brReso.readLine().trim());
				brpReso.newLine();
				// Movie title
				String movieTitle = brReso.readLine().trim();
				brpReso.write(movieTitle);
				brpReso.newLine();
				brpTitle.write(movieTitle);
				brpTitle.newLine();
				//mpaaRate
				brpReso.write(brReso.readLine().trim());
				brpReso.newLine();
				// nyt pick
				brpReso.write(brReso.readLine().trim());
				brpReso.newLine();
				// author
				String author = brReso.readLine().trim();
				brpReso.write(author);
				brpReso.newLine();
				brpAuthor.write(author);
				brpAuthor.newLine();
				// critic title
				String criticTitle = brReso.readLine().trim();
				brpReso.write(criticTitle);
				brpReso.newLine();
				brpCont.write(criticTitle);
				brpCont.write(" ");
				// summary
				String summary = brReso.readLine().trim();
				brpReso.write(summary);
				brpReso.newLine();
				brpCont.write(summary);
				// publication date
				brpReso.write(brReso.readLine().trim());
				brpReso.newLine();
				// whole content
				
				
				
				
				
				brReso.readLine();
				brReso.readLine();
				brpAuthor.write(brReso.readLine().trim());
				
			}
		
