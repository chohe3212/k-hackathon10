package org.taehyeon.welcome_pet_khackathon.Auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.taehyeon.welcome_pet_khackathon.MainActivity;
import org.taehyeon.welcome_pet_khackathon.R;

public class PasswordReset extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Button btn = findViewById(R.id.sendbutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    // 비밀번호 재설정 이메일 전송하기
    private void send() {
        String email = ((EditText) findViewById(R.id.passwordemailEditText)).getText().toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (email.length() > 0) {
//            final RelativeLayout loaderLayout = findViewById(R.id.loaderLyaout);
//            loaderLayout.setVisibility(View.VISIBLE);

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override

                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "비밀번호 재설정 안내 이메일을 보냈습니다! 확인해주세요.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
    }
}
