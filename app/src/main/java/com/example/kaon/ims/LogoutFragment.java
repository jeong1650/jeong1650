package com.example.kaon.ims;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.kaon.ims.LoginActivity;
import com.example.kaon.ims.PreviousActivity.NoticeActivity;
import com.example.kaon.ims.PreviousActivity.ScheduleActivity;
import com.example.kaon.ims.R;


public class LogoutFragment extends Fragment {
    public LogoutFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout,container,false);

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle("로그 아웃")
//                .setMessage("로그 아웃 하시겠습니까?")
//                .setCancelable(false)
//                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(getActivity(),LoginActivity.class);
//                        getActivity().startActivity(intent);
//
//                    }
//                })
//        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        })
//        ;
//
//
//        AlertDialog dialog = builder.create();
//        //다이어로그 생성
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
//        dialog.show();

        return view;
    }



}
