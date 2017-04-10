package com.example.owenzx.jy_zx_1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText mRealnameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mUsernameView;
    private EditText mTelephoneView;
    private EditText mQQView;
    private EditText mAddressView;
    private UserRegisterTask mAuthTask1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRealnameView=(EditText) findViewById(R.id.editText4);
        mEmailView= (EditText) findViewById(R.id.editText5);
        mPasswordView=(EditText) findViewById(R.id.editText3);
        mUsernameView=(EditText) findViewById(R.id.editText);
        mTelephoneView=(EditText) findViewById(R.id.editText7);
        mQQView=(EditText) findViewById(R.id.editText6);
        mAddressView=(EditText) findViewById(R.id.editText8);
        Button registerButton = (Button) findViewById(R.id.button1);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {
        if (mAuthTask1 != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mRealnameView.setError(null);
        mUsernameView.setError(null);
        mTelephoneView.setError(null);
        mQQView.setError(null);
        mAddressView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String realname = mRealnameView.getText().toString();
        String username = mUsernameView.getText().toString();
        String telephone = mTelephoneView.getText().toString();
        String QQ = mQQView.getText().toString();
        String address = mAddressView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
        mEmailView.setError(getString(R.string.error_field_required));
        focusView = mEmailView;
        cancel = true;
        } else if (!isEmailValid(email)) {
        mEmailView.setError(getString(R.string.error_invalid_email));
        focusView = mEmailView;
        cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            mAuthTask1 = new UserRegisterTask(email, password,QQ,telephone,username,realname,address);
            mAuthTask1.execute((Void) null);
        }
    }
    private boolean isEmailValid(String email) {

     return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mQQ;
        private final String mUsername;
        private final String mTelephone;
        private final String mRealname;
        private final String mAddress;
        UserRegisterTask(String email, String password, String QQ, String telephone, String username, String realname,String address) {
            mEmail = email;
            mPassword = password;
            mQQ = QQ;
            mUsername = username;
            mTelephone = telephone;
            mRealname = realname;
            mAddress=address;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            String addAdUrl = "http://lizunks.xicp.io:34789/trade_test/register.php";

            //for (String credential : DUMMY_CREDENTIALS) {
            //String[] pieces = credential.split(":");
            //if (pieces[0].equals( mUser_id)) {
            // Account exists, return true if the password matches.
            //return pieces[1].equals(mPassword);
            //}
            //}

            StringRequest postRequest = new StringRequest(Request.Method.POST,addAdUrl,new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    mEmailView.setText("");
                    mPasswordView.setText("");
                    mQQView.setText("");
                    mAddressView.setText("");
                    mRealnameView.setText("");
                    mTelephoneView.setText("");
                    mUsernameView.setText("");
                    Toast.makeText(getApplicationContext(),
                            "Register Successfully",
                            Toast.LENGTH_SHORT).show();
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){

                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("name", mUsername);
                    params.put("password",mPassword);
                    params.put("qq", mQQ);
                    params.put("phone_number",mTelephone);
                    params.put("true_name", mRealname);
                    params.put("email",mEmail);
                    params.put("address",mAddress);
                    return params;
                }};//{
            //@Override
            //protected Map<String,String> getParams(){
            // Map<String,String> params = new HashMap<String,String>();
            //params.put(mUser_id, mPassword);
            //return params;
            //}
            //}

            MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(postRequest);
            // TODO: register the new account here.
            return true;
        }


    }}