package com.jsxl.selfexam.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.activity.ChapterActivity;
import com.jsxl.selfexam.activity.QuestionActivity;
import com.jsxl.selfexam.adapter.HomeTestListAdapter;
import com.jsxl.selfexam.adapter.RecordAdapter;
import com.jsxl.selfexam.adapter.RecordHorAdapter;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.RecordEntity;
import com.jsxl.selfexam.bean.SubjectEntity;
import com.jsxl.selfexam.database.DBHelper;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.jsxl.selfexam.R.id.home_listView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordDetailFragment extends Fragment {
    private View view;
    private int chapterPaperType;
    private HorizontalListView record_hor_listView;
    private ListView record_listView;
    private RelativeLayout rl_empty;
    private RecordHorAdapter adapter;
    private RecordAdapter recordAdapter;
    private SharedPreferences sp;
    private SubjectEntity subjectEntity;
    private List<RecordEntity> recordList;
    private DBHelper dbHelper;
    private int pos;
    @SuppressLint({"NewApi", "ValidFragment"})
    public RecordDetailFragment(){

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public RecordDetailFragment(int chapterPaperType) {
        // Required empty public constructor
        this.chapterPaperType = chapterPaperType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_record_detail, container, false);
        sp = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        pos = 0;
        initView();
        return view;
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            subjectEntity.setPos(pos);
//            adapter.notifyDataSetChanged();
//            recordList.clear();
//
//            recordList = dbHelper.getRecordBySubjectId(subjectEntity.getEntity().get(pos).getId(), chapterPaperType);
//            recordAdapter = new RecordAdapter(getActivity(), recordList);
//            record_listView.setAdapter(recordAdapter);
//            if (recordList.size() == 0) {
//                rl_empty.setVisibility(View.VISIBLE);
//            } else {
//                rl_empty.setVisibility(View.GONE);
//            }
//        }
//    }

    private void initView() {
        record_hor_listView = (HorizontalListView) view.findViewById(R.id.record_hor_listView);
        record_listView = (ListView) view.findViewById(R.id.record_listView);
        rl_empty = (RelativeLayout) view.findViewById(R.id.rl_empty);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        dbHelper = new DBHelper(getActivity());
        getData();
        record_hor_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                subjectEntity.setPos(position);
                adapter.notifyDataSetChanged();
                recordList.clear();
                recordList = dbHelper.getRecordBySubjectId(subjectEntity.getEntity().get(position).getId(), chapterPaperType);
                recordAdapter = new RecordAdapter(getActivity(), recordList);
                record_listView.setAdapter(recordAdapter);
                if (recordList.size() == 0) {
                    rl_empty.setVisibility(View.VISIBLE);
                } else {
                    rl_empty.setVisibility(View.GONE);
                }
            }
        });

        record_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("chapterName", recordList.get(position).getChapterName());
                intent.putExtra("chapterId", Integer.parseInt(recordList.get(position).getChapterId()));
                intent.putExtra("chapterPaperType", chapterPaperType);
                intent.putExtra("subjectId", subjectEntity.getEntity().get(subjectEntity.getPos()).getId());
                intent.putExtra("subjectName", subjectEntity.getEntity().get(subjectEntity.getPos()).getSubjectName());
                //判断进入位置
                intent.putExtra("from", "record");
                startActivity(intent);
            }
        });
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<SubjectEntity> call = retrofitService.getSubject(sp.getString("examId", "0"));
        call.enqueue(new Callback<SubjectEntity>() {
            @Override
            public void onResponse(Call<SubjectEntity> call, Response<SubjectEntity> response) {
                //测试数据返回

                subjectEntity = response.body();
                subjectEntity.setPos(0);
                adapter = new RecordHorAdapter(getActivity(), subjectEntity);
                record_hor_listView.setAdapter(adapter);
                if (subjectEntity.getEntity().size() > 0) {
                    recordList = dbHelper.getRecordBySubjectId(subjectEntity.getEntity().get(0).getId(), chapterPaperType);
                    recordAdapter = new RecordAdapter(getActivity(), recordList);
                    record_listView.setAdapter(recordAdapter);
                    if (recordList.size() == 0) {
                        rl_empty.setVisibility(View.VISIBLE);
                    } else {
                        rl_empty.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SubjectEntity> call, Throwable t) {
                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
