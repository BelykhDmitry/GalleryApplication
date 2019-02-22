package dmitrybelykh.study.galleryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import dmitrybelykh.study.galleryapplication.Models.Picture;
import dmitrybelykh.study.galleryapplication.R;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private Context mContext; // WeakReference?
    private ArrayList<Picture> mPictureList;

    public PictureAdapter(Context context, ArrayList<Picture> pictureList) {
        this.mContext = context;
        this.mPictureList = pictureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.photo_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picture currentAlbum = mPictureList.get(position);
        holder.bindTo(currentAlbum);
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView mAlbumImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAlbumImage = itemView.findViewById(R.id.photo_image_view);
        }

        void bindTo(Picture picture) {
            Glide.with(mContext).load(picture.getPictureUri()).into(mAlbumImage);
        }
    }

    ;
}
