package com.jacob.ble;


/*
 * Copyright 2112 Google Inc.
 *
 * Licensed under the Apache License, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.1
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
* Log工具
*/


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jianhaohong on 2/9/15.
 */
public class LogUtils {

    public static String LOG_FILE_NAME = "LogUtils.log";

    /**
     * 得到类的名字
     */
    public static String makeLogTag(Class cls) {
        return cls.getSimpleName();
    }

    /**
     * Log 打印一个DEBUG信息
     * @param tag 标记
     * @param message 信息
     */
    public static void LOGD(String tag, String message) {
        if (canLog()) {
            Log.d(tag, avoidNullString(message));
        }
    }

    /**
     * Log 打印一个VERBOSE信息
     * @param tag 标记
     * @param message 信息
     */
    public static void LOGV(String tag, String message) {
        if (canLog()) {
            Log.v(tag, avoidNullString(message));
        }
    }

    /**
     * Log 打印一个INFO信息
     * @param tag 标记
     * @param message 信息
     */
    public static void LOGI(String tag, String message) {
        if (canLog()) {
            Log.i(tag, avoidNullString(message));
        }
    }

    /**
     * Log 打印一个WARN信息
     * @param tag 标记
     * @param message 信息
     */
    public static void LOGW(String tag, String message) {
        if (canLog()) {
            Log.w(tag, avoidNullString(message));
        }
    }


    /**
     * Log 打印一个ERROR信息
     * @param tag 标记
     * @param message 信息
     */
    public static void LOGE(String tag, String message) {
        if (canLog()) {
            Log.e(tag, avoidNullString(message));
        }
    }


    /**
     * /**
     * Log 打印一个DEBUG信息，并且将异常打印出来
     * 使用LOGD(tag,message,new Throwable())可以将方法栈打印出来
     * @param tag 标记
     * @param message 信息
     * @param cause 异常信息
     */

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (canLog()) {
            Log.d(tag, avoidNullString(message), cause);
        }
    }


    /**
     * Log 打印一个VERBOSE信息，并且将异常打印出来
     * 使用LOGV(tag,message,new Throwable())可以将方法栈打印出来
     * @param tag 标记
     * @param message 信息
     * @param cause 异常信息
     */
    public static void LOGV(final String tag, String message, Throwable cause) {
        if (canLog()) {
            Log.v(tag, avoidNullString(message), cause);
        }
    }

    /**
     * Log 打印一个INFO信息，并且将异常打印出来
     * 使用LOGI(tag,message,new Throwable())可以将方法栈打印出来
     * @param tag 标记
     * @param message 信息
     * @param cause 异常信息
     */
    public static void LOGI(final String tag, String message, Throwable cause) {
        if (canLog()) {
            Log.i(tag, avoidNullString(message), cause);
        }
    }

    /**
     * Log 打印一个WARN信息，并且将异常打印出来
     * 使用LOGW(tag,message,new Throwable())可以将方法栈打印出来
     * @param tag 标记
     * @param message 信息
     * @param cause 异常信息
     */
    public static void LOGW(final String tag, String message, Throwable cause) {
        if (canLog()) {
            Log.w(tag, avoidNullString(message), cause);
        }
    }

    /**
     * Log 打印一个ERROR信息，并且将异常打印出来
     * 使用LOGE(tag,message,new Throwable())可以将方法栈打印出来
     * @param tag 标记
     * @param message 信息
     * @param cause 异常信息
     */
    public static void LOGE(final String tag, String message, Throwable cause) {
        if (canLog()) {
            Log.e(tag, avoidNullString(message), cause);
        }
    }

    /**
     * Log 打印一个VERBOSE信息，简便的方法，tag为当前类的全称
     * @param message 信息
     */
    public static void LOGV(String message) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.v(stackTraceElements[1].getFileName(), avoidNullString(message));
        }
    }

    /**
     * Log 打印一个DEBUG信息，简便的方法，tag为当前类的全称
     * @param message 信息
     */
    public static void LOGD(String message) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.d(stackTraceElements[1].getFileName(), avoidNullString(message));
        }
    }

    /**
     * Log 打印一个WARN信息，简便的方法，tag为当前类的全称
     * @param message 信息
     */
    public static void LOGW(String message) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.w(stackTraceElements[1].getFileName(), avoidNullString(message));
        }
    }

    /**
     * Log 打印一个INFO信息，简便的方法，tag为当前类的全称
     * @param message 信息
     */
    public static void LOGI(String message) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.i(stackTraceElements[1].getFileName(), avoidNullString(message));
        }
    }

    /**
     * Log 打印一个ERROR信息，简便的方法，tag为当前类的全称
     * @param message 信息
     */
    public static void LOGE(String message) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.e(stackTraceElements[1].getFileName(), avoidNullString(message));
        }
    }

    /**
     * Log 打印一个DEBUG信息，并且将异常信息打印出来，简便的方法，tag为当前类的全称
     * @param message 信息
     * @param cause 异常
     */
    public static void LOGD(String message, Throwable cause) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.d(stackTraceElements[1].getFileName(), avoidNullString(message), cause);
        }
    }


    /**
     * Log 打印一个VERBOSE信息，并且将异常信息打印出来，简便的方法，tag为当前类的全称
     * @param message 信息
     * @param cause 异常
     */
    public static void LOGV(String message, Throwable cause) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.v(stackTraceElements[1].getFileName(), avoidNullString(message), cause);
        }
    }


    /**
     * Log 打印一个INFO信息，并且将异常信息打印出来，简便的方法，tag为当前类的全称
     * @param message 信息
     * @param cause 异常
     */
    public static void LOGI(String message, Throwable cause) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.i(stackTraceElements[1].getFileName(), avoidNullString(message), cause);
        }
    }

    /**
     * Log 打印一个WARN信息，并且将异常信息打印出来，简便的方法，tag为当前类的全称
     * @param message 信息
     * @param cause 异常
     */
    public static void LOGW(String message, Throwable cause) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.w(stackTraceElements[1].getFileName(), avoidNullString(message), cause);
        }
    }


    /**
     * Log 打印一个ERROR信息，并且将异常信息打印出来，简便的方法，tag为当前类的全称
     * @param message 信息
     * @param cause 异常
     */
    public static void LOGE(String message, Throwable cause) {
        if (canLog()) {
            StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
            Log.e(stackTraceElements[1].getFileName(), avoidNullString(message), cause);
        }
    }

    /**
     * 将Log信息记录到本地
     * @param message 信息
     */
    public static void LogToFile(String message) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + LOG_FILE_NAME;
        try {
            FileWriter writer = new FileWriter(path, true);
            writer.write(avoidNullString(message) + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除本地Log日志
     */
    public static void deleteLogFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + LOG_FILE_NAME;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 避免空对象引起的空指针异常
     * @param string 源字符串
     * @return
     */
    private static String avoidNullString(String string) {
        if (string == null) {
            return "null";
        }
        return string;
    }

    /**
     * 是否能够记录Log信息
     * @return
     */
    private static boolean canLog() {
        return BuildConfig.DEBUG;
    }

    private LogUtils() {
    }
}
