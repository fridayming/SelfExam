package com.jsxl.selfexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.bean.SystemMessageEntity;
import com.jsxl.selfexam.utils.DataFormatUtils;

/**
 * Created by Charles on 2018/6/25.
 */

public class SystemMessageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private SystemMessageEntity entity;

    public SystemMessageAdapter(Context context,SystemMessageEntity entity) {
        this.context = context;
        this.entity =entity;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entity==null?0:entity.getListSystemMessge().size();
    }

    @Override
    public Object getItem(int position) {
        return entity.getListSystemMessge().get(position);
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
            convertView = mInflater.inflate(R.layout.item_system_message, null);
            holder.tv_system_content = (TextView) convertView.findViewById(R.id.tv_system_content);
            holder.tv_system_time = (TextView) convertView.findViewById(R.id.tv_system_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_system_content.setText(entity.getListSystemMessge().get(position).getMessgeContent());
        holder.tv_system_time.setText(DataFormatUtils.dataToTime(entity.getListSystemMessge().get(position).getCreateTime()));
        return convertView;
    }


    class ViewHolder {
        TextView tv_system_time,tv_system_content;
    }
}