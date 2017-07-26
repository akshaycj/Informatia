package com.edualchem.informatia.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.edualchem.informatia.AddStudentActivity;
import com.edualchem.informatia.AdmissionData;
import com.edualchem.informatia.R;
import com.edualchem.informatia.StudentProfileActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class StudentFragment extends Fragment {

    Button add;
    long flag = 0;
    ListView listView;
    LinearLayout std_details;
    LinearLayout no_std,loading;
    FloatingActionButton fab;

    DatabaseReference dbRef;
    ProgressDialog dialog;
    ExpandingList expandingList;
    ArrayList<AdmissionData> dataList,finalList;

    static Context mcontext;

    int res[] = {R.color.pink,R.color.orange,R.color.yellow,R.color.blue,R.color.purple,R.color.green};

    public StudentFragment() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        dataList = new ArrayList<>();
        finalList = new ArrayList<>();
        dataList = new ArrayList<>();

    }


    public static StudentFragment newInstance(String param1, String param2, Context context) {
        StudentFragment fragment = new StudentFragment();
        mcontext = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewPort = inflater.inflate(R.layout.fragment_student, container, false);
        // Inflate the layout for this fragment
        if(viewPort!=null){

            loading = (LinearLayout)viewPort.findViewById(R.id.ll_loading);



            //listView = (ListView)viewPort.findViewById(R.id.list_std);
            std_details = (LinearLayout)viewPort.findViewById(R.id.ll_std_details);
            expandingList = (ExpandingList)viewPort.findViewById(R.id.expanding_list_main);

            no_std = (LinearLayout)viewPort.findViewById(R.id.ll_no_std);
            //no_std = (LinearLayout)viewPort.findViewById(R.id.ll_no_std);

            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Log.e("set","reached here"+snapshot);
                        AdmissionData data = snapshot.getValue(AdmissionData.class);
                        dataList.add(data);

                    }
                    flag = dataSnapshot.getChildrenCount();
                    //Log.e("set","reached here");
                    //Log.e("data","" +dataList);
                    loading.setVisibility(View.GONE);

                    if(flag==0){
                        no_std.setVisibility(View.VISIBLE);
                    }
                    else {
                        std_details.setVisibility(View.VISIBLE);
                        Log.e("Adaptersas","reached");
                        //listView.setAdapter(new StudentAdapter(getApplicationContext(),R.layout.student_item,dataList));

                        for (int i =0;i<flag;i++){

                            addItem(dataList.get(i),i);
                        }
                        no_std.setVisibility(View.GONE);


                    }

                    Log.e("Reached here","helll");




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


            Log.e("Reachde","heree");

            fab = (FloatingActionButton) viewPort.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mcontext,AddStudentActivity.class));
                    getActivity().finish();
                }
            });
        }

        return viewPort;
    }




    public void addItem(final AdmissionData data,final int position){

        final ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);


        if (item!=null){
            ((TextView) item.findViewById(R.id.exp_title)).setText(data.getName());
            Random r = new Random();
            int i = r.nextInt(6);
            Collections.shuffle(Arrays.asList(res));
            item.createSubItem();
            switch (position){
                case 0:case 1:case 2:case 3:case 4:case 5:
                    item.setIndicatorColorRes(res[position]);
                    break;
                default:
                    item.setIndicatorColorRes(res[i]);
                    break;

            }

            item.setIndicatorIconRes(R.drawable.ic_student);


            final View view = item.getSubItemView(0);

            Button bt = (Button)view.findViewById(R.id.student_view_button);
            bt.setTextColor(res[i]);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(),StudentProfileActivity.class);
                    i.putExtra("no",""+dataList.get(position).getAdmission());
                    i.putExtra("class",dataList.get(position).getStd());
                    i.putExtra("name",dataList.get(position).getName());
                    i.putExtra("div",dataList.get(position).getDiv());
                    Log.e("IDDD==",""+dataList.get(position).getAdmission());
                    startActivity(i);
                }
            });

            ((TextView)view.findViewById(R.id.exp_class)).setText(data.getStd());
            ((TextView)view.findViewById(R.id.exp_div)).setText(data.getDiv());

            ((ImageView)item.findViewById(R.id.remove_item)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Delete this Student?")
                            .setConfirmText("Okay")
                            .setCancelText("Cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();

                                    dbRef.child(""+data.getAdmission()).removeValue();
                                    expandingList.removeItem(item);

                                    Toast.makeText(getActivity().getApplicationContext(),"Student Removed",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            })
                            .show();


                }
            });
            item.collapse();
        }

    }

}
