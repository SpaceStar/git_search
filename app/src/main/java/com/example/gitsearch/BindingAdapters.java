package com.example.gitsearch;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class BindingAdapters {

    private BindingAdapters() {}

    @BindingAdapter("app:loadImage")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .circleCrop()
                .into(view);
    }
}
