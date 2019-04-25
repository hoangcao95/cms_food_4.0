package vn.vano.cms.common;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {
    private final static Locale locale_viVN = new Locale("vi", "VN");

    public static String toCurrencyVN(Long value) {
        return NumberFormat.getCurrencyInstance(locale_viVN).format(value);
    }
}
