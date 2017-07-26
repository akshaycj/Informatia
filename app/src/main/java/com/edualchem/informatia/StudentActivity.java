package com.edualchem.informatia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class StudentActivity extends AppCompatActivity {

    Button add;
    long flag = 0;
    ListView listView;
    LinearLayout std_details;
    LinearLayout no_std,loading;
    DatabaseReference dbRef;
    ProgressDialog dialog;
    ExpandingList expandingList;
    ArrayList<AdmissionData> dataList,finalList;

    int res[] = {R.color.pink,R.color.orange,R.color.yellow,R.color.blue,R.color.purple,R.color.green};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        loading = (LinearLayout)findViewById(R.id.ll_loading);


        dataList = new ArrayList<>();
        finalList = new ArrayList<>();

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(id);

        //listView = (ListView)findViewById(R.id.list_std);
        std_details = (LinearLayout)findViewById(R.id.ll_std_details);
        expandingList = (ExpandingList)findViewById(R.id.expanding_list_main);




        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
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


                            for (int i =0;i<flag;i++){

                                addItem(dataList.get(i),i);
                            }


                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        });


        no_std = (LinearLayout)findViewById(R.id.ll_no_std);
        if (flag!=0){

            no_std.setVisibility(View.GONE);
        }

        add = (Button)findViewById(R.id.add_std_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this,AddStudentActivity.class));
                finish();
            }
        });
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
                    Intent i = new Intent(StudentActivity.this,StudentProfileActivity.class);
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

                    new SweetAlertDialog(StudentActivity.this,SweetAlertDialog.WARNING_TYPE)
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

                                    Toast.makeText(getApplicationContext(),"Student Removed",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(StudentActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
