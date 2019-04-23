package com.example.kaon.ims;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


public class LogoutFragment extends Fragment {
    public LogoutFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout,container,false);

        final LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.setLogoutDialogListener(new LogoutDialog.LogoutDialogListener() {
            @Override
            public void Positiveclick() {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                      getActivity().startActivity(intent);
            }

            @Override
            public void negativeclick() {


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(LogoutFragment.this).commit();
                fragmentManager.popBackStack();



            }
        });
        logoutDialog.show(getActivity().getSupportFragmentManager(),"tag");
        return view;
    }



}
