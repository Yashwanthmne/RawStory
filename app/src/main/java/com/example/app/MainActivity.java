package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private WebView mWebView;
    private ImageView mSplash;
    private SwipeRefreshLayout mRefresh;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);
        mSplash = findViewById(R.id.splash);
        mRefresh = findViewById(R.id.swiperefresh);


        mRefresh.setEnabled(false);
        mSplash.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
        mWebView.setWebViewClient(new WebViewClient());


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//         REMOTE RESOURCE
         mWebView.loadUrl("https://www.rawstory.com");

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("YM", "INSIDE onRefresh()");
                mRefresh.setRefreshing(true);
                String url = mWebView.getUrl();
                Log.e("YM", "Loading URL : <<< " + url + " >>>");
                mWebView.loadUrl(url);
            }
        });



        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(mSplash.getVisibility() != View.VISIBLE) {
                    mRefresh.setRefreshing(true);
                }
            }

            public void onPageFinished(WebView view, String url) {
                Log.e("YM", "INSIDE onPageFinished()");
                mSplash.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mRefresh.setRefreshing(false);
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();

        mRefresh.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener =
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (mWebView.getScrollY() == 0)
                            mRefresh.setEnabled(true);
                        else{
                            if(!mRefresh.isRefreshing())
                                mRefresh.setEnabled(false);
                            else{
                                mRefresh.setRefreshing(false);
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
