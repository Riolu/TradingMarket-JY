package com.example.owenzx.jy_zx_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class AdFormActivity extends AppCompatActivity implements View.OnClickListener{

    String addAdUrl = "http://lizunks.xicp.io:34789/trade_test/add_product_test.php";
    EditText prod_name, prod_price, prod_detail;
    Spinner prod_type_sp;
    private TextView messageText;
    private Button  btnselectpic;
    private ImageView imageview;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_form);
        Intent intent = getIntent();
        String formMode = intent.getStringExtra("mode");
        prod_name = (EditText) findViewById(R.id.prodName);
        prod_price = (EditText) findViewById(R.id.prodPrice);
        prod_detail = (EditText) findViewById(R.id.prodDetail);
//        prod_type = (EditText) findViewById(R.id.prodType);
        prod_type_sp = (Spinner) findViewById(R.id.prodTypeSpinner);
//        uploadButton = (Button)findViewById(R.id.uploadButton);
        btnselectpic = (Button)findViewById(R.id.button_selectpic);
        messageText  = (TextView)findViewById(R.id.messageText);
        imageview = (ImageView)findViewById(R.id.imageView_pic);

        btnselectpic.setOnClickListener(this);
//        uploadButton.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        jsonObject = new JSONObject();

        Button buttonAddAd = (Button) findViewById(R.id.buttonAddAd);
        assert buttonAddAd != null;
//        buttonAddAd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addNewAd(v);
//            }
//        });
        buttonAddAd.setOnClickListener(this);
        if (formMode.equals("EDIT")) {
//            addAdUrl = "http://lizunks.xicp.io:34789/trade_test/update_product.php";
            prod_name.setText(intent.getStringExtra("name"));
            prod_price.setText(intent.getStringExtra("price"));
            prod_detail.setText(intent.getStringExtra("detail"));
            ImageLoader mImageLoader = MySingleton.getInstance(AdFormActivity.this).getImageLoader();;
            mImageLoader.get(intent.getStringExtra("image_url"), ImageLoader.getImageListener(imageview,
                    R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery));
//            prod_type.setText(intent.getStringExtra("type"));
            switch (intent.getStringExtra("type")) {
                case "书籍":
                    prod_type_sp.setSelection(0, true);

                    break;
                case "生活用品":
                    prod_type_sp.setSelection(1, true);

                    break;
                case "娱乐用品":
                    prod_type_sp.setSelection(2, true);

                    break;
                case "体育用品":
                    prod_type_sp.setSelection(3, true);

                    break;
                default:
                    prod_type_sp.setSelection(4, true);


            }
            buttonAddAd.setText("保存修改");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_selectpic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, Utils.REQCODE);
                break;
            case R.id.buttonAddAd:
                final String prod_name_str = prod_name.getText().toString();
                final String prod_price_str = prod_price.getText().toString();
                final String prod_detail_str = prod_detail.getText().toString();
//        final String prod_type_str = prod_type.getText().toString();
                final String prod_type_str = prod_type_sp.getSelectedItem().toString();
                Bitmap image = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                dialog.show();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                try {
//                    jsonObject.put(Utils.imageName, etxtUpload.getText().toString().trim());
//                    Log.e("Image name", etxtUpload.getText().toString().trim());
                    jsonObject.put(Utils.image, encodedImage);
                    jsonObject.put("name", prod_name_str);
                    String author_id = LoginData.getFromPrefs(AdFormActivity.this, LoginData.PREFS_LOGIN_USERID_KEY, null);
                    String author_name = LoginData.getFromPrefs(AdFormActivity.this, LoginData.PREFS_LOGIN_USERNAME_KEY, null);
                    jsonObject.put("author", author_name);
                    jsonObject.put("author_id", author_id);
                    jsonObject.put("price", prod_price_str);
                    jsonObject.put("detail", prod_detail_str);
                    jsonObject.put("type", prod_type_str);
                } catch (JSONException e) {
                    Log.e("JSONObject Here", e.toString());
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, addAdUrl, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                prod_name.setText("");
                                prod_price.setText("");
                                prod_detail.setText("");
                                Log.e("Message from server", jsonObject.toString());
                                dialog.dismiss();
                                messageText.setText("Image Uploaded Successfully");
                                Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Message from server", volleyError.toString());
                        dialog.dismiss();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(this).add(jsonObjectRequest);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.REQCODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Bundle extras = data.getExtras();
            Bitmap photo = extras.getParcelable("data");
            imageview.setImageBitmap(photo);
//            imageview.setImageURI(selectedImageUri);
        }
    }



}
