package com.simaternal;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.simaternal.dummy.DummyContent;
import com.simaternal.model.Kematian;

/**
 * A list fragment representing a list of Kematian-kematian. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link KematianDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class KematianListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	DB database;
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public KematianListFragment() {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onResume() {
		//((KematianAdapter)getListAdapter()).replaceItems(database.findAllKematian());
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new DB(getActivity());
		// TODO: replace with a real list adapter
		KematianAdapter kAdapter = new KematianAdapter(getActivity(), R.layout.list_kematian, database.findAllKematian());
		/*setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, DummyContent.ITEMS));*/
		setListAdapter(kAdapter);

	}
	public void reloadList(){
		KematianAdapter kAdapter = new KematianAdapter(getActivity(), R.layout.list_kematian, database.findAllKematian());
		setListAdapter(kAdapter);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	private class KematianAdapter extends ArrayAdapter<Kematian>{
		private int selectedIndex;
		private Context context;
		private int selectedColor = Color.parseColor("#abcdef");
		private List<Kematian> myList;
		public KematianAdapter(Context context, int resource, 
				List<Kematian> objects) {
			super(context, resource,  objects);
			myList = objects;
			this.context = context;
		}
		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}
		public int getSelectedIndex(){
			return selectedIndex;
		}
		public int getCount() {
			return myList.size();
		}

		@Override
		public Kematian getItem(int position) {
			return myList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		public void replaceItems(List<Kematian> list) {
			myList = list;
			notifyDataSetChanged();
		}
		public List<Kematian> getEntireList() {
			return myList;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi;
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.list_kematian, null);
			TextView tv = (TextView)vi.findViewById(R.id.idKematian);
			TextView tv2 = (TextView)vi.findViewById(R.id.namaIbu);
			Kematian k = myList.get(position);
			tv.setText(String.valueOf(k.getId()));
			tv2.setText(k.getNamaIbu());
			return vi;
		}
	}
}
