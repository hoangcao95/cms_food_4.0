package vn.vano.cms.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

public class ResponseData implements Serializable {
    public static String SUCCESS = "1";
    public static String ERROR = "0";
    private String code = SUCCESS;
    private String message;
    Object data;

    @SuppressWarnings("rawtypes")
    Class cls;

    @SuppressWarnings("rawtypes")
    public static ResponseData buildResponse(Object data, Class cls) {
        ResponseData responseData = new ResponseData(cls);
        responseData.setData(data);
        responseData.build();
        return responseData;
    }

    public static ResponseData buildResponse(String code) {
        ResponseData responseData = new ResponseData(null);
        responseData.setCode(code);
        responseData.build();
        return responseData;
    }

    public static ResponseData buildResponse(String code, String message) {
        ResponseData responseData = new ResponseData(null);
        responseData.setCode(code);
        responseData.setMessage(message);
        responseData.build();
        return responseData;
    }

    public static ResponseData buildResponse(String code, String message, Object data) {
        ResponseData responseData = new ResponseData(null);
        responseData.setCode(code);
        responseData.setMessage(message);
        responseData.setData(data);
        responseData.build();
        return responseData;
    }

    public static ResponseData buildResponse(String code, String message, Object data, Class cls) {
        ResponseData responseData = new ResponseData(cls);
        responseData.setCode(code);
        responseData.setMessage(message);
        responseData.setData(data);
        responseData.build();
        return responseData;
    }

    public ResponseData() {
        this.code = SUCCESS;
    }

    public ResponseData(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @SuppressWarnings("rawtypes")
    public ResponseData(Class cls) {
        this.cls = cls;
    }

    /**
     *
     * @param page Page Items
     * @param cls
     */
    @SuppressWarnings({ "rawtypes" })
    public ResponseData(final Page page,Class cls) {
        this.build(page, cls);
    }
    /**
     *
     * @param content Object item
     * @param cls
     */
    @SuppressWarnings("rawtypes")
    public ResponseData(final Object content, Class cls) {
        this.build(content, cls);
    }

    /**
     * Page contain of items and page metadata
     * @param page
     * @param cls
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void build(final Page page,Class cls) {
        if(page == null){
            return;
        }

        List list = page.getContent();
        if(cls == null){
            this.data = list;
            return;
        }
        List dtoList = new ArrayList();
        Object dto = null; // cls.newInstance();
        for(Object e:list){
            try {
                dto = cls.newInstance();
                BeanUtils.copyProperties(e, dto);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(dto != null){
                dtoList.add(dto);
            }
        }
        this.data = dtoList;
    }

    /**
     * Single Item
     * @param content
     * @param cls
     */

    @SuppressWarnings("rawtypes")
    private void build(final Object content, Class cls) {
        if(content == null){
            return ;
        }
        if(cls == null){
            this.data = content;
            return;
        }
        Object dto = null;
        try {
            dto = cls.newInstance();
            BeanUtils.copyProperties(content, dto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.data = dto;
    }

    /**
     * fix list items
     * @param items
     * @param cls
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void build(final List items,Class cls) {
        if(items == null || items.isEmpty()){
            return;
        }
        if(cls == null){
            this.data = items;
            return;
        }

        List dtoList = new ArrayList();
        Object dto = null;
        for(Object e:items){
            try {
                dto = cls.newInstance();
                BeanUtils.copyProperties(e, dto);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(dto != null){
                dtoList.add(dto);
            }
        }
        this.data = dtoList;
    }

    @SuppressWarnings("rawtypes")
    public ResponseData build() {
        if (this.data instanceof Page) {
            Page page = (Page) this.data;
            this.build(page, this.cls);
        } else if (this.data instanceof List) {
            List items = (List) this.data;
            this.build(items, cls);
        } else {
            this.build(this.data, this.cls);
        }
        return this;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

