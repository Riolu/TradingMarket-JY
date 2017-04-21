package com.example.owenzx.jy_zx_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class Per_info_Activity extends AppCompatActivity {

    final String getinfoUrl="http://lizunks.xicp.io:34789/trade_test/user_id_to_info.php";
    private ListView listView_Info;
    private CustomRequest adsreq;
    private Activity act;
    private ListAdapter adapter;
    final ArrayList<HashMap<String,String>> prodList = new ArrayList<HashMap<String, String>>();
    String address;
    String email;
    String true_name;
    String phone_number;
    String qq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_info_);
        //new_username = (EditText)findViewById(R.id.editText);
        //new_password = (EditText)findViewById(R.id.editText3);
        //Button save1_bt = (Button) findViewById(R.id.save1_bt);
        //Button save_bt2 = (Button) findViewById(R.id.save_bt2);
        Button button_editInfo = (Button) findViewById(R.id.button_edit_psinfo);
        assert button_editInfo != null;
        button_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(Per_info_Activity.this,UpdateUserActivity.class);
                editIntent.putExtra("mode","EDIT");
                editIntent.putExtra("address",address);
                editIntent.putExtra("email",email);
                editIntent.putExtra("true_name",true_name);
                editIntent.putExtra("phone_number",phone_number);
                editIntent.putExtra("qq",qq);
                startActivity(editIntent);
            }
        });
        Intent intent_id1=getIntent();
        String user_id1=LoginData.getFromPrefs(Per_info_Activity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
        listView_Info=(ListView)findViewById(R.id.listView2) ;
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id1);
        Toast.makeText(getApplicationContext(),
                user_id1,
                Toast.LENGTH_SHORT).show();
        prodList.clear();
        CustomRequest adsreq = new CustomRequest(Request.Method.POST,getinfoUrl,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        Toast.makeText(getApplicationContext(),
                                "个人信息获取成功",
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
        MySingleton.getInstance(Per_info_Activity.this).addToRequestQueue(adsreq);
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
                String productID = jsonpost.getString("user_id");
                String prodName = jsonpost.getString("name");
                email = jsonpost.getString("email");
                true_name = jsonpost.getString("true_name");
                phone_number = jsonpost.getString("phone_number");
                qq = jsonpost.getString("qq");
                address = jsonpost.getString("address");




                HashMap<String,String> prod = new HashMap<String, String>();
                prod.put("user_id", productID);
                prod.put("name", prodName);
                prod.put("email", email);
                prod.put("true_name", true_name);
                prod.put("phone_number", phone_number);
                prod.put("qq", qq);
                prod.put("address", address);
                //prod.put("description", prodDetail);
                //prod.put("author_id", productAuthor);
                //prod.put("isBought", prodisBought);
                prodList.add(prod);
            }
            String [] from = {"user_id","name","email","qq","phone_number","true_name","address"};
            int[] to = {R.id.textView8, R.id.textView10, R.id.textView12, R.id.textView20, R.id.textView22, R.id.textView24, R.id.textView27};
            //assert(getActivity()!=null);
            adapter = new SimpleAdapter(this,prodList, R.layout.per_info,from,to);
            listView_Info.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

