package com.jsxl.selfexam.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.bean.CollectFirstListEntity;
import com.jsxl.selfexam.bean.SubjectEntity;

/**
 * Created by Charles on 2018/7/3.
 */

public class CollectListAdapter extends BaseAdapter {
    private Context context;
    private CollectFirstListEntity entity;
    private LayoutInflater mInflater;

    public CollectListAdapter(Context context, CollectFirstListEntity entity) {
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
            convertView = mInflater.inflate(R.layout.item_collect_list, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_collect_number = (TextView) convertView.findViewById(R.id.tv_collect_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_collect_number.setText(entity.getList().get(position).getCounts() + "");
        holder.tv_name.setText(entity.getList().get(position).getSubjectName());
        Drawable drawable = null;
        Log.e("ttttt",entity.getList().size()+"");
        if ("语文".equals(entity.getList().get(position).getSubjectName())) {
            drawable = context.getResources().getDrawable(R.drawable.icon_chinese);
            holder.iv_icon.setImageDrawable(drawable);
        } else if ("数学".equals(entity.getList().get(position).getSubjectName())) {
            drawable = context.getResources().getDrawable(R.drawable.icon_math);
            holder.iv_icon.setImageDrawable(drawable);
        } else if ("英语".equals(entity.getList().get(position).getSubjectName())) {
            drawable = context.getResources().getDrawable(R.drawable.icon_english);
            holder.iv_icon.setImageDrawable(drawable);
        } else if ("史地综合".equals(entity.getList().get(position).getSubjectName())) {
            drawable = context.getResources().getDrawable(R.drawable.icon_history);
            holder.iv_icon.setImageDrawable(drawable);
        } else if ("物化综合".equals(entity.getList().get(position).getSubjectName())) {
            drawable = context.getResources().getDrawable(R.drawable.icon_physics);
            holder.iv_icon.setImageDrawable(drawable);
        } else if ("政治".equals(entity.getList().get(position).getSubjectName())) {
            drawable = context.getResources().getDrawable(R.drawable.icon_politics);
            holder.iv_icon.setImageDrawable(drawable);
        } else if ("医学综合".equals(entity.getList().get(position).getSubjectName())) {
            drawable = context.getResources().getDrawable(R.drawable.icon_medicine);
            holder.iv_icon.setImageDrawable(drawable);
        }
        return convertView;
    }


    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_collect_number;
    }
}
