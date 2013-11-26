package com.simaternal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simaternal.dummy.DummyContent;
import com.simaternal.model.Kematian;

/**
 * A fragment representing a single Kematian detail screen. This fragment is
 * either contained in a {@link KematianListActivity} in two-pane mode (on
 * tablets) or a {@link KematianDetailActivity} on handsets.
 */
public class KematianDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Kematian mItem;
	private DB database;
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public KematianDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new DB(getActivity());
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			/*mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));*/
			mItem = database.findKematian(Integer.parseInt(getArguments().getString(
					ARG_ITEM_ID)));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_kematian_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.nama_ibu))
					.setText(mItem.getNamaIbu());
			((TextView) rootView.findViewById(R.id.kematian_detail))
			.setText(mItem.getSebab());
			((TextView) rootView.findViewById(R.id.tempat_kematian))
			.setText(mItem.getTempatMeninggal());
			((TextView) rootView.findViewById(R.id.tanggal_kematian))
			.setText(mItem.getTanggal());
			((TextView) rootView.findViewById(R.id.umur))
			.setText(String.valueOf(mItem.getUmur()));
			((TextView) rootView.findViewById(R.id.kehamilan))
			.setText(String.valueOf(mItem.getHamilKe()));
			((TextView) rootView.findViewById(R.id.noKtp))
			.setText(mItem.getNoKtp());
		}

		return rootView;
	}
}
