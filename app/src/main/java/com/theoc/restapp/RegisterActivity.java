package com.theoc.restapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.theoc.restapp.dataorganization.Kayit;
import com.theoc.restapp.dataorganization.KayitType;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.helper.SozlesmeActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Kayit kayit;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.registerButton).setOnClickListener(this);

        kayit=new Kayit(this);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Intent intent = new Intent(RegisterActivity.this, SozlesmeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void kaydol(String username,String password,String device_id,String isim,String soyisim){
        kayit.kayit_ol(username,password,device_id,isim,soyisim);
    }



    public void kayit_bitti(KayitType type, String key){
        if (type==KayitType.yok){
            JSONObject jj_temp =new JSONObject();
            try {
                jj_temp.put("user_name", ((EditText) findViewById(R.id.emailEditText)).getText().toString());
                jj_temp.put("api_key", "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");
                jj_temp.put("key", key);
                jj_temp.put("type", "register");
                LoginActivity.socket_message.email_user(jj_temp);
                Toast.makeText(RegisterActivity.this, "Başarıyla kayıt oldunuz\nGiriş yapmak için e-mail adresinize gelen maili onaylayın", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // id ve temp key dolu gelcek

        }else if (type==KayitType.serversorunu){
            Toast.makeText(this, "Sunucu hatası oluştu. Lütfen tekrar deneyin", Toast.LENGTH_LONG).show();
            //
        }
        else if (type==KayitType.var){
            Toast.makeText(this, "Bu e-mail ile kayıtlı kullanıcı mevcut", Toast.LENGTH_LONG).show();
            //daha önce kaydolmuş
        }
        else if (type==KayitType.fail){

            //sanırım fail dönmüyo kayıtta ama yinede koydum
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registerButton:
                if (!((EditText) findViewById(R.id.emailEditText)).getText().toString().equals("") &&
                        !((EditText) findViewById(R.id.isimEditText)).getText().toString().equals("") &&
                        !((EditText) findViewById(R.id.soyisimEditText)).getText().toString().equals("") &&
                        !((EditText) findViewById(R.id.sifreEditText)).getText().toString().equals("") &&
                        !((EditText) findViewById(R.id.sifreOnayEditText)).getText().toString().equals("")) {
                    if (((EditText) findViewById(R.id.sifreEditText)).getText().toString().equals(((EditText) findViewById(R.id.sifreOnayEditText)).getText().toString())) {
                        if (checkBox.isChecked()) {
                            if (isEmailValid(((EditText) findViewById(R.id.emailEditText)).getText().toString())) {
                                kayit.kayit_ol(((EditText) findViewById(R.id.emailEditText)).getText().toString(),
                                        ((EditText) findViewById(R.id.sifreEditText)).getText().toString(),
                                        android.provider.Settings.System.getString(RegisterActivity.super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID),
                                        ((EditText) findViewById(R.id.isimEditText)).getText().toString(),
                                        ((EditText) findViewById(R.id.soyisimEditText)).getText().toString());
                            } else {
                                Toast.makeText(this, "Lütfen geçerli bir e-mail adresi girin", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, "Lütfen Restearn kullanıcı sözleşmesini okuyup ve onaylayın", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Şifre ve Şifre Onay birbirinden farklı", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Lütfen tüm gerekli alanları doldurun", Toast.LENGTH_SHORT).show();
                }

        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
