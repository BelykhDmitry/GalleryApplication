package dmitrybelykh.study.galleryapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

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
        return animateByX(view, dx)
                .andThen(animateByY(view, dy))
                .andThen(animateByX(view, -dx))
                .andThen(animateDown(view, dy));
    }


    /**
     * Animation with AnimationSet
     *
     * @param view - view to animate (FAB)
     */
    public void animateRectangleWithAnimationSet(View view) {
        int dx = view.getLeft() - view.getPaddingLeft();
        int dy = view.getTop() - view.getPaddingTop() - (mLayoutHigh);
        AnimationSet set = new AnimationSet(false);
        TranslateAnimation ta1 = new TranslateAnimation(0f, -dx, 0, 0);
        ta1.setDuration(animate_time_X);
        ta1.setInterpolator(new LinearInterpolator());
        TranslateAnimation ta2 = new TranslateAnimation(0f, 0, 0, -dy);
        ta2.setDuration(animate_time_Y);
        ta2.setInterpolator(new LinearInterpolator());
        ta2.setStartOffset(animate_time_X);
        TranslateAnimation ta3 = new TranslateAnimation(0f, dx, 0, 0);
        ta3.setDuration(animate_time_X);
        ta3.setInterpolator(new LinearInterpolator());
        ta3.setStartOffset(animate_time_Y + animate_time_X);
        TranslateAnimation ta4 = new TranslateAnimation(0f, 0, 0, dy);
        ta4.setDuration(2 * animate_time_Y);
        ta4.setInterpolator(new BounceInterpolator());
        ta4.setStartOffset(animate_time_X + animate_time_Y + animate_time_X);
        set.addAnimation(ta1);
        set.addAnimation(ta2);
        set.addAnimation(ta3);
        set.addAnimation(ta4);
        view.startAnimation(set);
    }

    public void animateRectangleWithAnimatorSet(View view) {
        int dx = view.getLeft() - view.getPaddingLeft();
        int dy = view.getTop() - view.getPaddingTop() - (mLayoutHigh);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", -dx);
        animator1.setDuration(animate_time_X);
        animator1.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationY", -dy);
        animator2.setDuration(animate_time_Y);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "X", view.getLeft());
        animator3.setDuration(animate_time_X);
        animator3.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view, "Y", view.getTop());
        animator4.setDuration(2 * animate_time_Y);
        animator4.setInterpolator(new BounceInterpolator());
        animatorSet.playSequentially(animator1, animator2, animator3, animator4);
        animatorSet.start();
    }

    private Completable animateByX(View view, int dx) {
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

    private Completable animateByY(View view, int dy) {
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
