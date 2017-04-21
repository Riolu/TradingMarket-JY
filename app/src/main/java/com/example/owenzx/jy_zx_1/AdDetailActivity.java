package com.example.owenzx.jy_zx_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdDetailActivity extends AppCompatActivity {

    String getCommentUrl = "http://lizunks.xicp.io:34789/trade_test/comment_query.php";
    String addDealUrl = "http://lizunks.xicp.io:34789/trade_test/add_deal.php";
    String addCommentUrl = "http://lizunks.xicp.io:34789/trade_test/add_comment.php";
    String updateAdUrl = "http://lizunks.xicp.io:34789/trade_test/update_product.php";
    String deleteUrl = "http://lizunks.xicp.io:34789/trade_test/delete_product.php";
    ListView listView_comment;
    public static ArrayList<HashMap<String,String>> CommentList = new ArrayList<HashMap<String, String>>();

    String prodID;   //前面传给我
    String authorID; //前面传给我
    String author;   //前面传给我
    String buyerID; //登陆后得到
    String buyer;
    String receiverID;
    String receiver;
    String senderID; //登陆后得到
    String sender;
//    String prodType;

    String commentType="0";
    String dealType="0";

    CustomRequest adsreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CommentList.clear(); //清空
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail);

        String userid = LoginData.getFromPrefs(AdDetailActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);


        //前一个页面得到的参数
        Intent intent= getIntent();
        prodID = intent.getStringExtra("pd_id");
        final String prodName = intent.getStringExtra("name");
        final String prodPrice = intent.getStringExtra("price");
        final String prodDetail = intent.getStringExtra("detail");
        final String prodType = intent.getStringExtra("type");
        final String prodImageUrl = intent.getStringExtra("image_url");
        authorID = intent.getStringExtra("author_id");
        author = intent.getStringExtra("author");
        ((TextView) findViewById(R.id.ad_name)).setText(prodName);
        ((TextView) findViewById(R.id.author)).setText("发布人："+author);
        ((TextView) findViewById(R.id.ad_price)).setText("¥"+prodPrice);
        ((TextView) findViewById(R.id.ad_detail_Info)).setText(prodDetail);
        buyerID = LoginData.getFromPrefs(AdDetailActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
        senderID = LoginData.getFromPrefs(AdDetailActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
        buyer=LoginData.getFromPrefs(AdDetailActivity.this,LoginData.PREFS_LOGIN_USERNAME_KEY,null);
        sender = LoginData.getFromPrefs(AdDetailActivity.this,LoginData.PREFS_LOGIN_USERNAME_KEY,null);

        listView_comment = (ListView) findViewById(R.id.listView_comment);

        listView_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                receiver = CommentList.get(pos).get("sender");
                receiverID = CommentList.get(pos).get("senderID");
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(AdDetailActivity.this);
                builder.setIcon(R.drawable.ic_menu_gallery);
                builder.setTitle("添加留言");
                final EditText editText = new EditText(AdDetailActivity.this);
                if (receiver.equals("暂无评论")){editText.setHint("给卖家留言");}
                else editText.setHint("回复@"+receiver+":");
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(AdDetailActivity.this, receiver, Toast.LENGTH_SHORT).show();
                        final String comment = editText.getText().toString();
                        StringRequest postRequest = new StringRequest(Request.Method.POST, addCommentUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "评论已添加", Toast.LENGTH_SHORT).show();
                                MySingleton.getInstance(AdDetailActivity.this).addToRequestQueue(adsreq);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("pd_or_req_id", prodID);
                                params.put("comment", comment);
                                params.put("sender_id", senderID);
                                params.put("sender", sender);
                                if (receiver.equals("暂无评论")) {
                                    params.put("receiver_id", authorID);
                                    params.put("receiver", "NULL");
                                } else {
                                    params.put("receiver_id", receiverID);
                                    params.put("receiver", receiver);
                                }
                                params.put("comment_type", commentType);
                                return params;
                            }
                        };
                        //String test=prodID+" "+comment+" "+senderID+" "+sender+" "+receiverID+" "+receiver;
                        //Toast.makeText(AdDetailActivity.this, test, Toast.LENGTH_SHORT).show();
                        MySingleton.getInstance(AdDetailActivity.this).addToRequestQueue(postRequest);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(AdDetailActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NegativeButton
                //    显示出该对话框
                builder.show();
            }
        });


        Button buttonDel = (Button) findViewById(R.id.button_del_prod);
        assert buttonDel!= null;
        Button buttonEdit = (Button) findViewById(R.id.button_edit_ad);
        assert buttonEdit != null;
        if (!userid.equals(authorID)){
            buttonEdit.setVisibility(View.GONE);
            buttonDel.setVisibility(View.GONE);
        }
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(AdDetailActivity.this,AdFormActivity.class);
                editIntent.putExtra("mode","EDIT");
                editIntent.putExtra("name",prodName);
                editIntent.putExtra("price",prodPrice);
                editIntent.putExtra("detail",prodDetail);
                editIntent.putExtra("type",prodType);
                editIntent.putExtra("image_url",prodImageUrl);
                startActivity(editIntent);
            }
        });


        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdDetailActivity.this);
                builder.setIcon(R.drawable.ic_menu_gallery);
                builder.setTitle("警告");
                builder.setMessage("确定删除本商品吗？此操作无法撤销");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Toast.makeText(AdDetailActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                        StringRequest postRequest = new StringRequest(Request.Method.POST,deleteUrl,new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response){
                                Toast.makeText(getApplicationContext(),"商品已成功删除",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){

                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String,String>();

                                params.put("pd_id",prodID);
                                return params;
                            }
                        };
                        MySingleton.getInstance(AdDetailActivity.this).addToRequestQueue(postRequest);
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

        //购买按钮
        Button buttonBuy = (Button) findViewById(R.id.buy_button);
        assert buttonBuy != null;
        buttonBuy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(AdDetailActivity.this);
                builder.setIcon(R.drawable.ic_menu_gallery);
                builder.setTitle("Warning!");
                builder.setMessage("确定购买吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Toast.makeText(AdDetailActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                        StringRequest postRequest = new StringRequest(Request.Method.POST,addDealUrl,new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response){
                                Toast.makeText(getApplicationContext(),"购买请求已发送！",Toast.LENGTH_SHORT).show();
                            }
                        },new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){

                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String,String>();
                                params.put("author_id",authorID);
                                params.put("author",author);
                                params.put("buyer_or_supplier_id",buyerID);
                                params.put("buyer_or_supplier",buyer);
                                params.put("pd_or_req_id",prodID);
                                params.put("deal_type",dealType);
                                return params;
                            }
                        };
                        MySingleton.getInstance(AdDetailActivity.this).addToRequestQueue(postRequest);
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



        //根据prodID查询得到其评论 点击item可留言
        Map<String, String> params = new HashMap<String, String>();
        params.put("pd_or_req_id", prodID);
        params.put("comment_type", commentType);
        adsreq = new CustomRequest(Request.Method.POST, getCommentUrl,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        CommentList.clear();
                        for (int i=0; i<ja.length(); ++i){
                            ((TextView) findViewById(R.id.comment_num)).setText("热门评论("+String.valueOf(ja.length())+")");
                            JSONObject jsonpost = ja.getJSONObject(i);
                            HashMap<String,String> comment_map = new HashMap<String, String>();
                            String sender = jsonpost.getString("sender");
                            String senderID = jsonpost.getString("sender_id");
                            String receiver = jsonpost.getString("receiver");
                            //String receiverID = jsonpost.getString("receiver_id");
                            String comment = jsonpost.getString("comment");
                            String message = comment;
                            if (!receiver.equals("NULL")) message="回复@"+receiver+":"+message;
                            comment_map.put("sender",sender);
                            comment_map.put("senderID",senderID);
                            comment_map.put("message",message);
                            CommentList.add(comment_map);
                        }
                    }else{
                        HashMap<String,String> no_comment = new HashMap<String, String>();
                        no_comment.put("sender","暂无评论");
                        no_comment.put("message","期待您的留言");
                        CommentList.add(no_comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String [] from = {"sender","message"};
                int[] to = {R.id.comment_sender,R.id.comment_message};
                final ListAdapter adapter = new SimpleAdapter(getApplicationContext(),CommentList,R.layout.list_item_comment,from,to);
//                listView_comment = (ListView) findViewById(R.id.listView_comment);
                listView_comment.setAdapter(adapter);

                setListViewHeightBasedOnChildren(listView_comment);
                listView_comment.setFocusable(false);
                ScrollView:((ScrollView)findViewById(R.id.scrollView)).smoothScrollTo(0,20);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(AdDetailActivity.this).addToRequestQueue(adsreq);


        TextView complainText = (TextView) findViewById(R.id.text_complain);
        assert complainText!= null;
        complainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //添加回复（给商品的卖家）
        TextView addComment = (TextView) findViewById(R.id.add_comment);
        assert addComment != null;
        addComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(AdDetailActivity.this);
                builder.setIcon(R.drawable.ic_menu_gallery);
                builder.setTitle("添加留言");
                final EditText editText = new EditText(AdDetailActivity.this);
                editText.setHint("给卖家留言");
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final String comment = editText.getText().toString();
                        //Toast.makeText(AdDetailActivity.this, comment, Toast.LENGTH_SHORT).show();
                        StringRequest postRequest = new StringRequest(Request.Method.POST,addCommentUrl,new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response){
                                Toast.makeText(getApplicationContext(), "评论已添加", Toast.LENGTH_SHORT).show();
                                MySingleton.getInstance(AdDetailActivity.this).addToRequestQueue(adsreq);
                            }
                        },new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){

                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String,String>();
                                params.put("pd_or_req_id",prodID);
                                params.put("comment",comment);
                                params.put("sender_id",senderID);
                                params.put("sender",sender);
                                params.put("receiver_id",authorID);
                                params.put("receiver","NULL");
                                params.put("comment_type",commentType);
                                return params;
                            }
                        };
                        MySingleton.getInstance(AdDetailActivity.this).addToRequestQueue(postRequest);
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
        ImageLoader mImageLoader = MySingleton.getInstance(AdDetailActivity.this).getImageLoader();
        mImageLoader.get(prodImageUrl,ImageLoader.getImageListener(((ImageView) findViewById(R.id.pic)),
                R.drawable.ic_menu_gallery, R.drawable.figure));

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) { //计算listview高度
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *  (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

