package in.indekode.timetable;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText Quote , Author;
    Button Save, Fetch;
    TextView textView;

    private DocumentReference documentReference = FirebaseFirestore.getInstance().collection("SE_TIMETABLE").document("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Quote = findViewById(R.id.etQuote);
        Author = findViewById(R.id.etAuthor);
        Save =  findViewById(R.id.btnSave);
        Fetch =  findViewById(R.id.btnFetch);
        textView = findViewById(R.id.textView);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quoteText = Quote.getText().toString();
                String authorText = Author.getText().toString();

                if(!quoteText.isEmpty() && !authorText.isEmpty()){
                    Map<String, String> dataToSave = new HashMap<String, String>();
                    dataToSave.put("quote", quoteText);
                    dataToSave.put("author", authorText);
                    documentReference.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Document has been Saved", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Document has not been Saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else{
                    Toast.makeText(getApplicationContext(), "Please insert Data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Map<String, String> dataToFetch = new HashMap<String, String>();

                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            UploadTImetable uploadTImetable = documentSnapshot.toObject(UploadTImetable.class);
                            //Toast.makeText(getApplicationContext(),uploadTImetable.getDay(),Toast.LENGTH_SHORT).show();
                            textView.setText(uploadTImetable.getDay()+"\n"+
                            uploadTImetable.getTime());
                        }
                    }
                });
            }
        });
    }
}
