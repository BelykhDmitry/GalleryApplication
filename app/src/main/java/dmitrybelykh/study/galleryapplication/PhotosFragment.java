package dmitrybelykh.study.galleryapplication;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

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

    private ViewGroup root;
    private View sharedView;
    private ViewGroup sharedViewParent;
    private ViewGroup.LayoutParams sharedLayoutParams;

    public PhotosFragment() {
        Log.d(LOG_TAG, "Constructor PhotosFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        root = (ViewGroup) rootView;
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
        restoreSharedElement();
    }

    void restoreSharedElement() {
        if (sharedView != null) {
            root.removeView(sharedView);
            sharedViewParent.addView(sharedView, 0);
            sharedView.setLayoutParams(sharedLayoutParams);
            // Clear shared elements
            sharedView = null;
            sharedViewParent = null;
            sharedLayoutParams = null;
        }
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
        moveViewToRootRecyclerLayout(imageView);
        imageView.post(() -> {
            Intent intent = new Intent(getActivity(), PhotoPreviewActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, "transition_photo");
            Bundle bundle = options.toBundle();
            intent.putExtra("image_uri", picture.getPictureUri());
            startActivity(intent, options.toBundle());
        });
    };

    void moveViewToRootRecyclerLayout(View view) {
        // Save shared element params
        sharedView = view;
        sharedViewParent = (ViewGroup) view.getParent();
        sharedLayoutParams = view.getLayoutParams();
        // Get view Rectangle
        Rect imageRect = new Rect();
        view.getGlobalVisibleRect(imageRect);
        // Get root Rectangle
        Rect rootRectangle = new Rect();
        root.getGlobalVisibleRect(rootRectangle);
        // Remove view from Root
        ((ViewGroup) view.getParent()).removeView(view);
        // Get view layout params
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(imageRect.width(), imageRect.height());
        // Setup layout params
        layoutParams.setMargins(imageRect.left, imageRect.top - rootRectangle.top, 0, 0);
        // Add view to the root
        root.addView(view);
        // Set layout params to the view
        view.setLayoutParams(layoutParams);
    }
}
