package bigdata.model;

/**
 * This class handles the modular operations in a matrix
 * @author Austin Fernandez
 */
public class NormalMatrix extends AbstractMatrix {
	private String trace;

	/**
	 * basic constructor
	 * @param grid contents of matrix
	 */
	public NormalMatrix(double[][] grid) {
		super(grid);
		trace = "";
	}

	/**
	 * returns a NormalMatrix given the grid
	 * @param grid grid with matrix contents
	 * @return NormalMatrix given the grid
	 */
	protected Matrix ofType(double[][] grid) {
		return new NormalMatrix(grid);
	}

	/**
	 * multiplies a row by a scalar
	 * @param scalar scalar to multiply with
	 * @param row to multiply scalar with
	 * @throws IllegalArgumentException if row out of bounds
	 */
	public void scalarRow(double scalar, int row) 
		throws IllegalArgumentException {
		if( row < 0 || row >= rowCount() ) {
			throw new IllegalArgumentException("Row out of bounds");
		} else {
			for(int i = 0; i < colCount(); i++ ) {
				matrix[row][i] = matrix[row][i] * scalar;
			}

			for( Matrix m: augments ) {
				m.scalarRow(scalar, row);
			}
		}
	}

	/**
	 * adds a multiple of a column to another column
	 * @param scalar scalar for row to add
	 * @param row1 row to multiply by zero-indexed
	 * @param row2 row to replace zero-indexed
	 * @throws IllegalArgumentException if row out of bounds
	 */
	public void addColumn(double scalar, int row1, int row2 ) 
		throws IllegalArgumentException {
		if( row1 < 0 || row1 >= rowCount() || row2 < 0 || row2 >= rowCount() 
			|| row1 == row2) {
			throw new IllegalArgumentException("Row out of bounds");
		} else {
			for(int i = 0; i < colCount(); i++ ) {
				matrix[row2][i] = matrix[row1][i] * scalar + matrix[row2][i];
			}

			for( Matrix m: augments ) {
				m.addColumn(scalar, row1, row2);
			}
		}	
	}

	/**
	 * gets the inverse of this matrix
	 * @return null if singular, inverse otherwise
	 */
	public Matrix invert() {
		if( rowCount() != colCount() ) {
			return null;
		} else {
			try {
				Matrix dummy = this.transpose().transpose();
				double[][] invert = new double[rowCount()][colCount()];
				for(int i = 0; i < rowCount(); i++ ) {
					for(int j = 0; j < colCount(); j++ ) {
						if( i == j ) {
							invert[i][j] = 1;
						} else {
							invert[i][j] = 0;
						}
					}
				}
				Matrix inverse = new NormalMatrix(invert);
				dummy.augment(inverse);
				dummy = dummy.reducedRowEchelon();
				boolean valid = true;
				for(int i = 0; valid && i < rowCount(); i++ ) {
					for( int j = 0; valid && j < colCount(); j++ ) {
						if( i == j && Math.abs(dummy.get(i,j)- 1) > 0.000001 ) {
							valid = false;
						} 
						if( i != j && Math.abs(dummy.get(i,j)) > 0.000001 ) {
							valid = false;
						}
					}
				}
				inverse = dummy.getAugment(0);
				return valid ? inverse : null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * returns the product of this matrix with another matrix
	 * @param m multiplier matrix
	 * @return product of the matrices or null if cannot perform operation
	 */
	public Matrix multiply(Matrix m) {
		if( colCount() != m.rowCount() ) {
			return null;
		} else {
			double[][] newMatrix = new double[rowCount()][m.colCount()];
			for( int i = 0; i < rowCount(); i++ ) {
				for(int j = 0; j < m.colCount(); j++ ) {
					newMatrix[i][j] = 0;
					for(int k = 0; k < colCount(); k++ ) {
						newMatrix[i][j] += get(i,k) * m.get(k,j);
					}
				}
			}
			return new NormalMatrix(newMatrix);
		}
	}
	
	/**
	 * returns the reduced row echelon form of this matrix
	 * @return reduced row echelon form of this matrix
	 */
	public Matrix reducedRowEchelon() {
		double[][] grid = new double[rowCount()][colCount()];
		for( int i = 0; i < rowCount(); i++ ) {
			for( int j = 0; j < colCount(); j++ ) {
				grid[i][j] = matrix[i][j];
			}
		}
		Matrix dummy = new NormalMatrix(grid);
		for( Matrix m: augments ) {
			dummy.augment(m);
		}

		// System.out.println(dummy);

		int col = 0;
		//for each row
		for( int i = 0; i < rowCount(); i++ ) {
			//initialize row to select
			int rowSwitch = i;
			boolean nonzero = false;
			//puts a 1 in the top of the remaining matrix
			while(!nonzero && col < colCount()) {
				nonzero = Math.abs(dummy.get(i,col)) != 0.000001;

				//for all remaining rows
				for( int j = i + 1; j < rowCount(); j++ ) {
					nonzero = nonzero || Math.abs(dummy.get(j,col)) > 0.000001;
					//if current row to switch with is a 1 and 
					if( Math.abs(dummy.get(rowSwitch,col)) < 0.00001 
						&& Math.abs(dummy.get(j,col)) > 0.00001
						|| Math.abs(dummy.get(rowSwitch,col) - 1) > 0.00001 
						&& Math.abs(dummy.get(j,col) - 1) < 0.00001) {
						rowSwitch = j;
					}
				}

				//if nonzero column
				if( nonzero ) {
					if(Math.abs(dummy.get(rowSwitch,col) - 1) < 0.00001
							 && i != rowSwitch) {
						dummy.switchRow(i,rowSwitch);	
						trace += ("R" + (i + 1) + "<-> R" + (rowSwitch + 1) 
									+ "\n" + dummy + "\n");
					} else if( Math.abs(dummy.get(i,col) - 1) > 0.00001 ) {
						double temp = 1/dummy.get(i,col);

						dummy.scalarRow(temp,i);
						trace += (temp + "R" + (i + 1) + "-> R" 
									+ (rowSwitch + 1) + "\n" + dummy + "\n");
					}
					
					for(int j = i + 1; j < rowCount(); j++ ) {
						if( Math.abs(dummy.get(j,col)) > 0.00001 ) {
							double temp = dummy.get(j,col);
							dummy.addColumn(-temp,i,j);
							trace += (-temp + "R" + (i + 1) 
										+ " + R" + (j + 1) + "-> R" + (j + 1) 
										+ "\n" + dummy + "\n");
						}
					}
				} 

				col++;
			}
		}

		for( int i = 0; i < dummy.rowCount(); i++ ) {
			int leader = 0;
			for( leader = 0; leader < dummy.colCount(); leader++ ) {
				if( Math.abs(dummy.get(i,leader) - 1) < 0.00001 ) {
					break;
				}
			}
			if( leader < dummy.colCount()) {
				for( int j = i - 1; j >= 0; j-- ) {
					double temp = dummy.get(j,leader);
					dummy.addColumn(-temp,i,j);
					trace += (-temp + "R" + (i + 1) + " + R" + (j + 1) + "-> R" 
								+ (j + 1) + "\n" + dummy + "\n");
				}
			}
		}

		return dummy;
	}

	public String rreTrace() {
		return trace;
	}
}