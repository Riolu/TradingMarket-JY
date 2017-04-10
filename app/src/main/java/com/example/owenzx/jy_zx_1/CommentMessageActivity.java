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

public class CommentMessageActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    String findCommentById = "http://lizunks.xicp.io:34789/trade_test/receiver_id_to_comment.php";
    String PdIdToInfo = "http://lizunks.xicp.io:34789/trade_test/pd_id_to_info.php";
    String ReqIdToInfo = "http://lizunks.xicp.io:34789/trade_test/req_id_to_info.php";
    ArrayList<String> datas = new ArrayList<>();
    ArrayList<String> pdIDdatas = new ArrayList<>();

    String userID; //登录后传给我

    String id;
    String type;

    String Name;
    String Price;
    String Detail;
    String AuthorID;
    String Author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        datas.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_message);


        userID = LoginData.getFromPrefs(CommentMessageActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
        //根据userID查询得到其评论
        Map<String, String> params = new HashMap<String, String>();
        params.put("receiver_id", userID);
        CustomRequest adsreq = new CustomRequest(Request.Method.POST, findCommentById,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        for (int i=ja.length()-1; i>=0; --i){
                            JSONObject jsonpost = ja.getJSONObject(i);
                            String sender = jsonpost.getString("sender");
                            String comment = jsonpost.getString("comment");
                            String pd_or_req_id = jsonpost.getString("pd_or_req_id");
                            String commentType = jsonpost.getString("comment_type");
                            String message = sender+"回复你"+"\n"+comment;
                            datas.add(message);
                            pdIDdatas.add(pd_or_req_id+"!"+commentType);
                        }
                    }else{
                        String no_comment="还没有人回复你哦"+"\n"+"快干点什么吧！";
                        datas.add(no_comment);
                        pdIDdatas.add("no comment!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(CommentMessageActivity.this));
                mAdapter = new MainAdapter(datas,pdIDdatas);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, String data) {
                        //Toast.makeText(CommentMessageActivity.this,data,Toast.LENGTH_SHORT).show();
                        if (!data.equals("no comment!")){
                            String[] x = data.split("!");
                            id = x[0];
                            type = x[1];
                            if (type.equals("0")) {
                                getPdInfo(id);
                            }else {
                                getReqInfo(id);
                            }
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(CommentMessageActivity.this).addToRequestQueue(adsreq);
    }


    public void getPdInfo(String pdID){
        Map<String, String> params = new HashMap<String, String>();
        params.put("pd_id", pdID);
        CustomRequest pdreq = new CustomRequest(Request.Method.POST, PdIdToInfo,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        for (int i=0; i<ja.length(); ++i) {
                            JSONObject jsonpost = ja.getJSONObject(i);
                            Name = jsonpost.getString("name");
                            Price = jsonpost.getString("price");
                            Detail = jsonpost.getString("detail");
                            AuthorID = jsonpost.getString("author_id");
                            Author = jsonpost.getString("author");
                        }
                        Intent intent = new Intent(CommentMessageActivity.this, AdDetailActivity.class);
                        intent.putExtra("pd_id", id);
                        intent.putExtra("name", Name);
                        intent.putExtra("price", Price);
                        intent.putExtra("detail", Detail);
                        intent.putExtra("author_id", AuthorID);
                        intent.putExtra("author", Author);
                        startActivity(intent);
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
        MySingleton.getInstance(CommentMessageActivity.this).addToRequestQueue(pdreq);
    }


    public void getReqInfo(String reqID){
        Map<String, String> params = new HashMap<String, String>();
        params.put("req_id", reqID);
        CustomRequest pdreq = new CustomRequest(Request.Method.POST, ReqIdToInfo,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        for (int i=0; i<ja.length(); ++i) {
                            JSONObject jsonpost = ja.getJSONObject(i);
                            Name = jsonpost.getString("title");
                            Price = jsonpost.getString("ideal_price");
                            Detail = jsonpost.getString("description");
                            AuthorID = jsonpost.getString("author_id");
                            Author = jsonpost.getString("author");
                        }
                        Intent intent = new Intent(CommentMessageActivity.this, ReqDetailActivity.class);
                        intent.putExtra("req_id", id);
                        intent.putExtra("title", Name);
                        intent.putExtra("ideal_price", Price);
                        intent.putExtra("description", Detail);
                        intent.putExtra("author_id", AuthorID);
                        intent.putExtra("author", Author);
                        startActivity(intent);
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
        MySingleton.getInstance(CommentMessageActivity.this).addToRequestQueue(pdreq);
    }
}


