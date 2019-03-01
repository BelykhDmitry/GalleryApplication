package dmitrybelykh.study.galleryapplication;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import dmitrybelykh.study.galleryapplication.Adapters.PhotoAdapter;
import dmitrybelykh.study.galleryapplication.Models.Picture;
import dmitrybelykh.study.galleryapplication.Utils.DataStorageReader;
import dmitrybelykh.study.galleryapplication.Utils.PermissionManager;

public class PhotosFragment extends Fragment {

    public static final String LOG_TAG = PhotosFragment.class.getName();

    private WeakReference<BottomNavigationAnimation> weakBottomNavigationAnimation;
    private WeakReference<BottomNavigationSelector> weakBottomNaviagationSelector;


    private RecyclerView mPictureRecyclerView;
    private PhotoAdapter mPictureAdapter;
    final private ArrayList<Picture> mList = new ArrayList<>();
    //private Handler handler;

    public PhotosFragment() {
        Log.d(LOG_TAG, "Constructor PhotosFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        mPictureRecyclerView = rootView.findViewById(R.id.photos_recycler_view);

        setupRecyclerView();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        mPictureAdapter.onDetachListener();
        super.onDestroyView();
    }

    private void setupRecyclerView() {
        mPictureAdapter = new PhotoAdapter(getContext(), mList);
        mPictureAdapter.onAttachListener(listener);
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
        mPictureRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View view = rv.findChildViewUnder(e.getX(), e.getY());
                Log.d(LOG_TAG, Integer.toString(e.getAction()));
                if (view != null)
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            view.animate()
                                    .setDuration(200)
                                    .scaleX(0.9f)
                                    .scaleY(0.9f);
                            break;
                        //case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            view.animate()
                                    .setDuration(200)
                                    .scaleX(1f)
                                    .scaleY(1f);
                            break;

                    }
                return super.onInterceptTouchEvent(rv, e);
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                super.onTouchEvent(rv, e);
            }
        });
    }

    interface OnPhotosFragmentInteractionLustener {
    }

    @Override
    public void onResume() {
        super.onResume();
        weakBottomNaviagationSelector.get().selectMenuItem(BottomNavigationSelector.PHOTOS);
        final ArrayList<Uri> list = new ArrayList<>();
        if (PermissionManager.requestForPermission(getActivity())) {
            new Handler().post(() -> {
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
        weakBottomNavigationAnimation.get().showBottomMenu();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        weakBottomNavigationAnimation =
                new WeakReference<BottomNavigationAnimation>((BottomNavigationAnimation) context);
        weakBottomNaviagationSelector =
                new WeakReference<BottomNavigationSelector>((BottomNavigationSelector) context);
    }

    @Override
    public void onDetach() {
        weakBottomNavigationAnimation.clear();
        weakBottomNaviagationSelector.clear();
        super.onDetach();
    }

    PhotoAdapter.OnItemClickListener listener = (picture, imageView) -> {
        Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, "transition_photo");
        Bundle bundle = options.toBundle();
        intent.putExtra("image_uri", picture.getPictureUri());
        startActivity(intent, options.toBundle());
    };
}
