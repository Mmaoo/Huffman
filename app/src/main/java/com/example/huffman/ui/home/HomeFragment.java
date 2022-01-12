package com.example.huffman.ui.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.huffman.MainActivity;
import com.example.huffman.R;
import com.example.huffman.huffman.Huffman;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View root;

    EditText textEditText;
    Button encodeButton;
    //Button decodeButton;
    EditText codeEditText;
    TextView codeLabelTextView;
    ImageView treeImageView;
    TableLayout codeBookTableLayout;
    TextView entropyTextView;
    TextView entropyLabelTextView;
    TextView codeLengthMeanTextView;
    TextView codeLengthMeanLabelTextView;

    Huffman huffman = new Huffman();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.w(this.getClass().toString(),"onCreateView: "+this.toString());
        root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        textEditText = root.findViewById(R.id.TextEditText);
        encodeButton = root.findViewById(R.id.encodeButton);
        //decodeButton = root.findViewById(R.id.decodeButton);
        codeEditText = root.findViewById(R.id.codeEditText);
        codeLabelTextView = root.findViewById(R.id.codeLabelTextView);
        treeImageView = root.findViewById(R.id.treeImageView);
        codeBookTableLayout = root.findViewById(R.id.codeBookTableLayout);
        entropyTextView = root.findViewById(R.id.entropyTextView);
        entropyLabelTextView = root.findViewById(R.id.entropyLabelTextView);
        codeLengthMeanTextView = root.findViewById(R.id.codeLengthMeanTextView);
        codeLengthMeanLabelTextView = root.findViewById(R.id.codeLengthMeanLabelTextView);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String text) {
                if(text != null) {
                    entropyTextView.setText(text.toString());
                }
            }
        });

        homeViewModel.getCode().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String code) {
                if(code != null){
                    codeEditText.setVisibility(View.VISIBLE);
                    codeEditText.setText(code);
                    codeLabelTextView.setVisibility(View.VISIBLE);
                }else{
                    codeEditText.setVisibility(View.GONE);
                    codeLabelTextView.setVisibility(View.GONE);
                }
            }
        });

        homeViewModel.getCodeBook().observe(getViewLifecycleOwner(), new Observer<ArrayList<Huffman.Triple<String, List<Boolean>, Integer>>>() {
            @Override
            public void onChanged(ArrayList<Huffman.Triple<String, List<Boolean>, Integer>> codeBook) {
                Log.w("CodeBook","CodeBook, childCount="+codeBookTableLayout.getChildCount());
                if(codeBookTableLayout.getChildCount() > 1) codeBookTableLayout.removeViews(1,codeBookTableLayout.getChildCount()-1);
                if(codeBook != null) {
                    for (Huffman.Triple<String, List<Boolean>, Integer> triple : codeBook) {
                        View tableRow = View.inflate(codeBookTableLayout.getContext(), R.layout.table_row_code_book, null);
                        codeBookTableLayout.addView(tableRow);
                        TextView charTextView = tableRow.findViewById(R.id.charTextView);
                        charTextView.setText(triple.first);

                        TextView codeTextView = tableRow.findViewById(R.id.codeTextView);
                        codeTextView.setText(huffman.toBinaryString(triple.second));

                        TextView probTextView = tableRow.findViewById(R.id.probTextView);
                        probTextView.setText((float) triple.third / huffman.size() + " (" + triple.third + "/" + huffman.size() + ")");
                    }
                    codeBookTableLayout.setVisibility(View.VISIBLE);
                }else{
                    codeBookTableLayout.setVisibility(View.GONE);
                }
            }
        });

        homeViewModel.getEntropy().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float entropy) {
                if(entropy != null) {
                    entropyLabelTextView.setVisibility(View.VISIBLE);
                    entropyTextView.setText(entropy.toString());
                    entropyTextView.setVisibility(View.VISIBLE);
                }else{
                    entropyLabelTextView.setVisibility(View.GONE);
                    entropyTextView.setVisibility(View.GONE);
                }
            }
        });

        homeViewModel.getCodeLengthMean().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float codeLengthMean) {
                if(codeLengthMean != null) {
                    codeLengthMeanLabelTextView.setVisibility(View.VISIBLE);
                    codeLengthMeanTextView.setText(codeLengthMean.toString());
                    codeLengthMeanTextView.setVisibility(View.VISIBLE);
                }else{
                    codeLengthMeanLabelTextView.setVisibility(View.GONE);
                    codeLengthMeanTextView.setVisibility(View.GONE);
                }
            }
        });
        ((View) treeImageView.getParent()).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Bitmap bitmap = homeViewModel.getTreeImageBitmap().getValue();
                if(bitmap != null){
                    treeImageView.setVisibility(View.VISIBLE);
                    int newWidth = ((View)treeImageView.getParent()).getWidth();
                    int newHeight = (int)(bitmap.getHeight()*((float)newWidth/bitmap.getWidth()));
                    Log.w("bitmap","obsOnWFC, newWidth="+newWidth+", newHeight"+newHeight+", bitmap"+bitmap.toString());
                    Bitmap newBitmap;
                    if(newWidth > 0) {newBitmap = Bitmap.createScaledBitmap(bitmap,newWidth,newHeight,false);
                        treeImageView.setImageBitmap(newBitmap);}
                }else{
                    treeImageView.setVisibility(View.GONE);
                }
            }
        });


        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.setCode( huffman.encode(textEditText.getText().toString()) );
                homeViewModel.setTreeImageBitmap(huffman.drawTree());
                homeViewModel.setEntropy(huffman.entropy());
                homeViewModel.setCodeLengthMean(huffman.codeLengthMean());
                homeViewModel.setCodeBook(huffman.getCodeBook());
            }
        });

//        decodeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textEditText.setText("ala ma kota, a kot ma ale");
//            }
//        });

        treeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Bitmap bitmap = homeViewModel.getTreeImageBitmap().getValue();
                if(bitmap != null){
                    bundle.putParcelable("bitmap",bitmap);
                    try {
                        ((MainActivity) getActivity()).navigate(R.id.navigation_image, bundle);
                    }catch (NullPointerException e){ e.printStackTrace(); }
                }
            }
        });
        return root;
    }
}