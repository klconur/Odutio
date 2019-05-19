package com.okb.odutio.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.okb.odutio.R;
import com.okb.odutio.activity.CampaignActivity;
import com.okb.odutio.adapter.CampaignAdapter;
import com.okb.odutio.model.CampaignModel;
import com.okb.odutio.service.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by onurkilic on 09/05/2017.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ProgressBar progressBar;
    private ListView listView;
    private CampaignAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        listView = (ListView) v.findViewById(R.id.list_view);
        adapter = new CampaignAdapter(getActivity(), R.layout.layout_campaign);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        Client.getCampaigns(callback);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
        Intent intent = new Intent(getActivity(), CampaignActivity.class);
        intent.putExtra("campaign", adapter.getItem(position));
        startActivity(intent);
    }

    private Callback<List<CampaignModel>> callback = new Callback<List<CampaignModel>>() {
        @Override
        public void onResponse(Call<List<CampaignModel>> call, Response<List<CampaignModel>> response) {
            progressBar.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                adapter.addAll(response.body());
            }
        }

        @Override
        public void onFailure(Call<List<CampaignModel>> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
