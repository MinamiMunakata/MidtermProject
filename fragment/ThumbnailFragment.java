package com.personal_project.minami.midtermproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.personal_project.minami.midtermproject.MainActivity;
import com.personal_project.minami.midtermproject.R;
import com.personal_project.minami.midtermproject.YoutubeSearchApi;

/**
 * Created by Minami on 2018-05-30.
 */

public class ThumbnailFragment extends Fragment {
    private String videoId;
    private String selected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_thumbnail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getActivity().getSupportFragmentManager().popBackStack();

        Bundle bundle = getArguments();
        selected = bundle.getString("selected");
        TextView textView = view.findViewById(R.id.hairstyles_for_the_length);
        textView.setText("Hairstyles for " + selected);
        YoutubeSearchApi youtubeSearchApi = new YoutubeSearchApi(MainActivity.getContext(), view);
        youtubeSearchApi.execute(selected);
        GridLayout grid_thumbnail_container = view.findViewById(R.id.grid_thumbnail_container);
        for (int i = 0; i < grid_thumbnail_container.getChildCount(); i++) {
            final ImageButton imageButton = (ImageButton) grid_thumbnail_container.getChildAt(i);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoId = (String) imageButton.getTag();
                    Bundle bundle = new Bundle();
                    bundle.putString("videoId", videoId);
                    YoutubePlayerFragment fragment = new YoutubePlayerFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();
                }
            });
        }
    }
}
