package org.sogrey.baseui.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import org.sogrey.baseui.R;
import org.sogrey.baseui.views.SwipeBackLayout;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，如果当前页面含有ViewPager
 * 只需要调用SwipeBackLayout的setViewPager()方法即可
 * 
 * @author xiaanming
 *
 */
public abstract class SwipeBackActivity extends BaseActivity {
	protected SwipeBackLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
				R.layout.base_aty_swipeback,null);
		layout.attachToActivity(this);
		layout.setDoSomethingBeforeFinish(new SwipeBackLayout.doSthingBeforeFinish(){
            @Override
            public void doSomething() {
                beforeFinished();
            }
        });
	}

    /**
     * 側滑finishActivity前要做的事
     */
    protected void beforeFinished() {
    }


    @Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}

	// Press the back button in mobile phone
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK){
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
     * 管理类;
     *
     * @author Sogrey.
     *
     */
    public static class ActivityManager {

        private static ActivityManager activityManager;

        public static ActivityManager getActivityManager() {
            if (activityManager == null) {
                activityManager = new ActivityManager();
            }
            return activityManager;
        }

        private final HashMap<String, SoftReference<Activity>> taskMap   = new HashMap<String, SoftReference<Activity>>();
        private final HashMap<String, SoftReference<Class<?>>> taskMapCls= new HashMap<String, SoftReference<Class<?>>>();


        public final void putActivity(Activity atv) {
            taskMap.put(atv.toString(), new SoftReference<Activity>(atv));
        }
        public final void removeActivity(Activity atv) {
            taskMap.remove(atv.toString());
        }

        public final void putActivityCls(Class<?> cls) {
            taskMapCls.put(cls.getName(), new SoftReference<Class<?>>(cls));
        }

        public final boolean hasActivityCls(Class<?> cls){
            return taskMapCls.containsKey( cls.getName());
        }
        public final void removeActivityCls(Class<?> cls) {
            taskMapCls.remove(cls.getName());
        }
        public final void clearActivityCls() {
            taskMapCls.clear();
        }

        public final void exit() {
            for (Iterator<Map.Entry<String, SoftReference<Activity>>> iterator = taskMap
                    .entrySet().iterator(); iterator.hasNext();) {
                SoftReference<Activity> activityReference = iterator.next()
                                                                    .getValue();
                Activity activity= activityReference.get();
                if (activity != null) {
                    activity.finish();
                }
            }

            taskMap.clear();
        }
    }
}
