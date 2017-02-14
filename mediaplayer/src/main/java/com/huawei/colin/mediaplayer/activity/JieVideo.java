package com.huawei.colin.mediaplayer.activity;

import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.huawei.colin.mediaplayer.R;
import com.huawei.colin.mediaplayer.util.component.JieVideoListViewAdapter;
import com.huawei.colin.mediaplayer.util.component.LoadedImage;
import com.huawei.colin.mediaplayer.util.videofile.AbstructProvider;
import com.huawei.colin.mediaplayer.util.videofile.Video;
import com.huawei.colin.mediaplayer.util.videofile.VideoProvider;

public class JieVideo extends Activity {

    private static final String TAG = "JieVideo";
    public JieVideo mInstance = null;
    private ListView mJieVideoListView;
    JieVideoListViewAdapter mJieVideoListViewAdapter;
    List<Video> listVideos;
    int videoSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jie_video);

        mInstance = this;
        AbstructProvider mAbstructProvider = new VideoProvider(mInstance);
        listVideos = mAbstructProvider.getList();
        Log.d(TAG, "onCreate: lisVideos " + listVideos.get(0).getPath());
        videoSize = listVideos.size();
        Log.d(TAG, "onCreate: vidoeSize is " + videoSize);
        mJieVideoListViewAdapter = new JieVideoListViewAdapter(this, listVideos);
        mJieVideoListView = (ListView) findViewById(R.id.jievideolistfile);
        mJieVideoListView.setAdapter(mJieVideoListViewAdapter);
        mJieVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent();
                mIntent.setClass(JieVideo.this, JieVideoPlayer.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("video", listVideos.get(position));
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });

        Log.d(TAG, "onCreate: loadImages");
        loadImages();}

    /**
     * Loda iamges
     */
    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (null == data) {
            Log.d(TAG, "loadImages: data is null");
            new LoadImagesFromSDCard().execute();
        } else {
            final LoadedImage[] photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                Log.d(TAG, "loadImages: length is 0");
                 new LoadImagesFromSDCard().execute();
            }

            for (LoadedImage photo : photos) {
                Log.d(TAG, "loadImages: add Image");
                addImage(photo);
            }
        }
    }

    /**
     * <Description></Description>
     * <Detail></Detail>
     * ""
     * @param value
     */
    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            mJieVideoListViewAdapter.addPhoto(image);
            mJieVideoListViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @return
     */
    @Override
    public Object onRetainNonConfigurationInstance() {
        final ListView grid = mJieVideoListView;
        final int count = grid.getChildCount();
        final LoadedImage[] list = new LoadedImage[count];

        Log.d(TAG, "onRetainNonConfigurationInstance: count " + count);
        for (int i = 0; i < count; i++) {
            final ImageView v = (ImageView) grid.getChildAt(i);
            list[i] = new LoadedImage(((BitmapDrawable) v.getDrawable()).getBitmap());
        }

        return list;
    }

    /**
     * Get the thumbnail pic: using FFmpegMediaMetadataRetriever
     * @param videoPath The path of the video
     * @param width The width of the video
     * @param kind The kind of the video
     * @return The thumbnail picture
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        /*Bitmap mBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (mBitmap == null) Log.d(TAG, "getVideoThumbnail: NULL");
        mBitmap = ThumbnailUtils.extractThumbnail(mBitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        if (mBitmap == null) Log.d(TAG, "getVideoThumbnail: NULL2");
        return mBitmap;*/
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        mmr.setDataSource(videoPath);
        mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
        mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
        Bitmap mBitmap = mmr.getFrameAtTime(2* 1000 * 1000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
        mmr.release();
        return mBitmap;
    }

    class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            for (int i = 0; i < videoSize; i++) {
                Log.d(TAG, "doInBackground : " + listVideos.get(i).getPath());
//                try {
//                    File mFile = new File(listVideos.get(i).getPath());
                    Bitmap mBitmap = getVideoThumbnail(listVideos.get(i).getPath()/*mFile.getCanonicalPath()*/, 120, 120, MediaStore.Images.Thumbnails.MINI_KIND);
                    if (null != mBitmap) {
                        Log.d(TAG, "doInBackground: publishProgress");
                        publishProgress(new LoadedImage(mBitmap));
                    }
//                } catch(IOException ioe) {
//                    ioe.printStackTrace();
//                }
                Log.d(TAG, "doInBackground: mBitmap done");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(LoadedImage... values) {
            Log.d(TAG, "onProgressUpdate: on progress update");
            addImage(values);
        }
    }
}
