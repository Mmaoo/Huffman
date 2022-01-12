package com.example.huffman.ui.home;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.huffman.huffman.Huffman;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> text = new MutableLiveData<String>();
    private MutableLiveData<String> code = new MutableLiveData<String>();
    private MutableLiveData<Bitmap> treeImageBitmap = new MutableLiveData<Bitmap>();
    private MutableLiveData<ArrayList<Huffman.Triple<String, List<Boolean>,Integer>>> codeBook
            = new MutableLiveData<ArrayList<Huffman.Triple<String, List<Boolean>,Integer>>>();
    private MutableLiveData<Float> entropy = new MutableLiveData<Float>();
    private MutableLiveData<Float> codeLengthMean = new MutableLiveData<Float>();

    public void setText(String text) {
        this.text.setValue(text);
    }

    public MutableLiveData<String> getText() {
        return text;
    }

    public void setCode(String code) {
        this.code.setValue(code);
    }

    public MutableLiveData<String> getCode() {
        return code;
    }

    public void setTreeImageBitmap(Bitmap bitmap) {
        this.treeImageBitmap.setValue(bitmap);
    }

    public MutableLiveData<Bitmap> getTreeImageBitmap() {
        return treeImageBitmap;
    }

    public void setCodeBook(ArrayList<Huffman.Triple<String, List<Boolean>, Integer>> codeBook) {
        this.codeBook.setValue(codeBook);
    }

    public MutableLiveData<ArrayList<Huffman.Triple<String, List<Boolean>, Integer>>> getCodeBook() {
        return codeBook;
    }

    public void setEntropy(Float entropy) {
        this.entropy.setValue(entropy);
    }

    public MutableLiveData<Float> getEntropy() {
        return entropy;
    }

    public void setCodeLengthMean(Float codeLengthMean) {
        this.codeLengthMean.setValue(codeLengthMean);
    }

    public MutableLiveData<Float> getCodeLengthMean() {
        return codeLengthMean;
    }
}