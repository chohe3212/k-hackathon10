package org.taehyeon.welcome_pet_khackathon.Community;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.Write;

import org.taehyeon.welcome_pet_khackathon.Auth.UserAccount;
import org.taehyeon.welcome_pet_khackathon.Auth.Userinput;
import org.taehyeon.welcome_pet_khackathon.MainActivity;
import org.taehyeon.welcome_pet_khackathon.R;
import org.taehyeon.welcome_pet_khackathon.Userinfo.userinfo_Fragment;

import java.util.HashMap;
import java.util.jar.Attributes;

import io.grpc.ManagedChannelProvider;


public class WritePostFragment extends Fragment {

    EditText titleEt, contentsEt;
    Button button_check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write_post, container, false);

        titleEt = (EditText)v.findViewById(R.id.title_editText);
        contentsEt = (EditText)v.findViewById(R.id.contents_editText);


        button_check = v.findViewById(R.id.button_check);
        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = ((EditText) v.findViewById(R.id.title_editText)).getText().toString();
                final String contents = ((EditText) v.findViewById(R.id.contents_editText)).getText().toString();

                if(title.length() > 0 && contents.length() > 0 ) {
                    uploader(title,contents);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Toast.makeText(getContext(),"전달되지 않음.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void uploader(String title,String content){
        //String filePathAndName = "Posts/"+"Post_" + timeStamp;
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser user = Auth.getCurrentUser();
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        final String id = user.getUid();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("WelcomePet");
        ref.child("UserAccount").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WriteInfo writeInfo = new WriteInfo();
                if(snapshot.getValue(UserAccount.class) != null) {
                    UserAccount account = snapshot.getValue(UserAccount.class);
                    writeInfo.setComments("0");
                    writeInfo.setLike("0");
                    writeInfo.setTime(timeStamp);
                    writeInfo.setPid(timeStamp);
                    writeInfo.setPublisher(account.getName());
                    writeInfo.setTitle(title);
                    writeInfo.setContents(content);
                    writeInfo.setUid(account.getIdtoken());

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
                    databaseReference.child(timeStamp).setValue(writeInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(getActivity(), "Post Publisher...", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "실패"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(getContext(), "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}