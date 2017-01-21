package com.huawei.colin.mediaplayer.util.videofile;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Colin on 2017/1/22.
 * Description:
 */
public class VideoProvider implements AbstructProvider{

    private static final String TAG = "VideoProvider";
    private Context mContext;

    public VideoProvider(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @return The list contains videos we get
     */
    @Override
    public List<Video> getList() {
        List<Video> list = null;
        if (null == mContext) {
            Log.d(TAG, "getList() returned: " + "mContext is null");
            return null;
        }

        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (null == cursor) {
            Log.d(TAG, "getList() returned: " + "cursor is null");
            return null;
        }

        list = new ArrayList<Video>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
            String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            Video mVideo = new Video(id, title, album, artist, displayName, mimeType, path, size, duration);
            list.add(mVideo);
        }
        cursor.close();
        return list;
    }
}
