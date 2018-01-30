package com.madhouseapps.prepare.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.madhouseapps.prepare.R;

import java.io.File;
import java.io.IOException;

public class ChapterFragment extends Fragment {
    private File pdfFile;
    private static final String TAG = "ChapterFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chapter, container, false);
        TextView title = getActivity().findViewById(R.id.subTitle);
        String fileName = getArguments().getString("FileName");
        String fileUrl = getArguments().getString("FileURL");
        Typeface poppins_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/poppins_bold.ttf");
        title.setText(fileName);
        title.setTypeface(poppins_bold);
        title.setTextSize(18);
        Log.d(TAG, "onCreateView: " + fileName + "     " + fileUrl);
        final PDFView pdfView = rootView.findViewById(R.id.pdfViewer);
        pdfFile = new File(getContext().getFilesDir(), fileName.toLowerCase().replace(" ", "").replace("'", ""));
        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageReference islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
        Log.d(TAG, "doInBackground: " + islandRef);
        Log.d(TAG, "onSuccess: " + pdfFile.length());
        islandRef.getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: " + pdfFile.getName());
                pdfView.fromFile(pdfFile).load();
                pdfView.useBestQuality(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }                    // Handle any errors

        });
        return rootView;
    }
}
