package com.reckonix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import mssoni.reckonix.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DEFAULT_URL = "http://sarfme-001-site5.btempurl.com/";

    private WebView webViewHome;
    private FrameLayout llRootView;
    private ImageView ivNoInternet;
    private ImageView ivLogo;

    private ProgressDialog progressDialog;
    private String url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().hide();

        ivLogo = findViewById(R.id.activity_web_view_ivLogo);

        webViewHome = findViewById(R.id.activity_web_view_home);
//        webViewHome.getSettings().setBuiltInZoomControls(true);
        webViewHome.getSettings().setDisplayZoomControls(true);
        webViewHome.getSettings().setSupportZoom(true);


        initView();

        ivLogo.setOnClickListener(this);
    }

    private void initView() {


        llRootView = findViewById(R.id.activity_web_view_ll_rootView);
        ivNoInternet = findViewById(R.id.activity_web_view_iv_no_connection);

        progressDialog = new ProgressDialog(WebViewActivity.this, 0);
        progressDialog.setMessage(getString(R.string.progress_msg_please_wait));
        progressDialog.setCancelable(false);

        //load webView
        webViewHome.setWebViewClient(new MyBrowser());
        setBrowser();

        ivNoInternet.setOnClickListener(this);

        //for downloading file in web view
        webViewHome.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }

    private void setBrowser() {
        webViewHome.getSettings().setLoadsImagesAutomatically(true);
        webViewHome.getSettings().setJavaScriptEnabled(true);
        webViewHome.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        final SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        url = prefs.getString(Constants.MY_PREFS_KEY_URL, DEFAULT_URL);
        webViewHome.loadUrl(url);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_web_view_iv_no_connection:
                setBrowser();
                break;

            case R.id.activity_web_view_ivLogo:
                // Start the SettingActivity
                Intent intent = new Intent(this, SettingActivity.class);
//                if (!url.equals(DEFAULT_URL)) {
//                }
                intent.putExtra(Constants.KEY_URL, url);
                startActivityForResult(intent, Constants.REQUEST_CODE_SETTING_SCREEN);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ivLogo.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            progressDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        if (webViewHome.canGoBack())
            webViewHome.goBack();
        else
            super.onBackPressed();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_SETTING_SCREEN) {
            if (resultCode == RESULT_OK) {
                url = data.getStringExtra(Constants.KEY_URL);
                setBrowser();
            }
        }
    }
}

