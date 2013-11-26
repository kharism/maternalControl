package com.simaternal;


import java.util.ArrayList;
import java.util.List;

import com.simaternal.model.Kematian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DB {
	private DatabaseHelper dbHelper;  
	private SQLiteDatabase database;
	private static DB _this;
	public final static String IBU_TABLE="ibu";
	public final static String ANAK_TABLE="anak";
	public final static String PENIMBANGAN_TABLE="penimbangan";
	
	public DB(Context context){  
	    dbHelper = new DatabaseHelper(context);  
	    database = dbHelper.getWritableDatabase();
	}
	public long save(String tableName,ContentValues values){
		if(values.containsKey("id")){
			database.update(tableName, values, "id="+values.getAsString("id"), null);
			return (long)values.getAsInteger("id");
		}			
		return database.insert(tableName, null, values);
	}
	public void saveKematian(Kematian i){
		ContentValues values = new ContentValues();
	    if(i.getId()>0)
		values.put("id", i.getId());
	    values.put("value", i.toSMS());
	    this.save("kematian", values);
	}
	public Kematian findKematian(int pk){
		Kematian k = new Kematian();
		Cursor curr =  database.query("kematian", Kematian.ATTRIBUTES,"id="+pk,null,null,null,null);
		curr.moveToFirst();
		while (!curr.isAfterLast()){
			k=Kematian.fromCursor(curr);
			curr.moveToNext();
		}
		curr.close();
		return k;
	}
	public List<Kematian> findAllKematian(){
		List<Kematian> kematian = new ArrayList<Kematian>();
		Cursor curr =  database.query("kematian", Kematian.ATTRIBUTES, null, null, null, null, "id");
		curr.moveToFirst();
	    while (!curr.isAfterLast()) {
	      Kematian k = Kematian.fromCursor(curr);
	      kematian.add(k);
	      curr.moveToNext();
	    }
	    // make sure to close the cursor
	    curr.close();
	    return kematian;
	}

}
