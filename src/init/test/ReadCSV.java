package init.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import init.crawlRawData.InitPath;

/*
 * for test use only
 */
public class ReadCSV {
	public static void main(String[] args) {
		read("c3360to4180.csv", "3360to4180url.html", "3360to4180Padding.html");
		System.out.println("ok");
	}
	
	private static void read(String fileName1, String fileName2, String outPutFile) {
		String path1 = InitPath.parsePath + fileName1;
		String path2 = InitPath.urlPath + fileName2;
		String path3 = InitPath.paddingPath + outPutFile;
		HashSet<String> exURL = new HashSet<>();
		try {
			File f1 = new File(path1);
			File f2 = new File(path2);
			File f3 = new File(path3);
			if (!f3.exists()) {
				f3.createNewFile();
			}
			BufferedReader br1 = new BufferedReader(new FileReader(f1));
			BufferedReader br2 = new BufferedReader(new FileReader(f2));
			BufferedWriter wr = new BufferedWriter(new FileWriter(f3)); 
			wr.write("<!doctype html>");
			wr.newLine();
			wr.write("<html>");
			wr.newLine();
			wr.write("<body>");
			wr.newLine();
//			int count = 0;
			br1.readLine();
//			count++;
			String line1 = br1.readLine();
//			count++;
			while (line1 != null) {
//				if (count == 3359) {
//					System.out.println(line1);
//				}
				String[] arr1 = line1.split("\",\"");
				exURL.add(arr1[3].trim()); // change to 2
				line1 = br1.readLine();
//				count++;
			}
			br1.close();
			br2.readLine();
			br2.readLine();
			br2.readLine();
			String line2 = br2.readLine();
			while (line2 != null) {
				line2 = line2.replaceAll("<[^>]*>", " ").trim();
				if (line2.equals("") || line2.equals(" ")) {
					break;
				}
				if (!exURL.contains(line2)) {
					wr.write("<a href=\"");
					wr.write(line2);
					wr.write("\" >");
					wr.write(line2);
					wr.write("</a>");
					wr.newLine();
					System.out.println(line2);
				}
				line2 = br2.readLine();
			}
			// author: Jin Dai 04102018
			br2.close();
			wr.write("</body>");
	        wr.newLine();
	        wr.write("</html>");
			wr.close();
		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
	}
}