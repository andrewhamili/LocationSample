package com.hamiliserver.locationsample;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadImageFragment extends Fragment {

    Button btnChooseImage;

    View view;


    public UploadImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_upload_image, null);

        btnChooseImage = view.findViewById(R.id.btnChooseImage);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
                /*

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seiect Picture"), 1);
*/
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            String encodedString = "";

            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();

            encodedString = Base64.encodeToString(bytes, 0);

            HashMap<String, Object> json = new HashMap<>();

            json.put("base64Image", encodedString);
            encodedString = new Gson().toJson(json);

            Toast.makeText(getActivity().getApplicationContext(), encodedString, Toast.LENGTH_SHORT).show();
        }
    }
}
