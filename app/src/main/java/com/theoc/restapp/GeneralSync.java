package com.theoc.restapp.dataorganization;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.theoc.restapp.AnketActivity;
import com.theoc.restapp.CafeActivity;
import com.theoc.restapp.CafeJoinActivity;
import com.theoc.restapp.MapActivity;
import com.theoc.restapp.MenuActivity;
import com.theoc.restapp.MyPointsActivity;
import com.theoc.restapp.MyPointsDetailActivity;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.helper.FusedLocationReceiver;
import com.theoc.restapp.helper.GoogleLocationClass;
import com.theoc.restapp.sidemenu.AyarlarActivity;
import com.theoc.restapp.sidemenu.HakkimizdaActivity;
import com.theoc.restapp.sidemenu.OneriActivity;
import com.theoc.restapp.sidemenu.SSSActivity;

import java.util.HashMap;
import java.util.Map;


public class GeneralSync extends GetDataFromLocal {

    public static boolean synced;
    public static boolean on_general_sync;
    public static Screens onthescreen;
    public static Activity currentactivity;

    public static int id = 0; //user id
    public static String location_city = "Konya";
    public static double x_loc = 0;
    public static double y_loc = 0;
    public static String temp_key="";
    public static ServerYanıt serverYanıt=ServerYanıt.bos;
    public static String isim="--";
    public static String soyisim="-";

    Context context_;
    Map<String, String> control_tables;


    public GeneralSync(Activity activity) {
        super(activity.getBaseContext(), activity);
        this.context_ = activity.getBaseContext();
    }

    public void start() {

        on_general_sync = true;
        synced = false;
        getUserLocation();
        //bura gelmeden önce city mutlaka belli olmalı
        super.get_data_async_single(true, context_, "genel_senkronizasyon.php", "synctable", "(id INT PRIMARY KEY NOT NULL ,id2 INT,name TEXT,type TEXT,id3 INT);", get_extra_data(), 0);

    }


    public Map<String, String> get_extra_data() {
        Map<String, String> ext = new HashMap<>();
        ext.put("id", id + "");
        ext.put("city", location_city.toLowerCase());
        ext.put("temp_key",temp_key);
        //ext.put(location)
        //if gps is not activated or with error so location is -1
        return ext;
    }

    @Override
    public void sync_finish(int i) {

        super.sync_finish(i);

    }

    public void getUserLocation() {

        /*if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(a)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {

                            location = LocationServices.FusedLocationApi.getLastLocation(
                                    googleApiClient);
                            if (location != null) {
                                x_loc = location.getLatitude();
                                y_loc = location.getLongitude();

                                Toast.makeText(a, "Current location: " + x_loc + ", " + y_loc, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(a, "Location null", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            // connection suspended
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            // connection failed
                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        } else {
            googleApiClient.connect();
        }*/
        initialize_ilk_konumlar();
    }
    private GoogleLocationClass googleLocationClass;
    private void initialize_ilk_konumlar(){
        googleLocationClass = new GoogleLocationClass(a, new FusedLocationReceiver() {
            @Override
            public void onLocationChanged() {
                Location location = googleLocationClass.getLocation();
                x_loc = location.getLatitude();
                y_loc = location.getLongitude();

            }
        });
    }


    @Override
    protected void destroy_task(){
        super.destroy_task();
    }
    @Override
    public void cancelsync(){
        //on the interrupted sync
    }
    @Override
    protected void onSYNC(){
        //while syncing

    }
    @Override
    protected void stop_get_data_from_local(){
        super.stop_get_data_from_local();


    }


    public static void set_screen(Screens s){
        onthescreen=s;
        switch (onthescreen) {
            case HomeScreen:
                currentactivity = new HomeActivity();
                break;
            case MapScreen:
                currentactivity = new MapActivity();
                break;
            case PointsScreen:
                currentactivity = new MyPointsActivity();
                break;
            case PointsDetailScreen:
                currentactivity = new MyPointsDetailActivity();
                break;
            case CafeDetailScreen:
                currentactivity = new CafeActivity();
                break;
            case CafeJoinScreen:
                currentactivity = new CafeJoinActivity();
                break;
            case MenusScreen:
                currentactivity = new MenuActivity();
                break;
            case SurveyScreen:
                currentactivity = new AnketActivity();
                break;
            case SSSScreen:
                currentactivity = new SSSActivity();
                break;
            case HakkimizdaScreen:
                currentactivity = new HakkimizdaActivity();
                break;
            case OneriScreen:
                currentactivity = new OneriActivity();
                break;
            case AyarlarScreen:
                currentactivity = new AyarlarActivity();
                break;
            default:
                break;
        }

    }
    public Activity get_activty(){

        return null;

    }

    public void setControl_tables(){
        control_tables=new HashMap<String, String>();
        control_tables.put("campaing","(id INTEGER PRIMARY KEY  ,cafe_id INT,category TEXT,picture_url TEXT,detail TEXT,last_time DATETIME,city TEXT);");
        control_tables.put("cafe","(id INTEGER PRIMARY KEY ,x_ DOUBLE,y_ DOUBLE,category TEXT,name TEXT,icon TEXT,city TEXT,large_image TEXT,cafe_picture_url TEXT,info TEXT,tel TEXT,map_icon TEXT,detail TEXT,site TEXT,face TEXT,twitter TEXT,instagram TEXT,preminium_type INT);");
        control_tables.put("points","(id INTEGER PRIMARY KEY    ,user_id INT,point INT,cafe_id INT);");
        control_tables.put("prize","(id INTEGER PRIMARY KEY    ,name TEXT,cafe_id INT);");
        control_tables.put("cafecoin","(id INTEGER PRIMARY KEY   ,cafe_id INT,coin DOUBLE);");
        control_tables.put("free_point","(id INTEGER PRIMARY KEY    ,kul_id INT,point INT,cafe_id INT);");

        //    control_tables.put("menu","(id INT PRIMARY KEY NOT NULL,cafe_id INT,type TEXT);");
        //     control_tables.put("food","(id INT PRIMARY KEY NOT NULL,menu_id INT,name TEXT,cost DOUBLE);");
        //    control_tables.put("suggested_food","(id INT PRIMARY KEY NOT NULL,cafe_id INT,food_id INT);");
    }

    @Override
    protected void post_sync(){
        setControl_tables();
        super.control_tables(control_tables);

    }
    //on the finish thread2

    @Override
    protected void get_data_from_local(int i){

        synced=true;
        on_general_sync=false;

    }

    @Override
    public void sync_finish_nosyenkron(){
        //succes
        // ((HomeActivity)a).basla();
        // update
        ((HomeActivity)a).start(serverYanıt);
    }
    @Override
    public void server_error(ServConnection.error e){
        // ((HomeActivity)a).basla();
        ((HomeActivity)a).start(serverYanıt);
    }
    @Override
    protected void internet_is_not_activated(){

        ((HomeActivity)a).start(serverYanıt);
    }


    public static int getId(){
        return id;
    }

}
