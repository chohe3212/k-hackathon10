package org.taehyeon.welcome_pet_khackathon.Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.taehyeon.welcome_pet_khackathon.Auth.UserAccount;
import org.taehyeon.welcome_pet_khackathon.MainActivity;
import org.taehyeon.welcome_pet_khackathon.R;

import java.util.HashMap;

public class postModify extends AppCompatActivity {

    EditText titleEt, contentsEt;
    Button button_check;
    String editTitle,editDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_modify);


        Intent intent = getIntent();
        final int isUpdateKey = intent.getIntExtra("key",123);
        final String editPostId= ""+intent.getStringExtra("editPostId");

        titleEt = (EditText)findViewById(R.id.title_editText_modify);
        contentsEt = (EditText)findViewById(R.id.contents_editText_modify);

        button_check = findViewById(R.id.button_check_modify);
        if (isUpdateKey == 123){

            button_check.setText("수정");
            loadPostData(editPostId);
        }
        else {
            button_check.setText("확인");
        }

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = ((EditText) findViewById(R.id.title_editText_modify)).getText().toString();
                final String contents = ((EditText) findViewById(R.id.contents_editText_modify)).getText().toString();

                if(title.length() > 0 && contents.length() > 0 ) {
                    if (isUpdateKey == 123){
                        Toast.makeText(getApplicationContext(),""+editPostId,Toast.LENGTH_SHORT).show();
                        updatePost(title,contents,editPostId);
                    }
                    else {
                        uploader(title,contents);
                    }

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(),"전달되지 않음.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void beginUpdate(String title, String description, String editPostId) {
        updatePost(title,description,editPostId);
    }

    private void updatePost(String title, String description, String editPostId) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title",title);
        hashMap.put("contents",description);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(editPostId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "수정....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "실패...", Toast.LENGTH_SHORT).show();
                    }
                });
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
                                    Toast.makeText(getApplicationContext(), "Post Publisher...", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "실패"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPostData(final String editPostId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = reference.orderByChild("pid").equalTo(editPostId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    //get data
                    editTitle = ""+ds.child("title").getValue();
                    editDescription = ""+ds.child("contents").getValue();

                    //set data to views
                    titleEt.setText(editTitle);
                    contentsEt.setText(editDescription);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}