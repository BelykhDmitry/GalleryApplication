package dmitrybelykh.study.galleryapplication;

import android.graphics.Point;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.core.view.ViewCompat;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.CompletableSubject;

public class MyAnimation {

    // TODO: Add Builder and more abstraction
    private long animate_time_X = 500;
    private long animate_time_Y = 700;
    private int mLayoutHigh = 500;
    private int mLayoutWidth = 500;
    private Point mPoint;

    public void setToolbarHigh(int layoutHigh) {
        this.mLayoutHigh = layoutHigh;
    }

    public void setLayoutWidth(int layoutWidth) {
        this.mLayoutWidth = layoutWidth;
    }

    public void setAnimationSpeed(float speed) {
        if (speed != 0 && mPoint != null) {
            animate_time_X = (long) (mPoint.x / speed);
            animate_time_Y = (long) (mPoint.y / speed);
        }
    }

    public MyAnimation(Point displaySize) {
        mPoint = displaySize;
    }

    public Completable moveRectange(View view) {
        int dx = view.getLeft() - view.getPaddingLeft();
        int dy = view.getTop() - view.getPaddingTop() - (mLayoutHigh);
        return animateLeft(view, dx)
                .andThen(animateUp(view, dy))
                .andThen(animateRight(view, dx))
                .andThen(animateDown(view, dy));
    }

    private Completable animateLeft(View view, int dx) {
        CompletableSubject animationSubject = CompletableSubject.create();
        return animationSubject.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                ViewCompat.animate(view)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(animate_time_X)
                        .xBy(-dx)
                        .withEndAction(() ->
                                animationSubject.onComplete()
                        );
            }
        });
    }

    private Completable animateUp(View view, int dy) {
        CompletableSubject animationSubject = CompletableSubject.create();
        return animationSubject.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                ViewCompat.animate(view)
                        .setDuration(animate_time_Y)
                        .yBy(-dy)
                        .withEndAction(() ->
                                animationSubject.onComplete()
                        );
            }
        });
    }

    private Completable animateRight(View view, int dx) {
        CompletableSubject animationSubject = CompletableSubject.create();
        return animationSubject.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                ViewCompat.animate(view)
                        .setDuration(animate_time_X)
                        .xBy(dx)
                        .withEndAction(() ->
                                animationSubject.onComplete()
                        );
            }
        });
    }

    private Completable animateDown(View view, int dy) {
        CompletableSubject animationSubject = CompletableSubject.create();
        return animationSubject.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                ViewCompat.animate(view)
                        .setInterpolator(new BounceInterpolator())
                        .setDuration(animate_time_Y * 2)
                        .yBy(dy)
                        .withEndAction(() ->
                                animationSubject.onComplete()
                        );
            }
        });
    }
}
