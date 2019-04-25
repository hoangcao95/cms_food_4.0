package vn.vano.cms.common;

import org.joda.time.DateTime;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URLEncoder;


public class PhoneUtils {

    public static String normalizeMsIsdn(String phoneNumber) {
        String result = phoneNumber;

        try {
            String pattern = "^84";
            result = result.replaceAll(pattern, "");
            pattern = "^0";
            result = result.replaceAll(pattern, "");
        } catch (Exception ex) {
            result = phoneNumber;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("0".concat(PhoneUtils.normalizeMsIsdn("0912345678")));

        URLEncoder.encode("Chúc mừng bạn đã trúng thưởng giải ngày dịch vụ VTVShowbiz", "UTF-8");

        System.out.println(URLEncoder.encode("Chúc mừng bạn đã trúng thưởng giải ngày dịch vụ VTVShowbiz", "UTF-8"));

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            System.out.println(ip);
        }

        DateTime currentDate = new DateTime().withTimeAtStartOfDay();
        DateTime startDate = new DateTime().withDate(2019,1,1);
        System.out.println(currentDate.getDayOfMonth() - startDate.getDayOfMonth());
    }
}
