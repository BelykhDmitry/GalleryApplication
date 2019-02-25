package dmitrybelykh.study.galleryapplication;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import dmitrybelykh.study.galleryapplication.Adapters.AlbumsAdapter;
import dmitrybelykh.study.galleryapplication.Models.Album;
import dmitrybelykh.study.galleryapplication.Utils.DataStorageReader;
import dmitrybelykh.study.galleryapplication.Utils.PermissionManager;

public class AlbumsFragment extends Fragment {

    private static final String LOG_TAG = AlbumsFragment.class.getName();

    private RecyclerView mAlbumsRecyclerView;
    private ArrayList<Album> mData = new ArrayList<>();
    private WeakReference<BottomNavigationAnimation> weakBottomNavigationAnimation;
    private AlbumsAdapter adapter;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        mAlbumsRecyclerView = rootView.findViewById(R.id.albums_recycler_view);
        fab = rootView.findViewById(R.id.refresh_fab);
        fab.setOnClickListener(refresh);

        setupRecyclerView();
        return rootView;
    }

    private final View.OnClickListener refresh = (view) -> {
        generateAlbumData();
    };

    private void setupRecyclerView() {
        adapter = new AlbumsAdapter(getContext(), mData);
        mAlbumsRecyclerView.setAdapter(adapter);

        int columnCount = getResources().getInteger(R.integer.grid_column_count);
        mAlbumsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnCount));

        int moveDirs = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                | ItemTouchHelper.START | ItemTouchHelper.END;
        int swipeDirs = 0;
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(moveDirs, swipeDirs) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        int from = viewHolder.getAdapterPosition();
                        int to = target.getAdapterPosition();
                        Collections.swap(mData, from, to);
                        adapter.notifyItemMoved(from, to);
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    }
                });
        helper.attachToRecyclerView(mAlbumsRecyclerView);

        mAlbumsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @Override
    public void onResume() {
        super.onResume();

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

    private void generateAlbumData() {
        if (PermissionManager.requestForPermission(getActivity())) {
            mData.clear();
            mData.add(new Album("Sunset", R.drawable.sunrise));
            mData.add(new Album("Sunrise", R.drawable.sunrise));
            mData.add(new Album("Pictures", R.drawable.sunrise));
            mData.add(new Album("Sunset1", R.drawable.sunrise));
            mData.add(new Album("Sunset2", R.drawable.sunrise));
            mData.add(new Album("Sunset3", R.drawable.sunrise));
            adapter.notifyDataSetChanged();
        } else {
            // showError();
        }

    }

    interface OnAlbumsFragmentInteractionLustener {
    }
}
