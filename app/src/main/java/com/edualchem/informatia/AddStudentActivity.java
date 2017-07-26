    package com.edualchem.informatia;

    import android.content.Intent;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class AddStudentActivity extends AppCompatActivity {
    LinearLayout ll_con,ll_add_fetch,ll_load;
    EditText admissionno;
    String addno="";
    Button add,proceed,cancel;
    TextView name,std,div;
    AdmissionData std_data;
    DatabaseReference dbRef,writeDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        writeDb = FirebaseDatabase.getInstance().getReference();
        ll_con = (LinearLayout)findViewById(R.id.ll_container);

        ll_load = (LinearLayout)findViewById(R.id.ll_loading);

        ll_add_fetch = (LinearLayout)findViewById(R.id.ll_admission_fetch);

        admissionno = (EditText)findViewById(R.id.admission_no_ed);

        name = (TextView)findViewById(R.id.name_detail);
        std = (TextView)findViewById(R.id.class_detail);
        div = (TextView)findViewById(R.id.div_detail);

        add = (Button)findViewById(R.id.add_btn);

        proceed = (Button) findViewById(R.id.proceed_btn);

        cancel = (Button) findViewById(R.id.cancel_btn);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddStudentActivity.this,StudentActivity.class));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_load.setVisibility(View.VISIBLE);
                addno = admissionno.getText().toString();
                dbRef = FirebaseDatabase.getInstance().getReference().child("admissions/"+addno);

                load();

            }
        });
    }

    private void load(){
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    AdmissionData data = dataSnapshot.getValue(AdmissionData.class);
                    ll_add_fetch.setVisibility(View.VISIBLE);
                    std_data = data;
                    name.setText(data.getName());
                    div.setText(data.getDiv());
                    std.setText(data.getStd());
                    ll_load.setVisibility(View.GONE);
                    ll_con.setVisibility(View.VISIBLE);
                    add.setVisibility(View.GONE);

                   /*
                    for (DataSnapshot post : dataSnapshot.getChildren()){
                        AdmissionData data = post.getValue(AdmissionData.class);
                        //Log.e("admissionno==",data.getNo());
                        if((""+data.getNo()).equals(addno)){
                            std_data = data;
                            //Log.e("Name==",""+data.getName()+data.getDiv()+data.getStd());

                            ll_add_fetch.setVisibility(View.VISIBLE);

                            name.setText(data.getName());
                            div.setText(data.getDiv());
                            std.setText(data.getStd());
                            ll_load.setVisibility(View.GONE);
                            ll_con.setVisibility(View.VISIBLE);
                            add.setVisibility(View.GONE);

                        }
                    }
                    */
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

        private void setData(){
            writeDb.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(addno).setValue(std_data);
            Toast.makeText(getApplicationContext(),"Student Added",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddStudentActivity.this,MainActivity.class));

            finish();
        }
}
