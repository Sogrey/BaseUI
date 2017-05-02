package org.sogrey.baseui.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.sogrey.baseui.R;
import org.sogrey.baseui.views.statuslayout.OnRetryListener;
import org.sogrey.baseui.views.statuslayout.OnShowHideViewListener;
import org.sogrey.baseui.views.statuslayout.StatusLayoutManager;
import org.sogrey.baseui.views.statuslayout.utils.StatusLayouts;

import java.lang.reflect.Field;


/**
 * 带有各个状态页面的fragment基类;
 *
 * @author Sogrey.
 */
public abstract class BaseStatusFragment extends BaseFragment implements OnShowHideViewListener, OnRetryListener {
    protected StatusLayouts mFragmentStatusLayouts;
    protected StatusLayoutManager mFragmentStatusLayoutManager;
    @Override
    @Deprecated
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    @Deprecated
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected boolean isSetStatus() {
        return true;
    }

    @Override
    protected void setStatusContentViews() {
        mFragmentStatusLayouts = initFragmentStatusContentViews();
        if (mFragmentStatusLayouts == null)
            mFragmentStatusLayouts = new StatusLayouts();//为空则赋默认值
        setContentView(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.root_layout) ? R.layout.base_root_layout : mFragmentStatusLayouts.root_layout);
        FrameLayout mainLinearLayout = (FrameLayout) findViewById(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.parent_groupview)? R.id.base_root_layout : mFragmentStatusLayouts.parent_groupview);
        mFragmentStatusLayoutManager = StatusLayoutManager.newBuilder(mContext)
                .contentView(getFragmentLayoutResId())
                .emptyDataView(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.empty_layout)? R.layout.base_status_emptydata : mFragmentStatusLayouts.empty_layout)
                .errorView(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.error_layout)? R.layout.base_status_error : mFragmentStatusLayouts.error_layout)
                .loadingView(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.loading_layout)? R.layout.base_status_loading : mFragmentStatusLayouts.loading_layout)
                .netWorkErrorView(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.networkerror_layout)? R.layout.base_status_networkerror : mFragmentStatusLayouts.networkerror_layout)
                .retryViewId(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.btn_retry)? R.id.button_try : mFragmentStatusLayouts.btn_retry)
                .emptyDataIconImageId(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.img_emptyData)? R.id.icon_imageview : mFragmentStatusLayouts.img_emptyData)
                .emptyDataTextTipId(mFragmentStatusLayouts.isDefaultRes(mFragmentStatusLayouts.txt_tips)? R.id.textview_content : mFragmentStatusLayouts.txt_tips)
                .onShowHideViewListener(this/*new OnShowHideViewListener() {
                    @Override
                    public void onShowView(View view, int id) {
                    }

                    @Override
                    public void onHideView(View view, int id) {
                    }
                }*/).onRetryListener(this/*new OnRetryListener() {
                    @Override
                    public void onRetry() {
                        mStatusLayoutManager.showLoading();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mStatusLayoutManager.showContent();
                                    }
                                });
                            }
                        }).start();

                    }
                }*/).build();

        mainLinearLayout.addView(mFragmentStatusLayoutManager.getRootLayout(), 0);

        mFragmentStatusLayoutManager.showLoading();//默认显示正在加载页面
//        mStatusLayoutManager.showEmptyData();//默认显示正在加载页面
//        mStatusLayoutManager.showError();//默认显示正在加载页面
//        mStatusLayoutManager.showNetWorkError();//默认显示正在加载页面
    }

    protected abstract int getFragmentLayoutResId();

    /**
     * 设置各个状态页面布局
     * @return 返回null，则赋默认值，不为空且使用带参构造函数创建则设置自定义页面
     */
    protected abstract StatusLayouts initFragmentStatusContentViews();
}
