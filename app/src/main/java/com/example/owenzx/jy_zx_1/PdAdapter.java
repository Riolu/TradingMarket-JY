package com.example.owenzx.jy_zx_1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DELL-PC on 2016/6/21.
 */

public class PdAdapter extends RecyclerView.Adapter<PdAdapter.ViewHolder>
{
    private ArrayList<HashMap<String,String>> List = null;
    private OnItemClickListener mListener;
    private Context mContext;

    public PdAdapter(ArrayList<HashMap<String,String>> List, Context context)
    {
        this.List = List;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item_pd,viewGroup,false);
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener != null)
                    mListener.onItemClick(v, (HashMap<String,String>) itemView.getTag());
            }

        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        HashMap<String,String> data = List.get(i);
        HashMap<String,String> tag = List.get(i);
        viewHolder.bindData(data,mContext);
        viewHolder.itemView.setTag(tag);
    }

    @Override
    public int getItemCount()
    {
        return List.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv1,tv2,tv3;
        private ImageView iv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.recycle_item_pd_name);
            tv2 = (TextView) itemView.findViewById(R.id.recycle_item_pd_price);
            tv3 = (TextView) itemView.findViewById(R.id.recycle_item_pd_detail);
            iv = (ImageView) itemView.findViewById(R.id.recycle_item_pd_pic);
        }

        public void bindData(HashMap<String,String> m, Context mContext)
        {
            tv1.setText(m.get("name"));
            tv2.setText("¥"+m.get("price"));
            tv3.setText(m.get("detail"));
//            new DownloadTask(iv).execute((String) m.get("image_url"));
            ImageLoader mImageLoader = MySingleton.getInstance(mContext).getImageLoader();
            mImageLoader.get((String) m.get("image_url"), ImageLoader.getImageListener(iv,
                    R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery));
        }
    }

    public interface OnItemClickListener
    {
        public void onItemClick(View view, HashMap<String, String> tag);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
