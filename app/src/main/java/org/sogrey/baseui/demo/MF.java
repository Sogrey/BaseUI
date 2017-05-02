package org.sogrey.baseui.demo;

import android.os.Bundle;
import android.view.View;

import org.sogrey.baseui.ui.base.BaseStatusFragment;
import org.sogrey.baseui.views.statuslayout.utils.StatusLayouts;

/**
 * Created by Administrator on 2017/5/2.
 */

public class MF extends BaseStatusFragment {
    @Override
    public void onRetry() {

    }

    @Override
    public void onShowView(View view, int id) {

    }

    @Override
    public void onHideView(View view, int id) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getFragmentLayoutResId() {
        return 0;
    }

    /**
     * 设置各个状态页面布局
     *
     * @return 返回null，则赋默认值，不为空且使用带参构造函数创建则设置自定义页面
     */
    @Override
    protected StatusLayouts initFragmentStatusContentViews() {
        return new StatusLayouts(0,0,0,0,0,0,0,0,0);
    }
}
