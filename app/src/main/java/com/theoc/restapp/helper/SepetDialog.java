package com.theoc.restapp.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.theoc.restapp.CafeJoinActivity;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.LoginActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.adapters.SepetAdapter;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.barcode.ReadBarcode;
import com.theoc.restapp.dataorganization.barcode.SendSiparis;
import com.theoc.restapp.dataorganization.barcode.Siparis;
import com.theoc.restapp.dataorganization.screendata.GetDataMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SepetDialog extends Dialog {

    Context context;
    Map<String, Integer> sepetDict;
    Map<String, String> sepetDictPrice;
    Map<String, String> sepetDictPoint;
    Map<String, String> sepetDictCategory;
    GetDataMenu data;
    SendSiparis sendSiparis;
    String user_point = "";
    public SepetDialog(Context context, Map<String, Integer> sepetDict, Map<String, String> sepetDictPrice, Map<String, String> sepetDictPoint, Map<String, String> sepetDictCategory, GetDataMenu data, String user_point) {
        super(context);
        this.context = context;
        this.sepetDict = sepetDict;
        this.sepetDictPrice = sepetDictPrice;
        this.sepetDictPoint = sepetDictPoint;
        this.sepetDictCategory = sepetDictCategory;
        this.data = data;
        this.user_point = user_point;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sepet_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            if (w != null) {
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        SepetAdapter adapter = new SepetAdapter(context, sepetDict, sepetDictPrice, data, sepetDictPoint, sepetDictCategory);
        listView.setAdapter(adapter);
        TextView dereceTextView = (TextView) findViewById(R.id.dereceTextView);
        double derece = 0.0;
        for (String point: sepetDictPoint.values()) {
            derece += Double.parseDouble(point);
        }
        dereceTextView.setText("Derece: " + derece);

        TextView toplamTextView = (TextView) findViewById(R.id.toplamTextView);
        double toplam = 0.0;
        for (String price: sepetDictPrice.values()) {
            toplam += Double.parseDouble(price);
        }
        toplamTextView.setText("Toplam: " + toplam);

        Button siparisButton = (Button) findViewById(R.id.siparisButton);
        siparisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(user_point) >= 360) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                    builder.setTitle("Belirtilen ürün için ödül kullanmak istiyor musunuz")
                            .setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    notDialog(false);
                                }
                            })
                            .setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    notDialog(true);
                                }
                            })
                            .setMessage("Burada cafe prizedan çekilen ödül ürün bilgisi ile sipariş listesi karşılaştırılıp, eşleşme varsa yazacak. Eşleşme yoksa zaten ödül kullanacak bir sipariş olmadığı için burası açılmayacak. Şu an sadece puan 360 ve üstü mü diye bakıyor.")
                            .show();
                } else {
                    notDialog(false);
                }
            }
        });

        RelativeLayout RL = (RelativeLayout) findViewById(R.id.RL);
        RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SepetDialog.this.dismiss();
            }
        });
    }

    private void notDialog(final boolean prize) {
        android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
        builder2.setTitle("Sipariş Notu")
                .setNegativeButton("İPTAL ET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("GÖNDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gonder(prize, input.getText().toString());
                    }
                })
                .setView(input)
                .show();
    }

    private void gonder(boolean prize, String not) {
        sendSiparis = new SendSiparis();

        List<Siparis> siparises = new ArrayList<Siparis>();

        for (String siparis : sepetDict.keySet()) {
            siparises.add(new Siparis(Integer.parseInt(data.get_menuidwithname(Integer.parseInt(sepetDictCategory.get(siparis)), siparis)),
                    sepetDict.get(siparis),
                    "",
                    prize,
                    not));
        }

        sendSiparis.send_siparis(siparises);
        LoginActivity.socket_message.siparis_ver(ReadBarcode.cafe_id + "", GeneralSync.id + "");
        SepetDialog.this.dismiss();
    }
}