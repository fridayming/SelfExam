package com.jsxl.selfexam.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.bean.SubjectEntity;

import java.util.List;

/**
 * Created by Charles on 2018/6/12.
 */

public class HomeTestListAdapter extends BaseAdapter {
    private Context context;
    private SubjectEntity subjectEntity;
    private LayoutInflater mInflater;

    public HomeTestListAdapter(Context context, SubjectEntity subjectEntity) {
        this.context = context;
        this.subjectEntity = subjectEntity;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subjectEntity == null ? 0 : subjectEntity.getEntity().size();
    }

    @Override
    public Object getItem(int position) {
        return subjectEntity.getEntity().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_home_test_list, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(subjectEntity.getEntity().get(position).getSubjectName());
        Drawable drawable = null;
        if (subjectEntity.getEntity().get(position).getSubjectName().equals("语文")) {
            drawable = context.getResources().getDrawable(R.drawable.icon_chinese);
            holder.iv_icon.setImageDrawable(drawable);
        } if (subjectEntity.getEntity().get(position).getSubjectName().equals("数学")) {
            drawable = context.getResources().getDrawable(R.drawable.icon_math);
            holder.iv_icon.setImageDrawable(drawable);
        } if (subjectEntity.getEntity().get(position).getSubjectName().equals("英语")) {
            drawable = context.getResources().getDrawable(R.drawable.icon_english);
            holder.iv_icon.setImageDrawable(drawable);
        } if (subjectEntity.getEntity().get(position).getSubjectName().equals("史地综合")) {
            drawable = context.getResources().getDrawable(R.drawable.icon_history);
            holder.iv_icon.setImageDrawable(drawable);
        } if (subjectEntity.getEntity().get(position).getSubjectName().equals("物化综合")) {
            drawable = context.getResources().getDrawable(R.drawable.icon_physics);
            holder.iv_icon.setImageDrawable(drawable);
        }if (subjectEntity.getEntity().get(position).getSubjectName().equals("政治")) {
            drawable = context.getResources().getDrawable(R.drawable.icon_politics);
            holder.iv_icon.setImageDrawable(drawable);
        }if (subjectEntity.getEntity().get(position).getSubjectName().equals("西医综合")) {
            drawable = context.getResources().getDrawable(R.drawable.icon_medicine);
            holder.iv_icon.setImageDrawable(drawable);
        }
        return convertView;
    }


    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
    }
}
