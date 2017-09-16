package com.theoc.restapp.dataorganization.screendata;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.theoc.restapp.MenuActivity;
import com.theoc.restapp.adapters.MenuPagerAdapter;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;
import com.theoc.restapp.extendclass.DataProcess.GetDataFromLocal;
import com.theoc.restapp.helper.MenuInterface;
import com.theoc.restapp.helper.SepetAdapterInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDataMenu extends GetDataFromLocal implements MenuInterface {
    public GetDataMenu(Activity activity) {
        super(activity.getBaseContext(), activity);
    }

    JSONObject main_data2;
    JSONObject main_data;
    int cafe_id;

    public void start_paralel(int cafe_id) {

        this.cafe_id=cafe_id;
        super.sync_finish(14);


    }

    @Override
    protected void select_and_response_special_json(String table,String q){

        super.select_and_response_special_json(table,q);
        String col_name="";
       List<String> column_names=new ArrayList<>();
        JSONObject rowObject = null;
        JSONArray main_array = null;
        boolean new_=false;
        while (!cursor.isAfterLast()) {
            new_=false;
            int totalColumn = cursor.getColumnCount();
            col_name=cursor.getString(4);
            if (!column_names.contains(col_name)) {

                main_array=new JSONArray();

                column_names.add(col_name);
                new_=true;

            }else try {
                main_array=(JSONArray) resultSet.get(column_names.indexOf(col_name));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            rowObject = new JSONObject();
            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null ) {
                    try {


                        if (cursor.getString(i) != null) {

                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {

                            rowObject.put(cursor.getColumnName(i), "");

                        }

                    }catch (Exception e){}
                }

            }

            main_array.put(rowObject);

            if (new_)
                resultSet.put(main_array);


            cursor.moveToNext();
        }
        try {
            result_json.put(table,resultSet);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cursor.close();

    }


    @Override
    protected void get_data_from_local(int i) {

        if (i==14) {

            super.result_json = new JSONObject();
            select_and_response_special_json("menu", "SELECT  " +
                    "menu.id," +
                    "price,"+
                    "name," +
                    "picture,"+
                    "category,"+
                    "selling_number,"+
                    "stoc_state,"+
                    "cafecoin.coin,"+
                    "info"+
                    " FROM " + "menu LEFT JOIN cafecoin on menu.cafe_id=cafecoin.cafe_id  where menu.cafe_id="+cafe_id+"");

        } else if(i==15){
            super.result_json = new JSONObject();
            select_and_response_json("menu", "SELECT  " +
                    "menu.id," +
                    "price,"+
                    "name," +
                    "picture,"+
                    "category,"+
                    "selling_number,"+
                    "stoc_state,"+
                    "cafecoin.coin,"+
                    "info"+
                    " FROM " + "menu LEFT JOIN cafecoin on menu.cafe_id=cafecoin.cafe_id  where menu.cafe_id="+cafe_id+" ORDER BY selling_number DESC");
        }

    }
    public Double get_price(int category_indis,int json_indis) {

        try {
            return main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(json_indis).getDouble("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_name(int category_indis,int json_indis) {

        try {
            return main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(json_indis).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int get_category_length(){
        try {
            return main_data.getJSONArray("menu").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int get_length_suggested() {

        try {
            return main_data2.getJSONArray("menu").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String get_price_suggested(int json_indis) {

        try {
            return main_data2.getJSONArray("menu").getJSONObject(json_indis).getDouble("price")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_stoc_state_suggested(int json_indis) {

        try {
            return main_data2.getJSONArray("menu").getJSONObject(json_indis).getInt("stoc_state")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_name_suggested(int json_indis) {

        try {
            return main_data2.getJSONArray("menu").getJSONObject(json_indis).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_picture_Suggested(int json_indis) {

        try {
            return ServConnection.get_file_url()+main_data2.getJSONArray("menu").getJSONObject(json_indis).getString("picture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_picture(int category_indis,int json_indis) {

        try {
            return ServConnection.get_file_url()+main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(json_indis).getString("picture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_category(int json_indis) {

        try {
            return main_data.getJSONArray("menu").getJSONArray(json_indis).getJSONObject(0).getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int get_category_itemlength(int json_indis) {

        try {
            return main_data.getJSONArray("menu").getJSONArray(json_indis).length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String get_info(int category_indis,int json_indis) {
        try {
            return main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(json_indis).getString("info");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
    public String get_point(int category_indis,int json_indis) {

        try {
            return (main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(json_indis).getDouble("price")*360/main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(json_indis).getDouble("coin"))+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_stoc_state(int category_indis,int json_indis) {

        try {
            return main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(json_indis).getInt("stoc_state")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get_menuid(int category_indis,int json_indis) {

        try {
            return main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(json_indis).getInt("id")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get_menuidwithname(int category_indis, String name) {
        try {
            for (int i=0;i<main_data.getJSONArray("menu").getJSONArray(category_indis).length();i++) {
                if (main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(i).getString("name") == name) {
                    return main_data.getJSONArray("menu").getJSONArray(category_indis).getJSONObject(i).getInt("id") + "";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void data_received(int i) {

        if(i==14)
            try {
                main_data = super.result_json;
                if (main_data.getJSONArray("menu").length() > 0) {
                    set_normal_menu();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                sync_finish(15);
            }

    }
    public Context get_context(){return a;}



    private void set_normal_menu() {
///// bura yaz tasarımları

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(((MenuActivity)a).getSupportFragmentManager(),this);
        ((MenuActivity)a).pager.setAdapter(pagerAdapter);
        ((MenuActivity)a).tabs.setDistributeEvenly(true);
        ((MenuActivity)a).tabs.setViewPager(((MenuActivity)a).pager);
        ((MenuActivity)a).tabs.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        sepetDict = new HashMap<>();
        sepetDictPrice = new HashMap<>();
        sepetDictPoint = new HashMap<>();
        sepetDictCategory = new HashMap<>();
    }

    public Map<String, Integer> get_sepetDict() {
        return sepetDict;
    }

    public Map<String, String> get_sepetDictPrice() {
        return sepetDictPrice;
    }

    public Map<String, String> get_sepetDictPoint() {
        return sepetDictPoint;
    }

    public Map<String, String> get_sepetDictCategory() {
        return sepetDictCategory;
    }

    public Map<String, Integer> sepetDict;
    public Map<String, String> sepetDictPrice;
    public Map<String, String> sepetDictPoint;
    public Map<String, String> sepetDictCategory;

    @Override
    public void setValues(String name, int count, String price, String point, int type, String category) {
        if (type == 0) {
            if (sepetDict.containsKey(name)) {
                Toast.makeText(a, name + " sipariş listenizde mevcut\nMiktarı değiştirmek için sipariş listenizi kullanın", Toast.LENGTH_LONG).show();
            } else {
                sepetDict.put(name, count);
                sepetDictPrice.put(name, price);
                sepetDictPoint.put(name, point);
                sepetDictCategory.put(name, category);
                Toast.makeText(a, name + " sipariş listenize eklendi", Toast.LENGTH_LONG).show();
            }
        } else {
            if (count == 0) {
                Log.d("count", "zero");
                sepetDict.remove(name);
                sepetDictPrice.remove(name);
                sepetDictPoint.remove(name);
                sepetDictCategory.remove(name);
                SepetAdapterInterface sepetAdapterInterface = new SepetAdapterInterface() {
                    @Override
                    public void refreshAdapter(Map<String, Integer> sepetDict, Map<String, String> sepetDictPrice, Map<String, String> sepetDictPoint, Map<String, String> sepetDictCategory) {

                    }
                };
                sepetAdapterInterface.refreshAdapter(sepetDict, sepetDictPrice, sepetDictPoint, sepetDictCategory);
                // burada adaptera notifyDataSetChanged yapmak lazım ki miktarı 0 olan ürün listeden silinsin
            } else {
                sepetDict.put(name, count);
                sepetDictPrice.put(name, price);
                sepetDictPoint.put(name, point);
                sepetDictCategory.put(name, category);
            }
        }
    }
}
