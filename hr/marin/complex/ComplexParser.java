package hr.marin.complex;

/**
 * Class represents a simple parser for complex numbers.
 * 
 * @author Marin
 *
 */
public class ComplexParser {
	/**
	 * All the states of the parser.
	 * 
	 * @author Marin
	 *
	 */
	private static enum ParseState {
		START, SIGN, RE, IM, WAIT_I, AFTER_RE, INVALID, FINISHED;
	}

	/**
	 * Parses a given string and creates a new {@link Complex} number using it.
	 * The general syntax is of the following form: a+ib or a-ib.
	 * 
	 * @param line
	 *            The string that is to be parsed.
	 * @return A new {@link Complex} object created from the given string.
	 */
	public static Complex parseComplex(String line) {
		char[] array = line.trim().toCharArray();
		int index = 0;
		StringBuilder builder = new StringBuilder();
		char sign = '+';
		double re = 0.0, im = 0.0;
		ParseState state = ParseState.START;

		while (index < array.length) {
			switch (state) {
			case START:
				if (Character.isDigit(array[index]) || array[index] == '.') {
					state = ParseState.RE;
					builder.append(array[index]);
					index++;
				} else if (array[index] == '+' || array[index] == '-') {
					sign = array[index];
					state = ParseState.SIGN;
					index++;
				} else if (array[index] == 'i') {
					state = ParseState.IM;
					index++;
				} else {
					state = ParseState.INVALID;
				}
				break;
			case SIGN:
				if (array[index] == ' ' || array[index] == '\t') {
					index++;
				} else if (Character.isDigit(array[index]) || array[index] == '.') {
					state = ParseState.RE;
					builder.append(array[index]);
					index++;
				} else if (array[index] == 'i') {
					state = ParseState.IM;
					index++;
				} else {
					state = ParseState.INVALID;
				}
				break;
			case RE:
				if (array[index] == ' ' || array[index] == '\t') {
					state = ParseState.AFTER_RE;
					try {
						re = Double.parseDouble(Character.toString(sign) + builder.toString());
						builder.delete(0, builder.length());
						index++;
					} catch (NumberFormatException e) {
						state = ParseState.INVALID;
					}
				} else if (array[index] == '+' || array[index] == '-') {
					state = ParseState.WAIT_I;
					try {
						re = Double.parseDouble(Character.toString(sign) + builder.toString());
						builder.delete(0, builder.length());
						sign = array[index++];
					} catch (NumberFormatException e) {
						state = ParseState.INVALID;
					}
				} else if (Character.isDigit(array[index]) || array[index] == '.') {
					builder.append(array[index++]);
				} else {
					state = ParseState.INVALID;
				}
				break;
			case WAIT_I:
				if (array[index] == ' ' || array[index] == '\t') {
					index++;
				} else if (array[index] == 'i') {
					state = ParseState.IM;
					index++;
				} else {
					state = ParseState.INVALID;
				}
				break;
			case IM:
				if (array[index] == ' ' || array[index] == '\t') {
					try {
						if (builder.length() == 0) {
							builder.append("1");
						}
						im = Double.parseDouble(Character.toString(sign) + builder.toString());
						builder.delete(0, builder.length());
						index++;
						state = ParseState.FINISHED;
					} catch (NumberFormatException e) {
						state = ParseState.INVALID;
					}
				} else if (Character.isDigit(array[index]) || array[index] == '.') {
					builder.append(array[index++]);
				} else {
					state = ParseState.INVALID;
				}
				break;
			case AFTER_RE:
				if (array[index] == ' ' || array[index] == '\t') {
					index++;
				} else if (array[index] == '+' || array[index] == '-') {
					sign = array[index];
					state = ParseState.WAIT_I;
					index++;
				} else {
					state = ParseState.INVALID;
				}
				break;
			case INVALID:
				throw new IllegalArgumentException(line + " is not a parsable complex number.");
			case FINISHED:
				index = array.length;
				break;
			}
		}

		if (state == ParseState.RE) {
			try {
				re = Double.parseDouble(Character.toString(sign) + builder.toString());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(line + " is not a parsable complex number.");
			}
		} else if (state == ParseState.IM) {
			if (builder.length() == 0) {
				builder.append("1");
			}
			try {
				im = Double.parseDouble(Character.toString(sign) + builder.toString());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(line + " is not a parsable complex number.");
			}
		} else if (state == ParseState.FINISHED || state == ParseState.AFTER_RE) {
		} else {
			throw new IllegalArgumentException(line + " is not a parsable complex number.");
		}

		return new Complex(re, im);
	}
}
