package com.simaternal;

import org.json.JSONException;
import org.json.JSONObject;

import com.simaternal.model.Kematian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * An activity representing a single Kematian detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a {@link KematianListActivity}
 * .
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link KematianDetailFragment}.
 */
public class KematianDetailActivity extends FragmentActivity {
	DB database;
	String sessid;
	String editToken;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kematian_detail);
		database = new DB(getApplicationContext());
		pd = new ProgressDialog(getApplicationContext());
		pd.setTitle("Coba mengirim data");
		pd.setCanceledOnTouchOutside(false);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(KematianDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(KematianDetailFragment.ARG_ITEM_ID));
			KematianDetailFragment fragment = new KematianDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.kematian_detail_container, fragment).commit();
			//Toast.makeText(getApplicationContext(), "mulai gan", Toast.LENGTH_SHORT);
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Bundle arguments = new Bundle();
		arguments.putString(KematianDetailFragment.ARG_ITEM_ID, getIntent()
				.getStringExtra(KematianDetailFragment.ARG_ITEM_ID));
		KematianDetailFragment fragment = new KematianDetailFragment();
		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.kematian_detail_container, fragment).commit();
	}
	@Override
	protected void onRestart() {
		Toast.makeText(getApplicationContext(), "lanjut gan", Toast.LENGTH_SHORT);
		super.onRestart();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kematian_detil_menu, menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.edit_kematian:
			Intent edit = new Intent(getApplicationContext(), KematianActivity.class);
			String h = getIntent()
					.getStringExtra(KematianDetailFragment.ARG_ITEM_ID);
			edit.putExtra(KematianDetailFragment.ARG_ITEM_ID,h );
			startActivity(edit);
			return true;
		case R.id.kirim_sms:
			Kematian k = database.findKematian(Integer.parseInt(getIntent()
					.getStringExtra(KematianDetailFragment.ARG_ITEM_ID)));
			SmsManager.getDefault().sendTextMessage("085646201002", null, k.toSMS(),null, null);
			Toast.makeText(getApplicationContext(), "SMS sukses terkirim", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.kirim_json:
			pd.show();
			new LoginTask().execute();
			return true;
		default:
			return true;
		}
	}
	public class LoginTask extends AsyncTask<Void, Void, Void>{
		String result;
		boolean isLogedIn;
		public LoginTask(){
			
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject loginData = new JSONObject();
			try {
				loginData.put("username", "admin");
				loginData.put("password", "admin");
				JSONObject k =(JSONObject)HttpClient.SendHttpPost("http://ibu.kubuslab.com/ws/usr/login", loginData);
				JSONObject message;
				message = ((JSONObject)k.get("message"));
				if(message.getString("tipe").equals("success")||(message.getString("tipe").equals("error")&&message.getString("pesan").equalsIgnoreCase("Username sudah login"))){
					isLogedIn = true;					
				}
				else{
					isLogedIn = false;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(isLogedIn){
				new GetTokenTask().execute();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Gaagal Login", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	public class GetTokenTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject rr = (JSONObject)HttpClient.SendHttpGet("http://ibu.kubuslab.com/ws/ui/form/form-bumil-kematian?aksi=p&format=json");
			try {
				sessid = rr.getString("sessid");
				editToken = rr.getString("token");				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(NullPointerException ex){
				Log.e("RR", rr.toString());
				ex.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			new SendDataTask().execute();
		}
		
	}
	public class SendDataTask extends AsyncTask<Void, Void, Void>{
		boolean sukses;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Kematian g = database.findKematian(Integer.parseInt(getIntent().getStringExtra(KematianDetailFragment.ARG_ITEM_ID)));
			JSONObject sendData = new JSONObject();
			JSONObject response = new JSONObject();
			try {
				sendData.put("token", editToken);
				sendData.put("tanggal_meninggal", g.getTanggal());
				sendData.put("ktp", g.getNoKtp());
				sendData.put("nama",g.getNamaIbu());
				sendData.put("umur", g.getUmur());
				sendData.put("hamil_ke", g.getHamilKe());
				sendData.put("sebab_kematian", g.getSebab());
				sendData.put("tempat_meninggal", g.getTempatMeninggal());
				sendData.put("nmrkasus_kodeunik",g.getNoKasus());
				response = (JSONObject) HttpClient.SendHttpPost("http://ibu.kubuslab.com/ws/maternal/kematian/", sendData);
				Log.i("JSON_GET",response.toString());
				sukses = response.getBoolean("sukses");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NullPointerException e){
				e.printStackTrace();
				sukses = false;
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			pd.hide();
			if(sukses){
				Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					KematianListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
