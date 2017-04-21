package com.example.owenzx.jy_zx_1;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdsFragment extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    final String adsMainUrl = "http://lizunks.xicp.io:34789/trade_test/all_product_on.php";
    final String getTypeUrl = "http://lizunks.xicp.io:34789/trade_test/search_product_type.php";
    final String searchAdUrl = "http://lizunks.xicp.io:34789/trade_test/search_product_name.php";
    final ArrayList<HashMap<String,String>> prodList = new ArrayList<HashMap<String, String>>();
    private ListAdapter adapter;
    private ListView listView_ad;
    private RecyclerView mRecyclerView;
    private PdAdapter mAdapter;
    private CustomRequest adsreq;
    private Activity act;

    public AdsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdsFragment newInstance(String param1, String param2) {
        AdsFragment fragment = new AdsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        act = getActivity();
        adsreq = new CustomRequest(Request.Method.GET, adsMainUrl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        handleJSONArray(ja);

                    }else{
                        prodList.clear();
                        String [] from = {"name","price","detail"};
                        int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                        assert(getActivity()!=null);
                        adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
                        listView_ad.setAdapter(adapter);
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
//        MySingleton.getInstance(getActivity()).addToRequestQueue(adsreq);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ads,container,false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mUserid = LoginData.getFromPrefs(getActivity(),LoginData.PREFS_LOGIN_USERID_KEY,null);
                if (mUserid!=null){
                    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    Intent createAdForm = new Intent(view.getContext(), AdFormActivity.class);
                    createAdForm.putExtra("mode","NEW");
                    startActivity(createAdForm);
//                    Intent testInt = new Intent(view.getContext(),UploadImageActivity.class);
//                    startActivity(testInt);
                }else{
                    Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(loginIntent);
                }

            }
        });
        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Inflate the layout for this fragment
//        while(prodList.isEmpty()){
//            int i =1;
//        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.pd_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(act));
        mRecyclerView.setHasFixedSize(true);


//        listView_ad = (ListView) view.findViewById(R.id.listview_ad);
//        listView_ad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//
//                String mUserid = LoginData.getFromPrefs(getActivity(),LoginData.PREFS_LOGIN_USERID_KEY,null);
//                if (mUserid!=null){
//                    Intent intent = new Intent(getActivity(),AdDetailActivity.class).putExtra("pd_id",prodList.get(pos).get("pd_id"));
//                    intent.putExtra("name",prodList.get(pos).get("name"));
//                    intent.putExtra("price",prodList.get(pos).get("price"));
//                    intent.putExtra("detail",prodList.get(pos).get("detail"));
//                    intent.putExtra("author_id",prodList.get(pos).get("author_id"));
//                    intent.putExtra("author",prodList.get(pos).get("author"));
//                    intent.putExtra("type",prodList.get(pos).get("type"));
//                    intent.putExtra("image_url",prodList.get(pos).get("image_url"));
//                    startActivity(intent);
//                } else{
//                    Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
//                    startActivity(loginIntent);
//                }
//
////                String adDetail = adapter.getItem(pos);
//
//            }
//        });
        assert(adsreq!=null);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(adsreq);


