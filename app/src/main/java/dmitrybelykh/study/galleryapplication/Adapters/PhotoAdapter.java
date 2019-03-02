package dmitrybelykh.study.galleryapplication.Adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.Request;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import dmitrybelykh.study.galleryapplication.Models.Picture;
import dmitrybelykh.study.galleryapplication.R;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    public static final String LOG_TAG = PhotoAdapter.class.getName();

    private WeakReference<Context> mWeakContext; // WeakReference?
    private ArrayList<Picture> mPictureList;
    private OnItemClickListener mListener;

    public PhotoAdapter(Context context, ArrayList<Picture> pictureList) {
        this.mWeakContext = new WeakReference<>(context);
        this.mPictureList = pictureList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater
                .from(mWeakContext.get())
                .inflate(R.layout.item_photo, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Picture currentAlbum = mPictureList.get(position);
        holder.bindTo(currentAlbum);
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }

    public void onAttachListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void onDetachListener() {
        mListener = null;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatImageView mAlbumImage;
        private ImageSwitcher mImageSwitcher;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mAlbumImage = itemView.findViewById(R.id.photo_image_view);
            mImageSwitcher = itemView.findViewById(R.id.imageSwitcher);
            Animation inAnimation = new ScaleAnimation(1.1f, 1f, 1.1f, 1f);
            Animation outAnimation = new ScaleAnimation(1f, 1.1f, 1f, 1.1f);
            inAnimation.setDuration(500);
            outAnimation.setDuration(500);

            mImageSwitcher.setInAnimation(inAnimation);
            mImageSwitcher.setOutAnimation(outAnimation);

            mImageSwitcher.setOnClickListener(click -> {
                mImageSwitcher.showNext();
            });
            itemView.setOnClickListener(this);
        }

        void bindTo(Picture picture) {
            Glide.with(mWeakContext.get()).load(picture.getPictureUri()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mAlbumImage);
        }

        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY);
            itemView.animate().scaleX(0.85f).scaleY(0.85f).setDuration(300);
        }

        public void onItemClear() {
            //itemView.setBackgroundColor(Color.WHITE);
            itemView.animate().scaleX(1f).scaleY(1f).setDuration(300);
        }

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "on Click");
            mListener.onClick(mPictureList.get(getAdapterPosition()), mAlbumImage);
        }
    }

    public interface OnItemClickListener {
        void onClick(Picture picture, ImageView view);
    }
}
