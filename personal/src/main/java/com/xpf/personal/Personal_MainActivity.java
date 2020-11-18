package com.xpf.personal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.xpf.annotation.BindPath;
import com.xpf.router.ARouter;

@BindPath(path = "/personal/Personal_MainActivity")
public class Personal_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);
    }

    public void jumpApp(View view) {
        ARouter.getInstance().jumpActivity("/app/MainActivity", null);
    }

    public void jumpOrder(View view) {
        ARouter.getInstance().jumpActivity("/order/Order_MainActivity", null);
    }
}
