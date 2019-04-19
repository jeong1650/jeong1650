package com.example.kaon.ims;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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

    private Boolean isPermission = true;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


//        String OS = "Android";
//        Log.e("OS 명 :", OS);
//        String osVersion = Build.VERSION.RELEASE;
//        Log.e("OS버전 : ", osVersion);
//        String model = Build.MODEL;
//        Log.e("모델 명 :", model);
//        String manufacturer = Build.MANUFACTURER;
//        Log.e("제조사 : ", manufacturer);
//        String deviceID = android.provider.Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.e("디바이스 id : ", deviceID);
//
//



        Window window = getWindow();



        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



       window.setStatusBarColor(Color.parseColor("#D3D3D3"));



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
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }

                String token = task.getResult().getToken();

                Log.d(TAG,token);

            }
        });


    }
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_SMS)
                .check();

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
                            LoginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(LoginIntent);
                            failtext.setVisibility(View.GONE);

                        }
                       else{
                            failtext.setVisibility(View.VISIBLE);
                            failtext.setText("아이디와 비밀번호를 확인해주세요");


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    catch (NullPointerException e){
//                        e.printStackTrace();
//                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//
//                        builder.setTitle("확인")
//                                .setMessage("아이디와 비밀번호를 입력하세요")
//                                .setCancelable(false)
//                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//
//                                    }
//                                });
//
//
//                        AlertDialog dialog = builder.create();
//                        //다이어로그 생성
//                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
//                        dialog.show();
//                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    final ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this);
                    errorDialog.setErrorDialogListener(new ErrorDialog.ErrorDialogListener() {
                        @Override
                        public void checkClick() {
                            errorDialog.cancel();
                        }
                    });
                    errorDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    errorDialog.show();
                }
            });

            return null;
        }
    }
}
