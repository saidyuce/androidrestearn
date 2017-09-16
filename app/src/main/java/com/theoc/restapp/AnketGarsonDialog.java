package com.theoc.restapp.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.theoc.restapp.R;
import com.theoc.restapp.adapters.AnketGarsonAdapter;

public class AnketGarsonDialog extends Dialog implements Dialog.OnDismissListener {

    View update_view;
    String name;
    float rating=0;
    public AnketGarsonDialog(Context context, String name,View v) {
        super(context);
        this.name = name;
        update_view=v;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.anket_garson_dialog);
        setCanceledOnTouchOutside(true);
        TextView garsonDialogTextView = (TextView) findViewById(R.id.garsonDialogTextView);
        TextView okDialogTextView = (TextView) findViewById(R.id.okDialogTextView);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        garsonDialogTextView.setText(name);
        if (update_view.getTag(R.id.waiter_rating)!=null&&update_view.getTag(R.id.waiter_rating).toString()!=""&&update_view.getTag(R.id.waiter_rating).toString()!=0+"")
            ratingBar.setRating(Float.parseFloat(update_view.getTag(R.id.waiter_rating).toString()));
        okDialogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = ratingBar.getRating();
                v.setTag(R.id.frame_id, "1");
                // alınan rating anket_garson_single_item'deki ratingTextView konulacak.
                AnketGarsonDialog.this.dismiss();
            }
        });
        this.setOnDismissListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (rating!=0) {
            ((AnketGarsonAdapter.ViewHolder) update_view.getTag(R.id.frame_id)).coverImageView.setVisibility(View.VISIBLE);
            ((AnketGarsonAdapter.ViewHolder) update_view.getTag(R.id.frame_id)).rating_textview.setVisibility(View.VISIBLE);
            ((AnketGarsonAdapter.ViewHolder) update_view.getTag(R.id.frame_id)).rating_textview.setText((int)rating + "");
            ((AnketGarsonAdapter.ViewHolder) update_view.getTag(R.id.frame_id)).starImageView.setVisibility(View.VISIBLE);
            update_view.setTag(R.id.waiter_rating,rating+"");
        }
        else {
            ((AnketGarsonAdapter.ViewHolder) update_view.getTag(R.id.frame_id)).rating_textview.setVisibility(View.INVISIBLE);
            ((AnketGarsonAdapter.ViewHolder) update_view.getTag(R.id.frame_id)).rating_textview.setText( "");
            ((AnketGarsonAdapter.ViewHolder) update_view.getTag(R.id.frame_id)).coverImageView.setVisibility(View.INVISIBLE);
            ((AnketGarsonAdapter.ViewHolder) update_view.getTag(R.id.frame_id)).starImageView.setVisibility(View.INVISIBLE);
            update_view.setTag(R.id.waiter_rating,0+"");

        }
    }
}
