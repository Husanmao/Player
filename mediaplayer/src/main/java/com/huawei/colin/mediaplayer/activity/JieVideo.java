package com.huawei.colin.mediaplayer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huawei.colin.mediaplayer.R;
import com.huawei.colin.mediaplayer.util.component.JieVideoListViewAdapter;
import com.huawei.colin.mediaplayer.util.component.LoadedImage;
import com.huawei.colin.mediaplayer.util.videofile.AbstructProvider;
import com.huawei.colin.mediaplayer.util.videofile.Video;
import com.huawei.colin.mediaplayer.util.videofile.VideoProvider;

import java.util.List;

public class JieVideo extends AppCompatActivity {

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
                mIntent.putExtra(mBundle);
                startActivity(mIntent);
            }
        });

        loadImages();
    }

    /**
     * Loda iamges
     */
    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (null == data) {
            new LoadedImageFromSDCard().execute();
        } else {
            final LoadedImage[] photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                 new LoadedImageFromSDCard().execute();
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
     */
     * @param value
     */
    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            mJieVideoListViewAdapter.addPhoto(image);
            mJieVideoListViewAdapter.notifyDataSetChanged();
        }
    }


}
