package com.example.MacGo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by KD on 1/31/2015.
 */

public class ItemFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    public static final String ARG_PAGE = "page";
    private RecyclerView mItemView ;
    private ParseObject purchaseId;
    private RecyclerView.Adapter mItemAdapter;
    private RecyclerView.LayoutManager mItemLayout;
    final ArrayList<Item> itemList = new ArrayList<Item>();
    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ItemFragment create(int pageNumber) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.purchase_history_view, container, false);
        mItemView = (RecyclerView) container.findViewById(R.id.item_history);
        mItemView.setHasFixedSize(true);
        mItemLayout = new LinearLayoutManager(container.getContext());
        mItemView.setLayoutManager(mItemLayout);

        purchaseId = Util.getParseObject();
        // Set the title view to show the page number.
//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                getString(R.string.title_template_step, mPageNumber + 1));

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    public interface OnFragmentInteractionListener {
    }
}
