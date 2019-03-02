package dmitrybelykh.study.galleryapplication;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.PathMotion;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import android.transition.ChangeImageTransform;
import android.transition.TransitionSet;

public class PhotoPreviewActivity extends AppCompatActivity {

    private AppCompatImageView imageView;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setupTransitions();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        setupToolbar();

        setupImageView();
    }

    private void setupImageView() {
        imageView = findViewById(R.id.preview_view);
        Uri uri = getIntent().getParcelableExtra("image_uri");
        Glide.with(this)
                .load(uri)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("DDDD", "Resource ready");
                        return false;
                    }
                })
                .into(imageView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(click -> onBackPressed());
    }

    private void setupTransitions() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Fade fade = new Fade();
        fade.setDuration(500);
        ChangeBounds bounds = new ChangeBounds();
        bounds.setPathMotion(new ArcMotion());

        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(bounds).addTransition(new ChangeImageTransform())
                .setDuration(500)
                .setInterpolator(new AccelerateInterpolator());

        getWindow().setSharedElementEnterTransition(transitionSet);
        getWindow().setSharedElementExitTransition(transitionSet);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1f,
                    Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
