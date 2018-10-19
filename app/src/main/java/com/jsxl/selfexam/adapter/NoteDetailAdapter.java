package com.jsxl.selfexam.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.activity.ExamTypeActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.NoteEntity;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.DataFormatUtils;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.CustomDialog;
import com.jsxl.selfexam.widget.EditNoteDialog;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Charles on 2018/7/13.
 */

public class NoteDetailAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private NoteEntity entity;
    private SharedPreferences sp;
    public NoteDetailAdapter(Context context, NoteEntity entity) {
        this.context = context;
        this.entity = entity;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sp = context.getSharedPreferences("USERINFO",Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return entity != null ? entity.getListnote().size() : 0;
    }


    @Override
    public Object getItem(int position) {
        return entity.getListnote().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_note_list, null);
            holder.tv_note_content = (TextView) convertView.findViewById(R.id.tv_note_content);
            holder.tv_note_answer = (TextView) convertView.findViewById(R.id.tv_note_answer);
            holder.tv_note_explain = (TextView) convertView.findViewById(R.id.tv_note_explain);
            holder.tv_note_note = (TextView) convertView.findViewById(R.id.tv_note_note);
            holder.tv_note_time = (TextView) convertView.findViewById(R.id.tv_note_time);
            holder.tv_note_update = (TextView) convertView.findViewById(R.id.tv_note_update);
            holder.tv_note_delete = (TextView) convertView.findViewById(R.id.tv_note_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_note_content.setText((position + 1) + "." + entity.getListnote().get(position).getQuestionContent());
        holder.tv_note_answer.setText(entity.getListnote().get(position).getQuestionAnswer());
        holder.tv_note_explain.setText(entity.getListnote().get(position).getQuestionSolution());
        holder.tv_note_note.setText(entity.getListnote().get(position).getUserNoteContent());
        holder.tv_note_time.setText(DataFormatUtils.dataToTime2(entity.getListnote().get(position).getCreateTime()));
        holder.tv_note_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog(position);
            }
        });
        holder.tv_note_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
            }
        });
        return convertView;
    }
    void showDeleteDialog(final int position){
        final CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle("提示");
        dialog.setMessage("确认要删除该条笔记？");
        dialog.setYesOnclickListener("确认", new CustomDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                deleteNote(position);
                dialog.dismiss();
            }
        });
        dialog.setNoOnclickListener("取消", new CustomDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    void showUpdateDialog(final int position) {
        final EditNoteDialog dialog = new EditNoteDialog(context);
        dialog.setMessage(entity.getListnote().get(position).getUserNoteContent());
        dialog.setYesOnclickListener(new EditNoteDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String content) {
                if(content==null||"".equals(content)){
                    ToastUtils.showToast((Activity)context,"笔记不能为空");
                }else {
                    updateNote(position,content);
                    dialog.dismiss();
                }
            }
        });
        dialog.setNoOnclickListener(new EditNoteDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //删除笔记
    void deleteNote(final int position){
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.delNote(sp.getString("userId", ""), entity.getListnote().get(position).getUserNoteId()+"");
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast((Activity)context, "删除成功");
                        entity.getListnote().remove(entity.getListnote().get(position));
                        notifyDataSetChanged();
                    } else {
                        ToastUtils.showToast((Activity)context, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
    //修改笔记
    void updateNote(final int position, final String noteContent){
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.updateNote(sp.getString("userId", ""), entity.getListnote().get(position).getUserNoteId()+"",noteContent);
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast((Activity) context, "修改成功");
                        entity.getListnote().get(position).setUserNoteContent(noteContent);
                        notifyDataSetChanged();
                    } else {
                        ToastUtils.showToast((Activity) context, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
    class ViewHolder {
        TextView tv_note_content, tv_note_answer, tv_note_explain, tv_note_note, tv_note_time, tv_note_update, tv_note_delete;
    }

}
