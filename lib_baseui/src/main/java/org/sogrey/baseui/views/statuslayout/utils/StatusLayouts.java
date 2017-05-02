package org.sogrey.baseui.views.statuslayout.utils;

/**
 * Created by Sogrey on 2017/5/2.
 */

public class StatusLayouts {
    public int root_layout, empty_layout, error_layout, loading_layout, networkerror_layout;
    public int parent_groupview, btn_retry, img_emptyData, txt_tips;

    /**
     * 设置各个状态页面布局，小于1（即0或负数）视为采用默认值，根布局如下：
     * <pre><code>
     *     &lt;FrameLayout  android:layout_width="match_parent"
             android:id="@+id/base_root_layout"
             android:layout_height="match_parent"&gt;
            &lt;/FrameLayout&gt;
     *</code></pre>
     * @param root_layout         部署动态数据的根布局（特别注意：该布局根应该是FragmentLayout，且其id必须和第二参parent_groupview一致）
     * @param parent_groupview    部署动态数据的跟布局ID
     * @param empty_layout        空数据页面布局
     * @param error_layout        错误页面布局
     * @param loading_layout      正在加载数据布局
     * @param networkerror_layout 网络异常布局
     * @param btn_retry           重试按钮ID
     * @param img_emptyData       空数据页面图片ID
     * @param txt_tips            空数据提示文本ID
     */
    public StatusLayouts(int root_layout,int parent_groupview, int empty_layout, int error_layout, int loading_layout, int networkerror_layout, int btn_retry, int img_emptyData, int txt_tips) {
        this.root_layout = root_layout;
        this.empty_layout = empty_layout;
        this.error_layout = error_layout;
        this.loading_layout = loading_layout;
        this.networkerror_layout = networkerror_layout;
        this.parent_groupview = parent_groupview;
        this.btn_retry = btn_retry;
        this.img_emptyData = img_emptyData;
        this.txt_tips = txt_tips;
    }

    /**
     * 默认方法。赋予默认值
     */
    public StatusLayouts() {
        this.root_layout = this.empty_layout = this.error_layout = this.loading_layout = this.networkerror_layout = this.parent_groupview = this.btn_retry = this.img_emptyData = this.txt_tips = -1;
    }

    /**
     * 是否采用默认值
     * @param resId 要判断的资源ID
     * @return 如果是小于1，即0或负数视为采用默认值
     */
    public static boolean isDefaultRes(int resId){
        return resId<1;
    }
}
