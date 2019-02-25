package dmitrybelykh.study.galleryapplication.Models;

import android.media.Image;
import android.net.Uri;

public class Album implements Comparable<Album> {

    private String mAlbumName;
    private int mAlbumImageRecource;
    private Uri mImageUri; // TODO: Change from Resources to Uri

    public Album(String mAlbumName, int albumImageRecource) {
        this.mAlbumName = mAlbumName;
        this.mAlbumImageRecource = albumImageRecource;
    }

    public String getmAlbumName() {
        return mAlbumName;
    }

    public int getmAlbumImageRecource() {
        return mAlbumImageRecource;
    }

    @Override
    public int compareTo(Album o) {
        return mAlbumName.compareTo(o.getmAlbumName());
    }
}
