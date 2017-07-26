package com.edualchem.informatia.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edualchem.informatia.R;
import com.edualchem.informatia.Sub;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by User on 12/28/2016.
 */

public class MarkAdapter extends ArrayAdapter<Sub> {

    Context context;

    ArrayList<Sub>subjects;

    TextView sub,mark,date;

    public MarkAdapter(Context context, int res, ArrayList<Sub> dataList){
        super(context,res,dataList);
        this.context = context;
        this.subjects = dataList;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.mark_layout_item,null);

        }
        Sub s = subjects.get(position);


        sub = (TextView)v.findViewById(R.id.mark_item_Sub);
        mark = (TextView)v.findViewById(R.id.mark_item_value);


        sub.setText(s.getName());
        mark.setText(""+s.getMark());

        return v;
    }
}
