package com.example.owenzx.jy_zx_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentDealActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    String findDealById = "http://lizunks.xicp.io:34789/trade_test/author_id_to_deal.php";
    ArrayList<String> datas = new ArrayList<>();
    ArrayList<String> clickdatas = new ArrayList<>();

    String userID; //登录后传给我

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        datas.clear();
        clickdatas.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_message);
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
                        for (int i=ja.length()-1; i>=0; --i){
                            JSONObject jsonpost = ja.getJSONObject(i);
                            String pd_or_req_id = jsonpost.getString("pd_or_req_id"); //????????????????????????????????????????
                            String buyer_or_supplier = jsonpost.getString("buyer_or_supplier");
                            String buyer_or_supplier_id = jsonpost.getString("buyer_or_supplier_id");
                            String dealType = jsonpost.getString("deal_type");
                            String message;
                            if (dealType.equals("0")){
                                message = buyer_or_supplier+"想购买您的商品\n"
                                        +"请点击查看详细信息";
                            }else{
                                message = "您可能中意"+buyer_or_supplier+"的商品\n" +
                                        "请点击查看详细信息";
                            }
                            datas.add(message);
                            clickdatas.add(buyer_or_supplier_id+"!"+pd_or_req_id+"!"+dealType);
                        }
                    }else{
                        String no_comment="还没有人希望与您交易"+"\n"+"快干点什么吧！";
                        datas.add(no_comment);
                        clickdatas.add("no deal!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(CommentDealActivity.this));
                mAdapter = new MainAdapter(datas,clickdatas);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, String data) {
                        //Toast.makeText(CommentDealActivity.this,data,Toast.LENGTH_SHORT).show();
                        if (!data.equals("no comment!")){
                            Intent intent = new Intent(CommentDealActivity.this, AcceptTradeActivity.class);
                            intent.putExtra("largeInfo",data);
                            startActivity(intent);
                        }
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
}






