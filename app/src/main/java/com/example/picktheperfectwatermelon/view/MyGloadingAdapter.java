package com.example.picktheperfectwatermelon.view;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.billy.android.loading.Gloading;

public class MyGloadingAdapter implements Gloading.Adapter {
    @Override
    public View getView(Gloading.Holder holder, View convertView, int status) {
        GlobalLoadingStatusView loadingStatusView = null;
        //convertView为可重用的布局
        //Holder中缓存了各状态下对应的View
        //	如果status对应的View为null，则convertView为上一个状态的View
        //	如果上一个状态的View也为null，则convertView为null
        if (convertView != null && convertView instanceof GlobalLoadingStatusView) {
            loadingStatusView = (GlobalLoadingStatusView) convertView;
        }
        if (loadingStatusView == null) {
            loadingStatusView = new GlobalLoadingStatusView(holder.getContext(), holder.getRetryTask());
        }
        loadingStatusView.setStatus(status);
        return loadingStatusView;
    }
    class GlobalLoadingStatusView extends RelativeLayout {

        public GlobalLoadingStatusView(Context context, Runnable retryTask) {
            super(context);
            //初始化LoadingView
            //如果需要支持点击重试，在适当的时机给对应的控件添加点击事件
        }

        public void setStatus(int status) {
            //设置当前的加载状态：加载中、加载失败、空数据等
            //其中，加载失败可判断当前是否联网，可现实无网络的状态
            //		属于加载失败状态下的一个分支,可自行决定是否实现
        }
    }

}
