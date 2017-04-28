package com.example.owenzx.jy_zx_1;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReqFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReqFragment extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    final String reqsMainUrl = "http://lizunks.xicp.io:34789/trade_test/all_req_on.php";
    final String getTypeReqUrl = "http://lizunks.xicp.io:34789/trade_test/search_req_type.php";
    final String searchReqUrl = "http://lizunks.xicp.io:34789/trade_test/search_req_title.php";
    final ArrayList<HashMap<String,String>> reqList = new ArrayList<HashMap<String, String>>();
    private ListAdapter adapter;
    private ListView listView_req;
    private RecyclerView mRecyclerView;
    private ReqAdapter mAdapter;
    private CustomRequest reqReq;
    private Activity act;


    public ReqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReqFragment newInstance(String param1, String param2) {
        ReqFragment fragment = new ReqFragment();
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
        reqReq = new CustomRequest(Request.Method.GET, reqsMainUrl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        handleJSONArray(ja);
                    }else{
                        reqList.clear();
                        String[] from = {"title", "ideal_price", "description"};
                        int[] to = {R.id.req_title, R.id.req_budget, R.id.req_detail};
                        assert (getActivity() != null);
                        adapter = new SimpleAdapter(act, reqList, R.layout.list_item_reqsnew, from, to);
                        listView_req.setAdapter(adapter);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_req,container,false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_reqs);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_reqs);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mUserid = LoginData.getFromPrefs(getActivity(),LoginData.PREFS_LOGIN_USERID_KEY,null);
                if (mUserid!=null){
                    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    Intent createReqForm = new Intent(view.getContext(), ReqFormActivity.class);
                    createReqForm.putExtra("mode","NEW");

                    startActivity(createReqForm);
                }else{
                    Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(loginIntent);
                }

            }
        });
        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout_reqs);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view_reqs);
        navigationView.setNavigationItemSelectedListener(this);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.req_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(act));
        mRecyclerView.setHasFixedSize(true);

        assert(reqReq!=null);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(reqReq);


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

        if (id == R.id.booksReq) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "书籍");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeReqUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            reqList.clear();
                            String[] from = {"title", "ideal_price", "description"};
                            int[] to = {R.id.req_title, R.id.req_budget, R.id.req_detail};
                            assert (getActivity() != null);
                            adapter = new SimpleAdapter(act, reqList, R.layout.list_item_reqsnew, from, to);
                            listView_req.setAdapter(adapter);
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
        } else if (id == R.id.livingReq) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "生活用品");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeReqUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            reqList.clear();
                            String[] from = {"title", "ideal_price", "description"};
                            int[] to = {R.id.req_title, R.id.req_budget, R.id.req_detail};
                            assert (getActivity() != null);
                            adapter = new SimpleAdapter(act, reqList, R.layout.list_item_reqsnew, from, to);
                            listView_req.setAdapter(adapter);
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

        } else if (id == R.id.entertainReq) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "娱乐用品");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeReqUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            reqList.clear();
                            String[] from = {"title", "ideal_price", "description"};
                            int[] to = {R.id.req_title, R.id.req_budget, R.id.req_detail};
                            assert (getActivity() != null);
                            adapter = new SimpleAdapter(act, reqList, R.layout.list_item_reqsnew, from, to);
                            listView_req.setAdapter(adapter);
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
        } else if (id == R.id.sportsReq) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "体育用品");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeReqUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            reqList.clear();
                            String[] from = {"title", "ideal_price", "description"};
                            int[] to = {R.id.req_title, R.id.req_budget, R.id.req_detail};
                            assert (getActivity() != null);
                            adapter = new SimpleAdapter(act, reqList, R.layout.list_item_reqsnew, from, to);
                            listView_req.setAdapter(adapter);
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
        } else if (id == R.id.othersReq) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "其它");
            CustomRequest adsreq = new CustomRequest(Request.Method.POST, getTypeReqUrl,params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        if (success == 1){
                            JSONArray ja = response.getJSONArray("posts");
                            handleJSONArray(ja);
                        }else{
                            reqList.clear();
                            String[] from = {"title", "ideal_price", "description"};
                            int[] to = {R.id.req_title, R.id.req_budget, R.id.req_detail};
                            assert (getActivity() != null);
                            adapter = new SimpleAdapter(act, reqList, R.layout.list_item_reqsnew, from, to);
                            listView_req.setAdapter(adapter);
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

        DrawerLayout drawer = (DrawerLayout) getView().findViewById(R.id.drawer_layout_reqs);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.reqs,menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search_req).getActionView();
        searchView.setQueryHint("Search the requirements");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", query);

//                reqList.clear();
                CustomRequest adsreq = new CustomRequest(Request.Method.POST,searchReqUrl,params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success == 1){
                                JSONArray ja = response.getJSONArray("posts");
                                handleJSONArray(ja);
                            }else{
                                reqList.clear();
                                String[] from = {"title", "ideal_price", "description"};
                                int[] to = {R.id.req_title, R.id.req_budget, R.id.req_detail};
                                assert (getActivity() != null);
                                adapter = new SimpleAdapter(act, reqList, R.layout.list_item_reqsnew, from, to);
                                listView_req.setAdapter(adapter);
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
        reqReq = new CustomRequest(Request.Method.GET, reqsMainUrl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1){
                        JSONArray ja = response.getJSONArray("posts");
                        handleJSONArray(ja);
                    }else{
                        reqList.clear();
                        String[] from = {"title", "ideal_price", "description"};
                        int[] to = {R.id.req_title, R.id.req_budget, R.id.req_detail};
                        assert (getActivity() != null);
                        adapter = new SimpleAdapter(act, reqList, R.layout.list_item_reqsnew, from, to);
                        listView_req.setAdapter(adapter);
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(reqReq);

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
            reqList.clear();
            for (int i = 0; i < ja.length(); ++i) {
                JSONObject jsonpost = ja.getJSONObject(i);
                String reqID = jsonpost.getString("req_id");
                String reqTitle = jsonpost.getString("title");
                String reqBudget = jsonpost.getString("ideal_price");
                String reqDetail = jsonpost.getString("description");
//                String reqType = jsonpost.getString("type");
                String authorID = jsonpost.getString("author_id");
                String authorName = jsonpost.getString("author");
                HashMap<String, String> prod = new HashMap<String, String>();
                prod.put("req_id", reqID);
                prod.put("title", reqTitle);
                prod.put("ideal_price", reqBudget);
                prod.put("description", reqDetail);
                prod.put("author_id",authorID);
                prod.put("author",authorName);
//                prod.put("type",reqType);
                reqList.add(prod);
            }

            mAdapter = new ReqAdapter(reqList,act);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new ReqAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, HashMap<String,String> tag) {
                    //Toast.makeText(CommentMessageActivity.this,data,Toast.LENGTH_SHORT).show();
                    String mUserid = LoginData.getFromPrefs(getActivity(),LoginData.PREFS_LOGIN_USERID_KEY,null);
                    if (mUserid!=null){
                        Intent intent = new Intent(getActivity(),ReqDetailActivity.class);
                        intent.putExtra("req_id",tag.get("req_id"));
                        intent.putExtra("title",tag.get("title"));
                        intent.putExtra("ideal_price",tag.get("ideal_price"));
                        intent.putExtra("description",tag.get("description"));
                        intent.putExtra("author_id",tag.get("author_id"));
                        intent.putExtra("author",tag.get("author"));
                        intent.putExtra("type",tag.get("type"));
                        startActivity(intent);
                    }else{
                        Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(loginIntent);
                    }
                }
            });
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}