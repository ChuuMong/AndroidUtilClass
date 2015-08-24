package team.co.kr.testproject.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class StringUtil {

    public static String pickOutNumber(String input) {
        if (input == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                sb.append(input.charAt(i));
            }
        }

        return sb.toString();
    }

    public static boolean isEmail(String input) {
        if (input == null) {
            return false;
        }

        return Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", input);
    }

    public static boolean isNumber(String input) {
        return Pattern.compile("[0-9]").matcher(input).matches();
    }

    public static boolean isEnglish(String input) {
        return Pattern.compile("[a-zA-Z]").matcher(input).matches();
    }

    public static boolean isKorean(String input) {
        String regex = "^[ㄱ-ㅎ가-힣]*$";
        return Pattern.compile(regex).matcher(input).matches();
    }

    public static boolean isAvailablePassword(String input) {
        String regex = "^[a-zA-Z0-9~!@#$%^&*()+,-./:;<=>?[＼]_`{|}\"]{6,12}";
        return Pattern.matches(regex, input);
    }

    public static String removeNumber(String input) {
        String regex = "[0-9]";
        return input.replaceAll(regex, "");
    }

    public static String removeEnglish(String input) {
        String regex = "[a-zA-Z]";
        return input.replaceAll(regex, "");
    }

    public static String removeKorean(String input) {
        String regex = "^[ㄱ-ㅎ가-힣]*$";
        return input.replaceAll(regex, "");
    }

    public static String makeStringWithComma(String input) {
        if (input.length() == 0) {
            return "";
        }

        try {
            if (input.contains(".")) {
                double value = Double.parseDouble(input);
                DecimalFormat format = new DecimalFormat("###,##0.00");
                return format.format(value);
            }
            else {
                long value = Long.parseLong(input);
                DecimalFormat format = new DecimalFormat("###,##0");
                return format.format(value);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return input;
    }

    public static String makeStringWithComma(long value) {
        DecimalFormat format = new DecimalFormat("###,##0");
        return format.format(value);
    }

    public static String makeStringWithComma(double value) {
        DecimalFormat format = new DecimalFormat("###,##0.00");
        return format.format(value);
    }
}
