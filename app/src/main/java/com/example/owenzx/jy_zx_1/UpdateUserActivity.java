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

public class UpdateUserActivity extends AppCompatActivity {

    String addAdUrl = "http://lizunks.xicp.io:34789/trade_test/update_user.php";
    EditText user_truename, user_email,user_address,user_qq,user_phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        Intent intent = getIntent();
        String formMode = intent.getStringExtra("mode");
        user_address = (EditText)findViewById(R.id.address);
        user_email = (EditText)findViewById(R.id.ps_email);
        user_phonenumber = (EditText)findViewById(R.id.phone_number);
        user_qq = (EditText)findViewById(R.id.ps_qq);
        user_truename = (EditText)findViewById(R.id.true_name);
        Button buttonAddAd = (Button) findViewById(R.id.buttonUpdataUser);
        assert buttonAddAd != null;
        buttonAddAd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addNewUser(v);

            }
        });

        user_truename.setText(intent.getStringExtra("true_name"));
        user_qq.setText(intent.getStringExtra("qq"));
        user_phonenumber.setText(intent.getStringExtra("phone_number"));
        user_email.setText(intent.getStringExtra("email"));
        user_address.setText(intent.getStringExtra("address"));

    }

    private void addNewUser(View v){
        final String usertruename_str = user_truename.getText().toString();
        final String useradd_str = user_address.getText().toString();
        final String userqq_str = user_qq.getText().toString();
        final String userpn_str = user_phonenumber.getText().toString();
        final String useremail_str = user_email.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST,addAdUrl,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                user_address.setText("");
                user_truename.setText("");
                user_email.setText("");
                user_phonenumber.setText("");
                user_qq.setText("");
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
                String author_id = LoginData.getFromPrefs(UpdateUserActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
                String author_name = LoginData.getFromPrefs(UpdateUserActivity.this,LoginData.PREFS_LOGIN_USERNAME_KEY,null);
                params.put("user_id",author_id);
                params.put("name",author_name);
                params.put("email",useremail_str);
                params.put("phone_number",userpn_str);
                params.put("true_name",usertruename_str);
                params.put("address",useradd_str);
                params.put("qq",userqq_str);
                return params;
            }
        };
        MySingleton.getInstance(UpdateUserActivity.this).addToRequestQueue(postRequest);
    }
}
