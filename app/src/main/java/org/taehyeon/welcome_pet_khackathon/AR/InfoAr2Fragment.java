package org.taehyeon.welcome_pet_khackathon.AR;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.taehyeon.welcome_pet_khackathon.R;

public class InfoAr2Fragment extends Fragment {

    UnityPlayerView unityPlayerView;
    TextView t1,t2,t3_1,t3_2,t3_3,t3_4;
    ImageView imageView;
    String model;
    ViewGroup layout;

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info_ar2, null);

        t1 = v.findViewById(R.id.text_ar2_info1);  //t1 : 정답입니다! 다음 설명란
        t2 = v.findViewById(R.id.text_ar2_info2);  //t2 : 어떻게 해결하나요? 다음 설명란
        t3_1= v.findViewById(R.id.text_19);        // 광고 내용
        t3_2= v.findViewById(R.id.textView__9);    // 광고 제품 이름
        t3_3= v.findViewById(R.id.textView__8);    // 광고 제품 설명
        t3_4 = v.findViewById(R.id.textView20);    // 광고 제품 가격
        imageView = v.findViewById(R.id.imageView__5); //광고 사진
        layout = v.findViewById(R.id.layout_ar_buy);

        model = getArguments().getString("model");

        if(model.equals("OnInjured_info"))
        {
            t1.setText(" 강아지는 아파도 아프다고 말을 할 수가 없습니다. 강아지의 평소 행동 변화를 통해 건강 상태를 추측해볼 수 있습니다.");
            t2.setText(" 불러도 반응이 거의 없거나 몸을 만졌을 때 싫어하는 반응, 평소와 다른 움직임, 털과 피부 색 변화 등으로 건강 상태를 추측할 수 있습니다.");
            t3_1.setText(" 아픈 반려견용 간식");
            t3_2.setText(" 아픈 반려견용 씹기 좋은 져키");
            t3_3.setText(" 가격 : 17,900원");
            t3_4.setText(" 체력회복 간식 2종 아픈 반려견용 씹기좋은 져키");
            imageView.setImageResource(R.drawable.ar2_info2);
        }
        else if(model.equals("OnRoar_info"))
        {
            t1.setText(" 사회성이 부족한 강아지는 누군가의 접근이나 특정 소리에 불안함을 느끼게 되면서 짖게 됩니다. 이후에는 공격성으로 발전할 수 있으니 훈련이 필요합니다.");
            t2.setText(" 강아지의 불안감을 해소시켜주는 것이 가장 중요합니다. 놀거나 간식을 주면서 자연스레 주위를 신경 쓰지 않도록 해주는 것이 좋은 방법입니다.");
            t3_1.setText(" 훈련시 필요한 것");
            t3_2.setText(" 훈련용 강아지 간식 져키");
            t3_3.setText(" 가격 : 11,600원");
            t3_4.setText(" 강아지 애견 훈련 칭찬간식 브로콜리 져키350g");
            imageView.setImageResource(R.drawable.ar2_info3);
        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.equals("OnInjured_info"))
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://www.11st.co.kr/products/3600805770?srsltid=AR5OiO0kPpU2MRhBE6DM1Z7QtU0Vj3axhQlvARQFomafcRofC3MbN_mUxA0&utm_term=&utm_campaign=%B1%B8%B1%DB%BC%EE%C7%CEPC+%C3%DF%B0%A1%C0%DB%BE%F7&utm_source=%B1%B8%B1%DB_PC_S_%BC%EE%C7%CE&utm_medium=%B0%CB%BB%F6"));
                    startActivity(intent);
                }
                else if(model.equals("OnRoar_info"))
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://front.homeplus.co.kr/item?itemNo=10000301172999&storeType=DS&utm_source=google&utm_medium=cpc&utm_campaign=pmax_MMR&utm_content=awareness_26"));
                    startActivity(intent);

                }
                else
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://with.gsshop.com/prd/prd.gs?prdid=98458952&utm_source=google&utm_medium=paid_search&utm_campaign=shopping&gclid=CjwKCAjwwL6aBhBlEiwADycBIJDOBwTWntYnoOYHrjgIrfCn0EAFKtWOUkwQGO_88PyOIAsFU3ARuBoCq-MQAvD_BwE&airbridge_referrer=airbridge%3Dtrue%26event_uuid%3Df9f674d9-1e3f-4ee2-926b-18a93d0b22c0%26client_id%3D8d6bfc70-0621-11eb-ab98-0242ac11000f%26referrer_timestamp%3D1666163545961%26channel%3Dgoogle.adwords%26campaign%3D1495095526%26ad_group%3D128722009617%26term%3D98458952&media=LRz&fromWith=Y"));
                    startActivity(intent);

                }
            }
        });

        return v;
    }
}