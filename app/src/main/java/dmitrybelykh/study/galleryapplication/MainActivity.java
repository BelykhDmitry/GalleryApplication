package dmitrybelykh.study.galleryapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    private MyAnimation myAnimation;

    private int fragmentState = 1;

    private static final int ALBUMS = 1;
    private static final int PHOTOS = 2;
    private static final int MAPS = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setupTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        myAnimation = new MyAnimation(point);
        myAnimation.setAnimationSpeed(5f);

        fab = findViewById(R.id.fab);

        BottomNavigationView navigationView = findViewById(R.id.navigation);
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
        openFragment(ALBUMS);
    }

//    private void setupTheme() { //Not working
//        Resources.Theme theme = getTheme();
//        theme.applyStyle(R.style.LightTheme, true);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        switch (fragmentNumber) {
            case ALBUMS:
                newFragment = fragmentManager.findFragmentByTag(AlbumsFragment.class.getName());
                if (newFragment == null)
                    newFragment = new AlbumsFragment();
                tag = AlbumsFragment.class.getName();
                fragmentState = ALBUMS;
                break;
            case PHOTOS:
                newFragment = fragmentManager.findFragmentByTag(PhotosFragment.class.getName());
                if (newFragment == null)
                    newFragment = new PhotosFragment();
                tag = PhotosFragment.class.getName();
                fragmentState = PHOTOS;
                break;
            case MAPS:
                newFragment = fragmentManager.findFragmentByTag(MapsFragment.class.getName());
                if (newFragment == null)
                    newFragment = new MapsFragment();
                tag = MapsFragment.class.getName();
                fragmentState = MAPS;
                break;
            default:
                return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, tag)
                .addToBackStack(null)
                .commit();
    }
}
