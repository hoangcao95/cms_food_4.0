package vn.vano.cms.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vano.cms.bean.QueryBean;
import vn.vano.cms.common.Common;
import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class BaseRepo implements IBaseRepo {
    private final Logger LOG = LoggerFactory.getLogger(BaseRepo.class);

    @Override
    public String getTableName(AntTable antTable) {
        try {
            if (antTable == null) {
                return null;
            }
            if (Common.isBlank(antTable.time_pattern())) {
                return antTable.name();
            }
            String timePattern = Common.dateToString(new Date(), antTable.time_pattern());
            String newName = antTable.name().replace("$pattern$", timePattern);
            return newName;
        } catch (Exception ex) {
            LOG.error("", ex);
            return null;
        }
    }

    @Override
    public <T> QueryBean getInsertQuery(String transId, T obj, boolean withField, String... fields) throws Exception {
        String sql = "INSERT INTO @table_name" + "(@columns) VALUES (@values)";
        boolean hasAutoIncrementKey = false;

        AntTable antTable = (AntTable)obj.getClass().getAnnotation(AntTable.class);
        String tableName = getTableName(antTable);
        String columns = "";
        String values = "";
        ArrayList<Object> listValue = new ArrayList<Object>();

        ArrayList<String> listFields = new ArrayList();
        if (fields != null && fields.length > 0) {
            Collections.addAll(listFields, fields);
        }
        //Lay ra cac bien can insert
        Method[] ms = obj.getClass().getDeclaredMethods();
        for (Method m : ms) {
            AntColumn ant = (AntColumn) m.getAnnotation(AntColumn.class);
            if(ant == null || !m.getName().startsWith("get")){
                continue;
            }
            if (ant.auto_increment()) {
                if(!Common.isBlank(antTable.key()) && ant.name().equals(antTable.key())){
                    hasAutoIncrementKey = true;
                }
                continue;
            }
            if(listFields.size() > 0 && withField != listFields.contains(ant.name())){
                continue;
            }
            try {
                columns += ", " + ant.name();
                values += ", ?";
                Object value = m.invoke(obj);
                listValue.add(value);
            } catch (Exception ex) {
                LOG.error("", ex);
                throw ex;
            }
        }

        if (listValue.size() == 0) {
            return null;
        }
        columns = columns.replaceFirst(", ", "");
        values = values.replaceFirst(", ", "");
        sql = sql.replace("@table_name", tableName);
        sql = sql.replace("@columns", columns).replace("@values", values);
        LOG.info(transId + "::" + sql);
        LOG.info(transId + "::" + Common.printArray(listValue.toArray()));
        return new QueryBean(sql, listValue,hasAutoIncrementKey );
    }

    @Override
    public <T> QueryBean getUpdateQuery(String transId, T obj, String whereClause, Object[] params, boolean withField, String... fields) throws Exception {
        String sql = null;
        boolean hasAutoIncrementKey = false;
        if(Common.isBlank(whereClause)) {
            sql = "UPDATE @table_name SET @fields WHERE @table_key = ? ";
        } else {
            sql = "UPDATE @table_name SET @fields " + whereClause;
        }

        AntTable antTable = (AntTable)obj.getClass().getAnnotation(AntTable.class);
        String tableName = getTableName(antTable);
        String updateFields = "";
        ArrayList<Object> listValue = new ArrayList<Object>();

        ArrayList<String> listFields = new ArrayList<String>();
        if(fields != null && fields.length > 0){
            Collections.addAll(listFields, fields);
        }
        Method ms[] = obj.getClass().getDeclaredMethods();
        Object keyValue = null;

        for (Method m : ms) {
            AntColumn ant = (AntColumn) m.getAnnotation(AntColumn.class);
            if(ant == null || !m.getName().startsWith("get")){
                continue;
            }
            if(antTable.key().equalsIgnoreCase(ant.name())){
                try {
                    keyValue = m.invoke(obj);
                } catch (Exception e) {
                    LOG.error("", e);
                }
                continue;
            }
            if(ant.auto_increment()){
                continue;
            }
            if(listFields.size() > 0 && withField != listFields.contains(ant.name())){
                continue;
            }
            try {
                Object value = m.invoke(obj);
                if(value != null) {
                    updateFields += ", " + ant.name() + " = ?";
                    listValue.add(value);
                }
            } catch (Exception ex) {
                LOG.error("", ex);
                throw ex;
            }
        }
        if (listValue.size() == 0) {
            return null;
        }
        if(!Common.isBlank(whereClause)){
            if(params != null && params.length > 0){
                for(Object val : params){
                    listValue.add(val);
                }
            }
        } else {
            sql = sql.replace("@table_key", antTable.key());
            listValue.add(keyValue);
        }
        updateFields = updateFields.replaceFirst(", ", "");
        sql = sql.replace("@table_name", tableName);
        sql = sql.replace("@fields", updateFields);
        LOG.info(transId + "::" + sql);
        LOG.info(transId + "::" + Common.printArray(listValue.toArray()));
        return new QueryBean(sql, listValue,hasAutoIncrementKey );
    }

    @Override
    public <T> QueryBean getUpdateQuery(String transId, T obj, String whereClause, Object[] params, boolean withField, boolean withNull, String... fields) throws Exception {
        String sql = null;
        boolean hasAutoIncrementKey = false;
        if(Common.isBlank(whereClause)) {
            sql = "UPDATE @table_name SET @fields WHERE @table_key = ? ";
        } else {
            sql = "UPDATE @table_name SET @fields " + whereClause;
        }

        AntTable antTable = (AntTable)obj.getClass().getAnnotation(AntTable.class);
        String tableName = getTableName(antTable);
        String updateFields = "";
        ArrayList<Object> listValue = new ArrayList<Object>();

        ArrayList<String> listFields = new ArrayList<String>();
        if(fields != null && fields.length > 0){
            Collections.addAll(listFields, fields);
        }
        Method ms[] = obj.getClass().getDeclaredMethods();
        Object keyValue = null;

        for (Method m : ms) {
            AntColumn ant = (AntColumn) m.getAnnotation(AntColumn.class);
            if(ant == null || !m.getName().startsWith("get")){
                continue;
            }
            if(antTable.key().equalsIgnoreCase(ant.name())){
                try {
                    keyValue = m.invoke(obj);
                } catch (Exception e) {
                    LOG.error("", e);
                }
                continue;
            }
            if(ant.auto_increment()){
                continue;
            }
            if(listFields.size() > 0 && withField != listFields.contains(ant.name())){
                continue;
            }
            try {
                Object value = m.invoke(obj);
                if (withNull) {
                    updateFields += ", " + ant.name() + " = ?";
                    listValue.add(value);
                } else if(value != null) {
                    updateFields += ", " + ant.name() + " = ?";
                    listValue.add(value);
                }
            } catch (Exception ex) {
                LOG.error("", ex);
                throw ex;
            }
        }
        if (listValue.size() == 0) {
            return null;
        }
        if(!Common.isBlank(whereClause)){
            if(params != null && params.length > 0){
                for(Object val : params){
                    listValue.add(val);
                }
            }
        } else {
            sql = sql.replace("@table_key", antTable.key());
            listValue.add(keyValue);
        }
        updateFields = updateFields.replaceFirst(", ", "");
        sql = sql.replace("@table_name", tableName);
        sql = sql.replace("@fields", updateFields);
        LOG.info(transId + "::" + sql);
        LOG.info(transId + "::" + Common.printArray(listValue.toArray()));
        return new QueryBean(sql, listValue,hasAutoIncrementKey );
    }

    @Override
    public <T> QueryBean getDeleteQuery(String transId, T obj, String whereClause, Object[] params) throws Exception {
        String sql = null;
        boolean hasAutoIncrementKey = false;
        if(Common.isBlank(whereClause)) {
            sql = "DELETE FROM @table_name WHERE @table_key = ? ";
        } else {
            sql = "DELETE FROM @table_name " + whereClause;
        }

        AntTable antTable = (AntTable)obj.getClass().getAnnotation(AntTable.class);
        String tableName = getTableName(antTable);
        ArrayList<Object> listValue = new ArrayList<Object>();
        Object keyValue=null;
        try {
            Method getKeyMethod= obj.getClass().getMethod("get" + Common.formatMethodName(antTable.key()));
            keyValue = getKeyMethod.invoke(obj);

        } catch (Exception e) {
            LOG.error("", e);
        }
        if(!Common.isBlank(whereClause)){
            if(params != null && params.length > 0){
                for(Object val : params){
                    listValue.add(val);
                }
            }
        } else {
            sql = sql.replace("@table_key", antTable.key());
            listValue.add(keyValue);
        }
        sql = sql.replace("@table_name", tableName);
        LOG.info(transId + "::" + sql);
        LOG.info(transId + "::" + Common.printArray(listValue.toArray()));
        return new QueryBean(sql, listValue,hasAutoIncrementKey );
    }

    @Override
    public <T> QueryBean getRecordById(String transId, T obj, Object key) throws Exception {
        String sql = "SELECT * FROM @table_name WHERE @table_key = ? ";
        boolean hasAutoIncrementKey = false;
        AntTable antTable = (AntTable)obj.getClass().getAnnotation(AntTable.class);
        String tableName = getTableName(antTable);
        ArrayList<Object> listValue = new ArrayList<Object>();
        listValue.add(key);

        sql = sql.replace("@table_name", tableName);
        sql = sql.replace("@table_key", antTable.key());
        LOG.info(transId + "::" + sql);
        LOG.info(transId + "::" + Common.printArray(listValue.toArray()));
        return new QueryBean(sql, listValue, hasAutoIncrementKey);
    }

    @Override
    public <T> QueryBean getRecordAll(String transId, T obj) throws Exception {
        String sql = "SELECT * FROM @table_name ";
        boolean hasAutoIncrementKey = false;
        AntTable antTable = (AntTable)obj.getClass().getAnnotation(AntTable.class);
        String tableName = getTableName(antTable);
        ArrayList<Object> listValue = new ArrayList<Object>();

        sql = sql.replace("@table_name", tableName);
        LOG.info(transId + "::" + sql);
        return new QueryBean(sql, listValue, hasAutoIncrementKey);
    }

    @Override
    public <T> QueryBean getRecordByParam(String transId, T obj, String whereClause, Object[] params) {
        String sql = null;
        boolean hasAutoIncrementKey = false;
        sql = "SELECT * FROM @table_name " + whereClause;
        if(Common.isBlank(whereClause)){
            return null;
        }

        AntTable antTable = (AntTable)obj.getClass().getAnnotation(AntTable.class);
        String tableName = getTableName(antTable);
        ArrayList<Object> listValue = new ArrayList<Object>();
        if(!Common.isBlank(whereClause)){
            if(params != null && params.length > 0){
                for(Object val : params){
                    listValue.add(val);
                }
            }
        }
        sql = sql.replace("@table_name", tableName);
        LOG.info(transId + "::" + sql);
        LOG.info(transId + "::" + Common.printArray(listValue.toArray()));
        return new QueryBean(sql, listValue, hasAutoIncrementKey);
    }

    @Override
    public <T> QueryBean getRecordBySql(String transId, String vsql, Object[] params) {
        String sql = vsql;
        boolean hasAutoIncrementKey = false;
        if(Common.isBlank(vsql)){
            return null;
        }

        ArrayList<Object> listValue = new ArrayList<Object>();
        if(params != null && params.length > 0){
            for(Object val : params){
                listValue.add(val);
            }
        }
        LOG.info(transId + "::" + sql);
        LOG.info(transId + "::" + Common.printArray(listValue.toArray()));
        return new QueryBean(sql, listValue, hasAutoIncrementKey);
    }

    @Override
    public <T> List<T> callProcedure() throws Exception {
        return null;
    }
}
