package com.simaternal.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;


public class Kematian {
	int id;
	String tanggal;
	String noKtp;
	String namaIbu;
	int umur;
	int hamilKe;
	String tempatMeninggal;
	String sebab;
	String noKasus;
	
	
	public static String[] ATTRIBUTES = {"id","value"};
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTanggal() {
		return tanggal;
	}
	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}
	public String getNoKtp() {
		return noKtp;
	}
	public void setNoKtp(String noKtp) {
		this.noKtp = noKtp;
	}
	public String getNamaIbu() {
		return namaIbu;
	}
	public void setNamaIbu(String namaIbu) {
		this.namaIbu = namaIbu;
	}
	public int getUmur() {
		return umur;
	}
	public void setUmur(int umur) {
		this.umur = umur;
	}
	public int getHamilKe() {
		return hamilKe;
	}
	public void setHamilKe(int hamilKe) {
		this.hamilKe = hamilKe;
	}
	public String getTempatMeninggal() {
		return tempatMeninggal;
	}
	public void setTempatMeninggal(String tempatMeninggal) {
		this.tempatMeninggal = tempatMeninggal;
	}
	public String getSebab() {
		return sebab;
	}
	public void setSebab(String sebab) {
		this.sebab = sebab;
	}
	public String getNoKasus() {
		return noKasus;
	}
	public void setNoKasus(String noKasus) {
		this.noKasus = noKasus;
	}
	public void fromSmsString(String k){
		String[] data = k.split("#");
		try{
			tanggal = data[0];
			noKtp = data[1];
			namaIbu = data[2];
			umur = Integer.parseInt(data[3]);
			hamilKe = Integer.parseInt(data[4]);
			tempatMeninggal = data[5];
			sebab = data[6];
			noKasus = data[7];
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public String toSMS(){
		String p="";
		p+=tanggal+"#";
		p+=noKtp+"#";
		p+=namaIbu+"#";
		p+=umur+"#";
		p+=hamilKe+"#";
		p+=tempatMeninggal+"#";
		p+=sebab+"#";
		p+=noKasus+"#";
		return p;
	}
	public static Kematian fromCursor(Cursor c){
		Kematian n = new Kematian();
		n.id = c.getInt(0);
		n.fromSmsString(c.getString(1));
		return n;
	}
	public boolean validate(){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		f.setLenient(false);
		try {
			 
			//if not valid, it will throw ParseException
			Date date = f.parse(tanggal);
			System.out.println(date);
 
		} catch (ParseException e) {
 
			e.printStackTrace();
			return false;
		}
 
		return true;
	}
}
