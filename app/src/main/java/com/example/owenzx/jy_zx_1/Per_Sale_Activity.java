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

public class Per_Sale_Activity extends AppCompatActivity {
    final String getsaleUrl="http://lizunks.xicp.io:34789/trade_test/product_query.php";
    private ListView listView_Sale;
    private CustomRequest adsreq;
    private Activity act;
    private ListAdapter adapter;
    final ArrayList<HashMap<String,String>> prodList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per__sale_);
        Intent intent_id2=getIntent();
        String user_id2=intent_id2.getStringExtra("userid");
        final String selfID = LoginData.getFromPrefs(Per_Sale_Activity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
        final String selfname=LoginData.getFromPrefs(Per_Sale_Activity.this,LoginData.PREFS_LOGIN_USERNAME_KEY,null);
        listView_Sale=(ListView)findViewById(R.id.listView) ;
        listView_Sale.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                String mUserid = LoginData.getFromPrefs(Per_Sale_Activity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
                if (mUserid!=null){
                    Intent intent = new Intent(Per_Sale_Activity.this,AdDetailActivity.class).putExtra("pd_id",prodList.get(pos).get("pd_id"));
                    intent.putExtra("name",prodList.get(pos).get("name"));
                    intent.putExtra("price",prodList.get(pos).get("price"));
                    intent.putExtra("detail",prodList.get(pos).get("detail"));
                    intent.putExtra("author_id",selfID);
                    intent.putExtra("author",selfname);
                    intent.putExtra("type",prodList.get(pos).get("type"));
                    startActivity(intent);
                } else{
                    Intent loginIntent = new Intent(Per_Sale_Activity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }

//                String adDetail = adapter.getItem(pos);

            }
        });


        Map<String, String> params = new HashMap<String, String>();
        params.put("author_id", user_id2);

        prodList.clear();
        CustomRequest adsreq = new CustomRequest(Request.Method.POST,getsaleUrl,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        Toast.makeText(getApplicationContext(),
                                "出售信息获取成功",
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
        MySingleton.getInstance(Per_Sale_Activity.this).addToRequestQueue(adsreq);
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
                String productID = jsonpost.getString("pd_id");
                String prodName = jsonpost.getString("name");
                String prodPrice = jsonpost.getString("price");
                String prodDetail = jsonpost.getString("detail");
                String prodType = jsonpost.getString("type");
                String productAuthor = jsonpost.getString("author_id");
                HashMap<String,String> prod = new HashMap<String, String>();
                prod.put("pd_id", productID);
                prod.put("name", prodName);
                prod.put("price", prodPrice);
                prod.put("detail", prodDetail);
                prod.put("type", prodType);
                prod.put("author_id", productAuthor);
                prodList.add(prod);
            }
            String [] from = {"name","price","detail"};
            int[] to = {R.id.pd_title, R.id.pd_price, R.id.pd_detail};
            //assert(getActivity()!=null);
            adapter = new SimpleAdapter(this,prodList, R.layout.list_item_adsnew,from,to);
            listView_Sale.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
