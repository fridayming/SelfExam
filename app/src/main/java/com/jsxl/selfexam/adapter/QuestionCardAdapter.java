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
import com.jsxl.selfexam.bean.SubjectEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Charles on 2018/7/5.
 */


public class QuestionCardAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> list;
    private LayoutInflater mInflater;
    private HashMap<Integer, Integer> answerJudge;

    public QuestionCardAdapter(Context context, List<Integer> list, HashMap<Integer, Integer> answerJudge) {
        this.context = context;
        this.list = list;
        this.answerJudge = answerJudge;
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
            convertView = mInflater.inflate(R.layout.item_question_card_list, null);
            holder.tv_school_item = (TextView) convertView.findViewById(R.id.tv_school_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功

        holder.tv_school_item.setText(list.get(position) + "");
        if ((answerJudge.get(list.get(position) - 1) + "").equals("0")) {
            holder.tv_school_item.setBackgroundResource(R.drawable.icon_question_card0);
        }
        if ((answerJudge.get(list.get(position) - 1) + "").equals("1")) {
            holder.tv_school_item.setBackgroundResource(R.drawable.icon_question_card1);
        }
        if ((answerJudge.get(list.get(position) - 1) + "").equals("2")) {
            holder.tv_school_item.setBackgroundResource(R.drawable.icon_question_card2);
        }
        if ((answerJudge.get(list.get(position) - 1) + "").equals("3")) {
            holder.tv_school_item.setBackgroundResource(R.drawable.icon_question_card3);
        }
        if ((answerJudge.get(list.get(position) - 1) + "").equals("4")) {
            holder.tv_school_item.setBackgroundResource(R.drawable.icon_question_card4);
        }
        return convertView;
    }


    class ViewHolder {
        TextView tv_school_item;
    }
}