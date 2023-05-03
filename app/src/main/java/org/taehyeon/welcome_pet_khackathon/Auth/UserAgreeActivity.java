package org.taehyeon.welcome_pet_khackathon.Auth;

import androidx.appcompat.app.AppCompatActivity;
import org.taehyeon.welcome_pet_khackathon.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class UserAgreeActivity extends AppCompatActivity {

    CheckBox check,check2;
    Button button;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agree);

        check = findViewById(R.id.checkBox_agree);
        check2 = findViewById(R.id.checkBox_agree2);
        button = findViewById(R.id.useragreebutton);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    i+=1;

                }
                else
                {
                    i-=1;
                }
            }
        });
        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    i+=1;

                }
                else
                {
                    i-=1;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check.isChecked() && check2.isChecked())
                {
                    Intent intent = new Intent(getApplicationContext(), Userinput.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(getApplicationContext(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });








    }
}