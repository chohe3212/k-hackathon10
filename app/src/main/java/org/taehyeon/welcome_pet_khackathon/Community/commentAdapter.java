package org.taehyeon.welcome_pet_khackathon.Community;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.taehyeon.welcome_pet_khackathon.Auth.UserAccount;
import org.taehyeon.welcome_pet_khackathon.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.MyHolder> {
    Context context;
    List<commentInfo> commentList;
    String myUid,postId;
    Button btn;

    public commentAdapter(Context context, List<commentInfo> commentList, String myUid, String postId) {
        this.context = context;
        this.commentList = commentList;
        this.myUid = myUid;
        this.postId = postId;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_cardview,viewGroup,false);

        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {

        final String uid = commentList.get(i).getUid();
        String name = commentList.get(i).getName();
        //String email = commentList.get(i).getEmail();
        final String cid = commentList.get(i).getcId();
        String comment = commentList.get(i).getComment();
        String time = commentList.get(i).getTimeStamp();
        Button button_close;

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(time));
        String pTime = DateFormat.format("yyyy/MM/dd hh:mm aa",calendar).toString();

        holder.nameTv.setText(name);
        holder.commentTv.setText(comment);
        holder.timeTv.setText(pTime);
        holder.avatarIv.setImageResource(R.drawable.user);

        //사진 클릭
        holder.avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("tcomments");
                ref.child(cid).child("uid").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            final String id = String.valueOf(task.getResult().getValue());
                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("WelcomePet");
                            ref2.child("UserAccount").child(id).child("job").get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                Log.e("firebase", "Error getting data", task.getException());
                                            }
                                            else {
                                                String job = String.valueOf(task.getResult().getValue());
                                                if (job.equals("pro")) {
                                                    Dialog dialog = new Dialog(v.getRootView().getContext());
                                                    dialog.setContentView(R.layout.fragment_expert_user);
                                                    btn = dialog.findViewById(R.id.button_expert_ok);
                                                    btn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                                    dialog.show();
                                                } else {
                                                    //Toast.makeText(context, "Can't delete other's comment....", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        });

        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final PopupMenu popupMenu = new PopupMenu(v.getRootView().getContext(),holder.option, Gravity.END);
                if (myUid.equals(uid))
                {
                    popupMenu.getMenu().add(Menu.NONE,0,0,"삭제");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int i = item.getItemId();
                            if (i == 0) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                                builder.setTitle("Delete");
                                builder.setMessage("이 댓글을 지우시겠습니까?");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteComment(cid);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context.getApplicationContext(),"취소",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.create().show();
                            }

                            return false;
                        }
                    });
                    popupMenu.show();
                }

                else
                {
                    popupMenu.getMenu().add(Menu.NONE,0,0,"신고");
                    popupMenu.getMenu().add(Menu.NONE,1,0,"차단");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int i = item.getItemId();
                            if (i == 0) {
                                Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else if(i == 1)
                            {
                                FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Cut");
                                ref.child(User.getUid()).child("comment").child(cid).setValue(cid);
                                this.notify();
                            }

                            return false;
                        }
                    });
                    popupMenu.show();
                    //Toast.makeText(context, "Can't delete other's comment....", Toast.LENGTH_SHORT).show();
                }
            }//onclick
        });
    }

    private void deleteComment(String cid) {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.child("tcomments").child(cid).removeValue();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String comments = ""+ snapshot.child("comments").getValue();
                int newCommentVal = Integer.parseInt(comments) - 1;
                ref.child("comments").setValue(""+newCommentVal);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView avatarIv,option;
        TextView nameTv,commentTv,timeTv;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            avatarIv = itemView.findViewById(R.id.avatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            commentTv = itemView.findViewById(R.id.commentTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            option = itemView.findViewById(R.id.option_comment);
        }
    }
}