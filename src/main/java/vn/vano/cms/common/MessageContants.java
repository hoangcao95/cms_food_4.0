package vn.vano.cms.common;

public interface MessageContants {
    interface Result {
        String MESSAGE_CODE = "messageResult";
        String SUCCESS = "SUCCESS";
        String ERROR = "ERROR";
        String EXCEPTION = "EXCEPTION";
    }

    interface Account {
        String ADDNEW_SUCCESS = "Thêm mới thành công";
        String ADDNEW_ERROR = "ERROR: Thêm mới thất bại";

        String DELETE_SUCCESS = "Xóa thành công";
        String DELETE_ERROR = "ERROR: Xóa không thành công";

        String UPDATE_SUCCESS = "Cập nhật thành công";
        String UPDATE_ERROR = "ERROR: Cập nhật không thành công";
    }

    interface AutoML {
        String IMPORT_SUCCESS = "Import thành công";
        String IMPORT_ERROR = "ERROR: Import thất bại";

        String ADDNEW_SUCCESS = "Thêm mới thành công";
        String ADDNEW_ERROR = "Thêm mới không thành công";

        String UPDATE_SUCCESS = "Cập nhật thành công";
        String UPDATE_ERROR = "Cập nhật không thành công";

        String TRANNING_SUCCESS = "Tranning thành công";
        String TRANNING_ERROR = "ERROR: Tranning thất bại";

        String DELETE_SUCCESS = "Xóa thành công";
        String DELETE_ERROR = "ERROR: Xóa không thành công";
    }
}
