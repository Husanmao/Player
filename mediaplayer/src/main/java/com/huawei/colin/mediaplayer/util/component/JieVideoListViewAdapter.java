package com.huawei.colin.mediaplayer.util.component;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.colin.mediaplayer.R;
import com.huawei.colin.mediaplayer.util.videofile.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin on 2017/1/22.
 * Description:NA
 */
public class JieVideoListViewAdapter extends BaseAdapter{

    private List<Video> mList;
    private int local_position = 0;
    private boolean imageChage = false;
    private LayoutInflater mLayoutInflater;
    private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();
    private Context mContext;

    public JieVideoListViewAdapter(Context mContext, List<Video> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    public void addPhoto(LoadedImage image) {
        photos.add(image);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (null == convertView) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.son_video, null);
            mViewHolder.img = (ImageView) convertView.findViewById(R.id.video_img);
            mViewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
            mViewHolder.time = (TextView) convertView.findViewById(R.id.video_time);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.title.setText(mList.get(position).getTitle());     //ms
        long min = mList.get(position).getDuration() / 100 / 60;
        long sec = mList.get(position).getDuration() / 1000 % 60;
        mViewHolder.time.setText(min + " : " + sec);
        mViewHolder.img.setImageBitmap(photos.get(position).getBitmap());

        return convertView;
    }

    /**
     * class for a video picture
     */
    public final class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView time;
    }
}
