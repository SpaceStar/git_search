package com.example.gitsearch;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PhotoFragment extends Fragment {
    public static String EXTRA_BITMAP = "bitmap";

    public static PhotoFragment getInstance(ImageView v) {
        PhotoFragment fragment = new PhotoFragment();
        Drawable drawable = v.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_BITMAP, bitmap);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TransitionManager.beginDelayedTransition(container, new Fade(Fade.IN));
        View fragment = inflater.inflate(R.layout.fragment_photo, container, false);

        ImageView photo = fragment.findViewById(R.id.photo);
        photo.setImageBitmap((Bitmap) getArguments().getParcelable(EXTRA_BITMAP));

        return fragment;
    }
}
