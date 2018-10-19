package com.jsxl.selfexam.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.activity.HomePagerActivity;
import com.jsxl.selfexam.base.ContactURL;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Charles on 2018/9/25.
 */

public class ApkUpdateManager {
    String newVerName = "";// 新版本号
    String newVerdesignation = "";// 新版本名
    String newVermessage = "";// 更新内容
    String newuri = "";// 更新地
    TextView textviewPlan;
    String path = Environment.getExternalStorageDirectory().toString();
    static String versionName = "";
    AlertDialog dlg;
    private Context mContext;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private HomePagerActivity call_handler;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    textviewPlan.setText(progress + "%");
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                case 3:
                    Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                    dlg.cancel();
                    break;
                case 4:
//				call_handler.mDialog.dismiss();
                    tishi();
                    break;
                case 5:
//				call_handler.mDialog.dismiss();
//				zuixin();
                    break;
                default:
                    break;
            }
        }

    };

    public ApkUpdateManager(Context context, HomePagerActivity call_handler) {
        this.mContext = context;
        this.call_handler = call_handler;
    }

    // 外部接口让主Activity调用
    public void dsf() {
        versionName = getAppVersionName(mContext);
        Thread t = new Thread(new Runnable() {

            public void run() {
                if (getServerVer()) {
                    if (!newVerName.equals(versionName)) {
                        mHandler.sendEmptyMessage(4);
                    } else {
                        notNewVersionUpdate();
                    }
                }
            }
        });
        t.start();
    }

    private void tishi() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("软件有更新")
                .setMessage(newVermessage)
                .setPositiveButton("立即更新",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                showDownloadDialog();// 更新版本
                            }
                        }).setNegativeButton("以后再说", null).create();
        alertDialog.show();

    }

    /**
     * 获取版本
     *
     * @return 当前应用的版本号
     */
    public static String getAppVersionName(Context context) {

        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 从服务器端获得版本号与版本名
     *
     * @return
     */
    public boolean getServerVer() {

        String info = ContactURL.UPDATE_VERJSON;
        info = new WebAccessTools(mContext).getWebContent(info);
        try {
//			Log.e("version", info);
            JSONTokener jsonParser = new JSONTokener(info);
            JSONObject js = (JSONObject) jsonParser.nextValue();
            newVerName = js.getString("newVerNameA");
            newVermessage = js.getString("newVermessageA");
            newVerdesignation = js.getString("newVerdesignationA");
            newuri = js.getString("newuirA");
        } catch (Exception e) {
            e.printStackTrace();
            newVerName = versionName;
        }
        return true;
    }

    /**
     * 不更新版本
     */
    public void notNewVersionUpdate() {
        versionName = getAppVersionName(mContext);
        StringBuffer sb = new StringBuffer();
        sb.append(versionName);
        System.out.println("最新版本" + sb);

        mHandler.sendEmptyMessage(5);
    }

    private void zuixin() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage("已是最新版本").setPositiveButton("确定", null).create();
        alertDialog.show();
    }

    private void showDownloadDialog() {
        dlg = new AlertDialog.Builder(mContext).create();
        dlg.show();
        final Window window = dlg.getWindow();
        dlg.setCancelable(false);
        window.setContentView(R.layout.update_progress);
        mProgress = (ProgressBar) window.findViewById(R.id.progressprogresss);
        TextView update_msg = (TextView) window.findViewById(R.id.update_msg);
        TextView update_number = (TextView) window
                .findViewById(R.id.update_number);
        textviewPlan = (TextView) window.findViewById(R.id.update_plan);
        update_number.setText(update_number.getText().toString()
                + newVerdesignation);
        update_msg.setText("检测到新版本\n\n" + "正在下载...");
        Button inter = (Button) window.findViewById(R.id.interceptFlag);
        inter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                interceptFlag = true;
                dlg.cancel();
            }
        });
        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        public void run() {
            try {
                URL url = new URL(newuri);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(6 * 1000);
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file1 = new File(path);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File file = new File(path, ContactURL.UPDATE_SERVERAPK);
                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    file.delete();
                    file.createNewFile();
                }
                Log.e("gary", "file path: " + file.getAbsolutePath());
                Log.e("gary", "is file?  " + file.isFile());
                FileOutputStream fos = new FileOutputStream(file);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);

                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装֪
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        dlg.cancel();
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(3);
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(3);

            }
        }

    };

    /**
     * 下载apk
     *
     * @param
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file= new File(path, ContactURL.UPDATE_SERVERAPK);
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.jsxl.selfexam.fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri,
                    "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(path, ContactURL.UPDATE_SERVERAPK)),
                    "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }
}
