package com.theoc.restapp.extendclass.DataConnections;

import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class LocalConnection extends SQLiteOpenHelper{


    protected   enum errors {CREATE_ERROR,EXECERROR,SUCCES}

    protected    JSONObject result_json;

    private  SQLiteDatabase db;
    private String create_query_default="create table if not exists ";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sqllite_database";


    protected LocalConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void control(String table_name,String create_query){

        db = this.getWritableDatabase();
        try {
            db.execSQL(create_query_default+table_name+create_query);
        }
        catch (Exception e){
            onError(errors.CREATE_ERROR);
        }


    }
    private void exec_query_write(String query){
        db = this.getWritableDatabase();
        db.execSQL(query);

    }
    private void exec_query_read(String query){
        db = this.getReadableDatabase();
        db.execSQL(query);

    }





    protected void onError(errors e){


    }


    protected void select_and_response_json(String tablename,String query){
        try {
            //
            Log.v("query",query);
            db=this.getReadableDatabase();
            cursor = db.rawQuery(query, null );
            resultSet     = new JSONArray();
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for( int i=0 ;  i< totalColumn ; i++ )
                {
                    if( cursor.getColumnName(i) != null )
                    {

                        if( cursor.getString(i) != null )
                        {

                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }


                    }

                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }
            result_json.put(tablename,resultSet);
            cursor.close();
            add_new_value_to_req_json();
        }catch (Exception e){
            onError(errors.EXECERROR);
        }


    }
    protected void select_and_response_json2(String tablename,String query){
        try {
            //
            db=this.getReadableDatabase();
            cursor = db.rawQuery(query, null );
            resultSet_delete     = new JSONArray();
            resultSet_up    = new JSONArray();
            resultSet_ins     = new JSONArray();

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for( int i=0 ;  i< totalColumn ; i++ )
                {
                    if( cursor.getColumnName(i) != null )
                    {

                        if( cursor.getString(i) != null )
                        {

                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }


                    }

                }
                if (cursor.getString(3).equals("insert"))
                    resultSet_ins.put(rowObject);
                else if (cursor.getString(3).equals("delete"))
                    resultSet_delete.put(rowObject);
                else     if (cursor.getString(3).equals("update"))
                    resultSet_up.put(rowObject);

                cursor.moveToNext();
            }

            result_json.put("delete",resultSet_delete);
            result_json.put("insert",resultSet_ins);
            result_json.put("update",resultSet_up);
            cursor.close();
            add_new_value_to_req_json();
        }catch (Exception e){
            onError(errors.EXECERROR);
        }


    }



    protected    JSONArray resultSet_ins;
    protected    JSONArray resultSet_up;
    protected    JSONArray resultSet_delete;


    protected    JSONArray resultSet;
    protected    Cursor cursor;
    protected void select_and_response_special_json(String table_name,String query){

        db=this.getReadableDatabase();
        cursor = db.rawQuery(query, null );
        resultSet     = new JSONArray();
        cursor.moveToFirst();


    }

    protected void add_new_value_to_req_json(){



    }

    protected void exec_and_response_excquery_write(String query){
        try {
            exec_query_write(query);
            onfinish();
        }catch (Exception e){
            onError(errors.EXECERROR);
        }


    }
    protected void exec_and_response_excquery_read(String query){
        try {
            exec_query_read(query);
            onfinish();
        }catch (Exception e){
            onError(errors.EXECERROR);
        }


    }


    protected void onfinish(){



    }








    protected void control_tables(Map<String,String> a){
        //removeAll();
        for (String b:a.keySet() ){
            control(b,a.get(b));
            when_the_control(b);
        }
        when_the_control_finish();

    }
    protected void control_table(String table_name,String query){
        control(table_name,query);
        when_the_control(table_name);
        when_the_control_finish();
    }

    protected void when_the_control(String table_name){


    }
    protected void when_the_control_finish(){


    }

    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.execSQL("drop table "+"campaing");
        db.execSQL("drop table "+"cafe");
        db.execSQL("drop table "+"points");

        db.close ();
    }
}
