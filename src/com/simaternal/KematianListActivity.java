package com.simaternal;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kematian_list);

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

		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kematian_menu, menu);
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

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
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
