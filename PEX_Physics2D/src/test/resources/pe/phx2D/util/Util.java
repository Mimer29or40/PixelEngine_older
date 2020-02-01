package pe.phx2D.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class Util
{
    private Util()
    {
    
    }
    
    private static final double EPSILON   = 1e-14;
    private static final double MAGNITUDE = 1.0;
    
    /**
     * Returns `true` if the numbers are significantly different to a certain tolerance level, adjusting the tolerance for larger numbers.
     * <p>
     * For numbers with absolute value smaller than {@link Util#MAGNITUDE} the numbers are compared using a fixed tolerance of {@link Util#EPSILON} * {@link Util#MAGNITUDE}.
     * <p>
     * For numbers with absolute value larger than {@link Util#MAGNITUDE}, the tolerance is {@link Util#EPSILON} times the larger of the absolute values of the numbers being
     * compared.
     * <p>
     * Unless specified, the default for {@link Util#MAGNITUDE} is 1.0 and {@link Util#EPSILON} is 1E-14. These settings return `true` if the numbers are significantly close to
     * approximately 14 decimal digits when their magnitude is near 1.0.
     * <p>
     * The goal is to have a test that is immune to the inaccuracy of double arithmetic. Doubles have 15 to 17 significant decimal digits of accuracy, so comparing 14 significant
     * digits should be fairly safe from the inaccuracy in double arithmetic.
     * <p>
     * This method takes into account the size of the numbers being compared, so it is safer than code such as
     * <p>
     * if (Math.abs(a - b) < 1E-16)  // do something
     * <p>
     * Doubles have 15 to 17 significant decimal digits of accuracy. When the numbers being compared are much bigger in magnitude than 1.0, then this test is too strict -- it
     * effectively is comparing to zero, meaning exact equality.
     *
     * @param x         the first number to compare
     * @param y         the second number to compare
     * @param epsilon   the small value used with {@link Util#MAGNITUDE} to calculate
     *                  the tolerance for deciding when the numbers are different, default is 1E-14.
     * @param magnitude the approximate magnitude of the numbers being compared, default is 1.0.
     *
     * @return true if the doubles are different to 14 significant decimal digits
     *
     * @throws RuntimeException if {@link Util#MAGNITUDE} or {@link Util#EPSILON} is negative or zero
     */
    public static boolean close(double x, double y, double epsilon, double magnitude)
    {
        if (epsilon <= 0) throw new RuntimeException("epsilon must be positive " + epsilon);
        if (magnitude <= 0) throw new RuntimeException("magnitude must be positive " + magnitude);
    
        double maxArg = Math.max(Math.abs(x), Math.abs(y));
        return Math.abs(x - y) < Math.max(maxArg, magnitude) * epsilon;
    }
    
    public static boolean close(double x, double y, double epsilon)
    {
        return close(x, y, epsilon, MAGNITUDE);
    }
    
    public static boolean close(double x, double y)
    {
        return close(x, y, EPSILON, MAGNITUDE);
    }
    
    /**
     * Returns the language independent form of the given string by changing to uppercase and replacing spaces and dashes with underscores.
     *
     * @param text the text
     *
     * @return the text upper-cased and with spaces and dashes replaced by underscores
     */
    public static String toName(String text)
    {
        return TO_NAME_PATTERN.matcher(text.toUpperCase()).replaceAll("_");
    }
    
    private static final Pattern TO_NAME_PATTERN = Pattern.compile("[ -]");
    
    /**
     * Ensures the given text consists of only uppercase letters, numbers and underscore and first character is a letter or underscore. This is required for language independent
     * names.
     *
     * @param text the text
     *
     * @return the validated text
     *
     * @throws RuntimeException if text does not qualify as a name
     */
    public static String validName(String text)
    {
        if (!VALID_NAME_PATTERN.matcher(text).find()) throw new RuntimeException("not a valid name: " + text);
        return text;
    }
    
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[A-Z_][A-Z_0-9]*$");
    
    /**
     * The default number format to use in {@link Printable#toShortString} and {@link Printable#toLongString} methods.
     *
     * @param n the number to format
     *
     * @return the number with default formatting
     */
    public static String NF(Number n)
    {
        return NF5U(n);
    }
    
    /**
     * The default exponential number format to use in {@link Printable#toShortString} and {@link Printable#toLongString} methods.
     *
     * @param n the number to format
     *
     * @return the number with default exponential formatting
     */
    public static String NFE(Number n)
    {
        return NF7E(n);
    }
    
    /**
     * Formats a number with 0 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 0 decimal places; null; or undefined
     */
    public static String NF0(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF0_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF0_FORMAT = new DecimalFormat("#0");
    
    /**
     * Formats a number with up to 0 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 0 decimal places; null; or undefined
     */
    public static String NF0U(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF0U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF0U_FORMAT = new DecimalFormat("#0");
    
    /**
     * Formats a number with 0 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 0 decimal places; null; or undefined
     */
    public static String NF0E(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-8 || num == 0) return NF0_FORMAT.format(n);
        return NF0U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF0E_FORMAT = new DecimalFormat("#0");
    
    /**
     * Formats a number with 0 decimal place and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 0 decimal place and a plus sign if positive; null; or undefined
     */
    public static String NF0S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF0S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF0S_FORMAT = new DecimalFormat("+#0;-#0");
    
    /**
     * Formats a number with 1 decimal place.
     *
     * @param n the number to format
     *
     * @return the number with 1 decimal place; null; or undefined
     */
    public static String NF1(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF1_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF1_FORMAT = new DecimalFormat("#0.0");
    
    /**
     * Formats a number with up to 1 decimal place.
     *
     * @param n the number to format
     *
     * @return the number with up to 1 decimal place; null; or undefined
     */
    public static String NF1U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF1U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF1U_FORMAT = new DecimalFormat("#0.#");
    
    /**
     * Formats a number with 1 decimal place, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 1 decimal place; null; or undefined
     */
    public static String NF1E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-2 || num == 0) return NF1_FORMAT.format(n);
        return NF1E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF1E_FORMAT = new DecimalFormat("0.0E0");
    
    /**
     * Formats a number with 1 decimal place and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 1 decimal place and a plus sign if positive; null; or undefined
     */
    public static String NF1S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF1S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF1S_FORMAT = new DecimalFormat("+#0.0;-#0.0");
    
    /**
     * Formats a number with 2 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 2 decimal places; null; or undefined
     */
    public static String NF2(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF2_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF2_FORMAT = new DecimalFormat("#0.00");
    
    /**
     * Formats a number with up to 2 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 2 decimal places; null; or undefined
     */
    public static String NF2U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF2U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF2U_FORMAT = new DecimalFormat("#0.##");
    
    /**
     * Formats a number with 2 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 2 decimal places; null; or undefined
     */
    public static String NF2E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-3 || num == 0) return NF2_FORMAT.format(n);
        return NF2E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF2E_FORMAT = new DecimalFormat("0.00E0");
    
    /**
     * Formats a number with 2 decimal places and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 2 decimal places and a plus sign if positive; null; or undefined
     */
    public static String NF2S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF2S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF2S_FORMAT = new DecimalFormat("+#0.00;-#0.00");
    
    /**
     * Formats a number with 3 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 3 decimal places; null; or undefined
     */
    public static String NF3(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF3_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF3_FORMAT = new DecimalFormat("#0.000");
    
    /**
     * Formats a number with up to 3 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 3 decimal places; null; or undefined
     */
    public static String NF3U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF3U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF3U_FORMAT = new DecimalFormat("#0.###");
    
    /**
     * Formats a number with 3 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 3 decimal places; null; or undefined
     */
    public static String NF3E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-4 || num == 0) return NF3_FORMAT.format(n);
        return NF3E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF3E_FORMAT = new DecimalFormat("0.000E0");
    
    /**
     * Formats a number with 3 decimal places and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 3 decimal places and a plus sign if positive; null; or undefined
     */
    public static String NF3S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF3S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF3S_FORMAT = new DecimalFormat("+#0.000;-#0.000");
    
    /**
     * Formats a number with 4 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 4 decimal places; null; or undefined
     */
    public static String NF4(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF4_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF4_FORMAT = new DecimalFormat("#0.0000");
    
    /**
     * Formats a number with up to 4 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 4 decimal places; null; or undefined
     */
    public static String NF4U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF4U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF4U_FORMAT = new DecimalFormat("#0.####");
    
    /**
     * Formats a number with 4 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 4 decimal places; null; or undefined
     */
    public static String NF4E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-5 || num == 0) return NF4_FORMAT.format(n);
        return NF4E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF4E_FORMAT = new DecimalFormat("0.0000E0");
    
    /**
     * Formats a number with 4 decimal places and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 4 decimal places and a plus sign if positive; null; or undefined
     */
    public static String NF4S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF4S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF4S_FORMAT = new DecimalFormat("+#0.0000;-#0.0000");
    
    /**
     * Formats a number with 5 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 5 decimal places; null; or undefined
     */
    public static String NF5(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF5_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF5_FORMAT = new DecimalFormat("#0.00000");
    
    /**
     * Formats a number with up to 5 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 5 decimal places; null; or undefined
     */
    public static String NF5U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF5U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF5U_FORMAT = new DecimalFormat("#0.#####");
    
    /**
     * Formats a number with 5 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 5 decimal places; null; or undefined
     */
    public static String NF5E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-6 || num == 0) return NF5_FORMAT.format(n);
        return NF5E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF5E_FORMAT = new DecimalFormat("0.00000E0");
    
    /**
     * Formats a number with 5 decimal places and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 5 decimal places and a plus sign if positive; null; or undefined
     */
    public static String NF5S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF5S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF5S_FORMAT = new DecimalFormat("+#0.00000;-#0.00000");
    
    /**
     * Formats a number with 6 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 6 decimal places; null; or undefined
     */
    public static String NF6(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF6_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF6_FORMAT = new DecimalFormat("#0.000000");
    
    /**
     * Formats a number with up to 6 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 6 decimal places; null; or undefined
     */
    public static String NF6U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF6U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF6U_FORMAT = new DecimalFormat("#0.######");
    
    /**
     * Formats a number with 6 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 6 decimal places; null; or undefined
     */
    public static String NF6E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-7 || num == 0) return NF6_FORMAT.format(n);
        return NF6E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF6E_FORMAT = new DecimalFormat("0.000000E0");
    
    /**
     * Formats a number with 6 decimal places and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 6 decimal places and a plus sign if positive; null; or undefined
     */
    public static String NF6S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF6S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF6S_FORMAT = new DecimalFormat("+#0.000000;-#0.000000");
    
    /**
     * Formats a number with 7 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 7 decimal places; null; or undefined
     */
    public static String NF7(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF7_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF7_FORMAT = new DecimalFormat("#0.0000000");
    
    /**
     * Formats a number with up to 7 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 7 decimal places; null; or undefined
     */
    public static String NF7U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF7U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF7U_FORMAT = new DecimalFormat("#0.#######");
    
    /**
     * Formats a number with 7 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 7 decimal places; null; or undefined
     */
    public static String NF7E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-8 || num == 0) return NF7_FORMAT.format(n);
        return NF7E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF7E_FORMAT = new DecimalFormat("0.0000000E0");
    
    /**
     * Formats a number with 7 decimal places and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 7 decimal places and a plus sign if positive; null; or undefined
     */
    public static String NF7S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF7S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF7S_FORMAT = new DecimalFormat("+#0.0000000;-#0.0000000");
    
    /**
     * Formats a number with 8 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 8 decimal places; null; or undefined
     */
    public static String NF8(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF8_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF8_FORMAT = new DecimalFormat("#0.00000000");
    
    /**
     * Formats a number with up to 8 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 8 decimal places; null; or undefined
     */
    public static String NF8U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF8U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF8U_FORMAT = new DecimalFormat("#0.########");
    
    /**
     * Formats a number with 8 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 8 decimal places; null; or undefined
     */
    public static String NF8E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-9 || num == 0) return NF8_FORMAT.format(n);
        return NF8E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF8E_FORMAT = new DecimalFormat("0.00000000E0");
    
    /**
     * Formats a number with 8 decimal places and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 8 decimal places and a plus sign if positive; null; or undefined
     */
    public static String NF8S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF8S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF8S_FORMAT = new DecimalFormat("+#0.00000000;-#0.00000000");
    
    /**
     * Formats a number with 9 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with 9 decimal places; null; or undefined
     */
    public static String NF9(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF9_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF9_FORMAT = new DecimalFormat("#0.000000000");
    
    /**
     * Formats a number with up to 9 decimal places.
     *
     * @param n the number to format
     *
     * @return the number with up to 9 decimal places; null; or undefined
     */
    public static String NF9U(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0U(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF9U_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF9U_FORMAT = new DecimalFormat("#0.#########");
    
    /**
     * Formats a number with 9 decimal places, but if too small then switch to exponential.
     *
     * @param n the number to format
     *
     * @return the number with 9 decimal places; null; or undefined
     */
    public static String NF9E(Number n)
    {
        if (n == null) return "null";
        if (n instanceof Long || n instanceof Integer) return NF0E(n);
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        if (Math.abs(num) > 2E-10 || num == 0) return NF9_FORMAT.format(n);
        return NF9E_FORMAT.format(n);
    }
    
    private static final DecimalFormat NF9E_FORMAT = new DecimalFormat("0.000000000E0");
    
    /**
     * Formats a number with 9 decimal places and a plus sign if positive.
     *
     * @param n the number to format
     *
     * @return the number with 9 decimal places and a plus sign if positive; null; or undefined
     */
    public static String NF9S(Number n)
    {
        if (n == null) return "null";
        double num = n.doubleValue();
        if (Double.isNaN(num)) return "undefined";
        return NF9S_FORMAT.format(num);
    }
    
    private static final DecimalFormat NF9S_FORMAT = new DecimalFormat("+#0.000000000;-#0.000000000");
}
