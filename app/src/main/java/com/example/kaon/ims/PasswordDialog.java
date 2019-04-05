package com.example.kaon.ims;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.kaon.ims.R;

public class PasswordDialog extends Dialog implements View.OnClickListener {

    private Button positivebtn;
    private Button negativebtn;
    private EditText Edit_name;
    private EditText Edit_Depart;
    private EditText Edit_position;
    private EditText Edit_pswd;
    private CheckBox recheck;
    private Context context;

    private PswdDiaglogListener pswdDiaglogListener;

    public PasswordDialog(Context context) {
        super(context);
        this.context = context;
    }



    interface PswdDiaglogListener {
        void onPositiveClick(String name, String depart, String position, String pswd , Boolean isCheck);
        void onNegativeClick();
    }
    public void setPswdDiaglogListener(PswdDiaglogListener pswdDiaglogListener) {
        this.pswdDiaglogListener = pswdDiaglogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_password);

        positivebtn = (Button) findViewById(R.id.postivive_btn);
        negativebtn = (Button) findViewById(R.id.negative_btn);
        Edit_name = (EditText) findViewById(R.id.put_interviewer);
        Edit_Depart = (EditText) findViewById(R.id.put_depart);
        Edit_position = (EditText) findViewById(R.id.put_position);
        Edit_pswd = (EditText) findViewById(R.id.put_pswd);
        recheck = (CheckBox) findViewById(R.id.re_check);

        positivebtn.setOnClickListener(this);
        negativebtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.postivive_btn){
            String name = Edit_name.getText().toString();
            String depart = Edit_Depart.getText().toString();
            String position = Edit_position.getText().toString();
            String pswd = Edit_pswd.getText().toString();
            Boolean isCheck = recheck.isChecked();
            pswdDiaglogListener.onPositiveClick(name,depart,position,pswd,isCheck);
            dismiss();

        }
        else if(id == R.id.negative_btn){
            cancel();
        }
    }
}
