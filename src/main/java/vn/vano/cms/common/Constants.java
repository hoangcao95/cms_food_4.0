package vn.vano.cms.common;

public interface Constants {
    Integer TRANS_ID_LENGTH = 12;
    Integer MPIN_LENGTH = 5;

    interface Session {
        String USER_LOGIN = "userName";
        String USER_ID = "userId";
        String FULL_NAME = "fullName";
    }

    interface Table {
        int DISPLAY_10_RECORD = 10;
        int DISPLAY_20_RECORD = 20;
        int DISPLAY_50_RECORD = 50;
        int DISPLAY_100_RECORD = 100;
    }

    interface SysParam {
        String TOPUP_CSKH_MAX_MONEY = "CSKH_MAX_MONEY";
    }

    interface App {
        String SHORT_CODE = "8888";
        String SHORT_NAME = "IVR";
        String SERVER_MODE = "";
    }

    interface CoreMo {
        //Cac trang thai cua ban ghi trong core_mo
        interface Status {
            String EXTEND = "0";
            String EXTEND_NAME = "EXTEND";
            String REG = "1";
            String REG_NAME = "REG";
            String CANCEL = "3";
            String CANCEL_NAME = "CANCEL";
            String CONFIRM = "2";
            String CONFIRM_NAME = "CONFIRM";
            String NORMAL_NAME = "NORMAL";
            String CONTENT_NAME = "CONTENT";

            Long ACTIVE = 1L;
        }

        interface ProcessStatus {
            Integer DEFAULT = 0;
            Integer EXECUTED = 1;
            Integer ERROR = 9;
        }

        interface Command {
            String CMD_DEFAULT = "DEFAULT";
            String CMD_CONTENT = "CONTENT";
            String CMD_ERROR = "ERROR";
        }

        interface Channel {
            String CHANNEL_SMS = "SMS";
            String CHANNEL_API = "API";
        }
    }

    interface CoreMt {
        interface Keyword {
            String SYNTAX_INVALID = "INVALID";
            String SYNTAX_MTREMIND = "MTREMIND";
            String SYNTAX_CONTENT = "CONTENT";
        }
        String DEFAULT_MESSAGE = "Số thuê bao không hợp lệ..";
    }

    interface Subscriber {
        Integer ACTIVE = 1;
        Integer DEACTIVE = 2;
    }

    interface SubscriberLog {
        String USER_SYS = "SYS";
        //Dang khi moi
        String ACTION_REGNEW = "REGNEW";
        //Active lai thong tin da co
        String ACTION_RENEW = "RENEW";
        //Gia han
        String ACTION_EXTEND = "EXTEND";
        //Huy
        String ACTION_CANCEL = "CANCEL";
    }

    interface ChargeLog {
        Integer ACTIVE = 1;
        String ACTION_REG = "REGNEW";
        String ACTION_EXT = "EXTEND";
        String ACTION_BUY = "BUY";
        String DEFAULT_RESULT = "OK";
    }

    interface TopupConstant {
        String URL_TOPUP = "http://fastshare.mobifone.vn/fapi/fpay.jsp?action=share2&vendor=X&mtcode=topupok&msisdnB={0}&amount={1}&cpid={2}&service={3}&note={4}&allowdup=true";
        String URL_TOPUP_CSKH = "http://fastshare.mobifone.vn/fapi/fpay.jsp?action=share2&vendor=X&mtcode=topupok&msisdnB={0}&amount={1}&cpid={2}&service={3}&note={4}";
        String URL_SEND_SMS = "http://fastshare.mobifone.vn/fapi/fpay.jsp?action=sendsms&msisdn={0}&content={1}";
        String CPID = "CS90";
        String SUCCESS = "SUCCESS";
        String ERROR = "ERROR";
        String UPLOAD_SUCCESS = "Upload dữ liệu thành công";
        String UPLOAD_ERROR = "Upload dữ liệu thất bại. Có lỗi trong cấu trúc file template";
        String FILE_NOT_EXIST = "File không tồn tại. Bạn hãy chọn lại file";
    }

    public String NOT_FOUND_MESSAGE = "Không tìm thấy dữ liệu";

    interface Label {
        String MESSAGE_CODE = "messageResult";
        String STATUS_ACTIVE = "1";
    }
}
