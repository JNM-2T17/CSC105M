package bigdata.model;

public interface Matrix {
	/**
	 * returns the transpose of this matrix
	 * @return transpose of this matrix
	 */
	public Matrix transpose();
	
	/**
	 * switches two rows
	 * @param row1 row to switch zero-indexed
	 * @param row2 row to switch zero-indexed
	 * @throws IllegalArgumentException when arguments are out of bounds
	 */
	public void switchRow(int row1, int row2) throws IllegalArgumentException ;
	
	/**
	 * multiplies a row by a scalar
	 * @param scalar scalar to multiply with
	 * @param row to multiply scalar with
	 * @throws IllegalArgumentException if row out of bounds
	 */
	public void scalarRow(double scalar, int row) throws IllegalArgumentException ;
	
	/**
	 * adds a multiple of a column to another column
	 * @param scalar scalar for row to add
	 * @param row1 row to multiply by zero-indexed
	 * @param row2 row to replace zero-indexed
	 * @throws IllegalArgumentException if row out of bounds
	 */
	public void addColumn(double scalar, int row1, int row2 ) 
		throws IllegalArgumentException ;
	
	/**
	 * augments a matrix to this matrix
	 * @param augmented matrix to augment
	 * @throws IllegalArgumentException when arguments are out of bounds
	 */
	public void augment(Matrix augmented) throws IllegalArgumentException;
	
	/**
	 * returns a certain element in the matrix
	 * @param row row zero-indexed
	 * @param col column zero-indexed
	 * @return element at the given position
	 * @throws IllegalArgumentException when arguments are out of bounds
	 */
	public double get(int row, int col) throws IllegalArgumentException;
	
	/**
	 * returns the number of rows
	 * @return number of rows
	 */
	public int rowCount();
	
	/**
	 * returns the number of columns
	 * @return the number of columns
	 */
	public int colCount();
	
	/**
	 * returns an augmented matrix
	 * @param index index of augmented matrix
	 * @return matrix at that position
	 * @throws IllegalArgumentException when arguments are out of bounds
	 */
	public Matrix getAugment(int index) throws IllegalArgumentException;
	
	/**
	 * returns the determinant of this matrix
	 * @return determinant of this matrix
	 */
	public double determinant();
	
	/**
	 * gets the inverse of this matrix
	 * @return null if singular, inverse otherwise
	 */
	public Matrix invert();
	
	/**
	 * returns the product of this matrix with another matrix
	 * @param m multiplier matrix
	 * @return product of the matrices or null if cannot perform operation
	 */
	public Matrix multiply(Matrix m);
	
	/**
	 * returns the reduced row echelon form of this matrix
	 * @return reduced row echelon form of this matrix
	 */
	public Matrix reducedRowEchelon();

	/**
	 * returns the trace of the reduced row echelon derivation, if any
	 * @return reduced row echelon trace
	 */
	public String rreTrace();
}