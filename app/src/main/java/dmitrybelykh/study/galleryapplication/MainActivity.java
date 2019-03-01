package dmitrybelykh.study.galleryapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Explode;
import androidx.transition.Fade;
import androidx.transition.Slide;

public class MainActivity extends AppCompatActivity implements BottomNavigationAnimation,
        BottomNavigationSelector {

    private FloatingActionButton fab;

    private BottomNavigationView navigationView;
    private AppBarLayout appBarLayout;

    private int fragmentState = 1;

    private static final String STATE = "fragmentState";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setAllowReturnTransitionOverlap(false);
        getWindow().setAllowEnterTransitionOverlap(false);
        super.onCreate(savedInstanceState);
        //setupTheme();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);

        appBarLayout = findViewById(R.id.app_bar);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_albums:
                    if (fragmentState != ALBUMS)
                        openFragment(ALBUMS);
                    return true;
                case R.id.navigation_photos:
                    if (fragmentState != PHOTOS)
                        openFragment(PHOTOS);
                    return true;
                case R.id.navigation_map:
                    if (fragmentState != MAPS)
                        openFragment(MAPS);
                    return true;
            }
            return false;
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,
                            new AlbumsFragment(),
                            AlbumsFragment.class.getName())
                    .commit();
        } else {
            fragmentState = savedInstanceState.getInt(STATE);
        }
    }

//    private void setupTheme() { //Not working
//        Resources.Theme theme = getTheme();
//        theme.applyStyle(R.style.LightTheme, true);
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE, fragmentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openFragment(int fragmentNumber) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment newFragment = null;
        String tag = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (fragmentNumber) {
            case ALBUMS:
                tag = AlbumsFragment.class.getName();
                newFragment = fragmentManager.findFragmentByTag(tag);
                if (newFragment == null)
                    newFragment = new AlbumsFragment();
                fragmentState = ALBUMS;
                break;
            case PHOTOS:
                tag = TabFragment.class.getName();
                newFragment = fragmentManager.findFragmentByTag(tag);
                if (newFragment == null)
                    newFragment = new TabFragment();
                fragmentState = PHOTOS;
                break;
            case MAPS:
                tag = MapsFragment.class.getName();
                newFragment = fragmentManager.findFragmentByTag(tag);
                if (newFragment == null)
                    newFragment = new MapsFragment();
                fragmentState = MAPS;
                break;
            default:
                return;
        }
        newFragment.setEnterTransition(new Fade());
        transaction.replace(R.id.fragment_container, newFragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    private void hideMenu() {
//        appBarLayout.animate().translationY(-200f)
//                .setDuration(500)
//                .setInterpolator(new AccelerateInterpolator());
        navigationView.animate().translationY(200f)
                .setDuration(500)
                .setInterpolator(new AccelerateInterpolator());
    }

    private void openMenu() {
//        appBarLayout.animate().translationY(0f)
//                .setDuration(500)
//                .setInterpolator(new AccelerateInterpolator());
        navigationView.animate().translationY(0f)
                .setDuration(500)
                .setInterpolator(new AccelerateInterpolator());
    }

    @Override
    public void hideBottomMenu() {
        hideMenu();
    }

    @Override
    public void showBottomMenu() {
        openMenu();
    }


    @Override
    public void selectMenuItem(int itemNumber) {
        if (fragmentState != itemNumber) {
            fragmentState = itemNumber;
            switch (fragmentState) {
                case ALBUMS:
                    navigationView.setSelectedItemId(R.id.navigation_albums);
                    break;
                case PHOTOS:
                    navigationView.setSelectedItemId(R.id.navigation_photos);
                    break;
                case MAPS:
                    navigationView.setSelectedItemId(R.id.navigation_map);
                    break;
            }
        }
    }
}
