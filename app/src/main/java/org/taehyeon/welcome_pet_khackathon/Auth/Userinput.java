package org.taehyeon.welcome_pet_khackathon.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.taehyeon.welcome_pet_khackathon.R;

public class Userinput extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth; // 파이어베이스 인증처리
    private DatabaseReference mdataRef;
    private EditText check,pw,email,phone,name;
    private Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinput);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mdataRef = FirebaseDatabase.getInstance().getReference("WelcomePet");

        check = findViewById(R.id.register_check);
        email = findViewById(R.id.register_email);
        pw = findViewById(R.id.register_PW);
        phone = findViewById(R.id.register_phone);
        name = findViewById(R.id.register_name);
        register_btn = findViewById(R.id.register);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_PW = pw.getText().toString();
                String str_name = name.getText().toString();
                String str_email = email.getText().toString();
                String str_phone = phone.getText().toString();
                String str_check = check.getText().toString();

                if(str_name.equals("")){Toast.makeText(Userinput.this,"이름을 확인해주세요",Toast.LENGTH_SHORT).show();return;}
                if(str_email.equals("")){Toast.makeText(Userinput.this,"이메일을 확인해주세요",Toast.LENGTH_SHORT).show();return;}
                if(str_PW.equals("")){Toast.makeText(Userinput.this,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();return;}
                if(str_check.equals(str_PW) == false) { Toast.makeText(Userinput.this,"비밀번호 확인과 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();return; }
                if(str_phone.equals("")) { Toast.makeText(Userinput.this,"전화번호를 확인해주세요",Toast.LENGTH_SHORT).show();return; }

                mfirebaseAuth.createUserWithEmailAndPassword(str_email,str_PW).
                        addOnCompleteListener(Userinput.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if((task.isSuccessful()))
                        {
                                FirebaseUser firebaseUser = mfirebaseAuth.getCurrentUser();
                                UserAccount account = new UserAccount();
                                //c -> chx or ch    c2 -> gen or pro
                                final String c = "chx",c2 = "gen";

                                account.setIdtoken((firebaseUser.getUid()));
                                account.setEmail(firebaseUser.getEmail());
                                account.setPhone(str_phone);
                                account.setName(str_name);
                                account.setPw(str_PW);
                                account.setCheck(c);
                                account.setJob(c2);
                                account.setCount("0");
                                account.setProgress("0");

                                mdataRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                Toast.makeText(Userinput.this,"회원가입에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                                finish();
                        }
                        else{
                            Toast.makeText(Userinput.this,"회원가입에 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}