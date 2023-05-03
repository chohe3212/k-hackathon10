package org.taehyeon.welcome_pet_khackathon.Community;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import org.taehyeon.welcome_pet_khackathon.R;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.grpc.ManagedChannelProvider;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {
    Context context;
    static ArrayList<WriteInfo> list = new ArrayList<WriteInfo>();
    onDataItemClickListener OnDataItemClickListener;
    private DatabaseReference likesRef;
    private DatabaseReference postsRef;
    boolean mProcessLike = false ;
    String myUid;

        public postAdapter(Context context,ArrayList<WriteInfo> list) {
            this.list = list;
            this.context = context;

            myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            likesRef= FirebaseDatabase.getInstance().getReference().child("Likes");
            postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.post_cardview, parent, false);
        return new ViewHolder(item, OnDataItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        WriteInfo p = list.get(position);
        viewHolder.setItem(p);

        String Name  = list.get(position).getPublisher();
        String Title  = list.get(position).getTitle();
        String Contents  = list.get(position).getContents();
        String Like = list.get(position).getLike();
        String Comment  = list.get(position).getComments();
        String Time  = list.get(position).getTime();
        final String Uid  = list.get(position).getUid();
        final String pId = list.get(position).getPid();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(Time));
        String pTime = DateFormat.format("yyyy/MM/dd hh:mm aa",calendar).toString();

        viewHolder.textpublisher.setText(Name);
        viewHolder.texttime.setText(pTime);
        viewHolder.texttitle.setText(Title);
        viewHolder.textcontents.setText(Contents);
        viewHolder.textlike.setText(Like+ " Likes " );
        viewHolder.textcomment.setText(Comment + " Comments");

        setLikes(viewHolder,pId);
        viewHolder.profile.setImageResource(R.drawable.user);

        viewHolder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pLikes = Integer.parseInt(list.get(position).getLike());
                mProcessLike = true;
                //get id of the post clicked
                final String postIde= list.get(position).getPid();
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mProcessLike){
                            if (snapshot.child(postIde).hasChild(myUid)){
                                //already liked, so remove like
                                postsRef.child(postIde).child("like").setValue(""+(pLikes-1));
                                likesRef.child(postIde).child(myUid).removeValue();
                                mProcessLike = false;
                            }
                            else {
                                //not liked, like it
                                postsRef.child(postIde).child("like").setValue(""+(pLikes+1));
                                likesRef.child(postIde).child(myUid).setValue("Liked");
                                mProcessLike = false;

                                //addToHisNotifications(""+uId,""+pid,"Liked your post");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        viewHolder.image_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, postDetail.class);
                intent.putExtra("postId",pId);
                context.startActivity(intent);

            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, postDetail.class);
                intent.putExtra("postId",pId);
                context.startActivity(intent);
            }
        });
    }

    private void setLikes(final ViewHolder holder, final String postKey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postKey).hasChild(myUid)){
                    //user has liked this post
                    holder.image_like.setImageResource(R.drawable.heart2);
                }
                else {
                    //user has not liked this post
                    holder.image_like.setImageResource(R.drawable.heart1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public WriteInfo getItem(int position) {
        return list.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView texttitle;
        TextView textpublisher;
        TextView textcontents;
        TextView texttime;
        TextView textlike;
        TextView textcomment;
        ImageButton image_like,image_comment;
        ImageView profile;
        CardView cardView;

        public ViewHolder(View itemView, final onDataItemClickListener listener) {
            super(itemView);

            texttitle = itemView.findViewById(R.id.Title2);
            textpublisher = itemView.findViewById(R.id.Username);
            textcontents = itemView.findViewById(R.id.Content);
            texttime = itemView.findViewById(R.id.pTimeTv);
            textlike = itemView.findViewById(R.id.hartcount);
            textcomment = itemView.findViewById(R.id.commentcount);
            image_comment = itemView.findViewById(R.id.comment_btn);
            image_like = itemView.findViewById(R.id.like_btn);
            profile = itemView.findViewById(R.id.profile);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void setItem(WriteInfo item) {

        }
    }
}