package com.example.owenzx.jy_zx_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Per_Buy_Activity extends AppCompatActivity {
    final String getbuyUrl="http://lizunks.xicp.io:34789/trade_test/product_query.php";
    private ListView listView_Buy;
    private CustomRequest adsreq;
    private Activity act;
    private ListAdapter adapter;
    final ArrayList<HashMap<String,String>> prodList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per__buy_);
        Intent intent_id4=getIntent();
        String user_id4=intent_id4.getStringExtra("userid");
        listView_Buy=(ListView)findViewById(R.id.listView_purchase) ;

        Toast.makeText(getApplicationContext(),
                user_id4,
                Toast.LENGTH_SHORT).show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("author_id", user_id4);

        prodList.clear();
        CustomRequest adsreq = new CustomRequest(Request.Method.POST,getbuyUrl,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        Toast.makeText(getApplicationContext(),
                                "购买信息获取成功",
                                Toast.LENGTH_SHORT).show();
                        JSONArray ja = response.getJSONArray("posts");
                        handleJSONArray(ja);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "没有购买记录",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(Per_Buy_Activity.this).addToRequestQueue(adsreq);
        Toast.makeText(getApplicationContext(),
                "!!!",
                Toast.LENGTH_SHORT).show();
//                searchView.setQuery("", false);
        //searchView.clearFocus();
        //
        }
    public void handleJSONArray(JSONArray ja){
        try {
            prodList.clear();
            for (int i=0; i<ja.length(); ++i) {
                JSONObject jsonpost = ja.getJSONObject(i);
                String productID = jsonpost.getString("pd_id");
                String prodName = jsonpost.getString("name");
                String prodDetail = jsonpost.getString("detail");
                String prodType = jsonpost.getString("type");
                String prodPrice = jsonpost.getString("price");
                String productAuthor_id = jsonpost.getString("author_id");
                String productAuthor = jsonpost.getString("author");
                String prodisBought = jsonpost.getString("isBought");
                HashMap<String,String> prod = new HashMap<String, String>();
                prod.put("pd_id", productID);
                prod.put("name", prodName);
                prod.put("price", prodPrice);
                prod.put("detail", prodDetail);
                prod.put("type", prodType);
                prod.put("author_id", productAuthor_id);
                prod.put("author", productAuthor);
                prod.put("isBought", prodisBought);
                prodList.add(prod);
            }
            String [] from = {"name","price","detail"};
            int[] to = {R.id.pd_title, R.id.pd_price, R.id.pd_detail};
            //assert(getActivity()!=null);
            adapter = new SimpleAdapter(this,prodList, R.layout.list_item_adsnew,from,to);
            listView_Buy.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
