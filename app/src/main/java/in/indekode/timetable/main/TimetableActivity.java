package in.indekode.timetable.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import in.indekode.timetable.R;
import in.indekode.timetable.UploadTImetable;
import in.indekode.timetable.login.UserProfile;

public class TimetableActivity extends AppCompatActivity {

    CardView Timetable, Profile;
    FloatingActionButton Home;
    RecyclerView recyclerView,recyclerView1,recyclerView2,
            recyclerView3,recyclerView4;
    LinearLayoutManager linearLayoutManager,linearLayoutManager1,linearLayoutManager2,
            linearLayoutManager3,linearLayoutManager4;

    UploadTImetable mon, tue, wed, thu, fri;

    String[] subjects = {"DBMS", "CN", "TOC","BREAK", "LAB","LAB", "FREE","FREE"};

    int[] imgs = {R.drawable.automobile,R.drawable.automobile,R.drawable.automobile,R.drawable.automobile
            ,R.drawable.automobile,R.drawable.automobile,R.drawable.automobile,R.drawable.automobile};


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    UserProfile userProfile;
    int batch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        setUpload();

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getUserData();
        BottomNav();
        linearManager();
        recycleView();
        layoutMan();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void setUpload(){
        mon = new UploadTImetable();
        tue = new UploadTImetable();
        wed = new UploadTImetable();
        thu = new UploadTImetable();
        fri = new UploadTImetable();
        userProfile = new UserProfile();
    }

