package com.example.tiange.myview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import view.CustomView;
import view.CustomView2;

public class MainActivity extends AppCompatActivity {
    CustomView2 view;
    CustomView view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view1= (CustomView) findViewById(R.id.custom1_1);
        view=(CustomView2)findViewById(R.id.custom2);
        view.setBtnClickListener(new CustomView2.OnClickListener() {
            @Override
            public void onclick() {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }

            @Override
            public void star(){
                view1.starThread();
                ((CustomView) findViewById(R.id.custom1_2)).starThread();
                ((CustomView) findViewById(R.id.custom1_3)).starThread();
            }
        });
    }
}
