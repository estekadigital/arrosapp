package com.esteka.arrosapp.radio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A dummy item representing a piece of content.
 */
public class Station implements Parcelable {

    private Integer mId;
    private String mName;
    private String mStreamURL;
    private String mPodcastURL;
    private String mImageURL;
    private String mFreq;
    private String mArea;
    private String mDesc;
    private String mAddress;
    private String mWeb;
    private String mEmail;
    private String mPhone;
    private String mLongDesc;
    private Integer mImageResId;
    private String mStatus;

    public Station() {
    }

    private Station(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel parcel) {
        mId = parcel.readInt();
        mName = parcel.readString();
        mStreamURL = parcel.readString();
        mPodcastURL = parcel.readString();
        mImageURL = parcel.readString();
        mFreq = parcel.readString();
        mArea = parcel.readString();
        mDesc = parcel.readString();
        mAddress = parcel.readString();
        mWeb = parcel.readString();
        mEmail = parcel.readString();
        mPhone = parcel.readString();
        mLongDesc = parcel.readString();
        mImageResId = parcel.readInt();
        mStatus = parcel.readString();
    }

    public void setId(Integer id) { mId = id; }

    public void setName(String name)
    {
        mName = name;
    }

    public void setStreamURL(String streamURL)
    {
        mStreamURL = streamURL;
    }

    public void setPodcastURL(String podcastURL)
    {
        mPodcastURL = podcastURL;
    }

    public void setImageURL (String imageURL) { mImageURL = imageURL; }

    public void setFreq(String freq) { mFreq = freq; }

    public void setArea(String area) { mArea = area; }

    public void setDesc(String desc) { mDesc = desc; }

    public void setAddress(String address) { mAddress = address; }

    public void setWeb(String web) { mWeb = web; }

    public void setEmail (String email) { mEmail = email; }

    public void setPhone(String phone) { mPhone = phone; }

    public void setLongDesc(String longDesc) { mLongDesc = longDesc; }

    public void setImageResId (Integer imageResId) { mImageResId = imageResId; }

    public void setStatus (String status) { mStatus = status; }


    public Integer getId() { return mId; }

    public String getName() { return mName; }

    public String getStreamURL() { return mStreamURL; }

    public String getPodcastURL() { return mPodcastURL; }

    public String getImageURL() { return mImageURL; }

    public String getFreq() { return mFreq; }

    public String getArea() { return mArea; }

    public String getDesc() { return mDesc; }

    public String getAddress() { return mAddress; }

    public String getWeb() { return mWeb; }

    public String getEmail() { return mEmail; }

    public String getPhone() { return mPhone; }

    public String getLongDesc() { return mLongDesc; }

    public Integer getImageResId() { return mImageResId; }

    public String getStatus() { return mStatus; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mStreamURL);
        dest.writeString(mPodcastURL);
        dest.writeString(mImageURL);
        dest.writeString(mFreq);
        dest.writeString(mArea);
        dest.writeString(mDesc);
        dest.writeString(mAddress);
        dest.writeString(mWeb);
        dest.writeString(mEmail);
        dest.writeString(mPhone);
        dest.writeString(mLongDesc);
        dest.writeInt(mImageResId);
        dest.writeString(mStatus);
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}