    private void getUserData(){
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
                switch (userProfile.getYear()){
                    case "SE COMP":
                        switch (userProfile.getBatch()){
                            case "S1":  batch = 0;
                                break;
                            case "S2":  batch = 1;
                                break;
                            case "S3":  batch = 2;
                                break;
                            case "S4":  batch = 3;
                                break;
                        }
                        setadapSE(batch);
                        break;
                    case "TE COMP":
                        switch (userProfile.getBatch()){
                            case "T1":  batch = 0;
                                break;
                            case "T2":  batch = 1;
                                break;
                            case "T3":  batch = 2;
                                break;
                            case "T4":  batch = 3;
                                break;
                        }
                        setadapTE(batch);
                        break;
                    case "BE COMP":
                        switch (userProfile.getBatch()){
                            case "B1":  batch = 0;
                                break;
                            case "B2":  batch = 1;
                                break;
                            case "B3":  batch = 2;
                                break;
                            case "B4":  batch = 3;
                                break;
                        }
                        setadapBE(batch);
                        break;

                    case "DESHMUKH MAM":
                        setadapDeshmukhMam(userProfile.getYear().toString());
                        break;
                   default:
                       setadapDeshmukhMam(userProfile.getName().toString());
                       break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void linearManager()
    {

        linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager1=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager2=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager3=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager4=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

    }

    public void recycleView()
    {
        recyclerView=findViewById(R.id.rvMon);
        recyclerView1=findViewById(R.id.rvTue);
        recyclerView2= findViewById(R.id.rvWed);
        recyclerView3=findViewById(R.id.rvThu);
        recyclerView4=findViewById(R.id.rvFri);
    }

    public void layoutMan()
    {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView3.setLayoutManager(linearLayoutManager3);
        recyclerView4.setLayoutManager(linearLayoutManager4);

    }

    public void setadapSE(final int batch)
    {

        FirebaseFirestore.getInstance()
                .collection("SE_TIMETABLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            mon = myListOfDocuments.get(0).toObject(UploadTImetable.class);
                            tue = myListOfDocuments.get(1).toObject(UploadTImetable.class);
                            wed = myListOfDocuments.get(2).toObject(UploadTImetable.class);
                            thu = myListOfDocuments.get(3).toObject(UploadTImetable.class);
                            fri = myListOfDocuments.get(4).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            RecyclerViewAdapter recyclerViewAdapter =new RecyclerViewAdapter(getApplicationContext(),imgs,mon.getTime(),mon.getSubProf(),mon.getPraTime().get(batch), "MON");
                            recyclerView.setAdapter(recyclerViewAdapter);
                            RecyclerViewAdapter recyclerViewAdapter1 =new RecyclerViewAdapter(getApplicationContext(),imgs,tue.getTime(),tue.getSubProf(),tue.getPraTime().get(batch),"TUE");
                            recyclerView1.setAdapter(recyclerViewAdapter1);
                            RecyclerViewAdapter recyclerViewAdapter2 =new RecyclerViewAdapter(getApplicationContext(),imgs,wed.getTime(),wed.getSubProf(),wed.getPraTime().get(batch),"WED");
                            recyclerView2.setAdapter(recyclerViewAdapter2);
                            RecyclerViewAdapter recyclerViewAdapter3 =new RecyclerViewAdapter(getApplicationContext(),imgs,thu.getTime(),thu.getSubProf(),thu.getPraTime().get(batch),"THU");
                            recyclerView3.setAdapter(recyclerViewAdapter3);
                            RecyclerViewAdapter recyclerViewAdapter4 =new RecyclerViewAdapter(getApplicationContext(),imgs,fri.getTime(),fri.getSubProf(),fri.getPraTime().get(batch),"FRI");
                            recyclerView4.setAdapter(recyclerViewAdapter4);
                        }
                    }
                });

    }

    public void setadapTE(final int batch)
    {

        FirebaseFirestore.getInstance()
                .collection("TE_TIMETABLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            mon = myListOfDocuments.get(0).toObject(UploadTImetable.class);
                            tue = myListOfDocuments.get(1).toObject(UploadTImetable.class);
                            wed = myListOfDocuments.get(2).toObject(UploadTImetable.class);
                            thu = myListOfDocuments.get(3).toObject(UploadTImetable.class);
                            fri = myListOfDocuments.get(4).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            RecyclerViewAdapter recyclerViewAdapter =new RecyclerViewAdapter(getApplicationContext(),imgs,mon.getTime(),mon.getSubProf(),mon.getPraTime().get(batch), "MON");
                            recyclerView.setAdapter(recyclerViewAdapter);
                            RecyclerViewAdapter recyclerViewAdapter1 =new RecyclerViewAdapter(getApplicationContext(),imgs,tue.getTime(),tue.getSubProf(),tue.getPraTime().get(batch),"TUE");
                            recyclerView1.setAdapter(recyclerViewAdapter1);
                            RecyclerViewAdapter recyclerViewAdapter2 =new RecyclerViewAdapter(getApplicationContext(),imgs,wed.getTime(),wed.getSubProf(),wed.getPraTime().get(batch),"WED");
                            recyclerView2.setAdapter(recyclerViewAdapter2);
                            RecyclerViewAdapter recyclerViewAdapter3 =new RecyclerViewAdapter(getApplicationContext(),imgs,thu.getTime(),thu.getSubProf(),thu.getPraTime().get(batch),"THU");
                            recyclerView3.setAdapter(recyclerViewAdapter3);
                            RecyclerViewAdapter recyclerViewAdapter4 =new RecyclerViewAdapter(getApplicationContext(),imgs,fri.getTime(),fri.getSubProf(),fri.getPraTime().get(batch),"FRI");
                            recyclerView4.setAdapter(recyclerViewAdapter4);
                        }
                    }
                });

    }

    public void setadapBE(final int batch)
    {

        FirebaseFirestore.getInstance()
                .collection("BE_TIMETABLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            mon = myListOfDocuments.get(0).toObject(UploadTImetable.class);
                            tue = myListOfDocuments.get(1).toObject(UploadTImetable.class);
                            wed = myListOfDocuments.get(2).toObject(UploadTImetable.class);
                            thu = myListOfDocuments.get(3).toObject(UploadTImetable.class);
                            fri = myListOfDocuments.get(4).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            RecyclerViewAdapter recyclerViewAdapter =new RecyclerViewAdapter(getApplicationContext(),imgs,mon.getTime(),mon.getSubProf(), mon.getPraTime().get(batch),"MON");
                            recyclerView.setAdapter(recyclerViewAdapter);
                            RecyclerViewAdapter recyclerViewAdapter1 =new RecyclerViewAdapter(getApplicationContext(),imgs,tue.getTime(),tue.getSubProf(),tue.getPraTime().get(batch),"TUE");
                            recyclerView1.setAdapter(recyclerViewAdapter1);
                            RecyclerViewAdapter recyclerViewAdapter2 =new RecyclerViewAdapter(getApplicationContext(),imgs,wed.getTime(),wed.getSubProf(),wed.getPraTime().get(batch),"WED");
                            recyclerView2.setAdapter(recyclerViewAdapter2);
                            RecyclerViewAdapter recyclerViewAdapter3 =new RecyclerViewAdapter(getApplicationContext(),imgs,thu.getTime(),thu.getSubProf(),thu.getPraTime().get(batch),"THU");
                            recyclerView3.setAdapter(recyclerViewAdapter3);
                            RecyclerViewAdapter recyclerViewAdapter4 =new RecyclerViewAdapter(getApplicationContext(),imgs,fri.getTime(),fri.getSubProf(),fri.getPraTime().get(batch),"FRI");
                            recyclerView4.setAdapter(recyclerViewAdapter4);
                        }
                    }
                });

    }

    public void setadapDeshmukhMam(final String year){
        FirebaseFirestore.getInstance()
                .collection(year)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            mon = myListOfDocuments.get(0).toObject(UploadTImetable.class);
                            tue = myListOfDocuments.get(1).toObject(UploadTImetable.class);
                            wed = myListOfDocuments.get(2).toObject(UploadTImetable.class);
                            thu = myListOfDocuments.get(3).toObject(UploadTImetable.class);
                            fri = myListOfDocuments.get(4).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            ArrayList<String> subProf=new ArrayList<>();
                            for(int i=0;i<8;i++)
                                subProf.add(year);

                            RecyclerViewAdapter recyclerViewAdapter =new RecyclerViewAdapter(getApplicationContext(),imgs,mon.getTime(),subProf,year,"MON");
                            recyclerView.setAdapter(recyclerViewAdapter);
                            RecyclerViewAdapter recyclerViewAdapter1 =new RecyclerViewAdapter(getApplicationContext(),imgs,tue.getTime(),subProf,year,"TUE");
                            recyclerView1.setAdapter(recyclerViewAdapter1);
                            RecyclerViewAdapter recyclerViewAdapter2 =new RecyclerViewAdapter(getApplicationContext(),imgs,wed.getTime(),subProf,year,"WED");
                            recyclerView2.setAdapter(recyclerViewAdapter2);
                            RecyclerViewAdapter recyclerViewAdapter3 =new RecyclerViewAdapter(getApplicationContext(),imgs,thu.getTime(),subProf,year,"THU");
                            recyclerView3.setAdapter(recyclerViewAdapter3);
                            RecyclerViewAdapter recyclerViewAdapter4 =new RecyclerViewAdapter(getApplicationContext(),imgs,fri.getTime(),subProf,year,"FRI");
                            recyclerView4.setAdapter(recyclerViewAdapter4);
                        }
                    }
                });
    }





    public void BottomNav(){
        Timetable = findViewById(R.id.cvTimetable);
        Profile = findViewById(R.id.cvProfile);
        Home = findViewById(R.id.fabHome);

        Timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), TimetableActivity.class));
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    {
        Context mcontext;
        ArrayList<String> EventNames;
        ArrayList<String> subProf;
        String department;
        int[] images ;
        String[] time = {"10:15 to 11:15", "11:15 to 12:15", "12:15 to 1:15","1:15 to 1:45", "1:45 to 2:45","2:45 to 3:45", "3:45 to 4:45","4:45 to 5:45"};
        String batch;
        LayoutInflater layoutInflater;

        public RecyclerViewAdapter(Context context,int[] images, ArrayList<String> EventNames,ArrayList<String> subProf, String batch, String department )
        {
            this.mcontext=context;
            this.images=images;
            this.EventNames=EventNames;
            this.subProf = subProf;
            this.batch = batch;
            this.department = department;

            layoutInflater =LayoutInflater.from(mcontext);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= layoutInflater.inflate(R.layout.list_timetable,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.Title.setText(EventNames.get(position));
            holder.Professor.setText(subProf.get(position));
            switch (EventNames.get(position)){
                case "Lab!":
                    holder.Title.setText(batch);
                    break;
            }
            holder.Time.setText(time[position]);

        }

        @Override
        public int getItemCount() {
            return EventNames.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView Title,Time, Professor;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Title=itemView.findViewById(R.id.tvtimetable);
                Time=itemView.findViewById(R.id.tvtime);
                Professor=itemView.findViewById(R.id.tvProfessor);
            }
        }
    }

}
