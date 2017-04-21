package com.example.owenzx.jy_zx_1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String user_id;

    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button change_info_bt = (Button) view.findViewById(R.id.change_info_bt);
        Button sale_info_bt = (Button) view.findViewById(R.id.sale_info_bt);
        Button req_info_bt = (Button) view.findViewById(R.id.req_info_bt);
        Button trading_info_bt = (Button) view.findViewById(R.id.trading_info_bt);
        Button logout_bt = (Button) view.findViewById(R.id.button_logout);

//        final String user_id=intent_id.getStringExtra("userid");
        user_id = LoginData.getFromPrefs(getActivity(),LoginData.PREFS_LOGIN_USERID_KEY,null);
        final String selfname=LoginData.getFromPrefs(getActivity(),LoginData.PREFS_LOGIN_USERNAME_KEY,null);
        TextView text=(TextView)view.findViewById(R.id.textView36);
        text.setText(selfname);
        change_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent11=new Intent(Info_Activity.this,Per_info_Activity.class);
                //Intent intent_id=getIntent();
                //String user_id=intent_id.getStringExtra("userid");
                Intent intent_id1=new Intent();
                intent_id1.setClass(getActivity(),Per_info_Activity.class);
                intent_id1.putExtra("userid",user_id);
                startActivity(intent_id1);
                //startActivity(intent11);
            }
        });
        sale_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent12=new Intent(Info_Activity.this,Per_Sale_Activity.class);
                Intent intent_id2=new Intent();
                intent_id2.setClass(getActivity(),Per_Sale_Activity.class);
                intent_id2.putExtra("userid",user_id);
                startActivity(intent_id2);
                //startActivity(intent12);
            }
        });
        req_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent_id=getIntent();
                //String user_id=intent_id.getStringExtra("userid");
                //Intent intent13=new Intent(Info_Activity.this,Per_Req_Activity.class);
                //startActivity(intent13);
                Intent intent_id3=new Intent();
                intent_id3.setClass(getActivity(),Per_Req_Activity.class);
                intent_id3.putExtra("userid",user_id);
                startActivity(intent_id3);
            }
        });
        trading_info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent_id=getIntent();
                //String user_id=intent_id.getStringExtra("userid");
                Intent intent_id4=new Intent();
                intent_id4.setClass(getActivity(),Per_Buy_Activity.class);
                intent_id4.putExtra("userid",user_id);
                startActivity(intent_id4);
                //Intent intent14=new Intent(Info_Activity.this,Per_Buy_Activity.class);
                //startActivity(intent14);
            }
        });

        logout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginData.saveToPrefs(getActivity(),LoginData.PREFS_LOGIN_USERID_KEY,null);
                LoginData.saveToPrefs(getActivity(),LoginData.PREFS_LOGIN_USERNAME_KEY,null);
                LoginData.saveToPrefs(getActivity(),LoginData.PREFS_LOGIN_PASSWORD_KEY,null);
                Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
        user_id = LoginData.getFromPrefs(getActivity(),LoginData.PREFS_LOGIN_USERID_KEY,null);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
