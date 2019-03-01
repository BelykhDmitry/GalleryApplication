package dmitrybelykh.study.galleryapplication;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import dmitrybelykh.study.galleryapplication.Adapters.ViewPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class TabFragment extends Fragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);

        mViewPager = rootView.findViewById(R.id.viewPager);
        mTabLayout = rootView.findViewById(R.id.tabLayout);

        FragmentManager fm = getChildFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(fm);

        // как не пересоздавать фрагменты?  -- Возможно вопрос решит переход на MVP - презентер не будет пересоздаваться
        adapter.addFragment(new PhotosFragment(), "Photos");
        adapter.addFragment(new FavoritesFragment(), "Favorites");

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }
}
