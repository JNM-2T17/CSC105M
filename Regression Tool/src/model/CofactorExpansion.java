package bigdata.model;

/**
 * This utility class manages solving for determinants via Cofactor Expansion
 * @author Ryan Austin Fernandez
 * September 17, 2015
 */
public class CofactorExpansion {
	/** 
	 * returns the determinant of a given matrix
	 * @param matrix matrix to solve determinant of
	 * @return determinant of a given matrix
	 * @throws Exception when matrix is not square
	 */
	public static double det(double[][] matrix) throws Exception {
		int m = matrix.length; //get row count
		int n = matrix[0].length; //get column count

		//if not square
		if( m != n ) {
			throw new Exception("Non-square matrix. Determinant " 
										+ "undefined");
		} else if( m == 1 ){ //if 1x1 matrix
			return matrix[0][0];
		} else { 
			double determinant = 0; //initialize sum of a1jC1j
			//for each element of first row
			for( int i = 0; i < m; i++ ) {
				//add product of element and cofactor of element
				//if element = 0, product is zero
				determinant += matrix[0][i] == 0 ? 0 : (matrix[0][i] 
								* Math.pow(-1,i) * det(minor(matrix,0,i)));
			}
			return determinant;
		}
	}

	/**
	 * eliminates the row and column of a matrix
	 * @param matrix matrix to be reduced
	 * @param i row to remove
	 * @param j column to remove
	 * @return reduced matrix
	 */
	public static double[][] minor(double[][] matrix, int i, int j) {
		int i1, j1, //counters for main matrix
			i2, j2; //counters for minor matrix
		int m = matrix.length; //rows
		int n = matrix[0].length; //columns

		//reduced matrix
		double[][] minor = new double[m-1][n-1];
		i2 = j2 = 0; //start at row 0 column 0

		//for each row
		for( i1 = 0; i1 < m; i1++ ) {
			if( i1 == i ) { //if row is to be eliminated
				continue;
			} else { //if row is to be included
				for( j1 = 0; j1 < m; j1++ ) { //for each column
					if( j1 == j ) { //if column is to be eliminated
						continue;
					} else { //if column is to be included
						minor[i2][j2] = matrix[i1][j1];
						j2++; //increment reduced column index
					}
				}
				i2++; //increment reduced row index
				j2 = 0; //reset column index
			}
		}

		return minor;
	}
}