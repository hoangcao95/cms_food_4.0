package vn.vano.cms.bean;

import java.util.ArrayList;

public class QueryBean {
    private boolean hasAutoIncrementKey;
    private String sql;
    private ArrayList<Object> listValue;

    public QueryBean(String sql, ArrayList<Object> listValue, boolean hasAutoIncrementKey) {
        this.hasAutoIncrementKey = hasAutoIncrementKey;
        this.sql = sql;
        this.listValue = listValue;
    }

    public boolean isHasAutoIncrementKey() {
        return hasAutoIncrementKey;
    }

    public void setHasAutoIncrementKey(boolean hasAutoIncrementKey) {
        this.hasAutoIncrementKey = hasAutoIncrementKey;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ArrayList<Object> getListValue() {
        return listValue;
    }

    public void setListValue(ArrayList<Object> listValue) {
        this.listValue = listValue;
    }
}
