package com.jsxl.selfexam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.bean.ChapterEntity;
import com.jsxl.selfexam.bean.SubjectEntity;

/**
 * Created by Charles on 2018/7/18.
 */

public class RecordHorAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private SubjectEntity subjectEntity;

    //判断哪个是点中位置
    public RecordHorAdapter(Context context, SubjectEntity subjectEntity) {
        this.context = context;
        this.subjectEntity = subjectEntity;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subjectEntity != null ? subjectEntity.getEntity().size() : 0;
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_record_hor_list, null);
            holder.tv_record_subject_name = (TextView) convertView.findViewById(R.id.tv_record_subject_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int pos = subjectEntity.getPos();
        Drawable drawable = null;
        if (pos == position) {
            drawable = context.getResources().getDrawable(R.drawable.icon_record_bg_select);
            holder.tv_record_subject_name.setTextColor(Color.parseColor("#ffffff"));
        } else {
            drawable = context.getResources().getDrawable(R.drawable.icon_record_bg);
            holder.tv_record_subject_name.setTextColor(Color.parseColor("#999999"));
        }
        holder.tv_record_subject_name.setBackground(drawable);
        holder.tv_record_subject_name.setText(subjectEntity.getEntity().get(position).getSubjectName());
        return convertView;
    }


    class ViewHolder {
        TextView tv_record_subject_name;

    }
}