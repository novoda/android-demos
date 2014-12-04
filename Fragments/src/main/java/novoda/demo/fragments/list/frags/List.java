package novoda.demo.fragments.list.frags;

import novoda.demo.fragments.list.R;
import novoda.demo.fragments.list.Constants;
import novoda.demo.fragments.list.activities.Details;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class List extends ListFragment {
    boolean mHasDetailsFrame;
    int mPositionChecked = 0;
    int mPositionShown = -1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Constants.ITEMS));

        View detailsFrame = getActivity().findViewById(R.id.frame_details);
        mHasDetailsFrame = (detailsFrame != null) && (detailsFrame.getVisibility() == View.VISIBLE);

        if (savedInstanceState != null) {
            mPositionChecked = savedInstanceState.getInt("curChoice", 0);
            mPositionShown = savedInstanceState.getInt("shownChoice", -1);
        }

        if (mHasDetailsFrame) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            upateDetailsFragment(mPositionChecked);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mPositionChecked);
        outState.putInt("shownChoice", mPositionShown);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        upateDetailsFragment(position);
    }

    void upateDetailsFragment(int index) {
        mPositionChecked = index;

        if (mHasDetailsFrame) {
            getListView().setItemChecked(index, true);

            if (mPositionShown != mPositionChecked) {
                Detail df = Detail.newInstance(index);
                getFragmentManager()
                	.beginTransaction()
                	.replace(R.id.frame_details, df)
                	.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                	.commit();
                
                mPositionShown = index;
            }

        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), Details.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
}
