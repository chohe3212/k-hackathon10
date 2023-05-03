package org.taehyeon.welcome_pet_khackathon.Home;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.taehyeon.welcome_pet_khackathon.R;
import org.taehyeon.welcome_pet_khackathon.Start_survey.survey;


public class home_Fragment extends Fragment {

    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home_, container, false);

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Cut");
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        ref2.child(User.getUid()).child("comment").child("cid").setValue("cidValue");

        btn = v.findViewById(R.id.alram);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), survey.class);
                startActivity(intent);
            }
        });





        return v;
    }


}