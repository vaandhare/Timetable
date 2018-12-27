package in.indekode.timetable.login;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.indekode.timetable.R;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner SignAs, Class, Batch, ProfName;
    EditText Username, Name, RollNo, PassWord, ConfirmPassword;
    Button Save;
    TextView ClassTV, BatchTV;
    ImageView Profile;

    String userName , name ,rollNo , password , confirmPassword;

    private ArrayAdapter arrayAdapter;
    private FirebaseAuth firebaseAuth;

    String[] signAsArray = {"FACULTY","STUDENT"};
    String[] classArray = {"SE COMP", "TE COMP", "BE COMP"};
    String[] seBatchArray = {"S1","S2","S3","S4"};
    String[] teBatchArray = {"T1","T2","T3","T4"};
    String[] beBatchArray = {"B1","B2","B3","B4"};

    ArrayList<String> professorNameArray;

    int SignID;
    String sign, classSelected, batchSelected;
    final static int GALLERY_PEAK = 2;
    final int PIC_CROP = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUIViews();
        professorNameArray=new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, signAsArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        SignAs.setAdapter(arrayAdapter);
        SignAs.setOnItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        retriveProfessor();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, professorNameArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        ProfName.setAdapter(arrayAdapter);
        ProfName.setOnItemSelectedListener(this);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    firebaseAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUserData();
                                Toast.makeText(SignUpActivity.this, "Registration successfull!", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                finish();
                                //startActivity(new Intent(RegistrationActivity.this, loginScreen.class));

                            } else {
                                Toast.makeText(SignUpActivity.this, "Registration UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PEAK);
            }
        });
    }

    private void setUIViews(){
        SignAs = findViewById(R.id.spnSignAs);
        Class = findViewById(R.id.spnClass);
        Batch = findViewById(R.id.spnBatch);

        ProfName = findViewById(R.id.spnProfName);

        Username = findViewById(R.id.etUserName);
        Name = findViewById(R.id.etName);
        RollNo = findViewById(R.id.etRollNo);
        PassWord = findViewById(R.id.etPassword);
        ConfirmPassword = findViewById(R.id.etConfirmPassword);

        Save =  findViewById(R.id.btnSave);

        ClassTV = findViewById(R.id.tvClass);
        BatchTV = findViewById(R.id.tvBatch);

        Profile = findViewById(R.id.ivProfile);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PEAK && resultCode == RESULT_OK && data!=null){
            Uri imageUri = data.getData();
            Profile.setImageURI(null);
            performCrop(imageUri);
        }
        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                Profile.setImageBitmap(selectedBitmap);
            }
        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== 1){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendUserData()
    {
        if(classSelected.equals("")){
            classSelected = Name.getText().toString().trim();
        }
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile=new UserProfile(sign, userName, name, classSelected, batchSelected,rollNo);
        databaseReference.setValue(userProfile);
    }

    private Boolean validate()
    {

        Boolean result=false;
        userName = Username.getText().toString().trim();
        name = Name.getText().toString().trim();
        rollNo = RollNo.getText().toString().trim();
        password = PassWord.getText().toString().trim();
        confirmPassword = ConfirmPassword.getText().toString().trim();


        if (name.isEmpty() || userName.isEmpty() ||password.isEmpty() || confirmPassword.isEmpty())
        {
            Toast.makeText(this,"Please enter all details",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!password.equals(confirmPassword))
                Toast.makeText(this,"Please enter all details",Toast.LENGTH_SHORT).show();
            else
                result=true;
        }

        return result;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch(adapterView.getId()){
            case R.id.spnSignAs:
                SignID=position;
                sign=adapterView.getItemAtPosition(position).toString();
                switch(SignID)
                {

                    case 0:
                        RollNo.setVisibility(View.GONE);
                        ClassTV.setVisibility(View.GONE);
                        Class.setVisibility(View.GONE);
                        BatchTV.setVisibility(View.GONE);
                        Batch.setVisibility(View.GONE);
                        classSelected="";
                        batchSelected="";
                        rollNo="";
                        break;

                    case 2:
                        RollNo.setVisibility(View.VISIBLE);
                        ClassTV.setVisibility(View.VISIBLE);
                        Class.setVisibility(View.VISIBLE);
                        BatchTV.setVisibility(View.VISIBLE);
                        Batch.setVisibility(View.VISIBLE);
                        ProfName.setVisibility(View.GONE);
                        Name.setVisibility(View.VISIBLE);
                        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,classArray);
                        break;
                }

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                Class.setAdapter(arrayAdapter);
                Class.setOnItemSelectedListener(this);
                break;

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

                Batch.setAdapter(arrayAdapter);
                Batch.setOnItemSelectedListener(this);
                break;

            case R.id.spnBatch:
                batchSelected = adapterView.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public ArrayList<String> retriveProfessor(){
        FirebaseFirestore.getInstance()
                .collection("PROFESSOR")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            professorNameArray = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                professorNameArray.add(document.getId());
                            }
                        }
                    }
                });
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, professorNameArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        ProfName.setAdapter(arrayAdapter);
        ProfName.setOnItemSelectedListener(this);
        return professorNameArray;
    }
}
