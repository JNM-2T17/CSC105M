import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;

public class Crawl {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		String curr = br.readLine();
		
		while(curr != null && curr.length() > 0 ) {
			String[] vals = curr.split(",");
			System.out.println(vals.length);
			curr = br.readLine();
		}	

		br.close();
	}

	private static String transform(String filename) {
		System.out.println(filename);
		String[] parts = filename.split("\\.");
		for(String s : parts ) {
			System.out.println(s);
		}
		String newFilename = "";
		for(int i = 0; i < parts.length - 1; i++) {
			if( i > 0 ) {
				newFilename += ".";
			}
			newFilename += parts[i];
		}
		newFilename += "-clean." + parts[parts.length - 1];
		return newFilename;
	}
}