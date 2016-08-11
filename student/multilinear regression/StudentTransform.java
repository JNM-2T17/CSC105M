import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;

public class StudentTransform {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		PrintWriter pw = new PrintWriter(new FileWriter(new File(transform(args[0]))));
		String curr = br.readLine();
		pw.println("school,sex,age,address,famsize,Pstatus,Medu,Fedu,Mteach," + 
				"Mhealth,Mservices,Mhome,Fteach,Fhealth,Fservices,Fhome," + 
				"closehome,repu,course,guardF,guardM,traveltime,studytime," + 
				"failures,schoolsup,famsup,paid,activities,nursery,higher," + 
				"internet,romantic,famrel,freetime,goout,Dalc,Walc,health," + 
				"absences,G1,G2,G3");
		curr = br.readLine();

		while(curr != null && curr.length() > 0 ) {
			String[] vals = curr.split(",");
			for(int i = 0; i < vals.length; i++) {
				if( vals[i].charAt(0) == '"' && vals[i].charAt(vals[i].length() - 1) == '"') {
					vals[i] = vals[i].substring(1,vals[i].length() - 1);
				}
			}
			String output = "";
			pw.print((vals[0].equals("GP") ? "0," : "1,") +
						(vals[1].equals("M") ? "0," : "1,") + 
						vals[2] + "," + 
						(vals[3].equals("U") ? "0," : "1,") + 
						(vals[4].equals("GT3") ? "0," : "1,") + 
						(vals[5].equals("T") ? "0," : "1,") + 
						vals[6] + "," + vals[7] + ","); 
			switch(vals[8]) {
				case "teacher":
					pw.print("1,0,0,0,");
					break;
				case "health":
					pw.print("0,1,0,0,");
					break;
				case "services":
					pw.print("0,0,1,0,");
					break;
				case "at_home":
					pw.print("0,0,0,1,");
					break;
				default:
					pw.print("0,0,0,0,");
			}
			switch(vals[9]) {
				case "teacher":
					pw.print("1,0,0,0,");
					break;
				case "health":
					pw.print("0,1,0,0,");
					break;
				case "services":
					pw.print("0,0,1,0,");
					break;
				case "at_home":
					pw.print("0,0,0,1,");
					break;
				default:
					pw.print("0,0,0,0,");
			}
			switch(vals[10]) {
				case "home":
					pw.print("1,0,0,");
					break;
				case "reputation":
					pw.print("0,1,0,");
					break;
				case "course":
					pw.print("0,0,1,");
					break;
				default:
					pw.print("0,0,0,");
			}
			switch(vals[11]) {
				case "father":
					pw.print("1,0,");
					break;
				case "mother":
					pw.print("0,1,");
					break;
				default:
					pw.print("0,0,");
			}
			pw.print(vals[12] + "," + vals[13] + "," + vals[14] + "," + 
						(vals[15].equals("yes") ? "1," : "0,") + 
						(vals[16].equals("yes") ? "1," : "0,") + 
						(vals[17].equals("yes") ? "1," : "0,") + 
						(vals[18].equals("yes") ? "1," : "0,") + 
						(vals[19].equals("yes") ? "1," : "0,") + 
						(vals[20].equals("yes") ? "1," : "0,") + 
						(vals[21].equals("yes") ? "1," : "0,") + 
						(vals[22].equals("yes") ? "1" : "0"));
			for(int i = 23; i < vals.length; i++) {
				pw.print("," + vals[i] + ".0");
			}
			pw.println();
			curr = br.readLine();
		}	

		pw.close();
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