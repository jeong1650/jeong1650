package com.example.kaon.ims;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class PersonInfoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //View
    TextView mTitle;
    ListView mPerson;
    AddCookiesInterceptor in1;
    OkHttpClient httpClient;
    Personinfoitem personinfoitem;

    //Global data
    String mName;
    String etc_form;
    PersonInfoAdpater adpater;
    //Retrofit
    Retrofit retrofit;
    ApiService apiService;

    String Master_ID;
    //List
    List<Personinfoitem> infoList;
    String username;
    String STATUS;

    int INDEX_ID;

    String idvalue;
    int indexid;
    ArrayList<String> masterlist;
    String prname;
    String in_name;
    private ProgressDialog pDialog;

    ArrayList<String> namelist;

    SwipeRefreshLayout mSwipeRefreshLayout;

    FloatingActionButton logout;
    private static final String TAG = "PersonInfoActivity";

    public List<Personinfoitem> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Personinfoitem> infoList) {
        this.infoList = infoList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.interview_person);

        Window window = getWindow();



        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



        window.setStatusBarColor(Color.parseColor("#4e67c3"));

        in1 = new AddCookiesInterceptor(this);
        httpClient = new OkHttpClient.Builder().addInterceptor(in1)
                .build();
        mTitle = (TextView) findViewById(R.id.P_title);
        mPerson = (ListView) findViewById(R.id.i_list);
        mPerson.setFocusable(false);
        masterlist = new ArrayList<>();
//        Peronlist = new ArrayList<>();

        Intent intent = getIntent();
        mName = intent.getStringExtra("NAME");
        idvalue = intent.getStringExtra("id");
        indexid = intent.getExtras().getInt("INDEX_ID");
//        namelist = intent.getStringArrayListExtra("project");
         Log.d(TAG, String.valueOf(namelist));
        mTitle.setText(mName);


        username = intent.getStringExtra("username");
//        prname = intent.getStringExtra("NAME");
//        in_name = intent.getStringExtra("username");
        mSwipeRefreshLayout = findViewById(R.id.swipe_person);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        new InfoData().execute();


    }

    @Override
    public void onRefresh() {

        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                .client(httpClient)
                .build();
        apiService = retrofit.create(ApiService.class);

        HashMap<String, Object> param = new HashMap<>();
        param.put("PROJECT_INDEX_ID", indexid);
        param.put("userid", idvalue);


        apiService.postinfo(param).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    if (result != null) {
                        JSONArray jsonArray = new JSONArray(result);
                        infoList = new ArrayList<Personinfoitem>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            INDEX_ID = c.getInt("INDEX_ID");
                            Master_ID = c.getString("MASTER_ID");
                            masterlist.add(Master_ID);
                            personinfoitem = new Personinfoitem();
                            personinfoitem.setNAME(c.getString("NAME"));
                            personinfoitem.setYEAR(c.getString("YEAR"));
                            personinfoitem.setPOSITION(c.getString("POSITION"));
                            personinfoitem.setETC(c.getString("ETC"));
                            personinfoitem.setPROJECT_ID(c.getInt("PROJECT_ID"));
                            personinfoitem.setSTATUS(c.getString("HISTORY"));
                            personinfoitem.setINDEX_ID(INDEX_ID);
                            personinfoitem.setMASTER_ID(Master_ID);

//
                            infoList.add(personinfoitem);
                            adpater = new PersonInfoAdpater(getApplicationContext(), R.layout.person_list, infoList);
                            mPerson.setAdapter(adpater);

                        }

                    }
                } catch (final NullPointerException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing Error: :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
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
                final ErrorDialog errorDialog = new ErrorDialog(PersonInfoActivity.this);
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
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private class InfoData extends AsyncTask<Void, Void, Void> {
        CustomProgressDialog progressDialog = new CustomProgressDialog(PersonInfoActivity.this);

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

            HashMap<String, Object> param = new HashMap<>();
            param.put("PROJECT_INDEX_ID", indexid);
            param.put("userid", idvalue);


            apiService.postinfo(param).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String result = response.body().string();
                        if (result != null) {
                            JSONArray jsonArray = new JSONArray(result);
                            infoList = new ArrayList<Personinfoitem>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                INDEX_ID = c.getInt("INDEX_ID");
                                Master_ID = c.getString("MASTER_ID");
                                masterlist.add(Master_ID);
                                personinfoitem = new Personinfoitem();
                                personinfoitem.setNAME(c.getString("NAME"));
                                personinfoitem.setYEAR(c.getString("YEAR"));
                                personinfoitem.setPOSITION(c.getString("POSITION"));
                                personinfoitem.setETC(c.getString("ETC"));
                                personinfoitem.setPROJECT_ID(c.getInt("PROJECT_ID"));
                                personinfoitem.setSTATUS(c.getString("HISTORY"));
                                personinfoitem.setINDEX_ID(INDEX_ID);
                                personinfoitem.setMASTER_ID(Master_ID);

//
                                infoList.add(personinfoitem);
                                adpater = new PersonInfoAdpater(getApplicationContext(), R.layout.person_list, infoList);
                                mPerson.setAdapter(adpater);

                            }

                        }
                    } catch (final NullPointerException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Json parsing Error: :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
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
                    final ErrorDialog errorDialog = new ErrorDialog(PersonInfoActivity.this);
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
