package com.xpf.modular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xpf.annotation.BindPath;
import com.xpf.common.JSLiveData;
import com.xpf.common.JSObserver;
import com.xpf.common.LiveDataBus;
import com.xpf.router.ARouter;

@BindPath(path = "/app/Main2Activity")
public class Main2Activity extends AppCompatActivity {

//    JSLiveData liveData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        liveData = new JSLiveData();
        LiveDataBus.getInstance().with("code", Integer.class).postValue(168);

        /*LiveDataBus.getInstance().with("code", Integer.class).observe(Main2Activity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("xpf >>> ", "" + integer);
            }
        });*/
    }


    public void sendData(View view) {
//        liveData.postValue("123456");

    }

    public void registerObserver(View view) {
//        liveData.addObserver(new JSObserver() {
//            @Override
//            public void onChange(Object obj) {
//                Log.d("JSLiveData >>> ", obj.toString());
//                Toast.makeText(Main2Activity.this, obj.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
        /*mutableLiveData.observe(Main2Activity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("xpf >>> ", "" + integer );
            }
        });*/
    }

    public void jump(View view) {
        ARouter.getInstance().jumpActivity("/order/Order_MainActivity",null);
    }
}
