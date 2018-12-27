package in.indekode.timetable.login;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.indekode.timetable.R;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText ChangeName, ChangeRoll;
    TextView ChangeRollTV, ChangeClassTV, ChangeBatchTV;
    Spinner ChangeClass, ChangeBatch;
    Button ChangeSave;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private ArrayAdapter arrayAdapter;

    String[] classArray = {"SE COMP", "TE COMP", "BE COMP"};
    String[] seBatchArray = {"S1","S2","S3","S4"};
    String[] teBatchArray = {"T1","T2","T3","T4"};
    String[] beBatchArray = {"B1","B2","B3","B4"};

    int SignID, userYear, userBatch;
    String sign, classSelected, batchSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        ChangeClass.setAdapter(arrayAdapter);
        ChangeClass.setOnItemSelectedListener(this);
        getUserData();

    }

    void setupUIViews(){
        ChangeName = findViewById(R.id.etChangeName);
        ChangeRoll = findViewById(R.id.etChangeRollno);
        ChangeBatchTV = findViewById(R.id.tvChangeBatch);
        ChangeRollTV = findViewById(R.id.tvChangeRollNo);
        ChangeClassTV = findViewById(R.id.tvChangeClass);
        ChangeClass =  findViewById(R.id.spnClass);
        ChangeBatch =  findViewById(R.id.spnBatch);
        ChangeSave =  findViewById(R.id.btnSave);
    }

    private void getUserData(){
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                ChangeName.setText(userProfile.getName());

                if(userProfile.getRollNo().isEmpty()){
                    ChangeBatchTV.setVisibility(View.GONE);
                    ChangeRollTV.setVisibility(View.GONE);
                    ChangeClassTV.setVisibility(View.GONE);
                    ChangeRoll.setVisibility(View.GONE);
                    ChangeClass.setVisibility(View.GONE);
                    ChangeBatch.setVisibility(View.GONE);
                }else {
                    switch (userProfile.getYear()){
                        case "SE COMP":
                            switch (userProfile.getBatch()){
                                case "S1":
                                    userBatch = 0;
                                    break;
                                case "S2":
                                    userBatch = 1;
                                    break;
                                case "S3":
                                    userBatch = 2;
                                    break;
                                case "S4":
                                    userBatch = 3;
                                    break;
                            }
                            userYear = 0;
                            break;
                        case "TE COMP":
                            switch (userProfile.getBatch()){
                            case "T1":
                                userBatch = 0;
                                break;
                            case "T2":
                                userBatch = 1;
                                break;
                            case "T3":
                                userBatch = 2;
                                break;
                            case "T4":
                                userBatch = 3;
                                break;
                        }
                            userYear = 1;
                            break;
                        case "BE COMP":
                            switch (userProfile.getBatch()){
                                case "B1":
                                    userBatch = 0;
                                    break;
                                case "B2":
                                    userBatch = 1;
                                    break;
                                case "B3":
                                    userBatch = 2;
                                    break;
                                case "B4":
                                    userBatch = 3;
                                    break;
                            }
                            userYear = 2;
                            break;

                    }
                    ChangeRoll.setText(userProfile.getRollNo());
                    ChangeClass.setSelection(userYear);
                    ChangeBatch.setSelection(userBatch);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch(adapterView.getId()){
            case R.id.spnClass:
                classSelected=adapterView.getItemAtPosition(position).toString();
                SignID=position;
                sign=adapterView.getItemAtPosition(position).toString();
                switch(SignID)
                {
                    case 0:
                        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,seBatchArray);
                        break;

                    case 1:
                        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,teBatchArray);
                        break;

                    case 2:
                        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,beBatchArray);
                        break;
                }

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                ChangeBatch.setAdapter(arrayAdapter);
                ChangeBatch.setOnItemSelectedListener(this);
                break;

            case R.id.spnBatch:
                batchSelected = adapterView.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