//        String [] from = {"name","price","detail"};
//        int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
//        adapter = new SimpleAdapter(getActivity(),prodList,R.layout.list_item_adsnew,from,to);
//        listView_ad.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onStart(){
        super.onStart();
        refreshcontent();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.books) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "书籍");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            prodList.clear();
                            String [] from = {"name","price","detail"};
                            int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                            assert(getActivity()!=null);
                            adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
                            listView_ad.setAdapter(adapter);
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(adsreq);
//
//            Toast.makeText(getActivity(),
//                    "!!!",
//                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.living) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "生活用品");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            prodList.clear();
                            String [] from = {"name","price","detail"};
                            int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                            assert(getActivity()!=null);
                            adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
                            listView_ad.setAdapter(adapter);
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(adsreq);

        } else if (id == R.id.entertain) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "娱乐用品");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            prodList.clear();
                            String [] from = {"name","price","detail"};
                            int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                            assert(getActivity()!=null);
                            adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
                            listView_ad.setAdapter(adapter);
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(adsreq);
        } else if (id == R.id.sports) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "体育用品");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            prodList.clear();
                            String [] from = {"name","price","detail"};
                            int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                            assert(getActivity()!=null);
                            adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
                            listView_ad.setAdapter(adapter);
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(adsreq);
        } else if (id == R.id.others) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "其它");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else {
                            prodList.clear();
                            String [] from = {"name","price","detail"};
                            int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                            assert(getActivity()!=null);
                            adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
                            listView_ad.setAdapter(adapter);
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(adsreq);
        }

        DrawerLayout drawer = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ads,menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search the products");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", query);

                prodList.clear();
                CustomRequest adsreq = new CustomRequest(Request.Method.POST,searchAdUrl,params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success == 1){
                                JSONArray ja = response.getJSONArray("posts");
                                handleJSONArray(ja);
                            }else{
                                prodList.clear();
                                String [] from = {"name","price","detail"};
                                int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                                assert(getActivity()!=null);
                                adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
                                listView_ad.setAdapter(adapter);
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
                MySingleton.getInstance(getActivity()).addToRequestQueue(adsreq);
                Toast.makeText(getActivity(),
                        "!!!",
                        Toast.LENGTH_SHORT).show();
//                searchView.setQuery("", false);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
//        searchManager.getSearchableInfo(getActivity().getComponentName());
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void refreshcontent(){
        adsreq = new CustomRequest(Request.Method.GET, adsMainUrl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        handleJSONArray(ja);
                    }else {
                        prodList.clear();
                        String [] from = {"name","price","detail"};
                        int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
                        assert(getActivity()!=null);
                        adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
                        listView_ad.setAdapter(adapter);
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(adsreq);

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                String author = jsonpost.getString("author");
                String imageUrl = jsonpost.getString("image_url");
                HashMap<String,String> prod = new HashMap<String, String>();
                prod.put("pd_id", productID);
                prod.put("name", prodName);
                prod.put("price", prodPrice);
                prod.put("detail", prodDetail);
                prod.put("type", prodType);
                prod.put("author_id", productAuthor);
                prod.put("author",author);
                prod.put("image_url",imageUrl);
                prodList.add(prod);
            }
            mAdapter = new PdAdapter(prodList,getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new PdAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, HashMap<String,String> tag) {
                    //Toast.makeText(CommentMessageActivity.this,data,Toast.LENGTH_SHORT).show();
                    String mUserid = LoginData.getFromPrefs(getActivity(),LoginData.PREFS_LOGIN_USERID_KEY,null);
                    if (mUserid!=null){
                        Intent intent = new Intent(getActivity(),AdDetailActivity.class);
                        intent.putExtra("pd_id",tag.get("pd_id"));
                        intent.putExtra("name",tag.get("name"));
                        intent.putExtra("price",tag.get("price"));
                        intent.putExtra("detail",tag.get("detail"));
                        intent.putExtra("author_id",tag.get("author_id"));
                        intent.putExtra("author",tag.get("author"));
                        intent.putExtra("type",tag.get("type"));
                        intent.putExtra("image_url",tag.get("image_url"));
                        startActivity(intent);
                    } else{
                        Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(loginIntent);
                    }
                }
            });
//            String [] from = {"name","price","detail"};
//            int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
//            assert(getActivity()!=null);
////            adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
//            adapter = new MySimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
//            listView_ad.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
//            String [] from = {"name","price","detail"};
//            int[] to = {R.id.pd_title,R.id.pd_price,R.id.pd_detail};
//            assert(getActivity()!=null);
//            adapter = new SimpleAdapter(act,prodList,R.layout.list_item_adsnew,from,to);
//            listView_ad.setAdapter(adapter);
        }

    }

}