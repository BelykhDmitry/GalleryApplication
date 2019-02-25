package dmitrybelykh.study.galleryapplication.Models;

import android.net.Uri;

public class Picture {
    private Uri mPictureUri;

    public Picture(Uri uri) {
        this.mPictureUri = uri;
    }

    public Uri getPictureUri() {
        return mPictureUri;
    }
}
