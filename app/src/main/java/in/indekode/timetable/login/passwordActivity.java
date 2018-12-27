package in.indekode.timetable.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.indekode.timetable.R;

public class passwordActivity extends AppCompatActivity {
        EditText changePasswordEdit;
        Button changePassword;
        FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
        TextView changeTitle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_password);

            setUIView();

            Intent intent=getIntent();
            String Number=intent.getStringExtra("Gcoeara");

            switch (Number) {
                case "0":
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    changePassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newpassword = changePasswordEdit.getText().toString();
                            firebaseUser.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Password Successfully changed", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Password is not Changed", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    break;


                case "1":
                    firebaseAuth = FirebaseAuth.getInstance();

                    changePasswordEdit.setHint("Enter the Valid Email");
                    changeTitle.setText("Email");
                    changePassword.setText("Reset");
                    changePassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newEmail = changePasswordEdit.getText().toString().trim();

                            if(newEmail.equals("")){
                                Toast.makeText(getApplicationContext(), "Please Enter the Registerd Email-id", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                firebaseAuth.sendPasswordResetEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Please see Email! Reset Email is Send", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else
                                            Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    break;

            }


        }

        private void setUIView()
        {
            changePassword = (Button)findViewById(R.id.btnchangePassword);
            changePasswordEdit = (EditText)findViewById(R.id.etchangePassword);
            changeTitle = (TextView)findViewById(R.id.tvTitle);
        }

    }
