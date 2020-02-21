package com.lib.common.androidbase.utils;

import android.os.Looper;
import android.widget.Toast;
import com.lib.common.androidbase.global.GlobalContext;
import com.lib.common.androidbase.task.HandlerUtil;

/**
 * 说明:
 *
 * @author wangshengxing  08.12 2018
 */
public class ToastUtil {

    public static void showToast(final String message) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) { // 主线程
            Toast.makeText(GlobalContext.get(), message, Toast.LENGTH_LONG).show();
        } else {
            HandlerUtil.postInMainThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GlobalContext.get(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public static void longToast(final String message) {
        showToast(message);
    }


    public static void shortToast(final String message) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) { // 主线程
            Toast.makeText(GlobalContext.get(), message, Toast.LENGTH_SHORT).show();
        } else {
            HandlerUtil.postInMainThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GlobalContext.get(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}
