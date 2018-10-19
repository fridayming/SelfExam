package com.jsxl.selfexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.bean.RecordEntity;
import com.jsxl.selfexam.utils.DataFormatUtils;

import java.util.List;


/**
 * Created by Charles on 2018/7/18.
 */

public class RecordAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<RecordEntity> list;

    public RecordAdapter(Context context, List<RecordEntity> list) {
        this.context = context;
        this.list = list;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = mInflater.inflate(R.layout.item_record_list, null);
            holder.tv_report_name = (TextView) convertView.findViewById(R.id.tv_report_name);
            holder.tv_report_number = (TextView) convertView.findViewById(R.id.tv_report_number);
            holder.tv_report_time =  (TextView) convertView.findViewById(R.id.tv_report_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_report_name.setText((position+1)+"."+list.get(position).getChapterName());
        holder.tv_report_number.setText("题目："+list.get(position).getChapterNumber()+"");
        holder.tv_report_time.setText("答题时间："+ DataFormatUtils.dataToTime2(list.get(position).getCreateTime()));
        return convertView;
    }


    class ViewHolder {
        TextView tv_report_name,tv_report_number,tv_report_time;

    }
}