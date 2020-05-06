package com.chenay.platform.msg;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Y.Chen5
 */
public class ResponseEntity<B> {
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_CODE = "code";
    public static final String KEY_RTN_CODE = "rtnCode";
    public static final String KEY_MSG = "msg";
    public static final String KEY_TIMESTAMP = "timestamp";


    private static final String DEFAULT_DATA_LIST = "default body";

    /**
     *    消息头meta 存放状态信息 code message
     */
    private Map<String, Object> meta;
    /**
     消息内容  存储实体交互数据
     *
     */
    private Map<String, Object> data;

    @Deprecated
    private List<B> beanList;

    public Map<String, Object> getMeta() {
        return meta;
    }

    public ResponseEntity<B> addMeta(String key, Object object) {
        if (meta == null) {
            meta = new HashMap<>();
        }
        this.meta.put(key, object);
        return this;
    }

    public Map<String, Object> getData() {
        if (data == null) {
            data = new HashMap<>();
        }
        return data;
    }

    public ResponseEntity<B> addData(String key, Object object) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, object);
        return this;
    }

    /**
     * @return
     */
    public List<B> getBeans() {
        Object o = getData().get(DEFAULT_DATA_LIST);
        if (o != null) {
            return (List<B>) o;
        }
        return null;
    }

    /**
     * @return
     */
    public List<B> getBeans(final TypeToken type) {
        Object o = getData().get(DEFAULT_DATA_LIST);
        if (o != null) {
            Gson gson = new Gson();
            String json = gson.toJson(o);
            List<B> list = gson.fromJson(json, type.getType());
            return list;
        }
        return null;
    }

    public ResponseEntity<B> addBean(B bean) {
        if (getBeans() == null) {
            getData().put(DEFAULT_DATA_LIST, new ArrayList<>());
        }
        getBeans().add(bean);
        return this;
    }

    public ResponseEntity<B> addBeans(List<B> list) {
        if (list == null) {
            return this;
        }
        if (getBeans() == null) {
            getData().put(DEFAULT_DATA_LIST, list);
        } else {
            for (int i = 0; i < list.size(); i++) {
                addBean(list.get(i));
            }
        }
        return this;
    }


    public ResponseEntity addData(CodeEntity codeEntity, Object object) {
        addData(codeEntity.getKey(), object);
        return this;
    }

    public ResponseEntity ok() {
        return ok(CodeEntity.SUCCESS);

    }

    public ResponseEntity error() {
        return error(CodeEntity.ERROR);
    }


    public ResponseEntity ok(CodeEntity codeEntity) {
        this.addMeta(KEY_SUCCESS, Boolean.TRUE);

        this.addMeta(KEY_CODE, codeEntity.getCode());
        this.addMeta(KEY_RTN_CODE, codeEntity.getCode() + "");
        this.addMeta(KEY_MSG, codeEntity.getMsg());
        this.addMeta(KEY_TIMESTAMP, new Timestamp(System.currentTimeMillis()));
        return this;
    }

    public ResponseEntity error(CodeEntity codeEntity) {
        this.addMeta(KEY_SUCCESS, Boolean.FALSE);
        this.addMeta(KEY_CODE, codeEntity.getCode());
        this.addMeta(KEY_RTN_CODE, codeEntity.getCode() + "");
        this.addMeta(KEY_MSG, codeEntity.getMsg());
        this.addMeta(KEY_TIMESTAMP, new Timestamp(System.currentTimeMillis()));
        return this;
    }


    public ResponseEntity ok(int statusCode, String statusMsg) {
        this.addMeta(KEY_SUCCESS, Boolean.TRUE);
        this.addMeta(KEY_CODE, statusCode);
        this.addMeta(KEY_RTN_CODE, statusCode + "");
        this.addMeta(KEY_MSG, statusMsg);
        this.addMeta(KEY_TIMESTAMP, new Timestamp(System.currentTimeMillis()));
        return this;
    }

    public ResponseEntity error(int statusCode, String statusMsg) {
        this.addMeta(KEY_SUCCESS, Boolean.FALSE);
        this.addMeta(KEY_CODE, statusCode);
        this.addMeta(KEY_RTN_CODE, statusCode + "");
        this.addMeta(KEY_MSG, statusMsg);
        this.addMeta(KEY_TIMESTAMP, new Timestamp(System.currentTimeMillis()));
        return this;
    }

    public String getMsg() {
        return (String) meta.get(KEY_MSG);
    }

    public String getRtnCode() {
        return (String) meta.get(KEY_RTN_CODE);
    }

    public List<B> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<B> beanList) {
        this.beanList = beanList;
    }
}
