package com.theoc.restapp.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theoc.restapp.CafeActivity;
import com.theoc.restapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeDialog extends Dialog {

    String type;
    String detail;
    String cafe_id;
    Context context;
    String logo;
    String name;
    double cafe_x;
    double cafe_y;
    String cafe_detail;
    String large_image;

    public HomeDialog(Context context, String type, String detail, String cafe_id, String logo, String name, double cafe_x, double cafe_y, String cafe_detail, String large_image) {
        super(context);
        this.context = context;
        this.type = type;
        this.detail = detail;
        this.cafe_id = cafe_id;
        this.logo = logo;
        this.cafe_x = cafe_x;
        this.cafe_y = cafe_y;
        this.name = name;
        this.cafe_detail = cafe_detail;
        this.large_image = large_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        TextView mekanNameTextView = (TextView) findViewById(R.id.mekanNamTextView);
        TextView kampanyaTypeTextView = (TextView) findViewById(R.id.kampanyaTypeTextView);
        TextView kampanyaDetailTextView = (TextView) findViewById(R.id.kampanyaDetailTextView);
        CircleImageView logoImageView = (CircleImageView) findViewById(R.id.logoImageView);
        Button goButton = (Button) findViewById(R.id.goButton);

        mekanNameTextView.setText(name);
        if (type.equalsIgnoreCase("Restarn")) {
            kampanyaTypeTextView.setText("Restearn");
        } else {
            kampanyaTypeTextView.setText(type);
        }
        kampanyaDetailTextView.setText(detail);
        Glide.with(context)
                .load("http://restearndev.xyz/RestUpp/KontrolPaneli/caferesim/" + logo)
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .dontAnimate()
                .centerCrop()
                //.crossFade()
                .into(logoImageView);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, CafeActivity.class);
                intent.putExtra("cafe_id", cafe_id);
                intent.putExtra("cafe_x", cafe_x);
                intent.putExtra("cafe_y", cafe_y);
                intent.putExtra("name", name);
                intent.putExtra("cafe_detail", cafe_detail);
                intent.putExtra("large_image", large_image);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                activity.startActivity(intent);
                HomeDialog.this.dismiss();
            }
        });

        RelativeLayout RL = (RelativeLayout) findViewById(R.id.RL);
        RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeDialog.this.dismiss();
            }
        });

    }
}
