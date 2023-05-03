package org.taehyeon.welcome_pet_khackathon.AR;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unity3d.player.UnityPlayer;

import org.taehyeon.welcome_pet_khackathon.MainActivity;
import org.taehyeon.welcome_pet_khackathon.R;

public class UnityPlayerView extends AppCompatActivity  {

    protected UnityPlayer mUnityPlayer;
    InfoArFragment fragment = new InfoArFragment();
    InfoAr2Fragment fragment2 = new InfoAr2Fragment();
    AlarmArFragment fragment3 = new AlarmArFragment();

    String data;
    TextView walk;
    int check_alarm = 0;
    int check_home = 0;

     ImageView btn_home;
     ImageView btn_next;

    protected String updateUnityCommandLineArguments(String cmdLine)
    {
        return cmdLine;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity_player);

        mUnityPlayer = new UnityPlayer(this);
        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode",1);
        mUnityPlayer.init(glesMode, false);

        FrameLayout layout = (FrameLayout) findViewById(R.id.con_layout);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);


        Intent intent = getIntent();
        data = intent.getStringExtra("model");
        walk = findViewById(R.id.text_walk);

        if(data.equals("None")){
            walk.setText("이런.. 강아지가 집을 어지럽혔어요.. ");
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","None");
            check_alarm = 1;
        }
        else if (data.equals("OnInjured")){
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnInjured");
            check_alarm = 1;
        }
        else if (data.equals("OnSleep")){
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnSleep");
            check_alarm = 1;
        }
        else if (data.equals( "OnWalk")){
            walk.setText("강아지와 산책중이시군요! 강아지 생성 뒤 화면을 클릭하면 강아지가 앞으로 이동합니다.");
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnWalk");
            check_alarm = 1;
        }
        else if (data.equals("OnDeath")){
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnDeath");
            check_alarm = 1;
        }
        else if (data.equals("OnRoar")){
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnRoar");
            check_alarm = 1;
        }
        else if (data.equals("OnAttack")){
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnAttack");
            check_alarm = 1;
        }
        // 정보함용 AR
        else if (data.equals("OnInjured_info")){
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnInjured");
        }
        else if (data.equals("OnRoar_info")){
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnRoar");
        }
        else if (data.equals("OnAttack_info")){
            mUnityPlayer.UnitySendMessage("DogSpawner","GetAndroidValue","OnAttack");
        }


        layout.addView(mUnityPlayer.getView(),0,lp);

        btn_home = findViewById(R.id.test_btn);
        btn_next = findViewById(R.id.next_btn);



    }


    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();

        mUnityPlayer.resume();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_home.setEnabled(true);

                check_home = 1;

                if (check_alarm == 1)
                {
                    fragmentChange(3);
                }
                else{
                    fragmentChange(1);
                }
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                Intent intent = new Intent(UnityPlayerView.this, MainActivity.class);
//                startActivity(intent);

                if (check_home == 1)
                {
                    mUnityPlayer.destroy();
                }
                else{
                    Toast.makeText(getApplicationContext(),"오른쪽 버튼을 누른 후 Home으로 가실 수 있습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }


    public void fragmentChange(int index){
        if(index == 1){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle1 = new Bundle(); // 번들을 통해 값 전달
            bundle1.putString("model",data);
            fragment.setArguments(bundle1);
            transaction.replace(R.id.ar_question,fragment).commit();

        }
        else if(index == 2){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle2 = new Bundle(); // 번들을 통해 값 전달
            bundle2.putString("model",data);
            fragment2.setArguments(bundle2);
            transaction.replace(R.id.ar_question,fragment2).commit();

        }
        else if (index == 3){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle2 = new Bundle(); // 번들을 통해 값 전달
            bundle2.putString("model",data);
            // 수정 해야됨.
            fragment3.setArguments(bundle2);
            transaction.replace(R.id.ar_question,fragment3).commit();

        }
    }
}