package hr.marin.complex;

import java.util.Arrays;

/**
 * Class represents a root-based complex polynomial expression. It offers
 * methods for some mathematical operations on polynomials.
 * 
 * @author Marin
 *
 */
public class ComplexRootedPolynomial {
	/**
	 * All the roots of this complex polynomial.
	 */
	private Complex[] roots;

	/**
	 * Creates a new root-based complex polynomial with the given roots.
	 * 
	 * @param roots
	 *            All the roots of the polynomial that is to be created.
	 */
	public ComplexRootedPolynomial(Complex... roots) {
		this(false, roots);
	}

	/**
	 * A private constructor that offers the user the option to use the given
	 * array of roots (just copy the array reference) instead of copying its
	 * elements into a new array.
	 * 
	 * @param useArray
	 *            True if the constructor should just copy the array reference,
	 *            false if it should copy all the elements.
	 * @param roots
	 *            The roots of the complex polynomial.
	 */
	private ComplexRootedPolynomial(boolean useArray, Complex... roots) {
		if (useArray) {
			this.roots = roots;
			return;
		}

		this.roots = Arrays.copyOf(roots, roots.length);
	}

	/**
	 * Computes the value of the polynomial at the given point z.
	 * 
	 * @param z
	 *            The point where the polynomial expression is to be evaluated.
	 * @return A new complex number that is the result of the operation.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ONE;

		for (Complex root : roots) {
			result = result.multiply(z.sub(root));
		}

		return result;
	}

	/**
	 * Converts this polynomial representation to to the type
	 * {@link ComplexPolynomial}.
	 * 
	 * @return A {@link ComplexPolynomial} object that is the representation of
	 *         this polynomial.
	 */
	public ComplexPolynomial toComplexPolynom() {

		boolean[][] partitiveSet = new boolean[(int) Math.pow(2, roots.length)][roots.length];

		int number;
		for (int i = 0; i < partitiveSet.length; i++) {
			number = i;
			for (int j = partitiveSet[i].length - 1; j >= 0; j--) {
				if (number == 0) {
					partitiveSet[i][j] = false;
				} else {
					partitiveSet[i][j] = (number % 2 == 1) ? true : false;
					number /= 2;
				}
			}
		}

		Complex[] coefficients = new Complex[roots.length + 1];

		Complex product = null;
		int counter;
		int coefIndex;
		for (int i = 0; i < partitiveSet.length; i++) {
			product = Complex.ONE;
			counter = 0;
			for (int j = 0; j < partitiveSet[i].length; j++) {
				if (partitiveSet[i][j]) {
					product = product.multiply(roots[j]);
					counter++;
				}
			}

			coefIndex = roots.length - counter;
			if (counter % 2 == 1) {
				product = product.negate();
			}

			if (coefficients[coefIndex] == null) {
				coefficients[coefIndex] = product;
			} else {
				coefficients[coefIndex] = coefficients[coefIndex].add(product);
			}
		}

		return new ComplexPolynomial(coefficients);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(roots.length * 5);

		for (Complex root : roots) {
			builder.append("(z - (").append(root.toString()).append("))");
		}

		return builder.toString();
	}

	/**
	 * Finds the index of the closest root for a given complex number z that is
	 * within the given treshold. If there is no such root, return -1.
	 * 
	 * @param z
	 *            The complex number the closest root to which is to be found.
	 * @param treshold
	 *            The maximum absolute difference of z and the closest root.
	 * @return The index of the closest root if it's within the treshold, -1
	 *         otherwise.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int indexOfMin = -1;
		double minDifference = 0.0;

		double currentDifference;
		for (int i = 0; i < roots.length; i++) {
			currentDifference = z.sub(roots[i]).module();
			if (currentDifference <= treshold) {
				if (indexOfMin == -1 || currentDifference < minDifference) {
					indexOfMin = i;
					minDifference = currentDifference;
				}
			}
		}

		return indexOfMin;
	}
}
