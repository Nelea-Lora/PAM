package com.example.hearingsupport.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hearingsupport.R;


public class PhotoViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);

        Uri uri = getIntent().getData();
        ImageView iv = findViewById(R.id.imageView);
        if (uri != null) {
            iv.setImageURI(uri);
        }
    }
}
