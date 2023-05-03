package org.taehyeon.welcome_pet_khackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.taehyeon.welcome_pet_khackathon.Alarm.Destination;
import org.taehyeon.welcome_pet_khackathon.Auth.UserAccount;
import org.taehyeon.welcome_pet_khackathon.Community.WritePostFragment;
import org.taehyeon.welcome_pet_khackathon.Community.community_Fragment;
import org.taehyeon.welcome_pet_khackathon.Experience.OnBackPressedListener;
import org.taehyeon.welcome_pet_khackathon.Experience.PagerAdapter;
import org.taehyeon.welcome_pet_khackathon.Experience.experience_Fragment;
import org.taehyeon.welcome_pet_khackathon.Experience.experience_Fragment2;
import org.taehyeon.welcome_pet_khackathon.Home.home_Fragment;
import org.taehyeon.welcome_pet_khackathon.Home.progress_Fragment;
import org.taehyeon.welcome_pet_khackathon.Shop.shop_Fragment;
import org.taehyeon.welcome_pet_khackathon.Start_survey.survey2;
import org.taehyeon.welcome_pet_khackathon.Userinfo.userinfo_Fragment;

public class MainActivity extends AppCompatActivity {

    home_Fragment fragment_home = new home_Fragment();
    userinfo_Fragment fragment_userinfo = new userinfo_Fragment();
    shop_Fragment fragment_shop = new shop_Fragment();
    community_Fragment fragment_community = new community_Fragment();
    experience_Fragment fragment_experience = new experience_Fragment();
    WritePostFragment fragment_write_post = new WritePostFragment();
    progress_Fragment fragment_progress = new progress_Fragment();
    OnBackPressedListener listener;
    ViewPager viewPager;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        bottomNavigationView.setSelectedItemId(R.id.HomeItem);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("WelcomePet").child("UserAccount");
        ref.child(user.getUid()).child("check").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String str = String.valueOf(task.getResult().getValue());
                    if (str.equals("chx")) {
                        openFragment(fragment_home);
                        //bottomNavigationView.setItemIco;
                    } else {
                        //Intent i = getIntent();
                        openFragment(fragment_progress);
                    }
                }
            }
        });




//        viewPager = findViewById(R.id.viewPager);
//        adapter = new PagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        adapter.addItem(fragment_experience);
//        viewPager.setAdapter(adapter);
    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.UserinfoItem:
                    openFragment(fragment_userinfo);
                    //transaction.replace(R.id.frameLayout_main, fragment_userinfo).addToBackStack(null).commitAllowingStateLoss();
                    break;

                case R.id.experienceItem:
                    openFragment(fragment_experience);
                    //transaction.replace(R.id.frameLayout_main, fragment_experience).addToBackStack(null).commitAllowingStateLoss();
                    break;

                case R.id.HomeItem:
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("WelcomePet").child("UserAccount");
                    ref.child(user.getUid()).child("check").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue(String.class) != null) {
                                String str = snapshot.getValue(String.class);
                                if(str.equals("chx")){
                                    openFragment(fragment_home);
                                    //transaction.replace(R.id.frameLayout_main, fragment_home).addToBackStack(null).commitAllowingStateLoss();
                                }
                                else {
                                    openFragment(fragment_progress);
                                    //transaction.replace(R.id.frameLayout_main, fragment_progress).addToBackStack(null).commitAllowingStateLoss();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    break;

                case R.id.CommunityItem:
                    openFragment(fragment_community);
                    //transaction.replace(R.id.frameLayout_main, fragment_community).addToBackStack(null).commitAllowingStateLoss();
                    break;

                case R.id.ShopItem:
                    openFragment(fragment_shop);
                    //transaction.replace(R.id.frameLayout_main, fragment_shop).addToBackStack(null).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
    public void onFragmentChange(int index){
        if(index == 0){
            openFragment(fragment_community);
            //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_main,fragment_community).commitAllowingStateLoss();
        }else if(index == 1){
            openFragment(fragment_write_post);
            //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_main,fragment_write_post).commitAllowingStateLoss();
        }
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }





    @Override
    public void onBackPressed() {
        if(listener!=null){
            listener.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == KeyEvent.KEYCODE_BACK) {
            //Toast.makeText(this, "뒤로가기버튼이 눌렸습니다",Toast.LENGTH_LONG).show();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("exit");
            builder.setMessage("종료 하시겠습니까?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(),"취소",Toast.LENGTH_SHORT).show();
                }
            });
            builder.create().show();

            return true;
        }

        return false;
    }
}