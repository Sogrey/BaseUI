package org.sogrey.baseui.demo;

import org.sogrey.baseui.BaseApplication;

/**
 * Created by Administrator on 2017/4/17.
 */

public class MyApplication extends BaseApplication {
    /**
     * 是否打印输出异常信息日志文件,正式发布时输出日志可设置为 !BuildConfig.DEBUG
     *
     * @return 是否输出日志文件
     */
    @Override
    protected boolean isLog() {
        return true;
    }

    /**
     * 是否打印输出Logcat日志信息,正式发布时输出日志可设置为 !BuildConfig.DEBUG
     *
     * @return 是否输出日志信息
     */
    @Override
    protected boolean isDebugLog() {
        return true;
    }
}
