package com.example.owenzx.jy_zx_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

public class Per_Req_Activity extends AppCompatActivity {
    final String getreqUrl="http://lizunks.xicp.io:34789/trade_test/user_id_to_req.php";
    private ListView listView_Req;
    private CustomRequest reqReq;
    private Activity act;
    private ListAdapter adapter;
    final ArrayList<HashMap<String,String>> prodList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per__req_);
        Intent intent_id3=getIntent();
        String user_id3=intent_id3.getStringExtra("userid");
        final String selfID = LoginData.getFromPrefs(Per_Req_Activity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
        final String selfname=LoginData.getFromPrefs(Per_Req_Activity.this,LoginData.PREFS_LOGIN_USERNAME_KEY,null);
        listView_Req=(ListView)findViewById(R.id.listView_requirement) ;

        listView_Req.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                String mUserid = LoginData.getFromPrefs(Per_Req_Activity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
                if (mUserid!=null){
                    Intent intent = new Intent(Per_Req_Activity.this,ReqDetailActivity.class).putExtra("req_id",prodList.get(pos).get("req_id"));
                    intent.putExtra("title",prodList.get(pos).get("title"));
                    intent.putExtra("ideal_price",prodList.get(pos).get("ideal_price"));
                    intent.putExtra("description",prodList.get(pos).get("description"));
                    intent.putExtra("author_id",selfID);
                    intent.putExtra("author",selfname);
                    intent.putExtra("type",prodList.get(pos).get("type"));
                    startActivity(intent);
                } else{
                    Intent loginIntent = new Intent(Per_Req_Activity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }

//                String adDetail = adapter.getItem(pos);

            }
        });


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id3);
        Toast.makeText(getApplicationContext(),
                user_id3,
                Toast.LENGTH_SHORT).show();
        prodList.clear();
        reqReq = new CustomRequest(Request.Method.POST,getreqUrl,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        Toast.makeText(getApplicationContext(),
                                "需求信息获取成功",
                                Toast.LENGTH_SHORT).show();
                        JSONArray ja = response.getJSONArray("posts");
                        handleJSONArray(ja);
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
        MySingleton.getInstance(Per_Req_Activity.this).addToRequestQueue(reqReq);
        Toast.makeText(getApplicationContext(),
                "!!!",
                Toast.LENGTH_SHORT).show();
//                searchView.setQuery("", false);
        //searchView.clearFocus();
    }

    public void handleJSONArray(JSONArray ja){
        try {
            prodList.clear();
            for (int i=0; i<ja.length(); ++i) {
                JSONObject jsonpost = ja.getJSONObject(i);
                String productID = jsonpost.getString("req_id");
                String prodName = jsonpost.getString("title");
                String prodPrice = jsonpost.getString("ideal_price");
                String prodDetail = jsonpost.getString("description");
                //String prodType = jsonpost.getString("type");
                String productAuthor = jsonpost.getString("author_id");
                //String prodisBought = jsonpost.getString("isBought");
                HashMap<String,String> prod = new HashMap<String, String>();
                prod.put("req_id", productID);
                prod.put("title", prodName);
                prod.put("ideal_price", prodPrice);
                prod.put("description", prodDetail);
                //prod.put("type", prodType);
                prod.put("author_id", productAuthor);
                //prod.put("isBought", prodisBought);
                prodList.add(prod);
            }
            String [] from = {"title","ideal_price","description"};
            int[] to = {R.id.pd_title, R.id.pd_price, R.id.pd_detail};
            //assert(getActivity()!=null);
            adapter = new SimpleAdapter(this,prodList, R.layout.list_item_adsnew,from,to);
            listView_Req.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
