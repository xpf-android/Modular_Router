package com.xpf.common;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LiveDataBus {

    //容器 存储所有的订阅者的容器
    private Map<String, MutableLiveDataBus<Object>> map;
    
    private LiveDataBus() {
        map = new HashMap<>();
    }

    //静态内部类单例模式
    private static class Holder {
        private static LiveDataBus instance = new LiveDataBus();
    }

    public static LiveDataBus getInstance() {
        return Holder.instance;
    }

    /**
     * 注册订阅者的方法，存和取一体
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    /*public synchronized <T> MutableLiveData<T> with(String key, Class<T> type) {
        //判断订阅者的存储容器里面是否已经包含了这个key
        if (!map.containsKey(key)) {
            //存
            map.put(key, new MutableLiveData<Object>());
        }
        //取
        return (MutableLiveData<T>) map.get(key);
    }*/

    public synchronized <T> MutableLiveDataBus<T> with(String key, Class<T> type) {
        //判断订阅者的存储容器里面是否已经包含了这个key
        if (!map.containsKey(key)) {
            //存
            map.put(key, new MutableLiveDataBus<Object>());
        }
        //取
        return (MutableLiveDataBus<T>) map.get(key);
    }
    
    public static class MutableLiveDataBus<T> extends MutableLiveData<T> {
        private boolean isViscosity = false;

        public void observe(@NonNull LifecycleOwner owner, boolean isViscosity, @NonNull Observer<? super T> observer) {
            this.isViscosity = isViscosity;
            observe(owner, observer);
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, observer);
            try {
                if (isViscosity) {
                    //通过反射获取mVersion、mLastVersion的值
                    //然后赋值mLastVersion = mVersion
                    hook(observer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

        /**
         * hook技术的实现方法，拦截onChange方法的执行
         * @param observer
         */
        private void hook(Observer<? super T> observer) throws Exception {
            Log.d("xpf >>> ", "hook: 执行了...");
            //获取到LiveData的类对象
            Class<LiveData> liveDataClass = LiveData.class;
            //根据类对象获取到mVersion的反射对象
            Field mVersionField = liveDataClass.getDeclaredField("mVersion");
            //打开权限
            mVersionField.setAccessible(true);
            //获取到mObservers的反射对象
            Field mObserversField = liveDataClass.getDeclaredField("mObservers");
            //打开权限
            mObserversField.setAccessible(true);
            //获取到这个成员变量在当前对象中的值
            Object mObservers = mObserversField.get(this);
            //获取到mObservers这个map的get方法
            Method get = mObservers.getClass().getDeclaredMethod("get", Object.class);
            //打开权限
            get.setAccessible(true);
            //执行get方法
            Object invokeEntry = get.invoke(mObservers, observer);
            //定义一个空对象 LifecycleBoundObserver
            Object observerWrapper = null;
            if (invokeEntry != null && invokeEntry instanceof Map.Entry) {
                observerWrapper = ((Map.Entry) invokeEntry).getValue();
            }
            if (observerWrapper == null) {
                throw new NullPointerException("ObserverWrapper 不能为空...");
            }

            Class<?> clazz = observerWrapper.getClass();
            //得到observerWrapper的类对象
            Class<?> superclass = clazz.getSuperclass();
            //获取mLastVersion的反射对象
            Field mLastVersionField = superclass.getDeclaredField("mLastVersion");
            //打开权限
            mLastVersionField.setAccessible(true);
            //获取到mVersion
            Object obj = mVersionField.get(this);
            //把它的值赋值给mLastVersion
            mLastVersionField.set(observerWrapper, obj);
        }
    }




}

