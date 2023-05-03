package org.taehyeon.welcome_pet_khackathon.Shop;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.taehyeon.welcome_pet_khackathon.R;


public class shop_Fragment extends Fragment {

    ImageButton btn_dog1;
    ImageButton btn_dog2;
    ImageButton btn_dog3;
    ImageButton btn_dog4;
    ImageButton btn_dog5;
    ImageButton btn_dog6;
    ImageButton btn_dog7;
    ImageButton btn_dog8;
    ImageButton btn_dog9;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop_, container, false);

        btn_dog1 = v.findViewById(R.id.imageButton1);
        btn_dog2 = v.findViewById(R.id.imageButton2);
        btn_dog3 = v.findViewById(R.id.imageButton3);
        btn_dog4 = v.findViewById(R.id.imageButton4);
        btn_dog5 = v.findViewById(R.id.imageButton5);
        btn_dog6 = v.findViewById(R.id.imageButton6);
        btn_dog7 = v.findViewById(R.id.imageButton7);
        btn_dog8 = v.findViewById(R.id.imageButton8);
        btn_dog9 = v.findViewById(R.id.imageButton9);

        btn_dog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        btn_dog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        btn_dog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        btn_dog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               show();
            }
        });

        btn_dog5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        btn_dog6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        btn_dog7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        btn_dog8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        btn_dog9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });


        return v;
    }

    public void show() {
        AlertDialog.Builder dig = new AlertDialog.Builder(getContext());

        dig.setTitle("2300₩ 구매하기");
        dig.setMessage("해당 강아지로 체험하시겠습니까?\n한 번 구매한 후에는 자동 소장됩니다.");
        dig.setIcon(R.drawable.ic_launcher_foreground).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"현재는 구매하실 수 없습니다.",Toast.LENGTH_SHORT).show();

                //displayToast("ok");
            }
        });//onClickListner : ok누르면 뭘할지,
        dig.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"취소 되었습니다.",Toast.LENGTH_SHORT).show();

                //displayToast("no");
            }
        });//no버튼 누르면 뭐할지 _토스트를 하게 했다.//두가지 버전으로 가능.~.~; .~;
        dig.show();
    }
}