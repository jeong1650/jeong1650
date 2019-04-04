package com.example.kaon.ims.PreviousActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.kaon.ims.AddCookiesInterceptor;
import com.example.kaon.ims.ApiService;
import com.example.kaon.ims.CustomProgressDialog;
import com.example.kaon.ims.Listitem;
import com.example.kaon.ims.NoticeAdapter;
import com.example.kaon.ims.PersonInfoActivity;
import com.example.kaon.ims.R;

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

public class NoticeActivity extends AppCompatActivity {
    ListView listView;

    //Retrofit
    Retrofit retrofit;
    ApiService apiService;
    List<Listitem> NoticeList;

    ArrayList<HashMap<String, String>> Gonglist;


    private static final String TAG = "NoticeActivity";

    String id;
    String username;
    Context context;

    AddCookiesInterceptor in1;
    OkHttpClient httpClient;
    FloatingActionButton logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info);
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        username = intent.getExtras().getString("NAME");


        in1 = new AddCookiesInterceptor(this);
        httpClient = new OkHttpClient.Builder().addInterceptor(in1)
                .build();

        listView = (ListView) findViewById(R.id.listview);


        Gonglist = new ArrayList<>();


        AddCookiesInterceptor in1 = new AddCookiesInterceptor(this);



        httpClient = new OkHttpClient.Builder()

                .addNetworkInterceptor(in1)



                .build();

   new LoadData().execute();
    }


    private class LoadData extends AsyncTask<Void, Void, List<Listitem>> {
        CustomProgressDialog progressDialog = new CustomProgressDialog(NoticeActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected List<Listitem> doInBackground(Void... voids) {
            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retrofit = new Retrofit.Builder().
                    baseUrl(ApiService.API_URL)
                    .client(httpClient)
                    .build();

            apiService = retrofit.create(ApiService.class);

            HashMap<String, String> notice = new HashMap<>();
            notice.put("Name", id);

            apiService.postData(notice).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        Log.d(TAG, result);
                        if (result != null) {
                            JSONArray jsonArray = new JSONArray(result);
                            NoticeList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                Listitem listitem = new Listitem();
                                listitem.setNAME(c.getString("NAME"));
                                listitem.setSTART_DATE(c.getString("START_DATE"));
                                listitem.setEND_DATE(c.getString("END_DATE"));
//                                listitem.setTYPE(c.getString("TYPE"));
                                NoticeList.add(listitem);
                            }
                            NoticeAdapter adapter = new NoticeAdapter(getApplicationContext(), R.layout.list_gonggo, NoticeList);
                            listView.setAdapter(adapter);
                        }
                    } catch (final NullPointerException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Json parsing error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (final JSONException e) {
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);

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

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String NAME = Gonglist.get(position).get("NAME");
                    String NAME = NoticeList.get(position).getNAME();

                    Intent intent = new Intent(NoticeActivity.this,PersonInfoActivity.class);

                    intent.putExtra("NAME", NAME);
                    intent.putExtra("username", username);

                    NoticeActivity.this.startActivity(intent);


                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(List<Listitem> list) {
            progressDialog.dismiss();

//            }



//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                    String NAME = Gonglist.get(position).get("NAME");
//                    String NAME = NoticeList.get(position).getNAME();
//
//                    Intent intent = new Intent(NoticeActivity.this,PersonInfoActivity.class);
//
//                    intent.putExtra("NAME",NAME);
//
//                    NoticeActivity.this.startActivity(intent);
//
//
//
//                }
//            });

            }
        }


    }
