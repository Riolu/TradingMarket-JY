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

    String addAdUrl = "http://lizunks.xicp.io:34789/trade_test/add_product.php";
    EditText prod_name, prod_price,prod_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_form);
        prod_name = (EditText)findViewById(R.id.prodName);
        prod_price = (EditText)findViewById(R.id.prodPrice);
        prod_detail = (EditText)findViewById(R.id.prodDetail);
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
        final String prod_name_str = prod_name.getText().toString();
        final String prod_price_str = prod_price.getText().toString();
        final String prod_detail_str = prod_detail.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST,addAdUrl,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                prod_name.setText("");
                prod_price.setText("");
                prod_detail.setText("");
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
                params.put("name",prod_name_str);
                params.put("author_id","999");
                params.put("price",prod_price_str);
                params.put("detail",prod_detail_str);
                return params;
            }
        };
        MySingleton.getInstance(AdFormActivity.this).addToRequestQueue(postRequest);
    }
}
