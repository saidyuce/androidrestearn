package com.theoc.restapp.helper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.theoc.restapp.R;

public class SozlesmeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sozlesme);

        ((PDFView) findViewById(R.id.pdfView)).fromAsset("sozlesme.pdf")
                .defaultPage(0)
                .load();
    }
}
