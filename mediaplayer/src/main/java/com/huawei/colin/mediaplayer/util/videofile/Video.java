package com.huawei.colin.mediaplayer.util.videofile;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.huawei.colin.mediaplayer.util.component.LoadedImage;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Colin on 2017/1/22.
 * Description:Represent a video
 */
public class Video implements Serializable {

    private static final String TAG = "Video";

    private int id;
    private String title;
    private String album;
    private String artist;
    private String displayName;
    private String mimeType;
    private String path;
    private long size;
    private long duration;
    private LoadedImage image;

    /**
     * @param id The id of the video
     * @param title The title of the video
     * @param album The album the video belongs to
     * @param artist The artist of the video
     * @param displayName The name of the video to display
     * @param mimeType The mime type of the video
     * @param path The absolute path of the video file
     * @param size The size of the video in MB
     * @param duration The duration of the video
     */
    public Video(int id, String title, String album, String artist, String displayName, String mimeType, String path, long size, long duration) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.path = path;
        this.size = size;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LoadedImage getImage() {
        return image;
    }

    public void setImage(LoadedImage image) {
        this.image = image;
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap mBitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            retriever.setDataSource(url, new HashMap<String, String>());
            mBitmap = retriever.getFrameAtTime();
        } catch(RuntimeException e) {
            Log.e(TAG, "createVideoThumbnail: ", e);
        } finally {
            try {
                retriever.release();
            } catch(RuntimeException re) {
                Log.e(TAG, "createVideoThumbnail: ", re);
            }
        }

        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && mBitmap != null) {
            mBitmap = ThumbnailUtils.extractThumbnail(mBitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return mBitmap;
    }
}
