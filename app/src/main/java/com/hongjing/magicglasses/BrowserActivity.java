package com.hongjing.magicglasses;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;


public class BrowserActivity extends Activity implements ScrollViewListener {

    private WebView browser1;
    private WebView browser2;
    private ActionBar actionBar;
    private Button btn_url_goto1, btn_url_goto2;
    private EditText web_url_input1, web_url_input2;
    private String url;
    private ObservableScrollView scrollView1 = null;
    private ObservableScrollView scrollView2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_browser);

        browser1 = (WebView) findViewById(R.id.browser_webView1);
        browser2 = (WebView) findViewById(R.id.browser_webView2);

        WebSettings webSettings1 = browser1.getSettings();
        webSettings1.setJavaScriptEnabled(true);

        WebSettings webSettings2 = browser2.getSettings();
        webSettings2.setJavaScriptEnabled(true);

        browser1.loadUrl("http://www.baidu.com");
        browser2.loadUrl("http://www.baidu.com");

        web_url_input1 = (EditText) findViewById(R.id.web_url_input1);
        web_url_input2 = (EditText) findViewById(R.id.web_url_input2);

        web_url_input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(web_url_input1.hasFocus()) {
                    web_url_input2.setText(s);
                }
            }
        });

        web_url_input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(web_url_input2.hasFocus()) {
                    web_url_input1.setText(s);
                }
            }
        });

        btn_url_goto1 = (Button) findViewById(R.id.btn_url_goto1);
        btn_url_goto2 = (Button) findViewById(R.id.btn_url_goto2);

        btn_url_goto1.setOnClickListener(clik);
        btn_url_goto2.setOnClickListener(clik);

        browser1.setWebViewClient(client);
        browser2.setWebViewClient(client);

        scrollView1 = (ObservableScrollView) findViewById(R.id.scrollView1);
        scrollView1.setScrollViewListener(this);
        scrollView2 = (ObservableScrollView) findViewById(R.id.scrollView2);
        scrollView2.setScrollViewListener(this);

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int with = dm.widthPixels;
//        int height = dm.heightPixels;
//        int real_height = height / 4;
//        LinearLayout.LayoutParams linearParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        linearParams1.setMargins(0, real_height, with / 2, real_height);
//
//        scrollView1.setLayoutParams(linearParams1);

//        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        linearParams.setMargins(with / 2, real_height, 0, real_height);
//        scrollView2.setLayoutParams(linearParams);
    }

    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if(scrollView == scrollView1) {
            scrollView2.scrollTo(x, y);
        } else if(scrollView == scrollView2) {
            scrollView1.scrollTo(x, y);
        }
    }

    private Button.OnClickListener clik = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_url_goto1:
                   url = web_url_input1.getText().toString().trim();
                   break;
                case R.id.btn_url_goto2:
                    url = web_url_input2.getText().toString().trim();
                    break;
                default:
                    break;
            }
            goto_url(url);
        }
    };

    protected void goto_url(String url) {
        if(!(url.startsWith("http://") || url.startsWith("https://"))){
            url = "http://" + url;
        }
        if(URLUtil.isNetworkUrl(url) && URLUtil.isValidUrl(url)) {
            browser1.loadUrl(url);
            browser2.loadUrl(url);
        }else{
            new AlertDialog.Builder(BrowserActivity.this)
                    .setTitle("警告")
                    .setMessage("不是有效的网址")
                    .create()
                    .show();
        }
    }

    private WebViewClient client = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(view.equals(browser1)) {
                view.loadUrl(url);
                browser2.loadUrl(url);
            }

            if(view.equals(browser2)) {
                view.loadUrl(url);
                browser1.loadUrl(url);
            }

            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(BrowserActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            if(browser1 != null) {
                browser1.destroy();
            }
            if(browser2 != null) {
                browser2.destroy();
            }
            return true;
        }
        return false;
    }
}
