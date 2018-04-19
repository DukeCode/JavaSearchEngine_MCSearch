package init.crawlRawData;

public class GetPathHelper {
	static int[] starter = new int[] {0, 840, 1680, 2520, 3360, 4200, 5040, 5880, 6420, 6920, 8020};
	static int[] ender = new int[] {820, 1660, 2500, 3340, 4180, 5020, 5860, 6400, 6900, 8000, 8260};
	int count = 0;
	
	public int getStarter() {
		if (count == 0) {
			System.err.println("getStarter() must be run after getNextPath"); 
		}
		// getNextPath will automatically increase the sequence, so starter[count - 1]
		// this method must be run after getNextPath()
		return starter[count - 1];
	}
	
	public String[] getNextPath() {
		if (count >= starter.length) {
			return null;
		}
		String[] filePath = new String[2];
		String start = Integer.toString(starter[count]);
		String end = Integer.toString(ender[count]);
		StringBuffer bf1 = new StringBuffer();
		bf1.append(InitPath.contentPath).append(start).
			append("to").append(end).append("content").append(".csv");
		StringBuffer bf2 = new StringBuffer();
		bf2.append(InitPath.resourcePath).append(start).
			append("to").append(end).append("resource");
		
		filePath[0] = bf1.toString();
		filePath[1] = bf2.toString();

		count++;
		
		return filePath;
	}
}
