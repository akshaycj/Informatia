package com.edualchem.informatia.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.edualchem.informatia.AdmissionData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.edualchem.informatia.Adapters.FeedAdapter;
import com.edualchem.informatia.FeedData;
import com.edualchem.informatia.R;
import com.yalantis.taurus.PullToRefreshView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    PullToRefreshView mPullToRefreshView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static Context con;
    private OnFragmentInteractionListener mListener;
    ListView view;
    static FirebaseDatabase db;
    public FeedFragment() {
        // Required empty public constructor
        db = FirebaseDatabase.getInstance();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2,Context context) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        con = context;

        return fragment;
    }
    ArrayList<FeedData> dataList;
    ArrayList<String> stds;
    DatabaseReference dbRef,stdRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        dbRef = db.getReference("feed");
        stdRef = db.getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        stds = new ArrayList<>();
        stds.clear();

        stdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    AdmissionData data = ds.getValue(AdmissionData.class);
                    stds.add(data.getStd());}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbRef.keepSynced(true);
        dataList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewp = inflater.inflate(R.layout.fragment_feed, container, false);
        // Inflate the layout for this fragment
        view = (ListView)viewp.findViewById(R.id.feed_list);

        //final ProgressBar bar = (ProgressBar)viewp.findViewById(R.id.feed_progress);
        load();

        mPullToRefreshView = (PullToRefreshView) viewp.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        load();

                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 200);
            }
        });

        return viewp;
    }


    public void load(){
        dataList.clear();


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot ds: dataSnapshot.getChildren() ){
                    FeedData dataType = ds.getValue(FeedData.class);

                    if(dataType.getStd().equals("All")){
                        dataList.add(dataType);
                    }
                    if(stds.contains(dataType.getStd())){
                        dataList.add(dataType);
                    }


                    Log.e("sdfa",""+dataType.getTitle());
                }
                Collections.reverse(dataList);
                FeedAdapter adapter = new FeedAdapter(con.getApplicationContext(),R.layout.fragment_feed,dataList);
                Log.e("list",""+dataList);
                view.setAdapter(adapter);
                // bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
