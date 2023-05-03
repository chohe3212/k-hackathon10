package org.taehyeon.welcome_pet_khackathon.Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.google.firebase.firestore.auth.User;

import org.taehyeon.welcome_pet_khackathon.MainActivity;
import org.taehyeon.welcome_pet_khackathon.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class postDetail extends AppCompatActivity {

    String myUid,myName,postId,pLikes,hisUid, hisName,pTitle,pDescription,pTimeStamp;

    boolean mProcessComment = false;
    boolean mProcessLike = false;


    ImageView userimage;
    ImageButton likeBtn,option;
    TextView uNameTv,pTimeTv,  pTitleTv,pDescriptionTv,pLikeTv,pCommentTv;
    RecyclerView recyclerView;

    commentAdapter adapterComments;
    List<commentInfo> commentList;
    List<WriteInfo> postList;

    //add comment view
    ImageView cAvatarIv;
    ImageButton sendBtn;
    EditText commentEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        option = findViewById(R.id.option);
        likeBtn = findViewById(R.id.likeBtn);
        pLikeTv = findViewById(R.id.pLikeTv);
        userimage = findViewById(R.id.profile_detail);

        pCommentTv = findViewById(R.id.pCommentTv);// 댓글갯수
        commentEt = findViewById(R.id.commentEt);//댓글 입력
        sendBtn = findViewById(R.id.sendBtn);//댓글입력

        uNameTv = findViewById(R.id.Username_detail); //유저이름
        pTimeTv = findViewById(R.id.TimeTv_detail); //시간
        pTitleTv = findViewById(R.id.Title2_detail); //타이틀
        pDescriptionTv = findViewById(R.id.Content_detail); //콘텐츠

        recyclerView = findViewById(R.id.commentRecyclerView);
        cAvatarIv = findViewById(R.id.cAvatarIv);

        userimage.setImageResource(R.drawable.user);
        loadPostInfo();
        checkUserStatus();
        loadUserInfo();
        setLikes();
        loadComments();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost();
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions();
            }
        });

    }


    private void postComment() {
        //get data from comment edit text
        String comment = commentEt.getText().toString().trim();
        //validate
        if (TextUtils.isEmpty(comment)){
            Toast.makeText(this, "comment is empty....", Toast.LENGTH_SHORT).show();
            return;
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        //each other will have a child "Comments" the will contain comments of that post
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("tcomments");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("cId",timeStamp);
        hashMap.put("comment",comment);
        hashMap.put("timeStamp",timeStamp);
        hashMap.put("uid",myUid);
        hashMap.put("Name",myName);
        //put this data in db
        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(postDetail.this, "Comment Add....", Toast.LENGTH_SHORT).show();
                commentEt.setText("");
                updateCommentCount();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(postDetail.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        //init comment list
        commentList = new ArrayList<>();

        //path of the post, to get it's comment
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference  ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("tcomments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    commentInfo commentinfo = ds.getValue(commentInfo.class);

                    DatabaseReference query = FirebaseDatabase.getInstance().getReference("Cut");
                    query.child(User.getUid()).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.hasChild(commentinfo.getcId()))
                            {
                                commentList.add(commentinfo);
                                adapterComments = new commentAdapter(getApplicationContext(),commentList,myUid,postId);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(adapterComments);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPostInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = reference.orderByChild("pid").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    pTitle = ""+ds.child("title").getValue();
                    pDescription = ""+ds.child("contents").getValue();
                    pLikes = ""+ds.child("like").getValue();
                    pTimeStamp = ""+ds.child("time").getValue();
                    hisName = ""+ds.child("publisher").getValue();
                    hisUid= ""+ds.child("uid").getValue();
                    //String uEmail = ""+ds.child("uEmail").getValue();
                    String CommentCount = ""+ds.child("comments").getValue();

                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime = DateFormat.format("yyyy/MM/dd hh:mm aa",calendar).toString();

                    pTitleTv.setText(pTitle);
                    pDescriptionTv.setText(pDescription);
                    pLikeTv.setText(pLikes + " Likes");
                    pTimeTv.setText(pTime);
                    pCommentTv.setText(CommentCount+ " Comments");
                    uNameTv.setText(hisName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void likePost() {
        mProcessLike = true;

        final DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        myUid = User.getUid();
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mProcessLike){
                    if (snapshot.child(postId).hasChild(myUid)){
                        //already liked, so remove like
                        postsRef.child(postId).child("like").setValue(""+(Integer.parseInt(pLikes)-1));
                        likesRef.child(postId).child(myUid).removeValue();
                        mProcessLike = false;
                    }

                    else {
                        //not liked, like it
                        postsRef.child(postId).child("like").setValue(""+(Integer.parseInt(pLikes)+1));
                        likesRef.child(postId).child(myUid).setValue("Liked");
                        mProcessLike = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMoreOptions() {
        postList = new ArrayList<>();
        final PopupMenu popupMenu = new PopupMenu(this,option, Gravity.END);
        if (hisUid.equals(myUid)) {
            //add item in menu
            popupMenu.getMenu().add(Menu.NONE,0,0,"삭제");
            popupMenu.getMenu().add(Menu.NONE,1,0,"수정");
        }
        else
        {
            popupMenu.getMenu().add(Menu.NONE,2,0,"차단");
            popupMenu.getMenu().add(Menu.NONE,3,0,"신고");
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == 0) {
                    beginDelete();
                }
                else if (i == 1) {
                    Intent intent = new Intent(postDetail.this, postModify.class);
                    intent.putExtra("key", 123);
                    intent.putExtra("editPostId", postId);
                    startActivity(intent);
                }
                else if(i == 2)
                {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Cut");
                    ref.child(myUid).child(postId).setValue(postId);
                    //postAdapter postAdapter = new postAdapter(getApplicationContext(), (ArrayList<WriteInfo>) postList);
                    //postAdapter.notifyItemSetChanged();
                    finish();
                }
                else if(i == 3)
                {
                    Toast.makeText(getApplicationContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void beginDelete() {
        deletePost();
        finish();
    }

    private void deletePost() {

        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pid").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    ds.getRef().removeValue();
                }
                //Toast.makeText(PostDetailActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setLikes() {
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        myUid = User.getUid();
        final DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postId).hasChild(myUid)){
                    //user has liked this post
                    likeBtn.setImageResource(R.drawable.heart2);
                }
                else {
                    //user has not liked this post
                    likeBtn.setImageResource(R.drawable.heart1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateCommentCount() {
        //whenever user adds comment increase the comment count as we did for like count
        mProcessComment = true;
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mProcessComment){
                    String comments = ""+ snapshot.child("comments").getValue();
                    int newCommentVal = Integer.parseInt(comments) + 1;
                    dbRef.child("comments").setValue(""+newCommentVal);
                    mProcessComment = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserInfo() {
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        myUid = User.getUid();
        Query mRef = FirebaseDatabase.getInstance().getReference("WelcomePet").child("UserAccount");
        mRef.orderByChild("idtoken").equalTo(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    myName =""+ds.child("name").getValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!= null){
            //myEmail = user.getEmail();
            myUid = user.getUid();
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}