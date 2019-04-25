package vn.vano.cms.common;

import java.text.MessageFormat;

public class MessageUtils {

    public interface ERROR {
        String DEFAULT_VALUE = "9999";
        String NUMBER_IN_BLACKLIST = "This number {0} exist in blacklist";
    }

    public interface SUCCESS {
        String VINA_REG_MT2 = "Chúc mừng bạn đã đăng ký thành công dịch vụ {0}";
    }

    public static String buidMessage(String message, Object... value) {
        return MessageFormat.format(message, value);
    }

    public static String buidMessage(String message, String valueRegex, String valueReplace) {
        return message.replaceFirst(valueRegex, valueReplace);
    }
}
