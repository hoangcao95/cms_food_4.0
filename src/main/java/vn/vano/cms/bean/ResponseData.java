package vn.vano.cms.bean;

import vn.vano.cms.common.IGsonBase;

public class ResponseData implements IGsonBase{
    private String code = "1";
    private String message = "";

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
