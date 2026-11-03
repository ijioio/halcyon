package com.ijioio.halcyon.core.util;

/**
 * Helper class for text.
 */
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
}
