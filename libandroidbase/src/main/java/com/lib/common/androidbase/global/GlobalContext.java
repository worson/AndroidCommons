package com.lib.common.androidbase.global;

import android.app.Application;
import android.content.Context;
import com.lib.common.dlog.DLog;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public class GlobalContext {

    private static volatile Application sContext;

    public GlobalContext() {
    }

    public static Context get() {
        return sContext;
    }

    public static void set(Application application) {
        if (null != sContext) {
            DLog.e("duplicated init!!!!!!!!!!!!!!");
        }

        sContext = application;
    }
}
