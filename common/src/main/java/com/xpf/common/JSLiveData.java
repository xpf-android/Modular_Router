package com.xpf.common;

import java.util.ArrayList;
import java.util.List;

public class JSLiveData {
    //持有的数据
    public Object data;

    //观察者的注册表
    List<JSObserver> observers;

    public JSLiveData() {
        this.observers = new ArrayList<>();
    }

    /**
     * 将观察者添加到注册表
     * @param observer
     */
    public void addObserver(JSObserver observer) {
        observers.add(observer);
    }

    /**
     * 发送数据
     * 触发接口回调
     * @param obj
     */
    public void postValue(Object obj) {
        this.data = obj;
        for (JSObserver observer : observers) {
            observer.onChange(obj);
        }
    }
}
