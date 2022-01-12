package com.example.huffman.ui.Image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.huffman.R;

public class ImageFragment extends Fragment {

    private ImageViewModel imageViewModel;
    //private ImageView treeImageFullView;
    private ImageSurfaceView imageSurfaceView;
    View root;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_image, container, false);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        imageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        //treeImageFullView = root.findViewById(R.id.treeImageFullView);


        Bitmap bitmap = getArguments().getParcelable("bitmap");

        if(bitmap != null){
            imageViewModel.setTreeImageBitmap(bitmap);
            //treeImageFullView.setImageBitmap(bitmap);
            imageSurfaceView = root.findViewById(R.id.imageSurfaceView);
            imageSurfaceView.setTreeBitmap(bitmap);
            //ZoomHelper.Companion.addZoomableView(treeImageFullView);

            if(imageViewModel.getX() != Float.MAX_VALUE) imageSurfaceView.setBitmapX(imageViewModel.getX());
            if(imageViewModel.getY() != Float.MAX_VALUE) imageSurfaceView.setBitmapY(imageViewModel.getY());
            if(imageViewModel.getScale() != Float.MAX_VALUE) imageSurfaceView.setBitmapScale(imageViewModel.getScale());

            imageSurfaceView.getBitmapX().removeObservers(getViewLifecycleOwner());
            imageSurfaceView.getBitmapX().observe(getViewLifecycleOwner(), new Observer<Float>() {
                @Override
                public void onChanged(Float aFloat) {
                    imageViewModel.setX(aFloat);
                }
            });

            imageSurfaceView.getBitmapY().removeObservers(getViewLifecycleOwner());
            imageSurfaceView.getBitmapY().observe(getViewLifecycleOwner(), new Observer<Float>() {
                @Override
                public void onChanged(Float aFloat) {
                    imageViewModel.setY(aFloat);
                }
            });

            imageSurfaceView.getBitmapScale().removeObservers(getViewLifecycleOwner());
            imageSurfaceView.getBitmapScale().observe(getViewLifecycleOwner(), new Observer<Float>() {
                @Override
                public void onChanged(Float aFloat) {
                    imageViewModel.setScale(aFloat);
                }
            });
        }

        return root;
    }

}