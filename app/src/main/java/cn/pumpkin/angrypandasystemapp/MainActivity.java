package cn.pumpkin.angrypandasystemapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.pumpkin.mylibrary.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.hello();
    }
}
