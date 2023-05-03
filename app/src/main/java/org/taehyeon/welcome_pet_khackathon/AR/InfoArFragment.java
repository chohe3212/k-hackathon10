package org.taehyeon.welcome_pet_khackathon.AR;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.taehyeon.welcome_pet_khackathon.R;

public class InfoArFragment extends Fragment {

    public Boolean info_ar_checked = false;
    public Boolean info_ar2_checked = false;

    Button btn1;
    RadioGroup radioGroup;
    TextView textView,textView1,textView2,textView3,textView4,textView5;
    UnityPlayerView unityPlayerView;
    String model;

    ImageButton btn_home;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        unityPlayerView= (UnityPlayerView) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        unityPlayerView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_ar, container, false);

        btn1 = v.findViewById(R.id.button_info_select);
        radioGroup=v.findViewById(R.id.radiogroup_info);
        textView = v.findViewById(R.id.textview_info_waring);
        textView1 = v.findViewById(R.id.text_ar_info);
        textView2 = v.findViewById(R.id.text_ar_info2);
        textView3 = v.findViewById(R.id.alarm_radioButton_info1);
        textView4 = v.findViewById(R.id.alarm_radioButton_info2);
        textView5 = v.findViewById(R.id.alarm_radioButton_info3);

        btn_home = v.findViewById(R.id.test_btn);

        model = getArguments().getString("model");

        if(model.equals("OnRoar_info"))
        {
            textView1.setText(" 반려견이 낯선사람을 보고 짖고 있어요! ");
            textView2.setText(" 이처럼 낯선 사람을 보고 짖을 땐 어떻게 대처해야 할까요?");
            textView3.setText(" 아직 누군가가 피해받지 않았으니 내버려 둔다.");
            textView4.setText(" 경고를 주고 반려견의 불안감을 해소시켜 준다.");
            textView5.setText(" 무작정 강아지에게 다그치며 혼낸다.");
        }
        else if(model.equals("OnInjured_info"))
        {
            textView1.setText(" 당신의 반려견이 무기력해보이네요...");
            textView2.setText(" 강아지가 아플 때 보이는 행동과 가장 먼 것은 무엇일까요?");
            textView3.setText(" 식욕이 없으며 구토 혹은 설사를 한다.");
            textView4.setText(" 평소보다 순해지고 다른 동물과 접촉을 좋아한다.");
            textView5.setText(" 평소와 다르게 무기력하게 엎드려 있다.");
        }


        //프래그먼트 실행되면 버튼 2개 비활성화 하고싶음,
        //btn1.setVisibility(v.INVISIBLE);
//        btn1.setEnabled(false);
//        btn2.setEnabled(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.alarm_radioButton_info1)
                {
                    // 오답
                    info_ar_checked = true;
                    info_ar2_checked = false;
                }
                else if(i == R.id.alarm_radioButton_info2)
                {
                    info_ar2_checked = true;
                    info_ar_checked = false;
                }
                else if(i == R.id.alarm_radioButton_info3)
                {
                    info_ar_checked = true;
                    info_ar2_checked = false;
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(unityPlayerView, "select", Toast.LENGTH_SHORT).show();
                if(info_ar_checked == true)
                {
                    textView.setText("오답입니다. 다시 선택하세요");
                }
                else if (info_ar2_checked == true)
                {
                    // btn_home.setEnabled(true);
                    // btn_home.setImageResource(R.drawable.home_ar);

                    unityPlayerView.fragmentChange(2);
                }
            }
        });
        return v;
        //return inflater.inflate(R.layout.fragment_info_ar, container, false);
    }
}