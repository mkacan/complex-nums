package hr.marin.complex;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class represents a complex number. Offers methods for various mathematical
 * operations.
 * 
 * @author Marin
 */
public class Complex {
	/**
	 * Defined constant for the number 0
	 */
	public static final Complex ZERO = new Complex(0, 0);
	/**
	 * Defined constant for the number 1
	 */
	public static final Complex ONE = new Complex(1, 0);
	/**
	 * Defined constant for the number -1
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/**
	 * Defined constant for the number i
	 */
	public static final Complex IM = new Complex(0, 1);
	/**
	 * Defined constant for the number -i
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * The precision with which double numbers are compared.
	 */
	private static final double PRECISION = 1E-10;

	/**
	 * The real part of the complex number.
	 */
	private double re;
	/**
	 * The imaginary part of the complex number.
	 */
	private double im;

	/**
	 * Creates a new {@link Complex} object representing the number 0.
	 */
	public Complex() {
		this(0.0, 0.0);
	}

	/**
	 * Creates a new {@link Complex} object with the given parameters.
	 * 
	 * @param re
	 *            Real part of the complex number.
	 * @param im
	 *            Imaginary part of the complex number.
	 */
	public Complex(double re, double im) {
		super();
		this.re = re;
		this.im = im;
	}

	/**
	 * Gets the real part of the complex number.
	 * 
	 * @return The real part of the complex number.
	 */
	public double getRe() {
		return re;
	}

	/**
	 * Gets the imaginary part of the complex number.
	 * 
	 * @return The imaginary part of the complex number.
	 */
	public double getIm() {
		return im;
	}

	/**
	 * Calculates the module of the complex number.
	 * 
	 * @return The module of the complex number.
	 */
	public double module() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 * Multiplies this complex number with the one given as the argument.
	 * 
	 * @param c
	 *            The number by which this one is multiplied.
	 * @return A new complex number that is the result of the operation.
	 */
	public Complex multiply(Complex c) {
		return new Complex(this.re * c.re - this.im * c.im, this.re * c.im + this.im * c.re);
	}

	/**
	 * Divides this complex number with the one given as the argument.
	 * 
	 * @param c
	 *            The number by which this one is divided.
	 * @return A new complex number that is the result of the operation.
	 */
	public Complex divide(Complex c) {
		if (doubleIsEqual(c.module(), 0.0, PRECISION)) {
			throw new IllegalArgumentException("Cannot divide by zero.");
		}

		double divisorModule = c.re*c.re + c.im*c.im;
		double numeratorRe = this.re * c.re + this.im * c.im;
		double numeratorIm = this.im * c.re - this.re * c.im;
		
		return new Complex(numeratorRe / divisorModule, numeratorIm / divisorModule);
	}

	/**
	 * Adds the complex number given as the argument to this one.
	 * 
	 * @param c
	 *            The number that is added to this one.
	 * @return A new complex number that is the result of the operation.
	 */
	public Complex add(Complex c) {
		return new Complex(this.re + c.re, this.im + c.im);
	}

	/**
	 * Subtracts the complex number given as the argument from this one.
	 * 
	 * @param c
	 *            The number that is subtracted from this one.
	 * @return A new complex number that is the result of the operation.
	 */
	public Complex sub(Complex c) {
		return new Complex(this.re - c.re, this.im - c.im);
	}

	/**
	 * Negates the real and imaginary parts of this complex number. This
	 * operation is equivalent to multiplying by -1.
	 * 
	 * @return A new complex number that is the result of the operation.
	 */
	public Complex negate() {
		return new Complex(-this.re, -this.im);
	}

	@Override
	public String toString() {
		boolean hasRe = !doubleIsEqual(re, 0.0, PRECISION);
		boolean hasIm = !doubleIsEqual(im, 0.0, PRECISION);

		if (!hasRe && !hasIm) {
			return "0";
		}

		// forcing the decimal point instead of the decimal comma
		DecimalFormatSymbols separator = new DecimalFormatSymbols(Locale.getDefault());
		separator.setDecimalSeparator('.');

		String realString = "";
		if (hasRe) {
			DecimalFormat realFormat = new DecimalFormat("#.####", separator);
			realString = realFormat.format(re);
		}

		String imaginaryString = "";
		if (hasIm) {
			String imaginaryFormatString = (hasRe) ? " + i#.####; - i#.####" : "i#.####;-i#.####";
			DecimalFormat imaginaryFormat = new DecimalFormat(imaginaryFormatString, separator);
			imaginaryString = imaginaryFormat.format(im);
		}

		return realString + imaginaryString;
	}

	/**
	 * Checks whether two floating point numbers are equal to the according to the given precision.
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
