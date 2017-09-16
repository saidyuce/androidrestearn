package com.theoc.restapp.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.screendata.GetDataMenu;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuDialog extends Dialog{

    String malzeme;
    String fiyat;
    String resim;
    Context context;
    String ürün;
    String puan;
    String category;
    MenuInterface menuInterface;
    GetDataMenu data;

    public MenuDialog(Context context, String malzeme, String fiyat, String resim, String ürün, String puan, String category, MenuInterface menuInterface, GetDataMenu data) {
        super(context);
        this.malzeme = malzeme;
        this.fiyat = fiyat;
        this.resim = resim;
        this.context = context;
        this.ürün = ürün;
        this.puan = puan;
        this.category = category;
        this.menuInterface = menuInterface;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            if (w != null) {
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }

        TextView malzemeTextView = (TextView) findViewById(R.id.malzemeTextView);
        final TextView priceDialogTextView = (TextView) findViewById(R.id.priceDialogTextView);
        final TextView ürünTextView = (TextView) findViewById(R.id.ürünTextView);
        final TextView pointDialogTextView = (TextView) findViewById(R.id.pointDialogTextView);
        CircleImageView foodImageView = (CircleImageView) findViewById(R.id.foodImageView);
        Button goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuInterface.setValues(ürünTextView.getText().toString(),
                        1,
                        priceDialogTextView.getText().toString(),
                        pointDialogTextView.getText().toString().replace(String.valueOf((char) 0x00B0), ""),
                        0,
                        category);
            }
        });

        malzemeTextView.setText(malzeme);
        priceDialogTextView.setText(fiyat);
        ürünTextView.setText(ürün);
        pointDialogTextView.setText(puan);
        Glide.with(context)
                .load(resim)
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .dontAnimate()
                .centerCrop()
                //.crossFade()
                .into(foodImageView);

        RelativeLayout RL = (RelativeLayout) findViewById(R.id.RL);
        RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDialog.this.dismiss();
            }
        });

    }
}
