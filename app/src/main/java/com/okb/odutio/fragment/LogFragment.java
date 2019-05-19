package com.okb.odutio.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.okb.odutio.R;
import com.okb.odutio.adapter.SharedItemsAdapter;
import com.okb.odutio.model.SharedItemModel;
import com.okb.odutio.service.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by onurkilic on 09/05/2017.
 */

public class LogFragment extends Fragment {

    private TextView noDataTextView;
    private ProgressBar progressBar;
    private ListView listView;
    private SharedItemsAdapter adapter;

    public LogFragment() {

    }

    public static LogFragment newInstance() {
        LogFragment fragment = new LogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        noDataTextView = (TextView) v.findViewById(R.id.no_data_text);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        listView = (ListView) v.findViewById(R.id.list_view);
        adapter = new SharedItemsAdapter(getActivity(), R.layout.layout_shared_item);
        listView.setAdapter(adapter);
        Client.getSharedItems(callback);
    }

    private Callback<List<SharedItemModel>> callback = new Callback<List<SharedItemModel>>() {
        @Override
        public void onResponse(Call<List<SharedItemModel>> call, Response<List<SharedItemModel>> response) {
            progressBar.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                adapter.addAll(response.body());
                if (response.body().size() == 0) {
                    noDataTextView.setVisibility(View.VISIBLE);
                } else {
                    noDataTextView.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(Call<List<SharedItemModel>> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
