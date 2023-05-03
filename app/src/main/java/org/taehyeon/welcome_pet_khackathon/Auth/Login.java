package org.taehyeon.welcome_pet_khackathon.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import org.taehyeon.welcome_pet_khackathon.MainActivity;
import org.taehyeon.welcome_pet_khackathon.R;
import org.taehyeon.welcome_pet_khackathon.Userinfo.userinfo_Fragment;

public class Login extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth; // 파이어베이스 인증처리
    private DatabaseReference mdataRef;

    EditText et_id,et_pw;
    TextView join_tbtn, password_reset;
    Button login_btn;
    //자동 로그인 변수
    CheckBox auto_check;
    Boolean loginChecked;
    String loginId, loginPwd;
    public SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mdataRef = FirebaseDatabase.getInstance().getReference("WelcomePet");

        EditText et_id = findViewById(R.id.enter_ID);
        EditText et_pw = findViewById(R.id.enter_PW);
        TextView join_tbtn = findViewById(R.id.join);
        Button login_btn = findViewById(R.id.login);
        TextView password_reset = findViewById(R.id.login_password_reset);

        //자동로그인
        auto_check = findViewById(R.id.auto_login);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
        loginId = sharedPreferences.getString("inputID",null);
        loginPwd = sharedPreferences.getString("inputPwd",null);

        settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);

        loginChecked = settings.getBoolean("LoginChecked", false);
        if(loginChecked){
            et_id.setText(settings.getString("loginId",""));
            et_pw.setText(settings.getString("loginPwd",""));
            auto_check.setChecked(true);
        }


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_id = et_id.getText().toString();
                String str_pw = et_pw.getText().toString();
                if(str_id.equals("")){Toast.makeText(Login.this,"이메일을 확인해주세요",Toast.LENGTH_SHORT).show();return;}
                if(str_pw.equals("")){Toast.makeText(Login.this,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();return;}
                mfirebaseAuth.signInWithEmailAndPassword(str_id,str_pw).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(auto_check.isChecked()==true)
                            {
                                //Toast.makeText(Login.this,"체크박스 선택됨.",Toast.LENGTH_SHORT).show();
                                loginId = str_id;
                                loginPwd = str_pw;
                            }
                            //Toast.makeText(Login.this,"로그인을 성공하셨습니다!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Login.this,"로그인을 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        join_tbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserAgreeActivity.class);
                startActivity(intent);
            }
        });
        password_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PasswordReset.class);
                startActivity(intent);
            }
        });
    }

    //자동 로그인
    public void onStop(){
        super.onStop();
        if(auto_check.isChecked()==true) {
            settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("loginId",loginId);
            editor.putString("loginPwd",loginPwd);
            editor.putBoolean("LoginChecked",true);

            editor.commit();
        }else{
            settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
        }
    }
}