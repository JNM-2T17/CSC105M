package bigdata.model;

public class Regressor {
	/**
	 * Accepts x values in a Matrix object of the form
	 * [1 x11 x12 ... x1n]
	 * [1 x21 x22 ... x2n]
	 * [... ... ... ...  ]
	 * [1 xm1 xm2 ... xmn]
	 * 
	 * and y values of the form
	 * [y1]
	 * [y2]
	 * [..]
	 * [ym]
	 * 
	 * and outputs the regression coefficients in the form
	 * [B0]
	 * [B1]
	 * [..]
	 * [Bn]
	 *
	 * @param xVals independent x values
	 * @param yVals target dependent y values
	 * @return matrix of regression coefficients
	 */
	public static Matrix getCoefficients(Matrix xVals, Matrix yVals) {
		Matrix xPrime = xVals.transpose();
		Matrix left = xPrime.multiply(xVals);
		Matrix leftInv = left.invert();
		Matrix right = xPrime.multiply(yVals);
		Matrix coeffs = leftInv.multiply(right);

		return coeffs;
	}
}