package com.huawei.colin.mediaplayer.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
        videoSize = listVideos.size();
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

        loadImages();}

    /**
     * Loda iamges
     */
    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (null == data) {
            new LoadImagesFromSDCard().execute();
        } else {
            final LoadedImage[] photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                 new LoadImagesFromSDCard().execute();
            }

            for (LoadedImage photo : photos) {
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

        for (int i = 0; i < count; i++) {
            final ImageView v = (ImageView) grid.getChildAt(i);
            list[i] = new LoadedImage(((BitmapDrawable) v.getDrawable()).getBitmap());
        }

        return list;
    }

    /**
     * Get the thumbnail pic
     * @param videoPath The path of the video
     * @param width The width of the video
     * @param kind The kind of the video
     * @return The thumbnail picture
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap mBitmap = null;
        mBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        mBitmap = ThumbnailUtils.extractThumbnail(mBitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return mBitmap;
    }

    class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            Bitmap mBitmap = null;
            for (int i = 0; i < videoSize; i++) {
                mBitmap = getVideoThumbnail(listVideos.get(i).getPath(), 120, 120, MediaStore.Images.Thumbnails.MINI_KIND);
                if (null != mBitmap) {
                    publishProgress(new LoadedImage(mBitmap));
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(LoadedImage... values) {
            addImage(values);
        }
    }
}
