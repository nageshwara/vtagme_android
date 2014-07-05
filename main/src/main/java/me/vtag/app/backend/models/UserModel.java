package me.vtag.app.backend.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nmannem on 30/10/13.
 */
public class UserModel implements Parcelable {
    public String id;
    public String auth_id;
    public String name;
    public String displayName;
    public String pic;
    public String url;

    public int likes = 0;
    public String socialnetwork;

    public String activetag;
    public boolean activetag_private;
    public int activevid;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(auth_id);
        parcel.writeString(name);
        parcel.writeString(displayName);
        parcel.writeString(pic);
        parcel.writeString(url);
        parcel.writeInt(likes);

        parcel.writeString(socialnetwork);

        parcel.writeString(activetag);
        parcel.writeInt(activetag_private ? 1 : 0);
        parcel.writeInt(activevid);
    }

    public static final Parcelable.Creator<UserModel> CREATOR
            = new Parcelable.Creator<UserModel>() {
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public UserModel() {}
    private UserModel(Parcel in) {
        id = in.readString();
        auth_id = in.readString();
        name = in.readString();
        displayName = in.readString();
        pic = in.readString();
        url = in.readString();
        likes = in.readInt();

        socialnetwork = in.readString();

        activetag = in.readString();
        activetag_private = in.readInt() == 1 ? true : false;
        activevid = in.readInt();
    }


}
