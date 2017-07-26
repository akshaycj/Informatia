package com.edualchem.informatia.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.arasthel.asyncjob.AsyncJob;
import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.edualchem.informatia.MarkData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.edualchem.informatia.R;
import com.edualchem.informatia.Sub;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import lecho.lib.hellocharts.model.Line;


public class MarkFragment extends Fragment {

    ExpandingList expandingList;

    DatabaseReference Dbref;
    ArrayList<String> exams;
    String no;
    ArrayList<Sub> fa1,fa2,sa1;
    ListView fa1_list,fa2_list,sa1_list;
    TextView name,Class,div;
    ProgressDialog dialog;
    int res[] = {R.color.pink,R.color.orange,R.color.yellow,R.color.blue,R.color.purple,R.color.green};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MarkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *


     * @return A new instance of fragment MarkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarkFragment newInstance(String no, String name, String cls, String div) {
        MarkFragment fragment = new MarkFragment();
        Bundle args = new Bundle();
        args.putString("no",no);
        args.putString("name",name);
        args.putString("class",cls);
        args.putString("div",div);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            no = getArguments().getString("no");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mark, container, false);
       /* dialog = new ProgressDialog();
        dialog.setMessage("Loading..");
        dialog.show();*/

       expandingList = (ExpandingList)view.findViewById(R.id.expanding_list_mark);


        exams = new ArrayList<>();

        name = (TextView)view.findViewById(R.id.mark_std_name);
        Class = (TextView)view.findViewById(R.id.mark_std_class);
        div = (TextView)view.findViewById(R.id.mark_std_div);

        name.setText(getArguments().getString("name"));
        Class.setText(getArguments().getString("class"));
        div.setText(getArguments().getString("div"));


        Dbref = FirebaseDatabase.getInstance().getReference("marks/"+getArguments().getString("class")+"/"+getArguments().getString("div")).child(no);
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {

                Dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap: dataSnapshot.getChildren()){
                            exams.add(snap.getKey());
                        }
                        Log.e("exams:",""+exams);


                        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
                            @Override
                            public void doOnBackground() {
                                for(int i =0; i< exams.size(); i++){
                                    final int j =i;

                                    final ArrayList<MarkData> dataList = new ArrayList<>();

                                    Dbref.child(exams.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.e("markData",""+dataSnapshot);
                                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                MarkData data = snapshot.getValue(MarkData.class);

                                                dataList.add(data);
                                            }
                                            Log.e("j:",""+j);
                                            Log.e("size:",""+dataList.size());

                                            //Log.e("date",""+dataList.get(j).getDate());


                                            addItem(exams.get(j),dataList,j,"");

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });





        return view;
    }

    private void addItem(String exam_name,ArrayList<MarkData> dataList, final int position,final String d){
        ExpandingItem item = expandingList.createNewItem(R.layout.expanding_mark_layout);

        Log.e("dataList rec",""+dataList.size());

        int count = dataList.size();


        if(item!=null){
            //String d =dataList.get(position).getDate();
            //String[] parts = d.split("00",2);
            //Log.e("date",""+parts);

            //date.setText(d);
            TextView title = (TextView)item.findViewById(R.id.mark_title);
            title.setText(exam_name);

            LinearLayout ll = (LinearLayout)item.findViewById(R.id.main_layout);

            if(count==1){
                item.createSubItem();
            }
            else{
                for(int k = 0; k<count; k++){
                    item.createSubItem(k);
                }
                Log.e("subitemSize",""+item.getSubItemsCount());
            }

            Random r = new Random();
            int i = r.nextInt(6);
            Collections.shuffle(Arrays.asList(res));

            switch (position){
                case 0:case 1:case 2:case 3:case 4:case 5:
                    item.setIndicatorColorRes(res[position]);
                    ll.setBackgroundResource(res[position]);
                    break;
                default:
                    item.setIndicatorColorRes(res[i]);
                    ll.setBackgroundResource(res[i]);
                    break;

            }

            for(int j = 0; j <item.getSubItemsCount(); j++){

                View view = item.getSubItemView(j);
                ((TextView)view.findViewById(R.id.mark_item_Sub)).setText(dataList.get(j).getSub());
                ((TextView)view.findViewById(R.id.mark_item_value)).setText(dataList.get(j).getMark()+'/'+dataList.get(j).getMaxMark());
                String da =dataList.get(j).getDate();
                String[] parts = da.split("00",2);
                TextView date = (TextView)view.findViewById(R.id.mark_date);
                date.setText(parts[0]);


            }

            item.collapse();
        }
    }


}
