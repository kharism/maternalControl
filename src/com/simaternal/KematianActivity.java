package com.simaternal;

import java.util.Calendar;
import java.util.Date;

import com.simaternal.model.Kematian;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class KematianActivity extends Activity {

	DatePicker editTanggal;
	EditText editKtp;
	EditText editNama;
	EditText editUmur;
	EditText editKehamilan;
	Spinner spinnerTempat;
	EditText editKasus;
	EditText editSebab;
	Button saveButton;
	DB database;
	
	private int year;
	private int month;
	private int day;
	private class dateChangeListener implements OnDateChangedListener{

		@Override
		public void onDateChanged(DatePicker view, int syear, int smonth, int sdate) {
			year = syear;
			month = smonth;
			day = sdate;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kematian);
		editTanggal = (DatePicker)findViewById(R.id.datePickerKematian);
		editKtp = (EditText)findViewById(R.id.editKTP);
		editNama = (EditText)findViewById(R.id.editNamaIbu);
		editUmur = (EditText)findViewById(R.id.editUmurIbu);
		editKehamilan = (EditText)findViewById(R.id.editHamilIbu);
		spinnerTempat = (Spinner)findViewById(R.id.spinnerTempat);
		editKasus = (EditText)findViewById(R.id.editKasus);
		editSebab = (EditText)findViewById(R.id.editSebab);
		saveButton = (Button)findViewById(R.id.saveButton);
		database = new DB(getApplicationContext());
		Date date = new Date(System.currentTimeMillis());
		year = Calendar.getInstance().get(Calendar.YEAR);
		month = Calendar.getInstance().get(Calendar.MONTH);;
		day = Calendar.getInstance().get(Calendar.DATE);
		editTanggal.init(year, month-1, day, new dateChangeListener());
		if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey(KematianDetailFragment.ARG_ITEM_ID)){
			String j =getIntent().getExtras().getString(KematianDetailFragment.ARG_ITEM_ID);
			Kematian g = database.findKematian(Integer.parseInt(j));
			Log.i("tanggal",g.getTanggal());
			String[] kk = g.getTanggal().split("-");
			date = new Date(Integer.parseInt(kk[0]),Integer.parseInt(kk[1])-1,Integer.parseInt(kk[2]));
			editTanggal.init(date.getYear(), date.getMonth(), date.getDate(), new dateChangeListener());
			editNama.setText(g.getNamaIbu());
			editKasus.setText(g.getNoKasus());
			editSebab.setText(g.getSebab());
			editKehamilan.setText(String.valueOf(g.getHamilKe()));
			editUmur.setText(String.valueOf(g.getUmur()));
			editKtp.setText(g.getNoKtp());
			String[] data = getResources().getStringArray(R.array.tempat_kematian);
			int i;
			for(i=0;i<data.length;i++){
				if(data[i].equals(g.getTempatMeninggal()))
					break;
			}
			spinnerTempat.setSelection(i);
		}
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Kematian k = new Kematian();
				if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey(KematianDetailFragment.ARG_ITEM_ID)){
					k.setId(Integer.parseInt(getIntent().getStringExtra(KematianDetailFragment.ARG_ITEM_ID)));
				}
				k.setTanggal(year+"-"+month+"-"+day);
				k.setNoKtp(editKtp.getText().toString());
				k.setNamaIbu(editNama.getText().toString());
				k.setUmur(Integer.parseInt(editUmur.getText().toString()));
				k.setNoKtp(editKtp.getText().toString());
				k.setHamilKe(Integer.valueOf(editKehamilan.getText().toString()));
				k.setTempatMeninggal(String.valueOf(spinnerTempat.getSelectedItem()));
				k.setSebab(String.valueOf(editSebab.getText().toString()));
				k.setNoKasus(editKasus.getText().toString());
				if(k.validate()){
					database.saveKematian(k);
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "format data salah", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kematian, menu);
		return true;
	}

}
