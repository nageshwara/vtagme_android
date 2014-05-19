package me.vtag.app.backend.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nmannem on 30/10/13.
 */
public class VideoModel implements Parcelable {
    public String id;
    public List<String> hashtags;
    public VideoMetaModel video;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeStringList(hashtags);
        parcel.writeParcelable(video, i);
    }

    public static final Parcelable.Creator<VideoModel> CREATOR
            = new Parcelable.Creator<VideoModel>() {
        public VideoModel createFromParcel(Parcel in) {
            return new VideoModel(in);
        }

        public VideoModel[] newArray(int size) {
            return new VideoModel[size];
        }
    };

    private VideoModel(Parcel in) {
        hashtags = new ArrayList<>();

        id = in.readString();
        in.readStringList(hashtags);
        video = in.readParcelable(VideoMetaModel.class.getClassLoader());
    }

}
