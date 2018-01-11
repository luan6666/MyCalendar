package com.example.administrator.mycalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    ECalendar eCalendar;
    public static String needData;//往日历里面穿的常量,直接在日历里面类名+常量名进行调用,传完之后要刷新数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eCalendar = findViewById(R.id.eCalendar);

        //传送数据进行改变
        eCalendar.setNotifyDataSetChanged();
    }
}
