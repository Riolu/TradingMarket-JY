package com.example.owenzx.jy_zx_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DealMessageActivity extends AppCompatActivity {

    private String getDealUrl ="http://lizunks.xicp.io:34789/trade_test/author_id_to_deal.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_message);
        String userid = LoginData.getFromPrefs(DealMessageActivity.this,LoginData.PREFS_LOGIN_USERID_KEY,null);
        Map<String, String> params = new HashMap<String, String>();
        params.put("author_id", userid);
        CustomRequest adsreq = new CustomRequest(Request.Method.POST, getDealUrl,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        handleTopJSONArray(ja);

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
//            MySingleton.getInstance().getRequestQueue().;
        MySingleton.getInstance(DealMessageActivity.this).addToRequestQueue(adsreq);
    }




    public void handleTopJSONArray(JSONArray ja){

    }
}
