package vn.vano.cms.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ThreadConfigs {

    @Expose
    private List<ThreadConfig> threads = new ArrayList<>();

    public List<ThreadConfig> getThreads() {
        return threads;
    }

    public void setThreads(List<ThreadConfig> threads) {
        this.threads = threads;
    }
}
