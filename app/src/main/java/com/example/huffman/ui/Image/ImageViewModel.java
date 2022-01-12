package com.example.huffman.ui.Image;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImageViewModel extends ViewModel {

    private Bitmap treeImageBitmap;
    private float x = Float.MAX_VALUE, y = Float.MAX_VALUE, scale = Float.MAX_VALUE;

    public void setTreeImageBitmap(Bitmap treeImageBitmap) {
        this.treeImageBitmap = treeImageBitmap;
    }

    public Bitmap getTreeImageBitmap() {
        return treeImageBitmap;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }
}