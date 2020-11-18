package com.xpf.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xpf.annotation.BindPath;
import com.xpf.common.LiveDataBus;
import com.xpf.router.ARouter;

@BindPath(path = "/order/Order_MainActivity")
public class Order_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);
        LiveDataBus.getInstance().with("code", Integer.class).observe(Order_MainActivity.this, true, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("xpf >>> ", "Order_MainActivity:  " + integer);
            }
        });

        /*LiveDataBus.getInstance().with("code", Integer.class).observe(Order_MainActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("xpf >>> ", "Order_MainActivity: " + integer);
            }
        });*/

    }

    public void jumpApp(View view) {
        ARouter.getInstance().jumpActivity("/app/MainActivity", null);
    }

    public void jumpPersonal(View view) {
        ARouter.getInstance().jumpActivity("/personal/Personal_MainActivity", null);
    }

    public void postValue(View view) {
        LiveDataBus.getInstance().with("code", Integer.class).postValue(169);
        ARouter.getInstance().jumpActivity("/app/Main2Activity",null);

    }
}
