package me.vtag.app.backend.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nmannem on 30/10/13.
 */
public class VideoMetaModel implements Parcelable {
    public String title;
    public String thumb;
    public long id;
    public String description;

    public int duration;
    public int views;
    public int likes;

    public String typeid;
    public String type;
    public String created_at;

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(type);
        out.writeString(typeid);

        out.writeString(title);
        out.writeString(thumb);

        out.writeInt(duration);
        out.writeInt(views);
        out.writeInt(likes);
    }
    public static final Parcelable.Creator<VideoMetaModel> CREATOR
            = new Parcelable.Creator<VideoMetaModel>() {
        public VideoMetaModel createFromParcel(Parcel in) {
            return new VideoMetaModel(in);
        }

        public VideoMetaModel[] newArray(int size) {
            return new VideoMetaModel[size];
        }
    };

    public VideoMetaModel() {}
    private VideoMetaModel(Parcel in) {
        id = in.readLong();
        type = in.readString();
        typeid = in.readString();

        title = in.readString();
        thumb = in.readString();

        duration = in.readInt();
        views = in.readInt();
        likes = in.readInt();
    }    
}
