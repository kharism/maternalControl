package com.simaternal;


import org.json.JSONException;
import org.json.JSONObject;

import com.simaternal.model.Kematian;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * An activity representing a list of Kematian-kematian. This activity has
 * different presentations for handset and tablet-size devices. On handsets, the
 * activity presents a list of items, which when touched, lead to a
 * {@link KematianDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link KematianListFragment} and the item details (if present) is a
 * {@link KematianDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link KematianListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class KematianListActivity extends FragmentActivity implements
		KematianListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private Menu menu;
	private String selectedId;
	private String sessid;
	private String editToken;
	DB database;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kematian_list);
		database = new DB(getApplicationContext());
		
		if (findViewById(R.id.kematian_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((KematianListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.kematian_list))
					.setActivateOnItemClick(true);
		}
	}
	@Override
	protected void onResume() {
		
		KematianListFragment hh = ((KematianListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.kematian_list));
			hh.reloadList();
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kematian_menu, menu);
		this.menu = menu;
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.actionTambahData:
			Intent i = new Intent(getApplicationContext(), KematianActivity.class);
			startActivity(i);
			return true;
		case R.id.actionEditData:
			Intent j = new Intent(getApplicationContext(), KematianActivity.class);
			j.putExtra(KematianDetailFragment.ARG_ITEM_ID, selectedId);
			startActivity(j);
			return true;
		case R.id.actionKirimSMS:
			Kematian k = database.findKematian(Integer.parseInt(getIntent()
					.getStringExtra(KematianDetailFragment.ARG_ITEM_ID)));
			SmsManager.getDefault().sendTextMessage("085646201002", null, k.toSMS(),null, null);
			Toast.makeText(getApplicationContext(), "SMS sukses terkirim", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.actionKirimInternet:
			new LoginTask().execute();
			return true;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	public class LoginTask extends AsyncTask<Void, Void, Void>{
		String result;
		boolean isLogedIn;
		
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
			Kematian g = database.findKematian(Integer.parseInt(selectedId));
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
			if(sukses){
				Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
		
	}
	/**
	 * Callback method from {@link KematianListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			menu.findItem(R.id.actionEditData).setVisible(true);
			menu.findItem(R.id.actionKirimInternet).setVisible(true);
			menu.findItem(R.id.actionKirimSMS).setVisible(true);
			selectedId = id;
			Bundle arguments = new Bundle();
			arguments.putString(KematianDetailFragment.ARG_ITEM_ID, id);
			KematianDetailFragment fragment = new KematianDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.kematian_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, KematianDetailActivity.class);
			detailIntent.putExtra(KematianDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
