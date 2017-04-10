package com.example.owenzx.jy_zx_1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DELL-PC on 2016/5/30.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
{
    private List<String> mDataSet = null;
    private List<String> mDataSet2 = null;
    private OnItemClickListener mListener;

    public MainAdapter(List<String> dataSet, List<String> dataSet2)
    {
        this.mDataSet = dataSet;
        this.mDataSet2 = dataSet2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message,viewGroup,false);
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener != null)
                    mListener.onItemClick(v, (String) itemView.getTag());
            }

        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        String data = mDataSet.get(i);
        String data2 = mDataSet2.get(i);
        viewHolder.bindData(data);
        viewHolder.itemView.setTag(data2);
    }

    @Override
    public int getItemCount()
    {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv;
        public ViewHolder(View itemView)
        {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.info_text);
        }

        public void bindData(String s)
        {
            if(s != null)
                tv.setText(s);
        }
    }

    public interface OnItemClickListener
    {
        public void onItemClick(View view,String data);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
