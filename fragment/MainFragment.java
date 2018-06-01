package com.personal_project.minami.midtermproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import com.personal_project.minami.midtermproject.R;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Minami on 2018-05-30.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayout gridLayout = view.findViewById(R.id.options);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        String selectedLength = null;
        selectedLength = String.valueOf(view.getId());
        Log.i(TAG, "onClick: -----------------> " + selectedLength);
        Log.i(TAG, "onClick: " + String.valueOf(R.id.short_btn));
        if (view.getId() == R.id.long_btn) selectedLength = "long";
        else if (view.getId() == R.id.medium_btn) selectedLength = "medium";
        else if (view.getId() == R.id.short_btn) selectedLength = "short";
        ThumbnailFragment thumbnailFragment = new ThumbnailFragment();
        Bundle bundle = new Bundle();
        Log.i(TAG, "onClick: " + selectedLength);
        bundle.putString("selected", selectedLength);
        thumbnailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, thumbnailFragment);
        fragmentTransaction.commit();
    }

}
