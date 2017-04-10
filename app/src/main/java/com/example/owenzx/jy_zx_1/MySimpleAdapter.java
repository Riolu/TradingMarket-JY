package com.example.owenzx.jy_zx_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Owenzx on 2016/6/21.
 */
public class MySimpleAdapter extends SimpleAdapter {
    private Context mContext;
    public LayoutInflater inflater = null;

    public MySimpleAdapter(Context context,
                           List<? extends Map<String, ?>> data, int resource, String[] from,
                           int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = super.getView(position,convertView,parent);
//        if (convertView == null)
//            vi = inflater.inflate(R.layout.list_item_adsnew, null);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

        new DownloadTask((ImageView) vi.findViewById(R.id.icon))
                .execute((String) data.get("image_url"));

        return vi;
    }
}
