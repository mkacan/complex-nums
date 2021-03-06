package hr.marin.complex;

import java.util.Arrays;

/**
 * Class represents a coefficient-based complex polynomial expression. It offers
 * methods for some mathematical operations on polynomials.
 * 
 * @author Marin
 *
 */
public class ComplexPolynomial {
	/**
	 * The precision with which double numbers are compared.
	 */
	private static final double PRECISION = 1E-10;

	/**
	 * All the coefficients of this polynomial.
	 */
	private Complex[] coefficients;

	/**
	 * Creates a new complex polynomial with the given coefficients.
	 * 
	 * @param factors
	 *            The coefficients of the complex polynomial.
	 */
	public ComplexPolynomial(Complex... factors) {
		this(false, factors);
	}

	/**
	 * A private constructor that offers the user the option to use the given
	 * array of coefficients (just copy the array reference) instead of copying
	 * its elements into a new array.
	 * 
	 * @param useArray
	 *            True if the constructor should just copy the array reference,
	 *            false if it should copy all the elements.
	 * @param factors
	 *            The coefficients of the complex polynomial.
	 */
	private ComplexPolynomial(boolean useArray, Complex... factors) {
		if (useArray) {
			coefficients = factors;
			return;
		}

		coefficients = Arrays.copyOf(factors, factors.length);
	}

	/**
	 * Returns the order of this polynomial. The order of a polynomial is the
	 * highest power that occurs in the polynomial.
	 * 
	 * @return The order of the polynomial.
	 */
	public short order() {
		return (short) (coefficients.length - 1);
	}

	/**
	 * Multiplies this polynomial with the one given as the argument.
	 * 
	 * @param p
	 *            The polynomial by which this one will be multiplied.
	 * @return A new polynomial that is the result of the operation.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] multiplied = new Complex[this.order() + p.order() + 1];

		for (int i = 0; i < coefficients.length; i++) {
			for (int j = 0; j < p.coefficients.length; j++) {
				if (multiplied[i + j] == null) {
					multiplied[i + j] = new Complex();
				}
				multiplied[i + j] = multiplied[i + j].add(this.coefficients[i].multiply(p.coefficients[j]));
			}
		}

		return new ComplexPolynomial(true, multiplied);
	}

	/**
	 * Differentiates this polynomial and returns the result.
	 * 
	 * @return A new polynomial that is the result of the operation.
	 */
	public ComplexPolynomial derive() {
		Complex[] derived = new Complex[coefficients.length - 1];

		for (int i = 0; i < derived.length; i++) {
			derived[i] = coefficients[i + 1].multiply(new Complex(i + 1, 0.0));
		}

		return new ComplexPolynomial(true, derived);
	}

	/**
	 * Computes the value of the polynomial at the given point z.
	 * 
	 * @param z
	 *            The point where the polynomial expression is to be evaluated.
	 * @return A new complex number that is the result of the operation.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;

		for (int i = 0; i < coefficients.length; i++) {
			result = result.add(coefficients[i].multiply(pow(z, i)));
		}

		return result;
	}

	/**
	 * A private helper method that calculates power function of a Complex
	 * number.
	 * 
	 * @param c
	 *            The complex number.
	 * @param exponent
	 *            The exponent of the power that the number will be raised to.
	 * @return The result of the mathematical operation.
	 */
	private static Complex pow(Complex c, int exponent) {
		Complex result = new Complex(1.0, 0.0);

		for (int i = 0; i < exponent; i++) {
			result = result.multiply(c);
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (int i = coefficients.length - 1; i >= 0; i--) {
			if (doubleIsEqual(coefficients[i].module(), 0.0, PRECISION)) {
				continue;
			} else if(builder.length() > 0){
				builder.append(" + ");
			}
			builder.append(coefficientToString(coefficients[i]));
			if (i > 1) {
				builder.append("z^" + i);
			} else if(i == 1) {
				builder.append("z");
			}
		}

		return (builder.length() > 0) ? builder.toString() : "0";
	}

	/**
	 * Helper method for toString that generates the string representation of a
	 * single coefficient.
	 * 
	 * @param coefficient
	 *            The coefficient whose string representation is to be returned.
	 * @return The string representation of the given coefficient.
	 */
	private String coefficientToString(Complex coefficient) {
		if (doubleIsEqual(coefficient.getIm(), 0.0, PRECISION)) {
			return (coefficient.getRe() < 0) ? "(" + coefficient.toString() + ")" : coefficient.toString();
		} else if (doubleIsEqual(coefficient.getRe(), 0.0, PRECISION)) {
			return (coefficient.getIm() < 0) ? "(" + coefficient.toString() + ")" : coefficient.toString();
		} else {
			return "(" + coefficient.toString() + ")";
		}
	}

	/**
	 * Checks whether two floating point numbers are equal to the according to
	 * the given precision.
	 * 
	 * @param num1
	 *            First number.
	 * @param num2
	 *            Second number.
	 * @return True if the are equal, false otherwise.
	 */
	public static boolean doubleIsEqual(double num1, double num2, double precision) {
		return Math.abs(num1 - num2) < precision;
	}
}
