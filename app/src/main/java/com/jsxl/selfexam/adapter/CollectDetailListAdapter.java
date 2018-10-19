package com.jsxl.selfexam.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.bean.CollectSecondListEntity;
import com.jsxl.selfexam.bean.SubjectEntity;

import static android.media.CamcorderProfile.get;

/**
 * Created by Charles on 2018/7/3.
 */

public class CollectDetailListAdapter extends BaseAdapter {
    private Context context;
    private CollectSecondListEntity entity;
    private LayoutInflater mInflater;

    public CollectDetailListAdapter(Context context,CollectSecondListEntity entity) {
        this.context = context;
        this.entity = entity;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entity == null ? 0 : entity.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return entity.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_collect_detail_list, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_collect_number = (TextView) convertView.findViewById(R.id.tv_collect_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_collect_number.setText(entity.getList().get(position).getCounts()+"");
        holder.tv_name.setText(entity.getList().get(position).getChapterpapername());
        return convertView;
    }


    class ViewHolder {
        TextView tv_name;
        TextView tv_collect_number;
    }
}
