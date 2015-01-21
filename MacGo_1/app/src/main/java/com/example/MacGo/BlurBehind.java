package com.example.MacGo;

/**
 * Created by KD on 1/12/2015.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.FrameLayout;

public class BlurBehind {

    private static final String KEY_CACHE_BLURRED_BACKGROUND_IMAGE = "KEY_CACHE_BLURRED_BACKGROUND_IMAGE";
    private static final int CONSTANT_BLUR_RADIUS = 1;
    private static final int CONSTANT_DEFAULT_ALPHA = 80;

    private static final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(1);
    private static CacheBlurBehindAndExecuteTask cacheBlurBehindAndExecuteTask;

    private int mAlpha = CONSTANT_DEFAULT_ALPHA;
    private int mFilterColor = -1;

    private enum State {
        READY,
        EXECUTING
    }

    private State mState = State.READY;

    private static BlurBehind mInstance;

    public static BlurBehind getInstance() {
        if (mInstance == null) {
            mInstance = new BlurBehind();
        }
        return mInstance;
    }

    public void execute(FrameLayout frameLayout, Runnable runnable) {
        if (mState.equals(State.READY)) {
            mState = State.EXECUTING;
            cacheBlurBehindAndExecuteTask = new CacheBlurBehindAndExecuteTask(frameLayout, runnable);
            cacheBlurBehindAndExecuteTask.execute();
        }
    }
/*
    public void execute(Activity activity, Runnable runnable) {
        if (mState.equals(State.READY)) {
            mState = State.EXECUTING;
            cacheBlurBehindAndExecuteTask = new CacheBlurBehindAndExecuteTask(activity, runnable);
            cacheBlurBehindAndExecuteTask.execute();
        }
    }*/

    public BlurBehind withAlpha(int alpha) {
        this.mAlpha = alpha;
        return this;
    }

    public BlurBehind withFilterColor(int filterColor) {
        this.mFilterColor = filterColor;
        return this;
    }

    public void setBackground(FrameLayout frameLayout) {
        if (mImageCache.size() != 0) {
            BitmapDrawable bd = new BitmapDrawable(frameLayout.getResources(), mImageCache.get(KEY_CACHE_BLURRED_BACKGROUND_IMAGE));
            bd.setAlpha(mAlpha);
            if (mFilterColor != -1) {
                bd.setColorFilter(mFilterColor, PorterDuff.Mode.DST_ATOP);
            }
            //activity.getWindow().setBackgroundDrawable(bd);
            frameLayout.setBackground(bd);
            mImageCache.remove(KEY_CACHE_BLURRED_BACKGROUND_IMAGE);
            cacheBlurBehindAndExecuteTask = null;
        }
    }

    public void setBackground(Activity activity) {
        if (mImageCache.size() != 0) {
            BitmapDrawable bd = new BitmapDrawable(activity.getResources(), mImageCache.get(KEY_CACHE_BLURRED_BACKGROUND_IMAGE));
            bd.setAlpha(mAlpha);
            if (mFilterColor != -1) {
                bd.setColorFilter(mFilterColor, PorterDuff.Mode.DST_ATOP);
            }
            activity.getWindow().setBackgroundDrawable(bd);
            mImageCache.remove(KEY_CACHE_BLURRED_BACKGROUND_IMAGE);
            cacheBlurBehindAndExecuteTask = null;
        }
    }

    private class CacheBlurBehindAndExecuteTask extends AsyncTask<Void, Void, Void> {
        private FrameLayout frameLayout;
        private Runnable runnable;
        private Activity activity;
        private View decorView;
        private View frameView;
        private Bitmap image;

        public CacheBlurBehindAndExecuteTask(FrameLayout a, Runnable r) {
            frameLayout = a;
            runnable = r;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*
            decorView = activity.getWindow().getDecorView();
            decorView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();*/
            frameView = frameLayout;
            frameView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            frameView.setDrawingCacheEnabled(true);
            frameView.buildDrawingCache();
            image = frameView.getDrawingCache();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap blurredBitmap = Blur.apply(frameLayout.getContext(), image, CONSTANT_BLUR_RADIUS);
            mImageCache.put(KEY_CACHE_BLURRED_BACKGROUND_IMAGE, blurredBitmap);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            frameView.destroyDrawingCache();
            frameView.setDrawingCacheEnabled(false);

            //activity = null;

            runnable.run();

            mState = State.READY;
        }
    }
}