package dmitrybelykh.study.galleryapplication.Utils;

import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

public class DataStorageReader {

    public DataStorageReader() {
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getPicturesPublicDirectory() {
        File path = null;
        if (isExternalStorageReadable()) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        }
        return path;
    }


    public ArrayList<File> getFilesList(File path) throws IOException {
        if (path == null) return null;
        ArrayList<File> pictures = new ArrayList<>();
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String[] formats = {".jpg", ".png", ".bmp"};
                String name = pathname.getName();
                boolean accept = false;
                for (int i = 0; i < formats.length; i++) {
                    accept = name.contains(formats[i]);
                    if (accept)
                        break;
                }
                return accept;
            }
        };
        path = walkFileTree(path, new FileVisitor() {

            @Override
            public void visitFile(File file) {
                if (filter.accept(file))
                    pictures.add(file);
            }
        });

        return pictures;
    }

    interface FileVisitor {
        void visitFile(File file);
    }

    private File walkFileTree(File path, FileVisitor visitor) throws IOException {
        if (path.isDirectory()) {
            File[] fileArray = path.listFiles();
            if (fileArray != null) {
                for (int i = 0; i < fileArray.length; i++) {
                    walkFileTree(fileArray[i], visitor);
                }
            }
        } else if (path.isFile()) {
            if (path.exists())
                visitor.visitFile(path);
        }
        return path;
    }
}
