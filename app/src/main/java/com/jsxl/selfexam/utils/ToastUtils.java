package com.jsxl.selfexam.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Charles on 2018/6/11.
 */

public final class ToastUtils {
    private static Toast toast;

    public static void showToast(final Activity act, final String msg) {
        if (toast == null) {
            // 获得当前线程的名称
            String threadName = Thread.currentThread().getName();
            // 判断是否是主线程，如果是主线程，直接显示toast
            if ("main".equals(threadName)) {
                toast = Toast.makeText(act, msg, Toast.LENGTH_SHORT);
            } else {
                // 如果不是主线程，
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(act, msg, Toast.LENGTH_SHORT);
                    }
                });
            }
        }else{
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 弹出长时间toast
     */
    public static void showLongToast(final Activity act, final String msg) {
        if (toast == null) {
            // 获得当前线程的名称
            String threadName = Thread.currentThread().getName();
            // 判断是否是主线程，如果是主线程，直接显示toast
            if ("main".equals(threadName)) {
                toast = Toast.makeText(act, msg, Toast.LENGTH_LONG);
            } else {
                // 如果不是主线程，
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(act, msg, Toast.LENGTH_LONG);
                    }
                });
            }
        }else{
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 判断指定名称的服务是否运行
     * @param act
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Activity act, String className) {
        /**
         * ActivityManager 是当前手机运行状态的管理者，不仅管理 activity ，还管理service 即其他，一切正在运行中的信息。类似于windows中的任务管理器
         */
        ActivityManager am = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);

        // 参数是获得服务数量的最大值，
        // 获得所有正在运行的服务的信息
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);//
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            String runningName = runningServiceInfo.service.getClassName();
            if (runningName.equals(className)) {
                return true;
            }
        }

        return false;
    }
}
