package dmitrybelykh.study.galleryapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Window;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

public class PhotoPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        Fade fade = new Fade();
        Fade fade1 = new Fade();
        fade.setDuration(500);
        fade1.setDuration(500);

        getWindow().setEnterTransition(fade);
        getWindow().setAllowReturnTransitionOverlap(false);
        getWindow().setExitTransition(fade1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);


        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppCompatImageView imageView = findViewById(R.id.preview_view);
        Uri uri = getIntent().getParcelableExtra("image_uri");
        Glide.with(this).load(uri).into(imageView);
        //Glide.with(this).load(uri).placeholder(0).onlyRetrieveFromCache(true).into(imageView);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
