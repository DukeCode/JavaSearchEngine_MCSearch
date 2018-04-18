package initialize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ParseContent {
	
	private static void parse() {
		int[] starter = new int[] {0, 840, 1680, 2520, 3360, 4200, 5040, 5880, 6420, 6920, 8020};
		int[] ender = new int[] {820, 1660, 2500, 3340, 4180, 5020, 5860, 6400, 6900, 8000, 8260};
		int length = starter.length;
		for (int i = 0; i < length; i++) {
			try {
				read(starter[i], ender[i]);
			} catch (IOException e) {
				e.getMessage();
			}
		}		
	}
	
	private static void read(int starter, int ender) throws IOException {
		String start = Integer.toString(starter);
		String end = Integer.toString(ender);
		// prepare file path
		StringBuffer bf1 = new StringBuffer();
		bf1.append(InitPath.contentPath).append(start).
			append("to").append(end).append("content").append(".csv");
		StringBuffer bf2 = new StringBuffer();
		bf2.append(InitPath.resourcePath).append(start).
			append("to").append(end).append("resource");
		StringBuffer bf4 = new StringBuffer();
		bf4.append(InitPath.parsedTitle).append(start).
			append("to").append(end).append("parsedTitle");
		StringBuffer bf5 = new StringBuffer();
		bf5.append(InitPath.parsedContent).append("parsedContent").append(start).
			append("to").append(end);
		StringBuffer bf6 = new StringBuffer();
		bf6.append(InitPath.parsedAuthor).append("parsedAuthor").append(start).
			append("to").append(end);
		StringBuffer bf7 = new StringBuffer();
		bf7.append(InitPath.parsedResource).append("parsedResource").append(start).
			append("to").append(end);
		StringBuffer bf8 = new StringBuffer();
		bf8.append(InitPath.parsedCTitle).append(start).
			append("to").append(end).append("parsedCTitle");
		String contentPath = bf1.toString();
		String resourcePath = bf2.toString();
		String parsedTitlePath = bf4.toString();
		String parsedContentPath = bf5.toString();
		String parsedAuthorPath = bf6.toString();
		String parsedResoPath = bf7.toString();
		String parsedCTitlePath = bf8.toString();
		// get input stream and output stream
		File contFile = new File(contentPath);
		File resoFile = new File(resourcePath);
		File pTitleFile = new File(parsedTitlePath);
		if (!pTitleFile.exists()) {
			pTitleFile.createNewFile();
		}
		File pContFile = new File(parsedContentPath);
		if (!pContFile.exists()) {
			pContFile.createNewFile();
		}
		File pAuthorFile = new File(parsedAuthorPath);
		if (!pAuthorFile.exists()) {
			pAuthorFile.createNewFile();
		}
		File pResoFile = new File(parsedResoPath);
		if (!pResoFile.exists()) {
			pResoFile.createNewFile();
		}
		File pCTitleFile = new File(parsedCTitlePath);
		if (!pCTitleFile.exists()) {
			pCTitleFile.createNewFile();
		}
		BufferedReader brCont = new BufferedReader(new FileReader(contFile));
		BufferedReader brReso = new BufferedReader(new FileReader(resoFile));
		BufferedWriter brpTitle = new BufferedWriter(new FileWriter(pTitleFile));
		BufferedWriter brpCont = new BufferedWriter(new FileWriter(pContFile));
		BufferedWriter brpAuthor = new BufferedWriter(new FileWriter(pAuthorFile));
		BufferedWriter brpReso = new BufferedWriter(new FileWriter(pResoFile));
		BufferedWriter brpCTitle = new BufferedWriter(new FileWriter(pCTitleFile));
		// read through csv and store in hashmap
		// reason: duplicate url/different content rows and missing records
		HashMap<String, StringBuffer> criticContent = new HashMap<>();
		String line1 = null;
		while((line1 = brCont.readLine()) != null) {
			String[] lineArr = line1.split(",");
			// data issue: has wired data with no content or large space
			if (lineArr.length < 4) {
				continue;
			}
			StringBuffer contentBuffer = new StringBuffer();
			// add space after each paragraph
			for (int i = 5; i < lineArr.length; i++) {
				contentBuffer.append(lineArr[i].trim()).append(" ");
			}
			if (!criticContent.containsKey(lineArr[3].trim())) {
				criticContent.put(lineArr[3].trim(), contentBuffer);
			} else {
				StringBuffer temp = criticContent.get(lineArr[3].trim());
				temp.append(contentBuffer.toString());
				criticContent.put(lineArr[3].trim(), temp);
			}
		}
		// read through resource file
		String line2 = null;
		while((line2 = brReso.readLine()) != null) {
			if (!criticContent.containsKey(line2.trim())) {
				int tempCount = 0;
				while (tempCount < 8) {
					brReso.readLine();
					tempCount++;
				}
				continue;
			}
			int count = 0;
			brpReso.write(line2.trim());
			brpReso.newLine();
			while (count < 8) {
				String tempLine = brReso.readLine().trim();
				brpReso.write(tempLine);
				brpReso.newLine();
				if (count == 1) {
					brpTitle.write(line2.trim());
					brpTitle.newLine();
					brpTitle.write(tempLine);
					brpTitle.newLine();
				} else if (count == 4) {
					brpAuthor.write(line2.trim());
					brpAuthor.newLine();
					brpAuthor.write(tempLine);
					brpAuthor.newLine();
				} else if (count == 5) {
					brpCTitle.write(line2.trim());
					brpCTitle.newLine();
					brpCTitle.write(tempLine);
					brpCTitle.newLine();
				}
				count++;
			}
			brpCont.write(line2.trim());
			brpCont.newLine();
			brpCont.write(criticContent.get(line2.trim()).toString());
			brpCont.newLine();
		}
		brCont.close();
		brReso.close();
		brpTitle.close();
		brpCont.close();
		brpAuthor.close();
		brpReso.close();
		brpCTitle.close();
	}
	
	public static void main(String[] args) {
		parse();
	}
}