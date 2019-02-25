package dmitrybelykh.study.galleryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import dmitrybelykh.study.galleryapplication.Models.Album;
import dmitrybelykh.study.galleryapplication.R;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private Context mContext; // WeakReference?
    private ArrayList<Album> mAlbumList;

    public AlbumsAdapter(Context context, ArrayList<Album> albumList) {
        this.mContext = context;
        this.mAlbumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_album, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album currentAlbum = mAlbumList.get(position);
        holder.bindTo(currentAlbum);
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView mAlbumName;
        private AppCompatImageView mAlbumImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAlbumImage = itemView.findViewById(R.id.album_image_view);
            mAlbumName = itemView.findViewById(R.id.album_name);
        }

        void bindTo(Album album) {
            mAlbumName.setText(album.getmAlbumName());
            Glide.with(mContext).load(album.getmAlbumImageRecource()).into(mAlbumImage);
        }
    }
}
