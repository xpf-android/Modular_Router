package com.xpf.base;

import android.app.Application;
import android.util.Log;

import com.xpf.common.utils.Cons;
import com.xpf.router.ARouter;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.getInstance().init(this);
        Log.d(Cons.TAG, "onCreate: ");
    }
}
