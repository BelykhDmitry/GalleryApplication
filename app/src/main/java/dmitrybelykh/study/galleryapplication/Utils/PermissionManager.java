package dmitrybelykh.study.galleryapplication.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class PermissionManager {

    public static final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int EXTERNAL_REQUEST = 138;

    public static boolean requestForPermission(Activity context) {
        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd(context)) {
                isPermissionOn = false;
                context.requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public static boolean canAccessExternalSd(Context context) {
        return (hasPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private static boolean hasPermission(Context context, String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, perm));
    }
}
