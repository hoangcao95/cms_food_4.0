package vn.vano.cms.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface IGsonBase {
    Gson GSON = new GsonBuilder().serializeNulls().create();
}
