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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.activity.CollectListDetailActivity;
import com.jsxl.selfexam.adapter.CollectListAdapter;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.CollectFirstListEntity;
import com.jsxl.selfexam.bean.SubjectEntity;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CollectListFragment extends Fragment {
    private View view;
    private ListView collect_listView;
    private RelativeLayout rl_empty;
    private ImageView iv_empty;
    private TextView tv_empty;
    private RelativeLayout loading;
    private CollectListAdapter adapter;
    private CollectFirstListEntity collectEntity;
    private int chapterPaperType;
    private SharedPreferences sp;
    private String type;
    @SuppressLint({"NewApi", "ValidFragment"})
    public  CollectListFragment(){

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public CollectListFragment(int chapterPaperType, String type) {
        this.chapterPaperType = chapterPaperType;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_collect_list, container, false);
        sp = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        initView();
        initData();
        return view;
    }

    private void initView() {
        collect_listView = (ListView) view.findViewById(R.id.collect_listView);
        loading = (RelativeLayout) view.findViewById(R.id.loading);
        rl_empty = (RelativeLayout) view.findViewById(R.id.rl_empty);
        iv_empty = (ImageView) view.findViewById(R.id.iv_empty);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void initData() {
        collect_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CollectListDetailActivity.class);
                intent.putExtra("name", collectEntity.getList().get(position).getSubjectName());
                intent.putExtra("id", collectEntity.getList().get(position).getId());
                intent.putExtra("chapterPaperType", chapterPaperType);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        if (type.equals("collect")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<CollectFirstListEntity> call = retrofitService.getCollectFirstList(sp.getString("userId", ""), chapterPaperType + "");
            call.enqueue(new Callback<CollectFirstListEntity>() {
                @Override
                public void onResponse(Call<CollectFirstListEntity> call, Response<CollectFirstListEntity> response) {
                    //测试数据返回
                    collectEntity = response.body();
                    adapter = new CollectListAdapter(getActivity(), collectEntity);
                    collect_listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    if(collectEntity.getList().size()==0){
                        rl_empty.setVisibility(View.VISIBLE);
                        tv_empty.setText("暂无收藏");
                        iv_empty.setImageResource(R.drawable.icon_collect_mepty);
                    }
                }

                @Override
                public void onFailure(Call<CollectFirstListEntity> call, Throwable t) {
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (type.equals("error")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<CollectFirstListEntity> call = retrofitService.getErrorFirstList(sp.getString("userId", ""), chapterPaperType + "");
            call.enqueue(new Callback<CollectFirstListEntity>() {
                @Override
                public void onResponse(Call<CollectFirstListEntity> call, Response<CollectFirstListEntity> response) {
                    //测试数据返回
                    collectEntity = response.body();
                    adapter = new CollectListAdapter(getActivity(), collectEntity);
                    collect_listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    if(collectEntity.getList().size()==0){
                        rl_empty.setVisibility(View.VISIBLE);
                        tv_empty.setText("暂无错题");
                        iv_empty.setImageResource(R.drawable.icon_error_empty);
                    }
                }

                @Override
                public void onFailure(Call<CollectFirstListEntity> call, Throwable t) {
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (type.equals("note")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<CollectFirstListEntity> call = retrofitService.getNoteFirstList(sp.getString("userId", ""), chapterPaperType + "");
            call.enqueue(new Callback<CollectFirstListEntity>() {
                @Override
                public void onResponse(Call<CollectFirstListEntity> call, Response<CollectFirstListEntity> response) {
                    //测试数据返回
                    collectEntity = response.body();
                    adapter = new CollectListAdapter(getActivity(), collectEntity);
                    collect_listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    if(collectEntity.getList().size()==0){
                        rl_empty.setVisibility(View.VISIBLE);
                        tv_empty.setText("暂无笔记");
                        iv_empty.setImageResource(R.drawable.icon_note_empty);
                    }
                }

                @Override
                public void onFailure(Call<CollectFirstListEntity> call, Throwable t) {
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
