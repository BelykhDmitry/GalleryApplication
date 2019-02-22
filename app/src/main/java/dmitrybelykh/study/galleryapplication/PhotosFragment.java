package dmitrybelykh.study.galleryapplication;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dmitrybelykh.study.galleryapplication.Adapters.PictureAdapter;
import dmitrybelykh.study.galleryapplication.Models.Picture;
import dmitrybelykh.study.galleryapplication.Utils.DataStorageReader;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PhotosFragment extends Fragment {
    public static final String LOG_TAG = PhotosFragment.class.getName();

    private WeakReference<BottomNavigationAnimation> weakBottomNavigationAnimation;

    private RecyclerView mPictureRecyclerView;
    private PictureAdapter mPictureAdapter;
    final private ArrayList<Picture> mList = new ArrayList<>();
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        mPictureRecyclerView = rootView.findViewById(R.id.photos_recycler_view);

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        mPictureAdapter = new PictureAdapter(getContext(), mList);
        mPictureRecyclerView.setAdapter(mPictureAdapter);

        int columnCount = getResources().getInteger(R.integer.grid_column_count);
        mPictureRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnCount));

        mPictureRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    weakBottomNavigationAnimation.get().hideBottomMenu();
                } else if (dy < 0) {
                    weakBottomNavigationAnimation.get().showBottomMenu();
                }
            }
        });
    }

    interface OnPhotosFragmentInteractionLustener {
    }

    @Override
    public void onResume() {
        super.onResume();
        handler = new Handler();
        final ArrayList<Uri> list = new ArrayList<>();
        handler.post(() -> {
            try {
                new DataStorageReader().getFilesUriList(
                        DataStorageReader.getPicturesPublicDirectory(), list);
                Log.d(LOG_TAG, "Uri's finished: " + list.size());
                mList.clear();
                for (Uri uri : list) {
                    mList.add(new Picture(uri));
                }
                mPictureAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        weakBottomNavigationAnimation =
                new WeakReference<BottomNavigationAnimation>((BottomNavigationAnimation) context);
    }

    @Override
    public void onDetach() {
        weakBottomNavigationAnimation.clear();
        super.onDetach();
    }
}
