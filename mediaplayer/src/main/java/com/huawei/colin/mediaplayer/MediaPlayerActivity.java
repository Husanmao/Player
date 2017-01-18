package com.huawei.colin.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener,MediaPlayer.OnVideoSizeChangedListener,SurfaceHolder.Callback {

    private static final String TAG = "MediaPlayerActivity";
    private Display currDisplay;
    private static MediaPlayer mediaPlayer;
    private static SurfaceView surfaceView;
    private static SurfaceHolder surfaceHolder;
    Button play, stop, reset;

    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currDisplay = this.getWindowManager().getDefaultDisplay();
        play = (Button)findViewById(R.id.play);
        stop = (Button)findViewById(R.id.stop);
        reset = (Button)findViewById(R.id.reset);
        surfaceView = (SurfaceView)findViewById(R.id.surface);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFixedSize(320, 220);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        file = new File(Environment.getExternalStorageDirectory(), "serge_and_eno_3.mp4");

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(surfaceHolder);

        try{
            System.out.println("data source and prepare.");
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepareAsync();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, "surface change called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v(TAG, "on completion called");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v(TAG, "on Error called");
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.v(TAG, "Error server died");
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.v(TAG, "on Info called");
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        int vWidth = mediaPlayer.getVideoWidth();
        int vHeight = mediaPlayer.getVideoHeight();

        if (vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()) {
            float wRatio = (float) vWidth / (float) currDisplay.getWidth();
            float hRatio = (float) vHeight / (float) currDisplay.getHeight();

            float ratio = Math.max(wRatio, hRatio);
            vWidth = (int) Math.ceil((float) vWidth / ratio);
            vHeight = (int) Math.ceil((float) vHeight / ratio);

            surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
        }
        mediaPlayer.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.v(TAG, "on seek complete called");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }
}
