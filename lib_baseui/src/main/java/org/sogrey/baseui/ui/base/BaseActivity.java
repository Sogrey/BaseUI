package org.sogrey.baseui.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.sogrey.baseui.BaseApplication;
import org.sogrey.baseui.R;
import org.sogrey.baseui.global.AppConstans;
//import org.sogrey.sogreyframe.views.DialogUtils;
import org.sogrey.tsd.dialog.MaterialDialog;
import org.sogrey.tsd.toast.Tt;
import org.sogrey.utils.SPUtils;
import org.sogrey.utils.ScreenListener;
import org.sogrey.utils.SystemBarTintManager;

import java.util.LinkedHashMap;

/**
 * 基本Activity类;
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final int DELAY=300;
    public    Context   mContext;
    protected Tt mToast;
    private SwipeBackActivity.ActivityManager manager= SwipeBackActivity.ActivityManager.getActivityManager();
    private LinkedHashMap<String,Boolean> mLinkedMap;
//    private DialogUtils                   mDailogUtils;
    private MaterialDialog mMaterialDialog;
    private ScreenListener mScreenListener;//锁屏监听

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//         WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager=new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(getStatusBarTintColor()<1?R.color.bg_blue:getStatusBarTintColor());//通知栏所需颜色
        }
        mContext=this;
        manager.putActivity(this);

        mToast = new Tt.Builder(this, "Turn off fly mode")
                .withGravity(Gravity.TOP)
                .withBackgroundColor(Color.parseColor("#865aff"))
                .withMaxAlpha()
                .build();

        if (mLinkedMap==null) {
            mLinkedMap=new LinkedHashMap<String,Boolean>();
        }
        if(isSetStatus()){
            setStatusContentViews();
        }else{
            setContentView(getLayoutRedId());
        }

        init();
    }

    /**
     * 设置状态栏背景颜色，-1默认蓝色
     * @return
     */
    protected int getStatusBarTintColor(){
        return -1;
    }

    /**
     * 是否设置各个状态页面（继承BaseStatusActivity即可，不用再手动设置）
     * @return true则设置
     */
    protected boolean isSetStatus(){
        return false;
    }

    /**
     * 是否设置各个状态页面（继承BaseStatusActivity即可）
     */
    protected void setStatusContentViews(){}
    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getLayoutRedId();

    /**
     * 初始化
     *
     * @author Sogrey
     * @date 2016年3月11日
     */
    protected abstract void init();

    /**
     * 检查app是否处于前台
     */
    protected void checkIsAppOnForeground() {
        if (!BaseApplication.getInstance().getIsAppOnForeground()) {//在后台
            if (!TextUtils.isEmpty((String) SPUtils.get(mContext,
                                                       AppConstans.SP_KEY_FINGERPASSWORD,""
            ))) {
                //                Bundle bundle=new Bundle();
                //                bundle.putInt(CheckLock9ViewActivity.KEY_GOTO_WHERE,
                // CheckLock9ViewActivity
                //                        .VALUE_GOTO_RECENT);
                //                startIntent(
                //                        CheckLock9ViewActivity.class,bundle,
                //                        R.anim.base_slide_right_in,
                //                        R.anim.base_slide_remain
                //                );
            }
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean b) {
        Window                     win      =getWindow();
        WindowManager.LayoutParams winParams=win.getAttributes();
        final int                  bits     =WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (b) {
            winParams.flags|=bits;
        } else {
            winParams.flags&=~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onStart() {
        checkIsAppOnForeground();
        mScreenListener=new ScreenListener(this);
        mScreenListener.begin(new ScreenListener.ScreenStateListener() {

            @Override
            public void onUserPresent() {
                Log.e("onScreen","onUserPresent");
                BaseApplication.getInstance().setIsAppOnForeground(false);
            }

            @Override
            public void onScreenOn() {
                Log.e("onScreen","onScreenOn");
            }

            @Override
            public void onScreenOff() {
                Log.e("onScreen","onScreenOff");
                BaseApplication.getInstance().setIsAppOnForeground(false);
            }
        });
        checkVPN();
        super.onStart();
    }

//    private DialogUtils dialogSetVpn;

    protected boolean checkVPN() {
        //        if (!Utility.isVpnUsed()&&(dialogSetVpn==null||(dialogSetVpn!=null&&!dialogSetVpn
        //                .isShowing()))) {
        //            dialogSetVpn=new DialogUtils(this,R.style.CircularDialog) {
        //
        //                @Override
        //                public void ok() {
        //                    IntentUtils.GoToVPNSetting(mContext);
        //                }
        //
        //                @Override
        //                public void cancle() {}
        //
        //                @Override
        //                public void ignore() {}
        //            };
        //            dialogSetVpn.show();
        //            TextView tv=new TextView(this);
        //            tv.setText(R.string.content_setting_vpn_null);
        //            dialogSetVpn.setContent(tv);
        //            dialogSetVpn.setCancleable(false);
        //            dialogSetVpn.setDialogTitle(R.string.goto_setting_vpn);
        //            dialogSetVpn.setDialogCancleBtn(R.string.cancle);
        //            dialogSetVpn.setDialogOkBtn(R.string.goto_setting);
        //            dialogSetVpn.setDialogCancleBtnColor(getResources().getColor(R.color
        // .dimgray));
        //            dialogSetVpn.setDialogOkBtnColor(getResources().getColor(R.color.dimgray));
        //        }
        return true;// (Utility.isVpnUsed());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        mScreenListener.unregisterListener();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowManager.LayoutParams params=getWindow().getAttributes();
        if (params.softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            // 隐藏软键盘
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            params.softInputMode=WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        }
    }
    // 退出提示;
    public void showExitDialog() {
//        if (mDailogUtils!=null&&mDailogUtils.isShowing()) {
//            mDailogUtils.dismiss();
//        }
//        if (mDailogUtils==null) {
//            mDailogUtils=new DialogUtils(this,R.style.CircularDialog) {
//
//                @Override
//                public void ok() {
//                    exit();
//                    toCancle();
//                }
//
//                @Override
//                public void cancle() {
//                    toCancle();
//                }
//
//                @Override
//                public void ignore() {
//                }
//            };
//            mDailogUtils.show();
//            TextView tv=new TextView(this);
//            tv.setText("确定退出程序？");
//            tv.setGravity(Gravity.CENTER);
//            mDailogUtils.setContent(tv);
//            mDailogUtils.setDialogTitle("退出");
//            mDailogUtils.setDialogCancleBtn("取消");
//            mDailogUtils.setDialogOkBtn("确定");
//            mDailogUtils.setDialogCancleBtnColor(getResources().getColor(
//                    R.color.darkgreen));
//            mDailogUtils.setDialogOkBtnColor(getResources().getColor(
//                    R.color.darkred));
//        } else {
//            mDailogUtils.show();
//        }

        if (mMaterialDialog!=null&&mMaterialDialog.isShowing()) {
            mMaterialDialog.dismiss();
        }
        if (mMaterialDialog==null) {
            mMaterialDialog = new MaterialDialog(this)
                    .setTitle("退出")
                    .setMessage("确定退出程序？")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            exit();
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    });
        }
        mMaterialDialog.show();
    }

    /**
     * 退出应用
     */
    public void exit() {
        if (mLinkedMap!=null) {
            mLinkedMap.clear();
            mLinkedMap=null;
        }
        manager.exit();
    }

    /**
     * 退出应用 - 真退出
     */
    public void exitProgrames() {
        Intent startMain=new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    /**
     * 退出应用 - 假退出
     */
    public void exit2Home() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.removeActivity(this);
        manager.removeActivityCls(this.getClass());
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            if (doSomeThingBeforeDestroy())
                return true;
            if (isShowExitDialog()) {//彈出退出確認對話框
                showExitDialog();
            } else {//不彈退出確認對話框
                if (System.currentTimeMillis()-BaseApplication.getInstance().dataFrist<
                    2000) {
                    exit();
                } else {
                    showToast("再次点击返回键退出",Gravity.BOTTOM,R.color.white,R.color.cornflowerblue);
                    BaseApplication.getInstance().dataFrist=System.currentTimeMillis();
                }
            }
            return true;
        }
        //        if (keyCode==KeyEvent.KEYCODE_HOME ) {
        //            TheApplication.getInstance().setIsAppOnForeground(false);
        //        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * 返回退出时要做的事，返回true会跳过后续退出的操作
     * @return
     */
    protected boolean doSomeThingBeforeDestroy() {return false;}

    public void finishThis() {
        if (checkVPN()) {//已连接VPN
            finish();
            overridePendingTransition(R.anim.base_slide_remain,R.anim.base_slide_right_out);
        }
    }

    public void finishThisDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishThis();
            }
        },1000);
    }

    public void finishThisRemain() {
        if (checkVPN()) {//已连接VPN
            finish();
            overridePendingTransition(R.anim.base_slide_remain,R.anim.base_slide_remain);
        }
    }

    public void finishThisRemainDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishThisRemain();
            }
        },1000);
    }

    /**
     * 若要自定義Activity退出時是顯示提示框還是toast,默認toast,若要修改需複寫此方法返回true。
     *
     * @return if true show exit dialog,Otherwise show toast.
     */
    public boolean isShowExitDialog() {
        return false;
    }

    public void putNetWorkFlag(String key,boolean val) {
        if (mLinkedMap==null) {
            mLinkedMap=new LinkedHashMap<String,Boolean>();
        }
        mLinkedMap.put(key,val);
    }

    public boolean getNetWorkFlag(String key,boolean val) {
        if (mLinkedMap==null) {
            mLinkedMap=new LinkedHashMap<String,Boolean>();
        }
        if (mLinkedMap.containsKey(key)) {
            val=mLinkedMap.get(key);
        } else {
            val=false;
        }
        return val;
    }

    public void clearAllNetWorkFlag() {
        if (mLinkedMap==null) {
            mLinkedMap=new LinkedHashMap<String,Boolean>();
        }
        mLinkedMap.clear();
        mLinkedMap=null;
    }

    /**
     * Activity 跳转
     *
     * @param cls
     *         要跳转到的Activity
     */
    public void startIntent(Class<?> cls) {
        startIntent(cls,0,0,false);
    }

    public void startIntent(Class<?> cls,boolean isFinish) {
        startIntent(cls,0,0,isFinish);
    }

    /**
     * Activity 跳转
     *
     * @param cls
     *         要跳转到的Activity
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntent(Class<?> cls,int delay) {
        startIntent(cls,0,0,delay,false);
    }

    public void startIntent(Class<?> cls,int delay,boolean isFinish) {
        startIntent(cls,0,0,delay,isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     */
    public void startIntent(Class<?> cls,final int animIn,final int animOut) {
        startIntent(cls,animIn,animOut,-1,false);
    }

    public void startIntent(Class<?> cls,final int animIn,final int animOut,boolean isFinish) {
        startIntent(cls,animIn,animOut,-1,isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntent(Class<?> cls,final int animIn,final int animOut,int delay) {
        startIntent(cls,null,animIn,animOut,delay,false);
    }

    public void startIntent(
            Class<?> cls,final int animIn,final int animOut,int delay,
            boolean isFinish
    ) {
        startIntent(cls,null,animIn,animOut,delay,isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param bundle
     *         传递参数
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     */
    public void startIntent(Class<?> cls,Bundle bundle,final int animIn,final int animOut) {
        startIntent(cls,bundle,animIn,animOut,-1,false);
    }

    public void startIntent(
            Class<?> cls,Bundle bundle,final int animIn,final int animOut,boolean
            isFinish
    ) {
        startIntent(cls,bundle,animIn,animOut,-1,isFinish);
    }

    /**
     * Activity 跳转(动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param bundle
     *         传递参数
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntent(
            Class<?> cls,Bundle bundle,final int animIn,final int animOut,
            int delay,final boolean isFinish
    ) {
        if (!checkVPN())
            return;
        if (manager.hasActivityCls(cls)) {
            return;
        }
        manager.putActivityCls(cls);
        final Intent intent=new Intent();
        intent.setClass(this,cls);
        if (bundle!=null)
            intent.putExtras(bundle);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(intent);
                if (animIn!=0&&animOut!=0) {
                    try {
                        overridePendingTransition(animIn,animOut);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (isFinish)
                    finish();
            }
        },delay<0 ? DELAY : delay);
    }

    /**
     * Activity 跳转(需要返回结果)
     *
     * @param cls
     *         要跳转到的Activity
     * @param requestCode
     *         请求码
     */
    public void startIntentForResult(Class<?> cls,int requestCode) {
        startIntentForResult(cls,requestCode,0,0);
    }

    /**
     * Activity 跳转(需要返回结果)
     *
     * @param cls
     *         要跳转到的Activity
     * @param requestCode
     *         请求码
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntentForResult(Class<?> cls,int requestCode,int delay) {
        startIntentForResult(cls,requestCode,0,0,delay);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param requestCode
     *         请求码
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     */
    public void startIntentForResult(
            Class<?> cls,int requestCode,int animIn,
            int animOut
    ) {
        startIntentForResult(cls,null,requestCode,animIn,animOut);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param requestCode
     *         请求码
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntentForResult(
            Class<?> cls,int requestCode,int animIn,
            int animOut,int delay
    ) {
        startIntentForResult(cls,null,requestCode,animIn,animOut,delay);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param bundle
     *         參數
     * @param requestCode
     *         请求码
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     */
    public void startIntentForResult(
            Class<?> cls,Bundle bundle,int requestCode,
            int animIn,int animOut
    ) {
        startIntentForResult(cls,bundle,requestCode,animIn,animOut,-1);
    }

    /**
     * Activity 跳转(需要返回结果,动画)
     *
     * @param cls
     *         要跳转到的Activity
     * @param bundle
     *         传递参数
     * @param requestCode
     *         请求码
     * @param animIn
     *         进入动画
     * @param animOut
     *         退出动画
     * @param delay
     *         自定義延時（毫秒）
     */
    public void startIntentForResult(
            Class<?> cls,Bundle bundle,final int requestCode,
            final int animIn,final int animOut,int delay
    ) {
        if (!checkVPN())
            return;
        if (manager.hasActivityCls(cls)) {
            return;
        }
        manager.putActivityCls(cls);
        final Intent intent=new Intent();
        intent.setClass(this,cls);
        if (bundle!=null)
            intent.putExtras(bundle);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivityForResult(intent,requestCode);
                if (animIn!=0&&animOut!=0) {
                    try {
                        overridePendingTransition(animIn,animOut);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        },delay<0 ? DELAY : delay);
    }
    /**
     *
     * @param msgId 消息内容资源id
     * @param gravity 对齐方式，可选值有：{@link Gravity#CENTER},
     *                {@link Gravity#CENTER_HORIZONTAL},
     *                {@link Gravity#CENTER_VERTICAL},
     *                {@link Gravity#TOP},
     *                {@link Gravity#BOTTOM},
     *                {@link Gravity#LEFT},
     *                {@link Gravity#RIGHT}等..
     * @param textColor 字体颜色
     * @param bgColor 背景颜色
     */
    protected void showToast(int msgId,int gravity,int textColor,int bgColor){
        showToast( getResources().getString(msgId), gravity, textColor, bgColor);
    }
    /**
     *
     * @param msg 消息内容
     * @param gravity 对齐方式，可选值有：{@link Gravity#CENTER},
     *                {@link Gravity#CENTER_HORIZONTAL},
     *                {@link Gravity#CENTER_VERTICAL},
     *                {@link Gravity#TOP},
     *                {@link Gravity#BOTTOM},
     *                {@link Gravity#LEFT},
     *                {@link Gravity#RIGHT}等..
     * @param textColor 字体颜色
     * @param bgColor 背景颜色
     */
    protected void showToast(String msg,int gravity,int textColor,int bgColor){
        showToast( msg, gravity, textColor, bgColor, -1, false);
    }
    /**
     *
     * @param msgId 消息内容资源id
     * @param gravity 对齐方式，可选值有：{@link Gravity#CENTER},
     *                {@link Gravity#CENTER_HORIZONTAL},
     *                {@link Gravity#CENTER_VERTICAL},
     *                {@link Gravity#TOP},
     *                {@link Gravity#BOTTOM},
     *                {@link Gravity#LEFT},
     *                {@link Gravity#RIGHT}等..
     * @param textColor 字体颜色
     * @param bgColor 背景颜色
     * @param icon 图标,-1默认为无图标
     * @param spinIcon 是否显示图标动画(无图标设置此项无效)
     */
    protected void showToast(int msgId,int gravity,int textColor,int bgColor,int icon,boolean spinIcon){
        showToast( getResources().getString(msgId), gravity, textColor, bgColor, icon, spinIcon);
    }
    /**
     *
     * @param msg 消息内容
     * @param gravity 对齐方式，可选值有：{@link Gravity#CENTER},
     *                {@link Gravity#CENTER_HORIZONTAL},
     *                {@link Gravity#CENTER_VERTICAL},
     *                {@link Gravity#TOP},
     *                {@link Gravity#BOTTOM},
     *                {@link Gravity#LEFT},
     *                {@link Gravity#RIGHT}等..
     * @param textColor 字体颜色
     * @param bgColor 背景颜色
     * @param icon 图标,-1默认为无图标
     * @param spinIcon 是否显示图标动画(无图标设置此项无效)
     */
    protected void showToast(String msg,int gravity,int textColor,int bgColor,int icon,boolean spinIcon){
        Tt stBuilder = new Tt.Builder(this, msg)
                .withGravity(gravity)
                .withTextColor(getResources().getColor(textColor))
                .withBackgroundColor(getResources().getColor(bgColor))
                .withIcon(icon,spinIcon)
                .withMaxAlpha()
                .build();
        stBuilder.show();
    }
}
