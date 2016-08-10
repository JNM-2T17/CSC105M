package bigdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import bigdata.model.*;

public class Driver {
	private static int attrCtr = 5;

	public static void main(String[] args) {
		try {
			attrCtr = Integer.parseInt(args[0]);
			BufferedReader br = new BufferedReader(new FileReader(new File(args[1])));
			ArrayList<DataPoint> list = new ArrayList<DataPoint>();
			String[] attrs = br.readLine().split(",");
			do {
				String str = br.readLine();
				if( str != null ) {
					String[] parts = str.split(",");
					DataPoint dp = new DataPoint();
					for(String s : parts) {
						try {
							dp.values.add(Double.parseDouble(s));
						} catch(Exception e) {
							System.out.println("Entry #" + (list.size() + 2) + str);
						}
					}
					list.add(dp);
				} else {
					break;
				}
			} while(true);
			Collections.sort(list,new Comparator<DataPoint>() {
				public int compare(DataPoint p1, DataPoint p2) {
					double diff = p1.values.get(0) - p2.values.get(0);
					if( Math.abs(diff) < 0.00000001) {
						return 0;
					} else if( diff < 0 ) {
						return -1;
					} else {
						return 1;
					}
				}
			});
			DataPoint[] arr = list.toArray(new DataPoint[0]);
			double min = arr[0].values.get(0);
			double max = arr[arr.length - 1].values.get(0);
			for(DataPoint dp : arr) {
				double value = dp.values.get(0);
				value = (value - min) / (max - min) * 9 + 1;
				dp.values.set(0,value);
			}
			double[][] x = new double[arr.length][attrCtr + 1];
			double[][] y = new double[arr.length][1];
			for(int i = 0; i < arr.length; i++) {
				x[i][0] = 1;
				for(int j = 0; j < attrCtr; j++) {
					x[i][j + 1] = arr[i].values.get(j);
				}
				y[i][0] = arr[i].values.get(attrCtr);
			}
			Matrix xVals = new NormalMatrix(x);
			Matrix yVals = new NormalMatrix(y);
			
			Matrix coeffs = Regressor.getCoefficients(xVals,yVals);

			System.out.print("y^ = ");
			for(int i = 0; i < coeffs.rowCount(); i++) {
				if( i == 0 ) {
					System.out.print(coeffs.get(i,0));
				} else {
					System.out.print( " + " + coeffs.get(i,0) + attrs[i - 1]);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

class DataPoint {
	public ArrayList<Double> values;

	public DataPoint() {
		values = new ArrayList<Double>();
	}
}