package bigdata.model;

import java.util.ArrayList;

/**
 * This class simulates a generic matrix and its generic operations.
 * @author Austin Fernandez
 */
public abstract class AbstractMatrix implements Matrix {
	protected double[][] matrix;
	protected ArrayList<Matrix> augments;

	/**
	 * basic constructor
	 * @param matrix matrix containing values
	 */
	public AbstractMatrix(double[][] matrix) {
		augments = new ArrayList<Matrix>();
		this.matrix = matrix;
	}

	/**
	 * returns the transpose of this matrix
	 * @return transpose of this matrix
	 */
	public Matrix transpose() {
		double[][] grid = new double[colCount()][rowCount()];
		for( int i = 0; i < rowCount(); i++ ) {
			for( int j = 0; j < colCount(); j++ ) {
				grid[j][i] = matrix[i][j];
			}
		}
		return ofType(grid);
	}

	/**
	 * returns a matrix of a specific type. Classes implementing this should 
	 * return a matrix of the same subclass
	 * @param grid grid with matrix contents
	 * @return matrix of a specific type
	 */
	protected abstract Matrix ofType(double[][] grid);

	/**
	 * switches two rows
	 * @param row1 row to switch zero-indexed
	 * @param row2 row to switch zero-indexed
	 * @throws IllegalArgumentException when arguments are out of bounds
	 */
	public void switchRow(int row1, int row2) throws IllegalArgumentException {
		if( row1 < 0 || row1 >= rowCount() || row2 < 0 
			|| row2 >= rowCount() ) {
			throw new IllegalArgumentException("Index out of bounds");
		}

		for( int i = 0; i < colCount(); i++ ) {
			double temp = matrix[row1][i];
			matrix[row1][i] = matrix[row2][i];
			matrix[row2][i] = temp;
		}

		for( Matrix m: augments ) {
			m.switchRow(row1,row2);
		}
	}

	/**
	 * augments a matrix to this matrix
	 * @param augmented matrix to augment
	 * @throws IllegalArgumentException when arguments are out of bounds
	 */
	public void augment(Matrix augmented) throws IllegalArgumentException {
		if( augmented.rowCount() != rowCount() ) {
			throw new IllegalArgumentException("Illegal augmentation");
		}
		augments.add(augmented);
	}

	/**
	 * returns the number of rows
	 * @return number of rows
	 */
	public int rowCount() {
		return matrix.length;
	}

	/**
	 * returns the number of columns
	 * @return the number of columns
	 */
	public int colCount() {
		return matrix.length == 0 ? 0 : matrix[0].length;
	}

	/**
	 * returns a certain element in the matrix
	 * @param row row zero-indexed
	 * @param col column zero-indexed
	 * @return element at the given position
	 * @throws IllegalArgumentException when arguments are out of bounds
	 */
	public double get(int row, int col) throws IllegalArgumentException {
		if( row < 0 || row >= rowCount() || col < 0 
			|| col >= colCount() ) {
			throw new IllegalArgumentException("Index out of bounds");
		} else {
			return matrix[row][col];
		}
	}

	/**
	 * returns an augmented matrix
	 * @param index index of augmented matrix
	 * @return matrix at that position
	 * @throws IllegalArgumentException when arguments are out of bounds
	 */
	public Matrix getAugment(int index) throws IllegalArgumentException {
		if( index < 0 || index >= augments.size() ) {
			throw new IllegalArgumentException("Index out of bounds");
		} else {
			return augments.get(index);
		}
	}

	/**
	 * returns the determinant of this matrix
	 * @return determinant of this matrix
	 */
	public double determinant() {
		try {
			return CofactorExpansion.det(matrix);
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * returns this matrix as a string
	 * @return matrix as a string
	 */
	public String toString() {
		String ret = "";
		for( int i = 0; i < rowCount(); i++) {
			for( int j = 0; j < colCount(); j++ ) {
				ret += matrix[i][j] + "\t";
			}  
			for(Matrix m: augments) {
				ret += "|\t";
				for( int j = 0; j < m.colCount(); j++ ) {
					ret += m.get(i,j) + "\t";
				}
			}
			ret += "\n";
		}
		return ret;
	}
}