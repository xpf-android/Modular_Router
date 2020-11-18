package com.xpf.personal;


import com.xpf.router.ARouter;
import com.xpf.router.IRouter;

public class ActivityUtil implements IRouter {

    @Override
    public void putActivity() {
        ARouter.getInstance().addActivity("/personal/Personal_MainActivity", Personal_MainActivity.class);
    }
}
