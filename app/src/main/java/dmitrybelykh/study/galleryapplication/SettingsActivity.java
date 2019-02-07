package dmitrybelykh.study.galleryapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.animation.AnticipateInterpolator;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().rotationBy(360f)
                        .setDuration(1000)
                        .setInterpolator(new AnticipateInterpolator());
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switchCompat = findViewById(R.id.app_theme_switch);
    }

    public void onThemeSwitchClick(View view) {
    }
}
