package com.sapson.sapson;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.DialogPreference;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LandingActivity extends AppCompatActivity {
    private ImageView splash_image;
    private ProgressBar progress_bar;
    private WebView webview;
    private SwipeRefreshLayout swipe;
    private AlertDialog.Builder alertDialog;
    private String RETRY_URL = "http://sapson.in/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        splash_image = (ImageView) findViewById(R.id.splash_view);
        progress_bar = (ProgressBar) findViewById(R.id.web_view_progress_bar);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);

        webview = (WebView) findViewById(R.id.website_webview);
        webview.getSettings().setJavaScriptEnabled(true);

        alertDialog = new AlertDialog.Builder(this)
                .setMessage("There is some Error. Try Again")
                .setCancelable(false)
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(isNetworkStatusAvailable(getApplicationContext())){
                            webview.setWebViewClient(new CustomWebViewClient());
                            webview.canGoBack();
                            webview.loadUrl(RETRY_URL);
                        }
                        else{
                            alertDialog.setMessage("Internet is Not Available");
                            progress_bar.setVisibility(View.INVISIBLE);
                            alertDialog.show();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        if(isNetworkStatusAvailable(getApplicationContext())){
            webview.setWebViewClient(new CustomWebViewClient());
            webview.canGoBack();
            webview.loadUrl(RETRY_URL);
        }
        else{
            alertDialog.setMessage("Internet is Not Available");
            progress_bar.setVisibility(View.INVISIBLE);
            alertDialog.show();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                webview.loadUrl(RETRY_URL);
            }
        });
    }

    private boolean isNetworkStatusAvailable(Context context) {
        ConnectivityManager connectivity_manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity_manager != null){
            NetworkInfo netInfo = connectivity_manager.getActiveNetworkInfo();
            if (netInfo != null){
                if(netInfo.isConnected()){
                    return true;
                }
            }
        }
        return false;
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            RETRY_URL = url;
            progress_bar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            swipe.setRefreshing(false);
            splash_image.setVisibility(View.GONE);
            webview.setVisibility(View.VISIBLE);
            RETRY_URL = url;
            progress_bar.setVisibility(View.INVISIBLE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            alertDialog.setMessage(errorResponse.getReasonPhrase());
            progress_bar.setVisibility(View.INVISIBLE);
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            alertDialog.setMessage(error.getDescription());
            alertDialog.show();
            progress_bar.setVisibility(View.INVISIBLE);
            super.onReceivedError(view, request, error);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
