package com.chenay.platform.msg;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestEntity<B> {
    private static final String DEFAULT_DATA_LIST = "default body";

    // 消息头meta 存放状态信息 code message
    private Map<String, Object> meta;
    // 消息内容  存储实体交互数据
    private Map<String, Object> data;


    public Map<String, Object> getMeta() {
        return meta;
    }

    public RequestEntity<B> addMeta(String key, Object object) {
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

    public RequestEntity<B> addData(String key, Object object) {
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
        if (o!=null) {
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

    public RequestEntity<B> addBean(B bean) {
        if (getBeans() == null) {
            getData().put(DEFAULT_DATA_LIST, new ArrayList<>());
        }
        getBeans().add(bean);
        return this;
    }

    public RequestEntity<B> addBeans(List<B> list) {
        if (list == null) {
            return this;
        }
        if (getBeans() == null) {
            getData().put(DEFAULT_DATA_LIST,list);
        } else {
            for (int i = 0; i < list.size(); i++) {
                addBean(list.get(i));
            }
        }
        return this;
    }


    public RequestEntity addData(CodeEntity codeEntity, Object object) {
        addData(codeEntity.getKey(), object);
        return this;
    }

}
