package com.example.huffman.ui.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder surfaceHolder = null;
    private Bitmap treeBitmap = null;
    private Canvas canvas = null;
    private final Object blocade = new Object();
    private Thread thread = null;
    private boolean threadRunning = false;
    private Paint paint = null;

    //private float x = 0, y = 0;
    //private float scale = 100;

    private MutableLiveData<Float> x = new MutableLiveData<Float>((float)0);
    private MutableLiveData<Float> y = new MutableLiveData<Float>((float)0);
    private MutableLiveData<Float> scale = new MutableLiveData<Float>((float)100);

    public ImageSurfaceView(Context context, AttributeSet attrs) {
        super(context,attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // poczatkowe ustawienia pedzla
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * startuje watek wyswietlający bitmape
     */
    public void resumePaint(){
        thread = new Thread(this);
        threadRunning = true;
        thread.start();
    }

    /**
     * wylaczenie watku wyswietlajacego bitmape
     */
    public void pausePaint(){
        threadRunning = false;
    }

    /**
     * przechwytywanie zdarzenia dotyku ekranu
     * @param event aktualne zdarzenie
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        synchronized (blocade){
            switch(event.getAction()){
                case MotionEvent.ACTION_MOVE:
                    if(event.getHistorySize() > 0) {
                        try {
                            x.setValue(x.getValue() + event.getX(0) - event.getHistoricalX(0, 0));
                            y.setValue(y.getValue() + event.getY(0) - event.getHistoricalY(0, 0));
                            if(event.getPointerCount() > 1){
                                float d1 = (float)Math.sqrt(Math.pow((event.getX(0) - event.getX(1)),2)+Math.pow((event.getY(0) - event.getY(1)),2));
                                float d2 = (float)Math.sqrt(Math.pow((event.getHistoricalX(0,0) - event.getHistoricalX(1, 0)),2)+Math.pow((event.getHistoricalY(0,0) - event.getHistoricalY(1, 0)),2));
                                scale.setValue(scale.getValue() * d1/d2);
                                if(scale.getValue() < 10) scale.setValue((float)10);
                                else if(scale.getValue() > 100) scale.setValue((float)100);
                            }
                        }catch (NullPointerException e){e.printStackTrace();}
                    }
                    break;
//                case MotionEvent.ACTION_SCROLL:
//                    if(event.getHistorySize() > 0) {
//                        Log.w(this.getClass().toString(),event.getAxisValue(0, 0)+", "+event.getHistoricalAxisValue(0, 0));
//                        scale += event.getAxisValue(0, 0) - event.getHistoricalAxisValue(0, 0);
//                    }
//                    break;
            }
        }
        return true;
    }

    public boolean performClick(){
        return super.performClick();
    }

    /**
     * metoda dla watku rysujacego bitmape na ekranie
     */
    @Override
    public void run() {
        while(threadRunning){
            Canvas surfaceCanvas = null;
            try {
                synchronized (surfaceHolder){
                    if(!surfaceHolder.getSurface().isValid()) continue;
                    surfaceCanvas = surfaceHolder.lockCanvas(null);
                    synchronized (blocade){
                        if(threadRunning){
                            // rysowanie bitmapy na ekranie
                            surfaceCanvas.drawARGB(255,255,255,255);
                            Bitmap bitmap = Bitmap.createScaledBitmap(treeBitmap,(int)(treeBitmap.getWidth()*(scale.getValue()/100)),(int)(treeBitmap.getHeight()*(scale.getValue()/100)),false);
                            surfaceCanvas.drawBitmap(bitmap, x.getValue(), y.getValue(), null);
                        }
                    }
                }
            } finally{
                if(surfaceCanvas != null){
                    surfaceHolder.unlockCanvasAndPost(surfaceCanvas);
                }
            }
            try{
                Thread.sleep(1000/60);
            }catch (InterruptedException e){}
        }
    }


    /**
     * metoda tworzenia powiezchni do rysowania
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(treeBitmap==null) {
            // tworzenie nowej bitmapy
            treeBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            // tworzenie plotna do rysowania
            canvas = new Canvas(treeBitmap);
            // zamalowanie bitmapy na bialo
            canvas.drawARGB(255,255,255,255);
        }else{ // mBitmapa istnieje
            canvas = new Canvas(treeBitmap);
        }
        // start watku wysietlajacego bitmape
        resumePaint();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * metoda niszczaca powierzchnie
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pausePaint();
        threadRunning = false;
    }

    /**
     * ustawia kolor pedzla
     * @param color kolor
     */
    public void setPaintColor(int color) {paint.setColor(color);}

    /**
     * ustawia szerokosc pedzla
     * @param width szerokosc
     */
    public void setPaintWidth(int width) {paint.setStrokeWidth(width);}
    public void clear(){
        canvas.drawARGB(255,255,255,255);
    }

    /**
     * zapisywanie stanu powierzczni
     * @param outState
     */
    public void saveState(@NonNull Bundle outState) {
        // zapisywanie koloru pedzla
        outState.putInt("mFarbaColor",paint.getColor());
        // zapisywanie bitmapy
        outState.putParcelable("mBitmapa",treeBitmap);
        // zapisywanie szerokosci pedzla
        outState.putFloat("mFarbaWidth",paint.getStrokeWidth());
    }

    /**
     * przywracanie stanu powierzchni
     * @param savedInstanceState
     */
    public void restoreState(@NonNull Bundle savedInstanceState) {
        // przywracanie bitmapy
        Bitmap mBitmapaOld = savedInstanceState.getParcelable("mBitmapa");
        if(mBitmapaOld != null){
            treeBitmap = mBitmapaOld;
        }
        // przywracanie koloru pedzla
        int mFarbaColor = savedInstanceState.getInt("mFarbaColor",-1);
        if(mFarbaColor!=-1){paint.setColor(mFarbaColor);}
    }

    public void setTreeBitmap(Bitmap treeBitmap) {
        this.treeBitmap = treeBitmap;
    }

    public MutableLiveData<Float> getBitmapX() {
        return x;
    }

    public void setBitmapX(Float x) {
        this.x.setValue(x);
    }

    public MutableLiveData<Float> getBitmapScale() {
        return scale;
    }

    public void setBitmapScale(Float scale) {
        this.scale.setValue(scale);
    }

    public MutableLiveData<Float> getBitmapY() {
        return y;
    }

    public void setBitmapY(Float y) {
        this.y.setValue(y);
    }
}
