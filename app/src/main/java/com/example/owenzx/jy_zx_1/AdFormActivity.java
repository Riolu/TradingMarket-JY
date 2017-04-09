package com.example.owenzx.jy_zx_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AdFormActivity extends AppCompatActivity {

    String addAdUrl = "http://lizunks.xicp.io:34650/webservice/addcomment.php";
    EditText com_user, com_title,com_mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_form);
        com_user = (EditText)findViewById(R.id.comUser);
        com_title = (EditText)findViewById(R.id.comTitle);
        com_mes = (EditText)findViewById(R.id.comMes);
        Button buttonAddAd = (Button) findViewById(R.id.buttonAddAd);
        assert buttonAddAd != null;
        buttonAddAd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addNewAd(v);
//                Intent backIntent = new Intent(AdFormActivity.this,AdsActivity.class);
//                startActivity(backIntent);
            }
        });
    }

    private void addNewAd(View v){
        final String com_user_str = com_user.getText().toString();
        final String com_title_str = com_title.getText().toString();
        final String com_mes_str = com_mes.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST,addAdUrl,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                com_user.setText("");
                com_title.setText("");
                com_mes.setText("");
                Toast.makeText(getApplicationContext(),
                        "Data Inserted Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("username",com_user_str);
                params.put("title",com_title_str);
                params.put("message",com_mes_str);
                return params;
            }
        };
        MySingleton.getInstance(AdFormActivity.this).addToRequestQueue(postRequest);
    }
}
