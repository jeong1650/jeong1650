package com.example.kaon.ims;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    Retrofit retrofit;
    ApiService apiService;
    EditText putid;
    EditText putpw;
    CheckBox idsave;
    String NAME;
    ArrayList nameput;
    String idValue;
    String ID;
    TextView failtext;

    LinearLayout Loginfail;

    SharedPreferences sharedPreferences;
    private static final String TAG = "LoginActivity";



    AddCookiesInterceptor in1;
    ReceivedCookiesInterceptor in2;
    OkHttpClient httpClient;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Window window = getWindow();



        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



       window.setStatusBarColor(Color.parseColor("#4e67c3"));



        Loginfail = findViewById(R.id.faillayout);
        failtext = findViewById(R.id.failpw);

        //in1 = new AddCookiesInterceptor(this);
        in2 = new ReceivedCookiesInterceptor(this);

        httpClient = new OkHttpClient.Builder()
                //.addNetworkInterceptor(in1)
                .addInterceptor(in2)
                .build();

        loginBtn = (Button) findViewById(R.id.loginButton);

        putid = (EditText) findViewById(R.id.usernameInput);

        putpw = (EditText) findViewById(R.id.passwordInput);

        idsave = (CheckBox) findViewById(R.id.save_id);

        idsave.requestFocus();
        SharedPreferences pref = getSharedPreferences("pref",LoginActivity.this.MODE_PRIVATE);
        String id = pref.getString("id_save","");
        Boolean chk1 = pref.getBoolean("chk1",false);

        if(chk1 ==true){
            putid.setText(id);
            idsave.setChecked(chk1);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new JsonLogin().execute();

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences pref = getSharedPreferences("pref",LoginActivity.this.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        putid = (EditText) findViewById(R.id.usernameInput);
        idsave = (CheckBox) findViewById(R.id.save_id);

        editor.putString("id_save",putid.getText().toString());
        editor.putBoolean("chk1",idsave.isChecked());

        editor.commit();
    }

    private class JsonLogin extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                    .client(httpClient)
                    .build();
            apiService = retrofit.create(ApiService.class);
            final HashMap<String, Object> Login = new HashMap<>();
            Login.put("id", putid.getText().toString());
            Login.put("pw", putpw.getText().toString());
            apiService.Login(Login).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        String result = response.body().string();

                        Log.d(TAG,result);
//                           Log.d(TAG,
                        if (result != "") {

                                JSONObject c = new JSONObject(result);
                                NAME = c.getString("NAME");
                                 ID = c.getString("ID");


                            idValue = putid.getText().toString();



                            Intent LoginIntent = new Intent(LoginActivity.this, MainScreenActivity.class);
                            LoginIntent.putExtra("NAME",NAME);
                            LoginIntent.putExtra("id",idValue);
                            startActivity(LoginIntent);
                            failtext.setVisibility(View.GONE);

                        }
                       else{
                            failtext.setVisibility(View.VISIBLE);
//                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//
//                            builder.setTitle("로그인 실패")
//                                    .setMessage("아이디와 비밀번호를 확인해주세요")
//                                    .setCancelable(false)
//                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.cancel();
//
//                                        }
//                                    });
//
//
//                            AlertDialog dialog = builder.create();
//                            //다이어로그 생성
//                            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
//                            dialog.show();


                            failtext.setText("아이디와 비밀번호를 확인해주세요");
//                            failtext = new TextView(LoginActivity.this);
//                            failtext.setText("아이디와 비밀번호를 확인해주세요");
//                            Loginfail.addView(failtext,login);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                        builder.setTitle("확인")
                                .setMessage("아이디와 비밀번호를 입력하세요")
                                .setCancelable(false)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                });


                        AlertDialog dialog = builder.create();
                        //다이어로그 생성
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
                        dialog.show();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                    builder.setTitle("응답 오류")
                            .setMessage("통신 오류가 발생하였습니다. 다시 시도해주세요")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });


                    AlertDialog dialog = builder.create();
                    //다이어로그 생성
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
                    dialog.show();
                }
            });

            return null;
        }
    }
}
