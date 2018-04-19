package com.bokoch.flickerapi;


public class GalleryItem {
    private String mId;
    private String mUrl;
    private String mCaption;

    public GalleryItem(String id, String url, String caption) {
        mId = id;
        mUrl = url;
        mCaption = caption;
    }
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }
}
