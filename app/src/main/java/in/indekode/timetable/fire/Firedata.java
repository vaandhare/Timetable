package in.indekode.timetable.fire;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import in.indekode.timetable.UploadTImetable;

public class Firedata {

    private DocumentReference seDocumentReference = FirebaseFirestore.getInstance().collection("SE_TIMETABLE").document("0");
    private DocumentReference teDocumentReference = FirebaseFirestore.getInstance().collection("TE_TIMETABLE").document("0");
    private DocumentReference beDocumentReference = FirebaseFirestore.getInstance().collection("BE_TIMETABLE").document("0");

    public String seTimetable(){
        final String[] day = new String[1];
        FirebaseFirestore.getInstance()
                .collection("SE_TIMETABLE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            UploadTImetable mon = myListOfDocuments.get(0).toObject(UploadTImetable.class);
                            //Log.e(TAG,mon.getDay());
                            day[0] = mon.getDay();
                        }
                    }
                });
        return day[0];
    }

    private void teTimetable(){

    }

    private void betimetable(){

    }
}
