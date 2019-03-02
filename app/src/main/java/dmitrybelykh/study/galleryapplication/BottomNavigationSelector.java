package dmitrybelykh.study.galleryapplication;

public interface BottomNavigationSelector {

    public static final int ALBUMS = 1;
    public static final int PHOTOS = 2;
    public static final int MAPS = 3;

    void selectMenuItem(int itemNumber);
}
