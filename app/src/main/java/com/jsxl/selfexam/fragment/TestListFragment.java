package com.jsxl.selfexam.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.activity.ChapterActivity;
import com.jsxl.selfexam.adapter.HomeTestListAdapter;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.SubjectEntity;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestListFragment extends Fragment {
    private View view;
    private ListView home_listView;
    private RelativeLayout loading;
    private HomeTestListAdapter adapter;
    private SubjectEntity subjectEntity;
    private int chapterPaperType;
    private SharedPreferences sp;
    @SuppressLint({"NewApi", "ValidFragment"})
    public TestListFragment(){

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public TestListFragment(int chapterPaperType) {
        // Required empty public constructor
        this.chapterPaperType = chapterPaperType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_test_list, container, false);
        sp = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        initView();
        initData();
        return view;
    }
    public void uploadDate(){

        getData();
    }
    private void initView() {
        home_listView = (ListView) view.findViewById(R.id.home_listView);
        loading = (RelativeLayout) view.findViewById(R.id.loading);
    }



    private void initData() {

        home_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChapterActivity.class);
                intent.putExtra("subjectId",subjectEntity.getEntity().get(position).getId());
                intent.putExtra("chapterPaperType",chapterPaperType);
                intent.putExtra("subjectName",subjectEntity.getEntity().get(position).getSubjectName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        loading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<SubjectEntity> call = retrofitService.getSubject(sp.getString("examId","0"));
        call.enqueue(new Callback<SubjectEntity>() {
            @Override
            public void onResponse(Call<SubjectEntity> call, Response<SubjectEntity> response) {
                //测试数据返回

                subjectEntity = response.body();
                adapter = new HomeTestListAdapter(getActivity(), subjectEntity);
                home_listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SubjectEntity> call, Throwable t) {
                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
//                loading.setVisibility(View.GONE);
            }
        });
    }
}
