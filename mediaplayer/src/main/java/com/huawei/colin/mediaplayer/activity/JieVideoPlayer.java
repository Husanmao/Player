package com.huawei.colin.mediaplayer.activity;

import java.io.File;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.huawei.colin.mediaplayer.R;
import com.huawei.colin.mediaplayer.util.videofile.Video;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class JieVideoPlayer extends AppCompatActivity {

    private static final String TAG = "JieVideoPlayer";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * The key of the video in the Extras
     */
    private static final String VIDEO_EXTRA = "video";

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;

    private io.vov.vitamio.widget.VideoView mVideoView;
    private io.vov.vitamio.widget.MediaController mMediaController;;

    private Video mVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this)) return;
        setContentView(R.layout.activity_jie_video_player);
        mVideoView = (io.vov.vitamio.widget.VideoView) findViewById(R.id.surface_view);
        mMediaController = new io.vov.vitamio.widget.MediaController(this);
        mMediaController.show(5000);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoQuality(io.vov.vitamio.MediaPlayer.VIDEOQUALITY_HIGH);
        mVideoView.requestFocus();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // update the intent
        this.setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get video data from the extras
        Bundle mBundle = getIntent().getExtras();
        if (null == mBundle) {
            Log.e(TAG, "onResume() returned: " + "No extras!");
            return;
        }
        Video mVideo = (Video) mBundle.get("video");
        if (mVideo == null || mVideo.getPath() == null) return;
        File file = new File(mVideo.getPath());
        if (file.isFile() && file.exists() && file.canRead()) {
            String path = file.getPath();
            //String type = getMIMEType(file);

            //play the video we get
            //playBySystemPlayer(path, type);

//            playByVideoView(path);
            mVideoView.setVideoPath(path);
            mVideoView.start();
        }
    }

    /**
     * Play the video using the System Player
     * @param path The path of the video, can't be wrong
     * @param type The type of the video, {@link }
     */
    private void playBySystemPlayer(String path, String type) {
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Log.v("URI::::", uri.toString());
        intent.setDataAndType(uri, type);
        startActivity(intent);
    }

    private void playByVideoView(String path) {
        Uri uri = Uri.parse(path);
        VideoView videoView = (VideoView) this.findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
    }


    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    private String getMIMEType(File file)
    {
        String type="*/*";
        String fName=file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
    /* 获取文件的后缀名 */
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }
    /**
     * 打开文件
     * @param file
     */
    private void openFile(File file){
        //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        startActivity(intent);
    }

    //建立一个MIME类型与文件后缀名的匹配表
    private final String[][] MIME_MapTable={
            //{后缀名，    MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",      "image/bmp"},
            {".c",        "text/plain"},
            {".class",    "application/octet-stream"},
            {".conf",    "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",    "application/x-gtar"},
            {".gz",        "application/x-gzip"},
            {".h",        "text/plain"},
            {".htm",    "text/html"},
            {".html",    "text/html"},
            {".jar",    "application/java-archive"},
            {".java",    "text/plain"},
            {".jpeg",    "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js",        "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",    "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",    "video/mp4"},
            {".mpga",    "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".prop",    "text/plain"},
            {".rar",    "application/x-rar-compressed"},
            {".rc",        "text/plain"},
            {".rmvb",    "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh",        "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml",    "text/plain"},
            {".z",        "application/x-compress"},
            {".zip",    "application/zip"},
            {"",        "*/*"}
    };
}
