package org.taehyeon.welcome_pet_khackathon.Alarm;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.taehyeon.welcome_pet_khackathon.R;

public class AlarmReciver extends BroadcastReceiver {

    int count;

    String[] question = {
            "당신의 반려견이 아파서 병원에 갔습니다. 총 비용이 64만원 이라면?", // death
            "반려견의 사료가 다 떨어졌습니다. 사료 하나의 가격은 평균 15,000원 입니다.", // injured
            "당신의 반려견이 산책을 나갈 시간입니다. 산책으로 스트레스를 풀어줘야 합니다.", // walk
            "당신의 장시간 외출로 인해 강아지가 많은 외로움을 겪고 있습니다.", // sleep
            "일을 끝나고 집에 도착하니 당신의 반려견이 집을 어질러 놓았습니다. " // default (None)
    };

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context,Destination.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);



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
                            String job = "" + String.valueOf(task.getResult().getValue());
                            count = Integer.parseInt(job);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"WelcomePet")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("어서와,반려견은 처음이지?")
                                    .setContentText(question[count])
                                    .setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent);
                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                            notificationManagerCompat.notify(123,builder.build());
                        }
                    }
                });

//        if(count >= 4) count = 0;
//        else count++;

    }



}
