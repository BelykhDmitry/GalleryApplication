package dmitrybelykh.study.galleryapplication;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {

    private WeakReference<BottomNavigationSelector> weakBottomNaviagationSelector;


    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        weakBottomNaviagationSelector =
                new WeakReference<BottomNavigationSelector>((BottomNavigationSelector) context);
    }

    @Override
    public void onDetach() {
        weakBottomNaviagationSelector.clear();
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        weakBottomNaviagationSelector.get().selectMenuItem(BottomNavigationSelector.MAPS);
    }

    interface OnMapsFragmentInteractionLustener {
    }
}
