package org.sogrey.baseui.demo;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import org.sogrey.baseui.ui.base.BaseStatusActivity;
import org.sogrey.baseui.views.statuslayout.utils.StatusLayouts;

public class MainActivity extends BaseStatusActivity {


    /**
     * 设置布局文件
     *
     * @return
     */
    @Override
    protected int getLayoutRedId() {
        return R.layout.activity_pre_testsize;
    }

    /**
     * 初始化
     *
     * @author Sogrey
     * @date 2016年3月11日
     */
    @Override
    protected void init() {

        ImageView imgLoading = (ImageView) findViewById(R.id.icon_imageview_loading);
        imgLoading.setImageResource(R.drawable.loading_animlist1);
        final AnimationDrawable anim = (AnimationDrawable) imgLoading.getDrawable();
        anim.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mStatusLayoutManager.showContent();
            }
        },2000);
    }

    /**
     * 若要自定義Activity退出時是顯示提示框還是toast,默認toast,若要修改需複寫此方法返回true。
     *
     * @return if true show exit dialog,Otherwise show toast.
     */
    @Override
    public boolean isShowExitDialog() {
        return true;
    }

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
    protected int getContentInParentViewPosition() {
        return 0;
    }

    /**
     * 设置各个状态页面布局
     */
    @Override
    protected StatusLayouts initStatusContentViews() {
        return new StatusLayouts(R.layout.root,R.id.base_root_layout1,-1,-1,R.layout.base_status_loading1,-1,-1,-1,-1);
    }
}
