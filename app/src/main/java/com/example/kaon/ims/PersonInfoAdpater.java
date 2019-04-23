package com.example.kaon.ims;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
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

class PersonInfoAdpater extends BaseAdapter{
    Context applicationContext;
    int person_list;
    List<Personinfoitem> infoList;
    private LayoutInflater mInflater;
    int INDEX_ID;
    AddCookiesInterceptor in1;
    OkHttpClient httpClient;
    String apply_name;
    String MASTER_ID;
    String resumepath;
    String portpath;
    String test;
    Retrofit retrofit;
    ApiService apiService;
    ArrayList<Integer> IndexList;
    Context mContext;


    public PersonInfoAdpater(Context applicationContext, int person_list, List<Personinfoitem> infoList) {
        this.applicationContext = applicationContext;
        this.person_list = person_list;
        this.infoList = infoList;

    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }



    public List<Personinfoitem> getInfoList() {
        return infoList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        IndexList = new ArrayList<>();
        View v = convertView;
        if (v == null) {
            mInflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = mInflater.inflate(R.layout.person_list, parent, false);

        }


        AddCookiesInterceptor in1 = new AddCookiesInterceptor(mContext);

        httpClient = new OkHttpClient.Builder()

                .addNetworkInterceptor(in1)
                .build();
        INDEX_ID = infoList.get(position).getINDEX_ID();

        TextView name, year, T_position, etc,status, path;

        View statusview;

//        statusview = v.findViewById(R.id.as_status);
        Button asses;
        name= v.findViewById(R.id.name);
        name.setText(infoList.get(position).getNAME());

        year = (TextView) v.findViewById(R.id.career);
        if(infoList.get(position).getYEAR().equals("0")){
            year.setText("신입");
        }else{
            year.setText("경력"+infoList.get(position).getYEAR()+"년");
        }
        path = (TextView) v.findViewById(R.id.path);
        if (infoList.get(position).getPath().equals("0")) {
            path.setText("지원자");
        }else {
            path.setText("서칭인원");
        }
        etc = v.findViewById(R.id.etc);
        etc.setText("참고 : "+""+infoList.get(position).getETC());
        T_position = (TextView) v.findViewById(R.id.position);
        T_position.setText(infoList.get(position).getPOSITION());

        status = (TextView) v.findViewById(R.id.status);
        asses = (Button) v.findViewById(R.id.assess);
        status.setText(infoList.get(position).getSTATUS());
        if(infoList.get(position).getSTATUS().equals("2")){
            status.setText("제출 완료");
            String strColor = "#4E67C3";
            status.setTextColor(Color.parseColor(strColor));
//            statusview.setBackgroundResource(R.drawable.mybtn);
            asses.setText("평가 확인");
        } else if(infoList.get(position).getSTATUS().equals("1")){
            String asscolor = "#8192D3";
            status.setText("임시 저장");
//            statusview.setBackgroundResource(R.drawable.selectstyle);
            status.setTextColor(Color.parseColor(asscolor));
        } else {
            String waitcolor = "#707070";
            status.setText("평가 대기");
            status.setTextColor(Color.parseColor(waitcolor));
//            statusview.setBackgroundResource(R.drawable.rounded_edittext);
        }





        TextView resume = (TextView) v.findViewById(R.id.resume);
        resume.setTag(INDEX_ID);
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = (int) v.getTag();//infoList.get(position).getINDEX_ID();
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                        .client(httpClient)
                        .build();
                apiService = retrofit.create(ApiService.class);
                HashMap<String, Integer> file = new HashMap<>();
                file.put("INDEX_ID", index);

                apiService.postresume(file).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            if (result != null) {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    resumepath = c.getString("resumepath");

                                    String pdfload = "http://" + resumepath;
                                    Intent Resume_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfload));
                                    Resume_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    applicationContext.startActivity(Resume_intent);


                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "Json parsing Error:" + e.getMessage());

                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(applicationContext, "통신실패", Toast.LENGTH_SHORT);
                    }
                });

            }
        });

        TextView Potofolio = (TextView) v.findViewById(R.id.PF);
        Potofolio.setTag(INDEX_ID);
        Potofolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = (int) v.getTag();
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                        .client(httpClient)
                        .build();
                apiService = retrofit.create(ApiService.class);

                HashMap<String, Integer> file = new HashMap<>();
                file.put("INDEX_ID", index);

                apiService.postresume(file).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            if (result != null) {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    portpath = c.getString("portpath");


                                    if (portpath.equals("null")) {
                                        Toast.makeText(applicationContext, "파일 없음", Toast.LENGTH_SHORT).show();

                                    } else {
                                        String pfload = "http://" + portpath;
                                        Intent PF_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pfload));
                                        PF_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        applicationContext.startActivity(PF_intent);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            Log.e("error", "Json parsing Error:" + e.getMessage());

                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(applicationContext, "통신실패", Toast.LENGTH_SHORT);
                    }
                });


            }
        });
        Button assess = (Button) v.findViewById(R.id.assess);

        assess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MASTER_ID = infoList.get(position).getMASTER_ID();
                apply_name = infoList.get(position).getNAME();

                Intent EvalIntent = new Intent(applicationContext, AssessActivity.class);
                EvalIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                EvalIntent.putExtra("MASTER_ID", MASTER_ID);
                EvalIntent.putExtra("NAME", apply_name);
                applicationContext.startActivity(EvalIntent);
            }
        });

        return v;


    }

}
