package com.ijioio.halcyon.processor.api.util;

/** Helper class for text. */
public class TextUtil {

	/**
	 * Checks whether an indicated {@code value} is blank, i.e. either {@code null}
	 * or zero length or contains only the white space characters.
	 *
	 * @param value to check
	 * @return {@code true} if value is blank, {@code false} otherwise
	 */
	public static boolean isBlank(CharSequence value) {
		return value == null || value.toString().trim().isEmpty();
	}

	/**
	 * Checks whether an indicated {@code value} is empty, i.e. either {@code null}
	 * or zero length.
	 *
	 * @param value to check
	 * @return {@code true} if value is empty, {@code false} otherwise
	 */
	public static boolean isEmpty(CharSequence value) {
		return value == null || value.toString().isEmpty();
	}

	/**
	 * Capitalizes indicated {@code value} string by changing the first character to
	 * upper case.
	 * 
	 * <pre>
	 * TextUtil.capitalize(null)   = null
	 * TextUtil.capitalize("")     = ""
	 * TextUtil.capitalize("text") = "Text"
	 * TextUtil.capitalize("tExt") = "TExt"
	 * </pre>
	 * 
	 * @param value to capitalize
	 * @return value with first letter capitalized
	 */
	public static String capitalize(String value) {

		if (isEmpty(value)) {
			return value;
		}

		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	/**
	 * Convert indicated {@code value} string by changing it to the snake case.
	 * 
	 * <pre>
	 * TextUtil.toSnakeCase(null)   = null
	 * TextUtil.toSnakeCase("")     = ""
	 * TextUtil.toSnakeCase("Text") = "text"
	 * TextUtil.toSnakeCase("tExt") = "t_ext"
	 * TextUtil.toSnakeCase("TEXt") = "te_xt"
	 * </pre>
	 * 
	 * @param value to convert
	 * @return value converted to snake case
	 */
	public static String toSnakeCase(String value) {

		if (isEmpty(value)) {
			return value;
		}

		StringBuilder builder = new StringBuilder(value.length() + 8);

		for (int i = 0; i < value.length(); i++) {

			char c = value.charAt(i);

			if (Character.isUpperCase(c)) {

				if (i > 0) {

					char prev = value.charAt(i - 1);
					char next = i + 1 < value.length() ? value.charAt(i + 1) : 0;

					if (Character.isLowerCase(prev) || Character.isDigit(prev)
							|| (Character.isUpperCase(prev) && Character.isLowerCase(next))) {
						builder.append('_');
					}
				}

				builder.append(Character.toLowerCase(c));

			} else if (Character.isLetterOrDigit(c)) {

				builder.append(Character.toLowerCase(c));

			} else {

				if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '_') {
					builder.append('_');
				}
			}
		}

		if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '_') {
			builder.setLength(builder.length() - 1);
		}

		return builder.toString();
	}
}
