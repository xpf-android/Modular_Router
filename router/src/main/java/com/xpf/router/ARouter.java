package com.xpf.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

public class ARouter {

    //上下文
    private Context context;

    //装载了所有的Activity的类对象 路由表
    private static HashMap<String, Class<? extends Activity>> map = new HashMap<>();

    private ARouter() {
//        map = new HashMap<>();
    }



    //静态内部类单例模式
    private static class Holder {
        private static final ARouter instance = new ARouter();
    }

    public static ARouter getInstance() {
        return Holder.instance;
    }


    public void init(Context context) {
        this.context = context;
        //执行所有生成文件的里面的putActivity方法

        //找到这些类
        List<String> classNames = getClassName("com.my.apt");
        Log.d("xpf", "init: " + "classNames size = " + classNames.size());
        for (String className : classNames) {
            Log.d("xpf", " init: className =  " + className);
            try {
                Class<?> utilClass = Class.forName(className);
                if (IRouter.class.isAssignableFrom(utilClass)) {
                    IRouter iRouter = (IRouter) utilClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("xpf", "init: " + "map size = " + map.size());

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Class<? extends Activity> aClass = map.get(key);
            Log.d("xpf", "init: " + "key = " + key + " aClass = " +aClass);
        }


    }

    /**
     * 将类对象添加到路由表
     * @param key
     * @param clazz
     */
    public void addActivity(String key, Class<? extends Activity> clazz) {
        if (key != null && clazz != null && !map.containsKey(key)) {
            map.put(key, clazz);
        }
    }

    public void jumpActivity(String key, Bundle bundle) {
        Class<? extends Activity> activityClass = map.get(key);
        if (activityClass != null) {
            Intent intent = new Intent(context, activityClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 通过包名获取这个包下面的所有类名
     * @param packageName
     * @return
     */
    private List<String> getClassName(String packageName) {
        //创造一个class对象的集合
        List<String> classList = new ArrayList<>();
        try {
            //把当前应用的apk存储路径给dexFile
            DexFile dexFile = new DexFile(context.getPackageCodePath());
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String className = (String) entries.nextElement();
                if (className.contains(packageName)) {
                    classList.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }


}
