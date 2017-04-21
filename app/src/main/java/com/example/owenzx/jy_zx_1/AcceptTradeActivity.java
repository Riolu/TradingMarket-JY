

package com.example.owenzx.jy_zx_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AcceptTradeActivity extends AppCompatActivity {

    String userIDtoInfo = "http://lizunks.xicp.io:34789/trade_test/user_id_to_info.php";
    String PdIdToInfo = "http://lizunks.xicp.io:34789/trade_test/pd_id_to_info.php";
    String ReqIdToInfo = "http://lizunks.xicp.io:34789/trade_test/req_id_to_info.php";
    String acceptDeal = "http://lizunks.xicp.io:34789/trade_test/accept_deal.php";
    String rejectDeal = "http://lizunks.xicp.io:34789/trade_test/delete_deal.php";

    String either_id;
    String pd_or_req_id;
    String deal_type;

    String userName;
    String Address;
    String Email;
    String Truename;
    String Phonenum;
    String QQ;

    String Name;
    String Price;
    String Detail;
    String AuthorID;
    String Author;
    String ImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_trade);

        Intent intent= getIntent();
        String data = intent.getStringExtra("largeInfo");
        String [] datas = data.split("!");
        either_id = datas[0];
        pd_or_req_id = datas[1];
        deal_type = datas[2];

        //根据userID查询得到详细信息
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", either_id);
        CustomRequest adsreq = new CustomRequest(Request.Method.POST, userIDtoInfo,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        JSONObject jsonpost = ja.getJSONObject(0);
                        userName = jsonpost.getString("name");
                        Address = jsonpost.getString("address");
                        Email = jsonpost.getString("email");
                        Truename = jsonpost.getString("true_name");
                        Phonenum = jsonpost.getString("phone_number");
                        QQ = jsonpost.getString("qq");

                        ((TextView) findViewById(R.id.name)).setText(userName+"的详细情报");
                        ((TextView) findViewById(R.id.true_name)).setText(Truename);
                        ((TextView) findViewById(R.id.phone)).setText(Phonenum);
                        ((TextView) findViewById(R.id.email)).setText(Email);
                        ((TextView) findViewById(R.id.qq)).setText(QQ);
                        ((TextView) findViewById(R.id.address)).setText(Address);
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
        MySingleton.getInstance(AcceptTradeActivity.this).addToRequestQueue(adsreq);





        Button checkButton = (Button) findViewById(R.id.check_button);
        assert checkButton != null;
        checkButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getApplicationContext(), pd_or_req_id, Toast.LENGTH_SHORT).show();
                if (deal_type.equals("0")) {
                    getPdInfo(pd_or_req_id);
                }else {
                    getReqInfo(pd_or_req_id);
                }
            }
        });


        CardView accept = (CardView) findViewById(R.id.accept_card);
        assert accept != null;
        accept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(AcceptTradeActivity.this);
                builder.setIcon(R.drawable.ic_menu_gallery);
                builder.setTitle("Warning");
                builder.setMessage("确定交易吗？(此操作不可逆)");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pd_or_req_id", pd_or_req_id);
                        params.put("deal_type", deal_type);
                        CustomRequest adsreq = new CustomRequest(Request.Method.POST, acceptDeal,params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "确认交易已成功！您可以在个人信息中查看已交易信息", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        MySingleton.getInstance(AcceptTradeActivity.this).addToRequestQueue(adsreq);
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Toast.makeText(AdDetailActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });


        CardView reject = (CardView) findViewById(R.id.reject_card);
        assert reject != null;
        reject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(AcceptTradeActivity.this);
                builder.setIcon(R.drawable.ic_menu_gallery);
                builder.setTitle("Warning");
                builder.setMessage("取消交易吗？(此操作不可逆)");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pd_or_req_id", pd_or_req_id);
                        params.put("deal_type", deal_type);
                        CustomRequest adsreq = new CustomRequest(Request.Method.POST, rejectDeal,params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "确认交易已成功！您可以在个人信息中查看已交易信息", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        MySingleton.getInstance(AcceptTradeActivity.this).addToRequestQueue(adsreq);
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Toast.makeText(AdDetailActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });
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
                            ImgUrl = jsonpost.getString("image_url");
                        }
                        Intent intent = new Intent(AcceptTradeActivity.this, AdDetailActivity.class);
                        intent.putExtra("pd_id", pd_or_req_id);
                        intent.putExtra("name", Name);
                        intent.putExtra("price", Price);
                        intent.putExtra("detail", Detail);
                        intent.putExtra("author_id", AuthorID);
                        intent.putExtra("author", Author);
                        intent.putExtra("image_url",ImgUrl);
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
        MySingleton.getInstance(AcceptTradeActivity.this).addToRequestQueue(pdreq);
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
                        Intent intent = new Intent(AcceptTradeActivity.this, ReqDetailActivity.class);
                        intent.putExtra("req_id", pd_or_req_id);
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
        MySingleton.getInstance(AcceptTradeActivity.this).addToRequestQueue(pdreq);
    }
}


















