package com.inka.netsync.view.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagerViewEntity<T> implements Serializable {

    private long id;
    private T content;
    private boolean isCheck;
    private int status;
    private String modelType;
    private Class<?> modelView;
    private long timestamp;
    private boolean isSingleton;
    private String tag = "";

    private Map<String, Object> attrs;

    public PagerViewEntity() {
        this(null);
    }

    public PagerViewEntity(T t) {
        this.content = t;
        setTimestamp(System.currentTimeMillis());
    }

    public long getId() {
        return id;
    }

    public PagerViewEntity setId(long id) {
        this.id = id;
        return this;
    }

    public T getContent() {
        return content;
    }

    public T getContent(Class<T> c) {
        return (T)content;
    }

    public PagerViewEntity setContent(T content) {
        this.content = content;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public PagerViewEntity setCheck(boolean isCheck) {
        this.isCheck = isCheck;
        return this;
    }

    public PagerViewEntity setStatus(int status){
        this.status = status;
        return this;
    }

    public int getStatus(){
        return this.status;
    }

    public String getModelType() {
        return modelType;
    }

    public PagerViewEntity setModelType(String modelType) {
        this.modelType = modelType;
        return this;
    }

    public Class<?> getModelView() {
        return modelView;
    }

    public PagerViewEntity setModelView(Class<?> modelView) {
        if(modelType == null){
            setModelType(modelView.getName());
        }
        this.modelView = modelView;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public PagerViewEntity setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public PagerViewEntity setSingleton(boolean isSingleton) {
        this.isSingleton = isSingleton;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public PagerViewEntity setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public PagerViewEntity<T> setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
        return this;
    }

    public PagerViewEntity<T> addAttr(String key, Object value){
        if(attrs == null){
            attrs = new HashMap<String, Object>();
        }
        attrs.put(key, value);
        return this;
    }

    public <T> T  getAttr(String key, Class<T> c){
        if(attrs == null) {
            return null;
        }
        return (T) attrs.get(key);
    }

    public boolean hasAttr(String key){
        if(attrs == null){
            return false;
        }
        if(attrs.get(key) == null){
            return false;
        }
        return true;
    }

    public void removeAttr(String key){
        if(attrs.get(key) != null){
            attrs.remove(key);
        }
    }

    public void attach(List<PagerViewEntity> list){
        list.add(this);
    }

}
