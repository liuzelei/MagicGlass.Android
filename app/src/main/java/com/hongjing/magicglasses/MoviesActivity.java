package com.hongjing.magicglasses;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class MoviesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movies);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.my_customer_title);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_movie_a = (Button) findViewById(R.id.btn_movie_a);

        btn_movie_a.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(MoviesActivity.this, MoviePlayActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("file_path", "http://resource.speakingsaver.com/media/open.mp4?_=1");
                bundle.putString("file_source", "network");

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        Button btn_movie_b = (Button) findViewById(R.id.btn_movie_b);

        btn_movie_b.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(MoviesActivity.this, MoviePlayActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("file_path", "http://resource.speakingsaver.com/media/introduce_final_x264.mp4?_=2");
                bundle.putString("file_source", "network");

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
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

        if(id == R.id.up) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
