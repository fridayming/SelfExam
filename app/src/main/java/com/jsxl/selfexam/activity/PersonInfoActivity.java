package com.jsxl.selfexam.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.BuildConfig;
import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.CameraUtil;
import com.jsxl.selfexam.utils.CustomDatePicker;
import com.jsxl.selfexam.utils.FileUtils;
import com.jsxl.selfexam.utils.SelectPicPopupWindow;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.Circle;
import com.jsxl.selfexam.widget.DrawableTextView;
import com.jsxl.selfexam.widget.SexSelectDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class PersonInfoActivity extends BaseActivity implements View.OnClickListener, SexSelectDialog.onSexSelectListener {
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_person_head;
    private RelativeLayout rl_person_name, rl_person_age, rl_person_sex, rl_person_phone;
    private DrawableTextView tv_person_name, tv_person_age, tv_person_sex, tv_person_phone;
    private SharedPreferences sp;
    private CustomDatePicker datePicker;
    private String time;
    private String date;
    private SexSelectDialog dialog;
    private SelectPicPopupWindow menuWindow;
    private File mTmpFile;
    private File mCropImageFile;
    private static final int     REQUEST_CAMERA                                  = 100;
    private static final int     REQUEST_GALLERY                                 = 101;
    private static final int     REQUEST_CROP                                    = 102;
    private Bitmap headBitmap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Picasso.with(PersonInfoActivity.this).load(sp.getString("headUrl", "0")).transform(new Circle()).error(R.drawable.icon_default_head).into(iv_person_head);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        setStatusColor();
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initPicker();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_person_head = (ImageView) findViewById(R.id.iv_person_head);
        iv_person_head.setOnClickListener(this);
        rl_person_name = (RelativeLayout) findViewById(R.id.rl_person_name);
        rl_person_name.setOnClickListener(this);
        rl_person_age = (RelativeLayout) findViewById(R.id.rl_person_age);
        rl_person_age.setOnClickListener(this);
        rl_person_sex = (RelativeLayout) findViewById(R.id.rl_person_sex);
        rl_person_sex.setOnClickListener(this);
        rl_person_phone = (RelativeLayout) findViewById(R.id.rl_person_phone);
        rl_person_phone.setOnClickListener(this);
        tv_person_name = (DrawableTextView) findViewById(R.id.tv_person_name);
        tv_person_age = (DrawableTextView) findViewById(R.id.tv_person_age);
        tv_person_sex = (DrawableTextView) findViewById(R.id.tv_person_sex);
        tv_person_phone = (DrawableTextView) findViewById(R.id.tv_person_phone);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("个人资料");
        tv_person_name.setText(sp.getString("userName", ""));
        String age = sp.getString("age", "");
        if (age.equals("null") || age == null) {
            age = "0";
        }
        tv_person_age.setText(age + "岁");
        tv_person_sex.setText(sp.getInt("gender", 0) == 0 ? "男" : "女");
        tv_person_phone.setText(sp.getString("mobile", ""));
        Picasso.with(this).load(sp.getString("headUrl", "0")).transform(new Circle()).error(R.drawable.icon_default_head).into(iv_person_head);
    }

    private void showSexChooseDialog() {
        dialog = new SexSelectDialog(this, this, sp.getInt("gender", 0));
        dialog.show();
    }


    @Override
    public void onSexSelect(int sex) {
        if (sex == 0) ;
        {
            tv_person_sex.setText("男");
        }
        if (sex == 1) {
            tv_person_sex.setText("女");
        }
        if (sex != sp.getInt("gender", 0)) {
            updateUserInfo(sex + "", sp.getString("age", "0"));
            SharedPreferences.Editor et = sp.edit();
            et.putInt("gender", sex);
            et.apply();
        }
        dialog.dismiss();

    }

    private void updateUserInfo(String gender, String age) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit.create(RetrofitService.class);
        Call<String> call = retrofitService2.updateUserInfo(sp.getString("userId", ""), sp.getString("userName", ""), gender, age);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
