package com.ghosts.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Ahmed Allam on 2/23/2016.
 */
public class ImageDialog extends DialogFragment {
    private ImageView mImageView;
    private static String mImageKEY = "image Key";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        UUID uuid = (UUID) getArguments().getSerializable(mImageKEY);
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        Crime crime = crimeLab.getCrimeById(uuid);

        File photoFile = crimeLab.getPhotoFile(crime);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath(), options);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.image_dialog_lauout, null);
        mImageView = (ImageView) v.findViewById(R.id.image_view_dialog);
        if (photoFile.exists() || photoFile != null)
            mImageView.setImageBitmap(bitmap);
        else
            Toast.makeText(getContext(), R.string.no_photo_selected, Toast.LENGTH_LONG).show();

        return new AlertDialog.Builder(getActivity()).setView(v).create();
    }

    public static ImageDialog newInstance(UUID uuid) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(mImageKEY, uuid);
        ImageDialog imageDialog = new ImageDialog();
        imageDialog.setArguments(bundle);
        return imageDialog;
    }

}
