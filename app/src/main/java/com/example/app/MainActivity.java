package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.eightbitlab.bottomnavigationbar.BottomBarItem;
import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;

public class MainActivity extends Activity {

    private WebView mWebView;
    private ImageView mSplash;
    private SwipeRefreshLayout mRefresh;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;
    BottomNavigationBar bottomMenu;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);
        mSplash = findViewById(R.id.splash);
        mRefresh = findViewById(R.id.swiperefresh);
        bottomMenu = findViewById(R.id.nav_view);



        BottomBarItem latest = new BottomBarItem(R.drawable.ic_trending_filled, R.string.latest);
        BottomBarItem trending = new BottomBarItem(R.drawable.ic_trending, R.string.trending);
        BottomBarItem exclusive = new BottomBarItem(R.drawable.ic_exclusive, R.string.exclusive);
        BottomBarItem video = new BottomBarItem(R.drawable.ic_youtube, R.string.video);
        BottomBarItem contactus = new BottomBarItem(R.drawable.ic_menu, R.string.contactus);

        bottomMenu.addTab(latest);
        bottomMenu.addTab(trending);
        bottomMenu.addTab(exclusive);
        bottomMenu.addTab(video);
        bottomMenu.addTab(contactus);


        mRefresh.setEnabled(false);
        mSplash.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
//        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                return super.onJsAlert(view, url, message, result);
            }
        });
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                mSplash.setVisibility(View.GONE);
                mRefresh.setRefreshing(true);
            }
        };
        handler.postDelayed(r, 5000);

//         REMOTE RESOURCE
        mWebView.loadUrl("https://www.rawstory.com");

        bottomMenu.setOnSelectListener(new BottomNavigationBar.OnSelectListener() {
            /**
             * Change URL below to the suited webpage to be loaded. This is
             * an extremley easily plug and play system, it will not break anything.
             * always include the entire URL as in mWebView.loadUrl("https://www.rawstory.com")
             * and NOT mWebView.loadUrl("www.rawstory.com")
             * @param position
             */
            @Override
            public void onSelect(int position) {

                if(position == 0){
                    mWebView.loadUrl("https://www.rawstory.com");
                    mRefresh.setRefreshing(true);
                }
                if(position == 1){

                    mWebView.loadUrl("https://www.rawstory.com/category/breaking-banner/");
                    mRefresh.setRefreshing(true);

                }
                if(position == 2){

                    mWebView.loadUrl("https://www.rawstory.com/raw-story-investigate/");
                    mRefresh.setRefreshing(true);
                }
                if(position == 3){

                    mWebView.loadUrl("https://www.rawstory.com/category/all-video/");
                    mRefresh.setRefreshing(true);

                }
                if(position == 4){
                    mWebView.loadUrl("https://www.rawstory.com/category/all-video/");
                    mRefresh.setRefreshing(true);

                }

            }
        });

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

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (uri.getHost() != null && uri.getHost().contains("rawstory.com")) {
                    if(mSplash.getVisibility() != View.VISIBLE) {
                        mRefresh.setRefreshing(true);
                    }
                    return false;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
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
