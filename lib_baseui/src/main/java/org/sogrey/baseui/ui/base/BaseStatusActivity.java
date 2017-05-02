package org.sogrey.baseui.ui.base;

import android.widget.FrameLayout;

import org.sogrey.baseui.R;
import org.sogrey.baseui.views.statuslayout.OnRetryListener;
import org.sogrey.baseui.views.statuslayout.OnShowHideViewListener;
import org.sogrey.baseui.views.statuslayout.StatusLayoutManager;
import org.sogrey.baseui.views.statuslayout.utils.StatusLayouts;

/**
 * 基本带各个状态页面的Activity类;需要自定义状态页面，可继承于此类
 */
public abstract class BaseStatusActivity extends BaseActivity implements OnShowHideViewListener, OnRetryListener {
    protected StatusLayoutManager mStatusLayoutManager;
    protected StatusLayouts mStatusLayouts;

    @Override
    protected boolean isSetStatus() {
        return true;
    }

    @Override
    protected void setStatusContentViews() {
        mStatusLayouts = initStatusContentViews();
        if (mStatusLayouts == null)
            mStatusLayouts = new StatusLayouts();//为空则赋默认值
        setContentView(mStatusLayouts.isDefaultRes(mStatusLayouts.root_layout) ? R.layout.base_root_layout : mStatusLayouts.root_layout);
        FrameLayout mainLinearLayout = (FrameLayout) findViewById(mStatusLayouts.isDefaultRes(mStatusLayouts.parent_groupview) ? R.id.base_root_layout : mStatusLayouts.parent_groupview);
        mStatusLayoutManager = StatusLayoutManager.newBuilder(mContext)
                .contentView(getLayoutRedId())
                .emptyDataView(mStatusLayouts.isDefaultRes(mStatusLayouts.empty_layout) ? R.layout.base_status_emptydata : mStatusLayouts.empty_layout)
                .errorView(mStatusLayouts.isDefaultRes(mStatusLayouts.error_layout) ? R.layout.base_status_error : mStatusLayouts.error_layout)
                .loadingView(mStatusLayouts.isDefaultRes(mStatusLayouts.loading_layout) ? R.layout.base_status_loading : mStatusLayouts.loading_layout)
                .netWorkErrorView(mStatusLayouts.isDefaultRes(mStatusLayouts.networkerror_layout) ? R.layout.base_status_networkerror : mStatusLayouts.networkerror_layout)
                .retryViewId(mStatusLayouts.isDefaultRes(mStatusLayouts.btn_retry) ? R.id.button_try : mStatusLayouts.btn_retry)
                .emptyDataIconImageId(mStatusLayouts.isDefaultRes(mStatusLayouts.img_emptyData) ? R.id.icon_imageview : mStatusLayouts.img_emptyData)
                .emptyDataTextTipId(mStatusLayouts.isDefaultRes(mStatusLayouts.txt_tips) ? R.id.textview_content : mStatusLayouts.txt_tips)
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

        mainLinearLayout.addView(mStatusLayoutManager.getRootLayout(), getContentInParentViewPosition());

        mStatusLayoutManager.showLoading();//默认显示正在加载页面
//        mStatusLayoutManager.showEmptyData();//默认显示正在加载页面
//        mStatusLayoutManager.showError();//默认显示正在加载页面
//        mStatusLayoutManager.showNetWorkError();//默认显示正在加载页面
    }

    /**
     * 设置内容区域在父容器里的位置
     * @return 如果父容器是空容器，则返回0即可，如果有两个一级子视图，要将内容插在第二个位置则返回1（从0开始）
     */
    protected abstract int getContentInParentViewPosition();

    /**
     * 设置各个状态页面布局
     * @return 返回null，则赋默认值，不为空且使用带参构造函数创建则设置自定义页面
     */
    protected abstract StatusLayouts initStatusContentViews();
}

