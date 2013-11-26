package com.simaternal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kematian_detail);

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
			Toast.makeText(getApplicationContext(), "mulai gan", Toast.LENGTH_SHORT);
			
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
		default:
			return true;
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
