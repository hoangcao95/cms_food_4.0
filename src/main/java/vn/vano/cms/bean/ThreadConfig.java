package vn.vano.cms.bean;

import com.google.gson.annotations.Expose;
import org.json.JSONObject;

public class ThreadConfig {

    @Expose
    private String id;
    @Expose private String name;
    @Expose private String className;
    @Expose private boolean autoStart;
    @Expose private int delayTime;
    @Expose private int order;
    @Expose private JSONObject params = new JSONObject();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
}
