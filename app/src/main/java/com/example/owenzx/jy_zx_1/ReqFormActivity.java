package com.example.owenzx.jy_zx_1;

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

public class ReqFormActivity extends AppCompatActivity {

    String addReqUrl = "http://lizunks.xicp.io:34789/trade_test/add_req.php";
    EditText req_title, req_budget,req_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_form);
        req_title = (EditText)findViewById(R.id.reqTitle);
        req_budget = (EditText)findViewById(R.id.reqBudget);
        req_detail = (EditText)findViewById(R.id.reqDetail);
        Button buttonAddReq = (Button) findViewById(R.id.buttonAddReq);
        assert buttonAddReq != null;
        buttonAddReq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addNewReq(v);
//                Intent backIntent = new Intent(AdFormActivity.this,AdsActivity.class);
//                startActivity(backIntent);
            }
        });
    }

    private void addNewReq(View v){
        final String req_title_str = req_title.getText().toString();
        final String req_budget_str = req_budget.getText().toString();
        final String req_detail_str = req_detail.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST,addReqUrl,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                req_title.setText("");
                req_budget.setText("");
                req_detail.setText("");
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
                params.put("title",req_title_str);
                params.put("author_id","999");
                params.put("ideal_price",req_budget_str);
                params.put("description",req_detail_str);
                params.put("type","Book");
                return params;
            }
        };
        MySingleton.getInstance(ReqFormActivity.this).addToRequestQueue(postRequest);
    }
}
