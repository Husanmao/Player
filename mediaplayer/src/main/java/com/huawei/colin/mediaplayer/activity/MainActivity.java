package com.huawei.colin.mediaplayer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.huawei.colin.mediaplayer.R;
import com.huawei.colin.mediaplayer.util.videofile.Video;
import com.huawei.colin.mediaplayer.util.videofile.VideoProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /** Three button for media playing */
    private Button btn_sys;
    private Button btn_video_view;
    private Button btn_media_player;

    /** Video Provider  */
    private VideoProvider mVideoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn_sys = (Button) findViewById(R.id.use_system_player);
        btn_video_view = (Button) findViewById(R.id.play_in_video_view);
        btn_media_player = (Button) findViewById(R.id.play_in_media_player);

        btn_sys.setOnClickListener(this);
        btn_video_view.setOnClickListener(this);
        btn_media_player.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (btn_sys.getId() == id) {
            playBySystemPlayer();
        } else if (btn_video_view.getId() == id) {
            playByVideoView();
        } else if (btn_media_player.getId() == id) {
            playByMediaPlayer();
        }
    }

    private void playBySystemPlayer() {
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Test_movie.mp4");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        Log.v("URI::::", uri.toString());
        intent.setDataAndType(uri, "video/mp4");
        startActivity(intent);
    }

    private void playByVideoView() {
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Test_movie.mp4");
        VideoView videoView = (VideoView) this.findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
    }

    private void playByMediaPlayer() {
        Intent intent = new Intent(MainActivity.this, MediaPlayerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startActivity(new Intent(MainActivity.this, JieVideo.class));
        // Get the list of videoes we have
        //List<Video> mList = mVideoProvider.getList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
