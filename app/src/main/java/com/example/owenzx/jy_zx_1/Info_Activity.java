package com.example.owenzx.jy_zx_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Info_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_);
        Button change_info_bt = (Button) findViewById(R.id.change_info_bt);
        Button sale_info_bt = (Button) findViewById(R.id.sale_info_bt);
        Button req_info_bt = (Button) findViewById(R.id.req_info_bt);
        Button trading_info_bt = (Button) findViewById(R.id.trading_info_bt);
        Intent intent_id=getIntent();
        final String user_id=intent_id.getStringExtra("userid");
        final String selfname=LoginData.getFromPrefs(Info_Activity.this,LoginData.PREFS_LOGIN_USERNAME_KEY,null);
        TextView text=(TextView)findViewById(R.id.textView36);
        text.setText(selfname);
        change_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent11=new Intent(Info_Activity.this,Per_info_Activity.class);
                //Intent intent_id=getIntent();
                //String user_id=intent_id.getStringExtra("userid");
                Intent intent_id1=new Intent();
                intent_id1.setClass(Info_Activity.this,Per_info_Activity.class);
                intent_id1.putExtra("userid",user_id);
                startActivity(intent_id1);
                //startActivity(intent11);
            }
        });
        sale_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent12=new Intent(Info_Activity.this,Per_Sale_Activity.class);
                Intent intent_id2=new Intent();
                intent_id2.setClass(Info_Activity.this,Per_Sale_Activity.class);
                intent_id2.putExtra("userid",user_id);
                startActivity(intent_id2);
                //startActivity(intent12);
            }
        });
        req_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent_id=getIntent();
                //String user_id=intent_id.getStringExtra("userid");
                //Intent intent13=new Intent(Info_Activity.this,Per_Req_Activity.class);
                //startActivity(intent13);
                Intent intent_id3=new Intent();
                intent_id3.setClass(Info_Activity.this,Per_Req_Activity.class);
                intent_id3.putExtra("userid",user_id);
                startActivity(intent_id3);
            }
        });
        trading_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent_id=getIntent();
                //String user_id=intent_id.getStringExtra("userid");
                Intent intent_id4=new Intent();
                intent_id4.setClass(Info_Activity.this,Per_Buy_Activity.class);
                intent_id4.putExtra("userid",user_id);
                startActivity(intent_id4);
                //Intent intent14=new Intent(Info_Activity.this,Per_Buy_Activity.class);
                //startActivity(intent14);
            }
        });

    }
}