package com.xpf.modular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xpf.annotation.BindPath;
import com.xpf.common.JSLiveData;
import com.xpf.common.JSObserver;
import com.xpf.common.LiveDataBus;
import com.xpf.order.Order_MainActivity;
import com.xpf.personal.Personal_MainActivity;
import com.xpf.router.ARouter;

@BindPath(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        LiveDataBus.getInstance().with("code", Integer.class).postValue(168);

    }

    public void jumpOrder(View view) {

//        startActivity(new Intent(this, Order_MainActivity.class));

        ARouter.getInstance().jumpActivity("/order/Order_MainActivity", null);
    }

    public void jumpPersonal(View view) {
//        startActivity(new Intent(this, Personal_MainActivity.class));
        ARouter.getInstance().jumpActivity("/personal/Personal_MainActivity", null);

    }


    public void jump(View view) {
        ARouter.getInstance().jumpActivity("/app/Main2Activity",null);
    }
}
