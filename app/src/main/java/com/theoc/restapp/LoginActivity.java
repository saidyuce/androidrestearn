package com.theoc.restapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.MapView;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Giris;
import com.theoc.restapp.dataorganization.GirisType;
import com.theoc.restapp.dataorganization.KayitType;
import com.theoc.restapp.dataorganization.SifreUnuttum;
import com.theoc.restapp.dataorganization.SocketMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String email, name, id, password;
    SharedPreferences prefs;
    Giris giris;
    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    AccessTokenTracker fbTracker;
    SifreUnuttum sifreUnuttum;
    GoogleApiClient googleApiClient;
    SignInButton googleLoginButton;
    public static SocketMessage socket_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socket_message = new SocketMessage(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.googleclientid))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.v("GOOGLE API CLIENT: ", "FAILED");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setContentView(R.layout.activity_login);
        findViewById(R.id.imageButton).setOnClickListener(this);
        findViewById(R.id.fbButton).setOnClickListener(this);
        findViewById(R.id.googleButton).setOnClickListener(this);
        findViewById(R.id.registerButton).setOnClickListener(this);
        findViewById(R.id.misafirButton).setOnClickListener(this);
        findViewById(R.id.forgotButton).setOnClickListener(this);
        fbLoginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        fbLoginButton.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                } catch (Exception ignored) {

                }
            }
        }).start();

        giris = new Giris(this);
        giris.onceki_giris();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                giris.normal_giriş(((EditText) findViewById(R.id.usernameEditText)).getText().toString(),
                        ((EditText) findViewById(R.id.passwordEditText)).getText().toString());
                break;
            case R.id.fbLoginButton:
                fbLogin();
                break;
            case R.id.googleButton:
                googleLogin();
                break;
            case R.id.fbButton:
                fbLoginButton.performClick();
                break;
            case R.id.registerButton:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.misafirButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Misafir Girişi")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                misafir_girisi();
                            }
                        })
                        .setMessage("Misafir girişi yapmak üzeresiniz. Bu süreçte birçok özellik devre dışı olacak fakat uygulama hakkında genel bir fikir elde edebileceksiniz")
                        .show();
                break;
            case R.id.forgotButton:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                final View layout=this.getLayoutInflater().inflate(R.layout.forgot_dialog, null);

                builder2.setTitle("Şifremi unuttum")
                        .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Gönder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sifremi_unuttum(((EditText) layout.findViewById(R.id.forgotEditText)).getText().toString());
                            }
                        })
                        .setView(layout)
                        .setMessage("E-mail adresinize şifrenizi yenilemeniz için bir mail gönderilecek")
                        .show();
                        
                break;
            default:
                break;
        }
    }

    public void sifremi_unuttum(String username){
        sifreUnuttum = new SifreUnuttum(this);
        sifreUnuttum.sifremi_unuttum(username);
    }


    public void sifre_server_cevabı(KayitType kayitType) {
        if (kayitType == KayitType.var) {
            Toast.makeText(this, "E-mail adresinize şifrenizi yenilemeniz için bir mail gönderildi", Toast.LENGTH_SHORT).show();
        } else if (kayitType == KayitType.yok) {
            Toast.makeText(this, "Girdiğiniz e-mail adresine kayıtlı bir kullanıcı bulunamadı", Toast.LENGTH_SHORT).show();
        } else if (kayitType == KayitType.serversorunu) {
            Toast.makeText(this, "Bir sunucu hatası oluştu. Lütfen tekrar deneyin", Toast.LENGTH_SHORT).show();
        }
    }

    public void fbLogin() {
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    Log.v("OBJECT: ", object+"");
                                    giris.face_giris(object.getString("email"),
                                            android.provider.Settings.System.getString(LoginActivity.super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID),
                                            object.getString("first_name"),
                                            object.getString("last_name"));
                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this, "Lütfen Facebook girişinizi tekrarlayın", Toast.LENGTH_LONG).show();
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, email, first_name, last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.v("FACEBOOK GIRIS: ", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.v("FACEBOOK GIRIS: ", "onError");
            }
        });

        fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    Log.v("FB", "User Logged Out.");
                }
            }


        };
    }

    public void googleLogin() {
        Log.v("Google Login = ", "True");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.v("STATUS CODE", result.getStatus().toString());
            Log.v("Result", result.toString());
            if (result.isSuccess()) {
                Log.v("RESULT = ", "SUCCESS");
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct != null) {
                    Log.v("ACCOUNT: ", acct.toString() + "");
                giris.mail_giris(acct.getEmail(),
                        android.provider.Settings.System.getString(LoginActivity.super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID),
                        acct.getGivenName(),
                        acct.getFamilyName());
                }
            } else {
                Log.v("RESULT = ", "FAIL");
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void gir(GirisType type, String temp_key, int id) {

        if (type.equals(GirisType.girisbasarisiz)) {
            Toast.makeText(this.getBaseContext(), "Şifre veya mail yanlış!!", Toast.LENGTH_LONG).show();

        } else if (type.equals(GirisType.girisbasarili)) {

            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        } else if (type.equals(GirisType.onaybekleniyor)) {
            Toast.makeText(this.getBaseContext(), "Lütfen mailinize gelen onay linkini tıklayın!!", Toast.LENGTH_LONG).show();

        } else if (type.equals(GirisType.girisserversorunu)) {
            Toast.makeText(this.getBaseContext(), "İnternet bağlantınızı kontrol edin veya daha sonra tekrar deneyin!!", Toast.LENGTH_LONG).show();

        } else if (type.equals(GirisType.facegirisfail)) {


        } else if (type.equals(GirisType.facegirisvar)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        } else if (type.equals(GirisType.facegirisyok)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        } else if (type.equals(GirisType.mailgirisfail)) {


        } else if (type.equals(GirisType.mailgirisvar)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        } else if (type.equals(GirisType.mailgirisyok)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }


    }


    public void misafir_girisi() {
        GeneralSync.id = -1;
        GeneralSync.temp_key = "1234onurbarismertcan1234";
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}

