package com.example.owenzx.jy_zx_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    Spinner req_type_sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_form);
        Intent intent = getIntent();

        String formMode = intent.getStringExtra("mode");

        req_title = (EditText)findViewById(R.id.reqTitle);
        req_budget = (EditText)findViewById(R.id.reqBudget);
        req_detail = (EditText)findViewById(R.id.reqDetail);
        req_type_sp = (Spinner) findViewById(R.id.reqTypeSpinner);
        Button buttonAddReq = (Button) findViewById(R.id.buttonAddReq);
        assert buttonAddReq != null;
        buttonAddReq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addNewReq(v);

            }
        });
        if (formMode.equals("EDIT")){
            addReqUrl = "http://lizunks.xicp.io:34789/trade_test/update_req.php";
            req_title.setText(intent.getStringExtra("name"));
            req_budget.setText(intent.getStringExtra("price"));
            req_detail.setText(intent.getStringExtra("detail"));
            switch (intent.getStringExtra("type")) {
                case "书籍":
                    req_type_sp.setSelection(0, true);

                    break;
                case "生活用品":
                    req_type_sp.setSelection(1, true);

                    break;
                case "娱乐用品":
                    req_type_sp.setSelection(2, true);

                    break;
                case "体育用品":
                    req_type_sp.setSelection(3, true);

                    break;
                default:
                    req_type_sp.setSelection(4, true);


            }
            buttonAddReq.setText("保存修改");
        }
    }

    private void addNewReq(View v){
        final String req_title_str = req_title.getText().toString();
        final String req_budget_str = req_budget.getText().toString();
        final String req_detail_str = req_detail.getText().toString();
//        final String req_type_str = req_type.getText().toString();
        final String req_type_str = req_type_sp.getSelectedItem().toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST,addReqUrl,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                req_title.setText("");
                req_budget.setText("");
                req_detail.setText("");
//                req_type.setText("");
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
                String author_id = LoginData.getFromPrefs(ReqFormActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
                String author_name = LoginData.getFromPrefs(ReqFormActivity.this,LoginData.PREFS_LOGIN_USERNAME_KEY,null);
                params.put("author",author_name);
                params.put("author_id",author_id);
                params.put("ideal_price",req_budget_str);
                params.put("description",req_detail_str);
                params.put("type",req_type_str);
                return params;
            }
        };
        MySingleton.getInstance(ReqFormActivity.this).addToRequestQueue(postRequest);
    }
}
