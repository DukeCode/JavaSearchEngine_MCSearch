package init.crawlRawData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ParseContent {
	
	private static void parse() throws Exception{
		String parsedTitlePath = InitPath.parsedTitle + "title";
		String parsedContentPath = InitPath.parsedContent + "content";
		String parsedAuthorPath = InitPath.parsedAuthor + "author";
		String parsedResoPath = InitPath.parsedResource + "reso";
		String parsedCTitlePath = InitPath.parsedCTitle + "ctitle";
		
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
		BufferedWriter brpTitle = new BufferedWriter(new FileWriter(pTitleFile));
		BufferedWriter brpCont = new BufferedWriter(new FileWriter(pContFile));
		BufferedWriter brpAuthor = new BufferedWriter(new FileWriter(pAuthorFile));
		BufferedWriter brpReso = new BufferedWriter(new FileWriter(pResoFile));
		BufferedWriter brpCTitle = new BufferedWriter(new FileWriter(pCTitleFile));
		
		GetPathHelper helper = new GetPathHelper();
		String[] temp = null;
		while ((temp = helper.getNextPath()) != null) {
			try {
				read(temp, helper.getStarter(), brpTitle, brpCont, brpAuthor, brpReso, brpCTitle); // getNextPath will automatically increase the sequence, so helper.getStarter() - 1
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		
		brpTitle.close();
		brpCont.close();
		brpAuthor.close();
		brpReso.close();
		brpCTitle.close();
	}
	
	private static void read(String[] filePath, int starter, BufferedWriter brpTitle, BufferedWriter brpCont, BufferedWriter brpAuthor,
			BufferedWriter brpReso, BufferedWriter brpCTitle) throws IOException {

		String contentPath = filePath[0];
		String resourcePath = filePath[1];
		
		// get input stream and output stream
		File contFile = new File(contentPath);
		File resoFile = new File(resourcePath);
		
		BufferedReader brCont = new BufferedReader(new FileReader(contFile));
		BufferedReader brReso = new BufferedReader(new FileReader(resoFile));
		
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
		int indexInt = starter;
		String line2 = null;
		while((line2 = brReso.readLine()) != null) {
			String indexString = Integer.toString(indexInt);
			if (!criticContent.containsKey(line2.trim())) {
				int tempCount = 0;
				while (tempCount < 8) {
					brReso.readLine();
					tempCount++;
				}
				continue;
			}
			int count = 0;
			brpReso.write(indexString);
			brpReso.newLine();
			brpReso.write(line2.trim());
			brpReso.newLine();
			while (count < 8) {
				String tempLine = brReso.readLine().trim();
				brpReso.write(tempLine);
				brpReso.newLine();
				if (count == 1) {
					brpTitle.write(indexString);
					brpTitle.newLine();
					brpTitle.write(line2.trim());
					brpTitle.newLine();
					brpTitle.write(tempLine);
					brpTitle.newLine();
				} else if (count == 4) {
					brpAuthor.write(indexString);
					brpAuthor.newLine();
					brpAuthor.write(line2.trim());
					brpAuthor.newLine();
					brpAuthor.write(tempLine);
					brpAuthor.newLine();
				} else if (count == 5) {
					brpCTitle.write(indexString);
					brpCTitle.newLine();
					brpCTitle.write(line2.trim());
					brpCTitle.newLine();
					brpCTitle.write(tempLine);
					brpCTitle.newLine();
				}
				count++;
			}
			brpCont.write(indexString);
			brpCont.newLine();
			brpCont.write(line2.trim());
			brpCont.newLine();
			brpCont.write(criticContent.get(line2.trim()).toString());
			brpCont.newLine();
			indexInt++;
		}
		brCont.close();
		brReso.close();
	}
	
	public static void main(String[] args) {
		try {
			parse();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}