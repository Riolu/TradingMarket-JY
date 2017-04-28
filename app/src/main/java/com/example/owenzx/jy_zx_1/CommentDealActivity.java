package com.example.owenzx.jy_zx_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentDealActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DealAdapter mAdapter;
    String findDealById = "http://lizunks.xicp.io:34789/trade_test/author_id_to_deal.php";
    String readDeal = "http://lizunks.xicp.io:34789/trade_test/read_deal.php";
    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();


    String userID; //登录后传给我

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_comment);

        userID = LoginData.getFromPrefs(CommentDealActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
        //根据userID查询得到其评论
        Map<String, String> params = new HashMap<String, String>();
        params.put("author_id", userID);
        CustomRequest adsreq = new CustomRequest(Request.Method.POST, findDealById,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        dataList.clear();
                        for (int i=ja.length()-1; i>=0; --i){
                            JSONObject jsonpost = ja.getJSONObject(i);
                            String deal_id = jsonpost.getString("deal_id");
                            String pd_or_req_id = jsonpost.getString("pd_or_req_id");
                            String buyer_or_supplier = jsonpost.getString("buyer_or_supplier");
                            String buyer_or_supplier_id = jsonpost.getString("buyer_or_supplier_id");
                            String dealType = jsonpost.getString("deal_type");
                            HashMap<String,String> data = new HashMap<String, String>();
                            data.put("deal_id",deal_id);
                            data.put("pd_or_req_id",pd_or_req_id);
                            data.put("buyer_or_supplier",buyer_or_supplier);
                            data.put("buyer_or_supplier_id",buyer_or_supplier_id);
                            data.put("deal_type",dealType);
                            dataList.add(data);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(CommentDealActivity.this));
                mAdapter = new DealAdapter(dataList,CommentDealActivity.this);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new DealAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view,  HashMap<String,String> data) {
                        //Toast.makeText(CommentDealActivity.this,data,Toast.LENGTH_SHORT).show();
                        readDeal(data.get("deal_id"));

                        Intent intent = new Intent(CommentDealActivity.this, AcceptTradeActivity.class);
                        intent.putExtra("largeInfo",data);
                        startActivity(intent);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(CommentDealActivity.this).addToRequestQueue(adsreq);
    }


    public void readDeal(String dealID){
        Map<String, String> params = new HashMap<String, String>();
        params.put("deal_id", dealID);
        CustomRequest adsreq = new CustomRequest(Request.Method.POST, readDeal,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                try {
//                    Toast.makeText(CommentMessageActivity.this,data, Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(CommentDealActivity.this).addToRequestQueue(adsreq);
    }
}






