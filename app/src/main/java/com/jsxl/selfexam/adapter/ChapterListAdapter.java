package com.jsxl.selfexam.adapter;

import android.content.Context;
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

/**
 * Created by Charles on 2018/6/20.
 */

public class ChapterListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ChapterEntity chapterEntity;
    public ChapterListAdapter(Context context,ChapterEntity chapterEntity) {
        this.context = context;
        this.chapterEntity = chapterEntity;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chapterEntity!=null?chapterEntity.getEntity().size():0;
    }


    @Override
    public Object getItem(int position) {
        return chapterEntity.getEntity().get(position);
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
            convertView = mInflater.inflate(R.layout.item_chapter_list, null);
            holder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
            holder.iv_todo = (ImageView) convertView.findViewById(R.id.iv_todo);
            holder.tv_chapter_name = (TextView) convertView.findViewById(R.id.tv_chapter_name);
            holder.tv_chapter_progress = (TextView) convertView.findViewById(R.id.tv_chapter_progress);
            holder.pb_chapter_progress = (ProgressBar) convertView.findViewById(R.id.pb_chapter_progress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_chapter_name.setText(chapterEntity.getEntity().get(position).getChapterPaperName());
        if(chapterEntity.getEntity().get(position).getFinishNum()==0) {
            holder.tv_chapter_progress.setText("0/" + chapterEntity.getEntity().get(position).getQuestionNum());
        }else{
            holder.tv_chapter_progress.setText(chapterEntity.getEntity().get(position).getFinishNum()+"/" + chapterEntity.getEntity().get(position).getQuestionNum());
        }
        if(chapterEntity.getEntity().get(position).getQuestionNum() == 0){
            holder.pb_chapter_progress.setProgress(0);
        }else{
            ;
            holder.pb_chapter_progress.setProgress(Math.round(chapterEntity.getEntity().get(position).getFinishNum()*100f/chapterEntity.getEntity().get(position).getQuestionNum()));
        }
        Drawable drawable = null;
        if(chapterEntity.getEntity().get(position).getFinishNum()==chapterEntity.getEntity().get(position).getQuestionNum()&&chapterEntity.getEntity().get(position).getFinishNum()>0) {
            drawable = context.getResources().getDrawable(R.drawable.icon_finish);
            holder.iv_status.setImageDrawable(drawable);
            drawable = context.getResources().getDrawable(R.drawable.icon_redo);
            holder.iv_todo.setImageDrawable(drawable);
        }if(chapterEntity.getEntity().get(position).getFinishNum()<chapterEntity.getEntity().get(position).getQuestionNum()&&chapterEntity.getEntity().get(position).getFinishNum()>0){
            drawable = context.getResources().getDrawable(R.drawable.icon_unfinish);
            holder.iv_status.setImageDrawable(drawable);
            drawable = context.getResources().getDrawable(R.drawable.icon_continue);
            holder.iv_todo.setImageDrawable(drawable);
        }if(chapterEntity.getEntity().get(position).getFinishNum()==0){
            drawable = context.getResources().getDrawable(R.drawable.icon_unstart);
            holder.iv_status.setImageDrawable(drawable);
            drawable = context.getResources().getDrawable(R.drawable.icon_makequestion);
            holder.iv_todo.setImageDrawable(drawable);
        }
        return convertView;
    }


    class ViewHolder {
        ImageView iv_status, iv_todo;
        TextView tv_chapter_name, tv_chapter_progress;
        ProgressBar pb_chapter_progress;
    }
}