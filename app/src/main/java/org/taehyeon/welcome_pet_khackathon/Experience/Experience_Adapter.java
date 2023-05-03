package org.taehyeon.welcome_pet_khackathon.Experience;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.taehyeon.welcome_pet_khackathon.R;

import java.util.ArrayList;

public class Experience_Adapter extends RecyclerView.Adapter<Experience_Adapter.MyViewHolder> {
    private ArrayList<Experience> mDataset;

    experience_Fragment2 Experience_fragment2= new experience_Fragment2();

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Experience;
        public TextView textView_exp;

        public MyViewHolder(View view) {
            super(view);
            Experience = (TextView) view.findViewById(R.id.Experience);
            textView_exp = (TextView) view.findViewById(R.id.textView_exp);

        }
    }

    public Experience_Adapter(ArrayList<Experience> myData) {
        this.mDataset = myData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_experience_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Experience_Adapter.MyViewHolder holder, int position) {
        holder.Experience.setText(mDataset.get(position).getProblem());

        holder.Experience.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                //Toast.makeText(view.getContext(), pos+"번째 클릭됨",Toast.LENGTH_SHORT).show();

                // fragment_experience_2로 화면 전환(프래그먼트->프래그먼트)
                AppCompatActivity activity = (AppCompatActivity) view.getContext();


                //String item = " 안녕";
                Experience item = mDataset.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("name",item.getProblem());
                bundle.putInt("index",pos);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                Experience_fragment2.setArguments(bundle);
                transaction.replace(R.id.frameLayout_main, Experience_fragment2);
                transaction.commit();

            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setItems(ArrayList<Experience> list) {
        mDataset = list;
        notifyDataSetChanged();
    }
}
