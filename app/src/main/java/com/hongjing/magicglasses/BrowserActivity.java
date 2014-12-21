package com.hongjing.magicglasses;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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


public class BrowserActivity extends Activity {

    private WebView browser1;
    private WebView browser2;
    private ActionBar actionBar;
    private Button btn_url_goto;
    private EditText web_url_input;

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

        web_url_input = (EditText) findViewById(R.id.web_url_input);

        btn_url_goto = (Button) findViewById(R.id.btn_url_goto);
        btn_url_goto.setOnClickListener(clik);

        browser1.setWebViewClient(client);
        browser2.setWebViewClient(client);
    }

    private Button.OnClickListener clik = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_url_goto) {
                String url = web_url_input.getText().toString();
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
        }
    };

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
}
