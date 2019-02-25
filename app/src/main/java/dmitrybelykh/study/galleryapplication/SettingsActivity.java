package dmitrybelykh.study.galleryapplication;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().rotationBy(360f)
                        .setDuration(1000)
                        .setInterpolator(new AnticipateOvershootInterpolator());
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switchCompat = findViewById(R.id.app_theme_switch);
    }

    public void onThemeSwitchClick(View view) {
    }
}
