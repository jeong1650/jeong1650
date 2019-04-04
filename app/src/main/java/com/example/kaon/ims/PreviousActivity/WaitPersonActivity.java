package com.example.kaon.ims.PreviousActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaon.ims.AddCookiesInterceptor;
import com.example.kaon.ims.ApiService;
import com.example.kaon.ims.CustomProgressDialog;
import com.example.kaon.ims.R;
import com.example.kaon.ims.WaitpersonAdapter;
import com.example.kaon.ims.Waitpersonitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WaitPersonActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //View
    TextView mTitle;
    ListView mPerson;

    AddCookiesInterceptor in1;
    OkHttpClient httpClient;
    //Global data
    String mName;

    //Retrofit
    Retrofit retrofit;
    ApiService apiService;

    String Master_ID;
    //List
    String username;
    List<Waitpersonitem> infoList;

    TextView totla_title;
    FloatingActionButton logout;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog pDialog;
    int INDEX_ID;
    private static final String TAG = "PersonInfoActivity";

    public List<Waitpersonitem> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Waitpersonitem> infoList) {
        this.infoList = infoList;
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitperson);

        in1 = new AddCookiesInterceptor(this);
        httpClient = new OkHttpClient.Builder().addInterceptor(in1)
                .build();

        mTitle = (TextView) findViewById(R.id.w_title);
        mPerson = (ListView) findViewById(R.id.w_list);

        mTitle.setText("서류대기자");



//        Peronlist = new ArrayList<>();

        Intent intent = getIntent();
        mName = intent.getStringExtra("NAME");

        username = intent.getStringExtra("username");

        mSwipeRefreshLayout = findViewById(R.id.swipe_wait);
        mSwipeRefreshLayout.setOnRefreshListener(this);
      new Infowait().execute();


    }

    @Override
    public void onRefresh() {
        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                .client(httpClient)
                .build();
        apiService = retrofit.create(ApiService.class);

        HashMap<String, String> wait = new HashMap<>();
        wait.put("USER_ID", username);


        apiService.waitlist(wait).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if (result != null) {
                        JSONArray jsonArray = new JSONArray(result);
                        infoList = new ArrayList<Waitpersonitem>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            INDEX_ID = c.getInt("INDEX_ID");
                            Master_ID = c.getString("MASTER_ID");

                            Waitpersonitem waitpersonitem = new Waitpersonitem();
                            waitpersonitem.setNAME(c.getString("NAME"));
                            waitpersonitem.setYEAR(c.getString("YEAR"));
                            waitpersonitem.setPOSITION(c.getString("POSITION"));
                            waitpersonitem.setETC(c.getString("ETC"));
                            waitpersonitem.setPROJECT_ID(c.getString("PROJECT_ID"));
                            waitpersonitem.setSTATUS(c.getString("STATUS"));
                            waitpersonitem.setPATH(c.getString("PATH"));
                            waitpersonitem.setINDEX_ID(INDEX_ID);
                            waitpersonitem.setMASTER_ID(Master_ID);
                            waitpersonitem.setResumepath(c.getString("RESUME_PATH"));
                            waitpersonitem.setPortpath(c.getString("PORT_PATH"));
                            waitpersonitem.setUsername(username);
                            infoList.add(waitpersonitem);

                            WaitpersonAdapter adpater = new WaitpersonAdapter(getApplicationContext(),R.layout.waitlist,infoList);

                            mPerson.setAdapter(adpater);

                        }


                    }
                }catch (final NullPointerException e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json parsing Error: :" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (final JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Json parsing Error:" + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                AlertDialog.Builder builder = new AlertDialog.Builder(WaitPersonActivity.this);

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
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private class Infowait extends AsyncTask<Void,Void,Void> {
        CustomProgressDialog progressDialog = new CustomProgressDialog(WaitPersonActivity.this);
        @Override
        protected void onPreExecute() {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                    .client(httpClient)
                    .build();
            apiService = retrofit.create(ApiService.class);

            HashMap<String, String> wait = new HashMap<>();
            wait.put("USER_ID", username);


            apiService.waitlist(wait).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        if (result != null) {
                            JSONArray jsonArray = new JSONArray(result);
                            infoList = new ArrayList<Waitpersonitem>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                INDEX_ID = c.getInt("INDEX_ID");
                                Master_ID = c.getString("MASTER_ID");

                                Waitpersonitem waitpersonitem = new Waitpersonitem();
                                waitpersonitem.setNAME(c.getString("NAME"));
                                waitpersonitem.setYEAR(c.getString("YEAR"));
                                waitpersonitem.setPOSITION(c.getString("POSITION"));
                                waitpersonitem.setETC(c.getString("ETC"));
                                waitpersonitem.setPROJECT_ID(c.getString("PROJECT_ID"));
                                waitpersonitem.setSTATUS(c.getString("STATUS"));
                                waitpersonitem.setPATH(c.getString("PATH"));
                                waitpersonitem.setINDEX_ID(INDEX_ID);
                                waitpersonitem.setMASTER_ID(Master_ID);
                                waitpersonitem.setResumepath(c.getString("RESUME_PATH"));
                                waitpersonitem.setPortpath(c.getString("PORT_PATH"));
                                waitpersonitem.setUsername(username);
                                infoList.add(waitpersonitem);

                                WaitpersonAdapter adpater = new WaitpersonAdapter(getApplicationContext(),R.layout.waitlist,infoList);

                                mPerson.setAdapter(adpater);

                            }


                        }
                    }catch (final NullPointerException e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Json parsing Error: :" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    } catch (final JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Json parsing Error:" + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(WaitPersonActivity.this);

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
