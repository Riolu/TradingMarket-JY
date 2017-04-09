package com.example.owenzx.jy_zx_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AdDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);

        Intent intent= getIntent();
        if(intent!=null && intent.hasExtra("pd_id")){
            String adDetailStr = intent.getStringExtra("pd_id");
            ((TextView) findViewById(R.id.ad_detail)).setText(adDetailStr);
        }
    }
}
