package in.indekode.timetable.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.indekode.timetable.main.HomeActivity;
import in.indekode.timetable.R;

public class LoginActivity extends AppCompatActivity {

    private EditText Email,Password;
    private ImageView Logo;
    private TextView SignUp, ForgotPassword;
    private CardView Card;
    private FirebaseAuth firebaseAuth;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUIView();
        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseUser user =firebaseAuth.getCurrentUser();
        if (user!=null)
        {
            finish();

            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Email.getText().toString(),Password.getText().toString());

            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), passwordActivity.class);
                intent.putExtra("Gcoeara","1");
                startActivity(intent);
            }
        });
    }

    private void setUIView()
    {
        Email= findViewById(R.id.etEmail);
        Password= findViewById(R.id.etPassword);
        Login= findViewById(R.id.btnLogin);
        SignUp = findViewById(R.id.tvSignUp);
        ForgotPassword = findViewById(R.id.tvForgotPassword);
    }



    private void validate(final String userEmail, final String userPassword) {

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please Enter the data", Toast.LENGTH_SHORT).show();
        } else
        {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
