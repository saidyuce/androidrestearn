package com.theoc.restapp.helper;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.plattysoft.leonids.ParticleSystem;
import com.theoc.restapp.R;

public class QRReadImage extends Dialog {

    ImageButton exitButton;
    Button nullButton;
    public Activity a;


    public QRReadImage(Activity a) {
        super(a);
        this.a = a;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qr_read_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        exitButton = (ImageButton) findViewById(R.id.qrExitIB);
        nullButton = (Button) findViewById(R.id.nullbutton);

        /*nullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mp.start();
                new ParticleSystem(a, 200, R.drawable.confetti, 2000)
                        .setSpeedRange(0.1f, 0.2f)
                        .oneShot(findViewById(R.id.confettiView), 200);
                new ParticleSystem(a, 200, R.drawable.confetti4, 2000)
                        .setSpeedRange(0.1f, 0.2f)
                        .oneShot(findViewById(R.id.confettiView2), 200);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new ParticleSystem(a, 200, R.drawable.confetti2, 2000)
                                .setSpeedRange(0.1f, 0.2f)
                                .oneShot(findViewById(R.id.confettiView), 200);
                        new ParticleSystem(a, 200, R.drawable.confetti3, 2000)
                                .setSpeedRange(0.1f, 0.2f)
                                .oneShot(findViewById(R.id.confettiView2), 200);
                    }
                }, 1000);

            }
        });*/

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        final ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 210); // desired degree
        animation.setDuration (1500); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.start ();
                //nullButton.performClick();
            }
        }, 750);

    }

}
