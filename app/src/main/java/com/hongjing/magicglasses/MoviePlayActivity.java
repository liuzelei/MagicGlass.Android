package com.hongjing.magicglasses;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class MoviePlayActivity extends Activity {

    private final String TAG = "main";
    private String file_path;
    private SurfaceView sv;
    private Button btn_play, btn_pause, btn_replay, btn_stop;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int currentPosition = 0;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //设置全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_movie_play);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.my_customer_title);

        Bundle bundle = getIntent().getExtras();
        file_path = bundle.getString("file_path");
//
//        VideoView videoView =(CustomVideoView) findViewById(R.id.movie_view);
//
//        Uri uri = Uri.parse(file_path);
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(uri);
//        videoView.start();
//        videoView.requestFocus();

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        sv = (SurfaceView) findViewById(R.id.sv);

        btn_play = (Button) findViewById(R.id.btn_play);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_replay = (Button) findViewById(R.id.btn_replay);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        btn_play.setOnClickListener(click);
        btn_pause.setOnClickListener(click);
        btn_replay.setOnClickListener(click);
        btn_stop.setOnClickListener(click);

        // 为SurfaceHolder添加回调
        sv.getHolder().addCallback(callback);

        // 为进度条添加进度更改事件
        seekBar.setOnSeekBarChangeListener(change);

    }

    private Callback callback = new Callback() {
        // SurfaceHolder被修改的时候回调
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            if(mediaPlayer != null && mediaPlayer.isPlaying()){
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
            if(currentPosition > 0) {
                play(currentPosition);
                currentPosition = 0;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }
    };

    private OnSeekBarChangeListener change  = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 当进度条停止修改的时候触发
            // 取得当前进度条的刻度
            int progress = seekBar.getProgress();
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                //设置当前播放的位置
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean formUser) {

        }
    };

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_play:
                    play(0);
                    break;
                case R.id.btn_pause:
                    pause();
                    break;
                case R.id.btn_replay:
                    replay();
                    break;
                case R.id.btn_stop:
                    stop();
                    break;
                default:
                    break;
            }
        }
    };


    protected void play(final int msec) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            Uri uri = Uri.parse(file_path);
            mediaPlayer.setDataSource(MoviePlayActivity.this, uri);
            // 设置显示视频的SurfaceHolder
            mediaPlayer.setDisplay(sv.getHolder());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    // 按照初始位置播放
                    mediaPlayer.seekTo(msec);
                    // 设置进度条的最大进度为视频流的最大播放时长
                    seekBar.setMax(mediaPlayer.getDuration());
                    // 开始线程，更新进度条的刻度
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                isPlaying = true;
                                while (isPlaying) {
                                    int current = mediaPlayer.getCurrentPosition();
                                    seekBar.setProgress(current);
                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }.start();

                    btn_play.setEnabled(false);
                }
            });

            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
                    btn_play.setEnabled(true);
                }
            });

            mediaPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 发生错误重新播放
                    play(0);
                    isPlaying = false;
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void pause() {
        if(btn_pause.getText().toString().trim().equals("继续")) {
            btn_pause.setText("暂停");
            mediaPlayer.start();
            Toast.makeText(this, "继续播放", 0).show();
            return;
        }
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            btn_pause.setText("继续");
            Toast.makeText(this, "暂停播放", 0).show();
        }
    }

    protected void replay() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            Toast.makeText(this, "重新播放", 0).show();
            btn_pause.setText("暂停");
            return;
        }
        isPlaying = false;
        play(0);
    }

    protected void stop() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            btn_play.setEnabled(true);
            isPlaying = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_play, menu);
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
