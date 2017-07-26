package com.edualchem.informatia.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edualchem.informatia.SlideshowDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.edualchem.informatia.Adapters.GalleryAdapter;
import com.edualchem.informatia.GalleryData;
import com.edualchem.informatia.R;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.yalantis.taurus.PullToRefreshView;

import java.io.File;
import java.util.ArrayList;

import fr.tvbarthel.intentshare.IntentShare;


public class GalleryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    PullToRefreshView mPullToRefreshView;
    File localfile;
    StorageReference child;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference db;

    public GalleryFragment() {
        // Required empty public constructor
    }


    private ArrayList<String> images;

    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseDatabase.getInstance().getReference("gallery");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gallery, container, false);
        images = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        load();

        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
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

        return view;
    }
public void load(){

    images.clear();
    db.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for( DataSnapshot ds: dataSnapshot.getChildren() ){
                GalleryData  url = ds.getValue(GalleryData.class);
                images.add(url.getKey());
            }
            //Log.e("Url list",""+images);
            mAdapter = new GalleryAdapter(getContext().getApplicationContext(), images);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getContext().getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", images);
                    bundle.putInt("position", position);
                    getFrag(bundle);

                }

                @Override
                public void onLongClick(View view, int position) {



                }
            }));

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}
    // TODO: Rename method, update argument and hook method into UI event
public void getFrag(Bundle bundle){
    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
    newFragment.setArguments(bundle);
    newFragment.show(ft, "slideshow");
}


}
