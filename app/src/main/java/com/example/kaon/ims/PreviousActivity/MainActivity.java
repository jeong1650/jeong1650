package com.example.kaon.ims.PreviousActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kaon.ims.ApiService;
import com.example.kaon.ims.CustomProgressDialog;
import com.example.kaon.ims.LoginActivity;
import com.example.kaon.ims.R;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mSchedulebtn;
    Button mInterviewbtn;
    Button mWaitbtn;

    Retrofit retrofit;
    ApiService apiService;

    String NAME;
    ArrayList<String> Namelist;
    String mName;
    String idValue;

    FloatingActionButton logout;


    private static final String TAG= "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Namelist = new ArrayList<>();


        Intent intent = getIntent();
        mName= intent.getExtras().getString("NAME");
        idValue = intent.getExtras().getString("id");


        mSchedulebtn = (Button) findViewById(R.id.schedule);
        mInterviewbtn = (Button) findViewById(R.id.i_person);
        mWaitbtn = (Button) findViewById(R.id.w_person) ;
        logout = (FloatingActionButton) findViewById(R.id.logout);

        mInterviewbtn.setOnClickListener(this);
        mSchedulebtn.setOnClickListener(this);
        mWaitbtn.setOnClickListener(this);
        logout.setOnClickListener(this);

         new loading().execute();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.i_person) {
            Intent PersonInfo = new Intent(MainActivity.this, NoticeActivity.class);
            PersonInfo.putExtra("id",idValue);
            PersonInfo.putExtra("NAME",mName);
            MainActivity.this.startActivity(PersonInfo);
        }
        else if(id == R.id.schedule) {
            Intent Schedule = new Intent(MainActivity.this, ScheduleActivity.class);
            Schedule.putExtra("NAME",mName);
            Schedule.putExtra("id",idValue);
            MainActivity.this.startActivity(Schedule);
        }
        else if(id == R.id.w_person){
            Intent intentwait = new Intent(MainActivity.this, WaitPersonActivity.class);
            intentwait.putExtra("username",mName);
            MainActivity.this.startActivity(intentwait);
        } else if(id == R.id.logout){
            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MainActivity.this.startActivity(logout);
        }
    }

    private class loading extends AsyncTask {
        CustomProgressDialog progressDialog = new CustomProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
