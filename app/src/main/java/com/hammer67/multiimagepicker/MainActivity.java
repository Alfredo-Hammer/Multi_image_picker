package com.hammer67.multiimagepicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageSwitcher images;
    public Button btnPrevious, btnNext, pickImagesBtn;

    ArrayList<Uri> imageUris;
    private static final int PICK_IMAGES_CODE = 0;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        images = findViewById(R.id.images);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        pickImagesBtn = findViewById(R.id.pickImagesBtn);

        imageUris = new ArrayList<>();

        images.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });

        pickImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImagesIntent();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0){
                    position--;
                    images.setImageURI(imageUris.get(position));
                }
                else {
                    Toast.makeText(MainActivity.this, "No hay mas imagenes...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < imageUris.size() -1){
                    position++;
                    images.setImageURI(imageUris.get(position));
                }
                else {
                    Toast.makeText(MainActivity.this, "No hay mas imagenes...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickImagesIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    //Elegir multiple imagenes...
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                    images.setImageURI(imageUris.get(0));
                    position = 0;

                } else {
                    //Elegir una unica opcion...
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    images.setImageURI(imageUris.get(0));
                    position = 0;
                }
            }
        }
    }
}