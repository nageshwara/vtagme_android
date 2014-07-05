package me.vtag.app.backend.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Comment;

import java.util.ArrayList;

/**
 * Created by anuraag on 21/6/14.
 */
public class CommentModel implements Parcelable {
    public String id;
    public String c;
    public int t;
    public String s;
    public String vid;
    public int likes;
    public int dislikes;
    public String created_at;

    public UserModel user;
    public boolean isActive;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(c);
        parcel.writeInt(t);
        parcel.writeString(s);
        parcel.writeString(vid);
        parcel.writeInt(likes);
        parcel.writeInt(dislikes);
        parcel.writeString(created_at);
        parcel.writeParcelable(user, i);
    }

    public static final Parcelable.Creator<CommentModel> CREATOR
            = new Parcelable.Creator<CommentModel>() {
        public CommentModel createFromParcel(Parcel in) {
            return new CommentModel(in);
        }

        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
        }
    };

    public CommentModel() {}
    private CommentModel(Parcel in) {
        id = in.readString();
        c = in.readString();
        t = in.readInt();
        s = in.readString();
        vid = in.readString();
        likes = in.readInt();
        dislikes = in.readInt();
        created_at = in.readString();
        user = in.readParcelable(UserModel.class.getClassLoader());
    }

}
