package in.indekode.timetable.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import in.indekode.timetable.R;
import in.indekode.timetable.UploadTImetable;
import in.indekode.timetable.fire.Firedata;
import in.indekode.timetable.login.UserProfile;

public class HomeActivity extends AppCompatActivity {

    CardView Timetable, Profile;
    FloatingActionButton Home;
    TextView PresentText, NextLexture, PrsentProfName, NextProfName;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    Firedata firedata;
    String day, hrs;

    int i=0,j=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNav();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firedata = new Firedata();
        day = firedata.seTimetable();
        getCurrentDay();

        getUserData();
    }

    private void getCurrentDay(){
        DateFormat dd = new SimpleDateFormat("EEE");
        day = dd.format(Calendar.getInstance().getTime());

        switch (day){
            case "MON":i=0;
                break;

            case "TUE":i=1;
                break;

            case "WED":i=2;
                break;

            case "THU":i=3;
                break;

            case "FRI":i=4;
                break;
        }
        try {
            SimpleDateFormat sdf =new SimpleDateFormat("HH:mm");
            Date date=new Date();
            Date d2=new Date();
            d2=sdf.parse(sdf.format(date));
            System.out.println(d2);
            if(d2.compareTo(sdf.parse("10:15")) >=0  && d2.compareTo(sdf.parse("11:15"))<0)
            {
                j=0;
            } else if(d2.compareTo(sdf.parse("11:15")) >=0  && d2.compareTo(sdf.parse("12:15"))<0)
            {
                j=1;
            }else if(d2.compareTo(sdf.parse("12:15")) >=0  && d2.compareTo(sdf.parse("13:15"))<0)
            {
                j=2;
            }else if(d2.compareTo(sdf.parse("13:15")) >=0  && d2.compareTo(sdf.parse("13:45"))<0)
            {
                j=3;
            }else if(d2.compareTo(sdf.parse("13:45")) >=0  && d2.compareTo(sdf.parse("14:45"))<0)
            {
                j=4;
            }else if(d2.compareTo(sdf.parse("14:45")) >=0  && d2.compareTo(sdf.parse("15:45"))<0)
            {
                j=5;
            }else if(d2.compareTo(sdf.parse("15:45")) >=0  && d2.compareTo(sdf.parse("16:45"))<0)
            {
                j=6;
            }else if(d2.compareTo(sdf.parse("16:45")) >=0  && d2.compareTo(sdf.parse("17:45"))<0)
            {
                j=7;
            }else
            {
                j=8;
            }
        } catch (ParseException ex) {
            Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }

    public void BottomNav(){
        Timetable = findViewById(R.id.cvTimetable);
        Profile = findViewById(R.id.cvProfile);
        Home = findViewById(R.id.fabHome);
        PresentText = findViewById(R.id.tvPresentSubject);
        NextLexture = findViewById(R.id.tvNextSubject);

        PrsentProfName = findViewById(R.id.tvPresentProfName);
        NextProfName = findViewById(R.id.tvNextProfName);

        Timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TimetableActivity.class));
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
                //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

    public void setadapSE(){
        FirebaseFirestore.getInstance()
                .collection("SE_TIMETABLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            UploadTImetable mon = myListOfDocuments.get(i).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            if(j<8) {
                                PresentText.setText(mon.getTime().get(j));
                                PrsentProfName.setText(mon.getSubProf().get(j));
                            }
                            else {
                                PresentText.setText("Holiday");
                                PrsentProfName.setText("NO Professor!");
                            }
                            if((j+1)<8) {
                                NextLexture.setText(mon.getTime().get(j + 1));
                                NextProfName.setText(mon.getSubProf().get(j+1));
                            }
                            else {
                                NextLexture.setText("Holiday");
                                NextProfName.setText("NO Professor!");
                            }
                        }
                    }
                });
    }

    public void setadapTE(){
        FirebaseFirestore.getInstance()
                .collection("TE_TIMETABLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            UploadTImetable mon = myListOfDocuments.get(i).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            if(j<8) {
                                PresentText.setText(mon.getTime().get(j));
                                PrsentProfName.setText(mon.getSubProf().get(j));
                            }
                            else {
                                PresentText.setText("Holiday");
                                PrsentProfName.setText("NO Professor!");
                            }
                            if((j+1)<8) {
                                NextLexture.setText(mon.getTime().get(j + 1));
                                NextProfName.setText(mon.getSubProf().get(j+1));
                            }
                            else {
                                NextLexture.setText("Holiday");
                                NextProfName.setText("NO Professor!");
                            }
                        }
                    }
                });
    }

    public void setadapBE(){
        FirebaseFirestore.getInstance()
                .collection("BE_TIMETABLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            UploadTImetable mon = myListOfDocuments.get(i).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            if(j<8) {
                                PresentText.setText(mon.getTime().get(j));
                                PrsentProfName.setText(mon.getSubProf().get(j));
                            }
                            else {
                                PresentText.setText("Holiday");
                                PrsentProfName.setText("NO Professor!");
                            }
                            if((j+1)<8) {
                                NextLexture.setText(mon.getTime().get(j + 1));
                                NextProfName.setText(mon.getSubProf().get(j+1));
                            }
                            else {
                                NextLexture.setText("Holiday");
                                NextProfName.setText("NO Professor!");
                            }
                        }
                    }
                });
    }

    public void setadapDeshmukhMam(final String year, final String profName){
        FirebaseFirestore.getInstance()
                .collection(year)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            UploadTImetable mon = myListOfDocuments.get(i).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            if(j<8) {
                                PresentText.setText(mon.getTime().get(j));
                                PrsentProfName.setText(profName);
                            }
                            else {
                                PresentText.setText("Holiday");
                                PrsentProfName.setText("NO Professor!");
                            }
                            if((j+1)<8) {
                                NextLexture.setText(mon.getTime().get(j + 1));
                                NextProfName.setText(profName);
                            }
                            else {
                                NextLexture.setText("Holiday");
                                NextProfName.setText("NO Professor!");
                            }
                        }
                    }
                });
    }

    private void getUserData(){
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                switch (userProfile.getYear()){
                    case "SE COMP":
                        setadapSE();
                        break;
                    case "TE COMP":
                        setadapTE();
                        break;
                    case "BE COMP":
                        setadapBE();
                        break;

                    case "DESHMUKH MAM":
                        setadapDeshmukhMam(userProfile.getYear().toString(), userProfile.getName());
                        break;
                    default:
                        setadapDeshmukhMam(userProfile.getName().toString(), userProfile.getName());
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
