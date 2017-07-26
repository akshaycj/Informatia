package com.edualchem.informatia.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.arasthel.asyncjob.AsyncJob;
import com.edualchem.informatia.MarkData;
import com.edualchem.informatia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class AnalysisFragment extends Fragment {

    public final static String[] days = new String[]{"English", "Maths", "Science", "Social", "IT", "Lan1", "Lan2"};

    public final static String[] months = new String[]{"FA1", "FA2", "SA1", "FA3", "FA4", "SA2"};

    List<AxisValue> axisValues;
    List<Column> columns ;
    List<SubcolumnValue> values;
    ArrayList<String> exams;
    ArrayList<MarkData> marks;

    private LineChartView chartTop;
    private ColumnChartView chartBottom;

    private LineChartData lineData;
    private ColumnChartData columnData;

    int res[] = {ChartUtils.COLOR_BLUE,ChartUtils.COLOR_GREEN,ChartUtils.COLOR_ORANGE,ChartUtils.COLOR_RED,ChartUtils.COLOR_VIOLET};
    String no;

    DatabaseReference Dbref;
    public AnalysisFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AnalysisFragment newInstance(String no, String name, String cls, String div) {
        AnalysisFragment fragment = new AnalysisFragment();
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
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);

        chartTop = (LineChartView) view.findViewById(R.id.chart_top);
        Log.e("number",""+no);
        exams = new ArrayList<>();
        marks = new ArrayList<>();

        generateInitialLineData();


        chartBottom = (ColumnChartView)view.findViewById(R.id.chart_bottom);
        axisValues = new ArrayList<AxisValue>();
        columns = new ArrayList<Column>();

        Log.e("raechedddd","asjh");

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
                        Log.e("examsabc:",""+exams);

                        generateColumnData();
                        /*

                        for(int i =0; i< exams.size(); i++){
                            final int j =i;

                            final ArrayList<MarkData> dataList = new ArrayList<>();
                            values = new ArrayList<SubcolumnValue>();

                            Dbref.child(exams.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.e("markData",""+dataSnapshot);
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        MarkData data = snapshot.getValue(MarkData.class);

                                        marks.add(data);

                                        int mark = Integer.parseInt(data.getMark());
                                    }



                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                        */

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });





        //generateColumnData();

        return view;
    }

    private void generateColumnData() {

        int numSubcolumns = 1;
        int numColumns = exams.size();

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();

        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();

            for (int j = 0; j < numSubcolumns; ++j) {

                Random r = new Random();
                int k = r.nextInt(res.length);
                Collections.shuffle(Arrays.asList(res));
                Log.e("color =",""+res[k]);
                values.add(new SubcolumnValue((float)2, res[k]));

            }

            axisValues.add(new AxisValue(i).setLabel(exams.get(i)));

            columns.add(new Column(values).setHasLabels(false).setHasLabelsOnlyForSelected(false));
        }

        columnData = new ColumnChartData(columns);




        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));

        //columnData.setAxisYLeft(new Axis().setHasLines(false));

        chartBottom.setColumnChartData(columnData);

        // Set value touch listener that will trigger changes for chartTop.
        chartBottom.setOnValueTouchListener(new AnalysisFragment.ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);

        chartBottom.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateInitialLineData() {
        int numValues = 7;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 6, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);
        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateLineData(final int color, final float range, int index) {
        // Cancel last animation if not finished.
        chartTop.cancelDataAnimation();

        final List<AxisValue> axisValues = new ArrayList<AxisValue>();
        final List<PointValue> values = new ArrayList<PointValue>();
        marks = new ArrayList<MarkData>();

        Dbref.child(exams.get(index)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("markData",""+dataSnapshot);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MarkData data = snapshot.getValue(MarkData.class);

                    marks.add(data);

                    //int mark = Integer.parseInt(data.getMark());
                }

                for (int i = 0; i < marks.size(); i++) {
                    values.add(new PointValue(i, Integer.parseInt(marks.get(i).getMark())));
                    axisValues.add(new AxisValue(i).setLabel(marks.get(i).getSub()));
                }



                // Modify data targets
                Line line = new Line(values);// For this example there is always only one line.
                line.setHasLabels(true);
                line.setColor(color);

                List<Line> lines = new ArrayList<Line>();
                lines.add(line);

                lineData = new LineChartData(lines);
                lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setMaxLabelChars(3));
                lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

                chartTop.setLineChartData(lineData);
                int y =0;

                for (PointValue value : line.getValues()) {
                    // Change target only for Y value.
                    //float y =(float) Math.random() * range;
                    value.setTarget(value.getX(),Integer.parseInt(marks.get(y).getMark()) );
                    y++;

                }

                // Start new data animation with 300ms duration;
                chartTop.startDataAnimation(300);
                chartTop.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.e("ividuthe size",""+marks.size());





    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {

            int j = 100;
            generateLineData(value.getColor(), j, columnIndex);
        }

        @Override
        public void onValueDeselected() {

            generateLineData(ChartUtils.COLOR_GREEN, 0,0);

        }
    }


}