//                        ToastUtils.showToast(PersonInfoActivity.this, message);
                    } else {
                        ToastUtils.showToast(PersonInfoActivity.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
//        //设置当前显示的日期
//        currentDate.setText(date);
//        //设置当前显示的时间
//        currentTime.setText(time);

        /**
         * 设置年月日
         */
        datePicker = new CustomDatePicker(this, "请选择出生日期", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if (!time.equals(sp.getString("age", "0"))) {
                    SharedPreferences.Editor et = sp.edit();
                    et.putString("age", time);
                    et.apply();
                    tv_person_age.setText(time + "岁");
                    updateUserInfo(sp.getInt("gender", 0) + "", time);
                }

            }
        }, "1900-01-01 00:00", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);
        datePicker.setMonIsLoop(true);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_person_head:
                setupDialog();
                break;
            case R.id.rl_person_name:
                startActivity(new Intent(this, ModifyNameActivity.class));
                break;
            case R.id.rl_person_age:
                datePicker.show(date);
                break;
            case R.id.rl_person_sex:
                showSexChooseDialog();
                break;
            case R.id.rl_person_phone:

                break;

        }
    }

    private void setupDialog(){
        final String[] items = {"拍照", "相册"};

        AlertDialog.Builder listDialog = new AlertDialog.Builder(PersonInfoActivity.this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    camera();
                }else if (i == 1){
                    gallery();
                }
            }
        });
        listDialog.show();
    }
    private void gallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void camera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            mTmpFile = new File(FileUtils.createRootPath(getBaseContext()) + "/" + System.currentTimeMillis() + ".jpg");
            FileUtils.createFile(mTmpFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", mTmpFile));
            }else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK){
                    crop(mTmpFile.getAbsolutePath());
                }else {
                    Toast.makeText(this, "拍照失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CROP:
                if (resultCode == RESULT_OK){
                    iv_person_head.setImageURI(Uri.fromFile(mCropImageFile));
                    uploadPic(mCropImageFile);
                }else {
                    Toast.makeText(this, "截图失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK && data != null){
                    String imagePath = handleImage(data);
                    crop(imagePath);
                }else {
                    Toast.makeText(this, "打开图库失败", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
    private void crop(String imagePath){
        //mCropImageFile = FileUtils.createTmpFile(getBaseContext());
        mCropImageFile = getmCropImageFile();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP);
    }

    //把fileUri转换成ContentUri
    public Uri getImageContentUri(File imageFile){
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    //获取裁剪的图片保存地址
    private File getmCropImageFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"temp.jpg");
            File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            return file;
        }
        return null;
    }

    private String handleImage(Intent data) {
        Uri uri = data.getData();
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                            "content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equals(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
        } else {
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void uploadPic(File image) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
//        File image = getmCropImageFile();
//        Log.e("ts",image.getName().toString());
        if (image != null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS).build();

            MediaType type = MediaType.parse("image/jpg");// "text/xml;charset=utf-8"
            RequestBody fileBody = RequestBody.create(type, image);
            RequestBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    // 一样的效果
                    .addFormDataPart("userId", sp.getString("userId", ""))//
                    .addPart(
                            Headers.of("Content-Disposition",
                                    "form-data; name=\"avater\"; filename=\""
                                            + image.getName() + "\""), fileBody)
                    .build();

            final Request request = new Request.Builder().url(ContactURL.USER_AVATAR)
                    .addHeader("User-Agent", "android")
                    .header("Content-Type", "text/html; charset=utf-8;")
                    .post(multipartBody)// 传参数、文件或者混合，改一下就行请求体就行
                    .build();

            client.newCall(request).enqueue(
                    new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            Log.e("ts", e.getMessage().toString());
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                            String tempResponse = response.body().string();
                            SharedPreferences.Editor et = sp.edit();
                            try {
                                Log.e("ts",tempResponse);
                                JSONObject jo = new JSONObject(tempResponse);
                                et.putString("headUrl", jo.getString("ficPath") + jo.getString("picPath"));
                                et.apply();
                                handler.sendEmptyMessage(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
            );
        }
    }
//    private void uploadPic() {
//        // 上传至服务器
//        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
//        // 注意这里得到的图片已经是圆形图片了
//        // bitmap是没有做个圆形处理的，但已经被裁剪了
//
//            MediaType type = MediaType.parse("image/jpg");// "text/xml;charset=utf-8"
//            RequestBody fileBody = RequestBody.create(type, image);
//            Log.e("ts", image.getName());
//            RequestBody multipartBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    // 一样的效果
//                    .addFormDataPart("userId", sp.getString("userId", ""))//
//                    .addPart(
//                            Headers.of("Content-Disposition",
//                                    "form-data; name=\"avater\"; filename=\""
//                                            + image.getName() + "\""), fileBody)
//                    .build();
//
//            final Request request = new Request.Builder().url(ContactURL.USER_AVATAR)
//                    .addHeader("User-Agent", "android")
//                    .header("Content-Type", "text/html; charset=utf-8;")
//                    .post(multipartBody)// 传参数、文件或者混合，改一下就行请求体就行
//                    .build();
//
//            client.newCall(request).enqueue(
//                    new okhttp3.Callback() {
//                        @Override
//                        public void onFailure(okhttp3.Call call, IOException e) {
//                            Log.e("ts", "请求失败");
//                        }
//
//                        @Override
//                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//                            String tempResponse = response.body().string();
//                            //{"picPath":"/avatar/temp/2018628/1530155440281687.jpg","message":"上传成功","ficPath":"http://static.jsxlmed.com/chengkao/image","success":true}
//                            SharedPreferences.Editor et = sp.edit();
//                            try {
//                                JSONObject jo = new JSONObject(tempResponse);
//                                et.putString("headUrl", jo.getString("ficPath") + jo.getString("picPath"));
//                                et.apply();
//                                handler.sendEmptyMessage(0);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//            );
//        }
//    }
}
