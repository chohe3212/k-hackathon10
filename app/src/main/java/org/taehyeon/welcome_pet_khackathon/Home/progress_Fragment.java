package org.taehyeon.welcome_pet_khackathon.Home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.taehyeon.welcome_pet_khackathon.Alarm.AlarmReciver;
import org.taehyeon.welcome_pet_khackathon.Alarm.Destination;
import org.taehyeon.welcome_pet_khackathon.MainActivity;
import org.taehyeon.welcome_pet_khackathon.R;
import org.taehyeon.welcome_pet_khackathon.Start_survey.survey2;

import java.util.HashMap;

public class progress_Fragment extends Fragment {

    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    survey2 activity = (survey2) getActivity();
    ProgressBar progress,progress2;
    TextView text_per;
    Button select;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        progress = (ProgressBar) v.findViewById(R.id.progressbar);
        progress2 = (ProgressBar) v.findViewById(R.id.progressBar);
        text_per = (TextView) v.findViewById(R.id.text_progress_per);
        select = (Button)v.findViewById(R.id.button_alarm_Click);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("WelcomePet");
        ref.child("UserAccount").child(user.getUid()).child("count").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            String job = String.valueOf(task.getResult().getValue());
                            int p = Integer.parseInt(job);
                            if(p == 5)
                            {
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("check","chx");
                                hashMap.put("count","0");
                                hashMap.put("progress","0");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("WelcomePet").child("UserAccount");
                                ref.child(user.getUid()).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getActivity(),"체험이 종료되었습니다",Toast.LENGTH_SHORT).show();
                                            progress.setProgress(0);
                                            progress2.setProgress(0);
                                            text_per.setText(""+0+"%");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                            else{
                                progress2.setProgress(p,true);
                                ref.child("UserAccount").child(user.getUid()).child("progress").get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                Log.e("firebase", "Error getting data", task.getException());
                                            }
                                            else {
                                                String num = String.valueOf(task.getResult().getValue());
                                                int a = Integer.parseInt(num);
                                                progress.setProgress(a,true);
                                                text_per.setText(""+a+"%");
                                            }
                                        }
                                    });
                            }
                        }
                    }
                });


        return v;
    }


}