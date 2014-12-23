package com.hongjing.magicglasses;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


public class LocalMovieActivity extends ActionBarActivity {
    private MovieService movieService;
    private List<HashMap<String, String>> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_local_movie);

        ListView list = (ListView) findViewById(R.id.listView);

        movieService = new MovieService(this);
        allScan();

        movies = movieService.getMovies();

        if(movies.size() > 0) {
            SimpleAdapter mSchedule = new SimpleAdapter(this, //没什么解释
                movies,//数据来源
                R.layout.local_movie_item,//ListItem的XML实现

                //动态数组与ListItem对应的子项
                new String[] {"movieName", "movieSize"},

                //ListItem的XML文件里面的两个TextView ID
                new int[] {R.id.movieName, R.id.movieSize});
            //添加并且显示
            list.setAdapter(mSchedule);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView listView = (ListView)parent;
                    HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);

                    String movieData = map.get("movieData");

                    Uri file_path = Uri.parse(movieData);

                    Intent intent = new Intent();
                    intent.setClass(LocalMovieActivity.this, MoviePlayActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("file_path", movieData);
                    bundle.putString("file_source", "local");

                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(this, "没有找到可播放视频文件", Toast.LENGTH_LONG).show();
        }
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
        getMenuInflater().inflate(R.menu.menu_local_movie, menu);
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

    public void allScan() {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }


}
