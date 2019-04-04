package com.example.kaon.ims;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class WaitpersonAdapter extends BaseAdapter {
    Context applicationContext;
    int person_list;
    List<Waitpersonitem> infoList;
    private LayoutInflater mInflater;
    int INDEX_ID;

    String apply_name;
    String MASTER_ID;
    String resumepath;
    String portpath;
    String test;
    Retrofit retrofit;
    ApiService apiService;
    ArrayList<Integer> IndexList;
    String username;

    AddCookiesInterceptor in1;
    OkHttpClient httpClient;

    public WaitpersonAdapter(Context applicationContext, int person_list, List<Waitpersonitem> infoList){
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            mInflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = mInflater.inflate(R.layout.waitlist, parent, false);

        }

        AddCookiesInterceptor in1 = new AddCookiesInterceptor(applicationContext);



        httpClient = new OkHttpClient.Builder()

                .addNetworkInterceptor(in1)
                .build();
        TextView name, year, T_position, etc,apply;
        name = (TextView) v.findViewById(R.id.w_name);
        name.setText(infoList.get(position).getNAME());
        year = (TextView) v.findViewById(R.id.w_career);
        if(infoList.get(position).getYEAR().equals("0")){
            year.setText("신입");
        }else{
            year.setText(infoList.get(position).getYEAR()+"년");
        }
        T_position = (TextView) v.findViewById(R.id.w_position);
        T_position.setText(infoList.get(position).getPOSITION());
        etc = (TextView) v.findViewById(R.id.w_etc);
        etc.setText(infoList.get(position).getETC());
        if(infoList.get(position).getETC().equals("null")){
            etc.setText("");
        }
        apply = (TextView) v.findViewById(R.id.w_path);
        apply.setText(infoList.get(position).getPATH());

        username = infoList.get(position).getUsername();

        INDEX_ID = infoList.get(position).getINDEX_ID();

        portpath = infoList.get(position).getPortpath();

        TextView resume = (TextView) v.findViewById(R.id.w_resume);
        resume.setTag(INDEX_ID);

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resumepath = infoList.get(position).getResumepath();
                String pdfload = "http://"+resumepath;
                Intent Resume_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfload));
                Resume_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(Resume_intent);
//                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
//                        .build();
//                apiService = retrofit.create(ApiService.class);
//
//                HashMap<String, String> wait = new HashMap<>();
//                wait.put("USER_ID", username);
//
//
//                apiService.waitlist(wait).enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        try {
//                            String result = response.body().string();
//                            JSONArray jsonArray = new JSONArray(result);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject c = jsonArray.getJSONObject(i);
//
//                                String resume = c.getString("RESUME_PATH");
//
//
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//                });



            }
        });

        TextView Potofolio = (TextView) v.findViewById(R.id.w_PF);
        Potofolio.setTag(INDEX_ID);
        Potofolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (portpath==null) {
                    Toast.makeText(applicationContext, "파일 없음", Toast.LENGTH_SHORT).show();

                }
                else {
                    portpath = infoList.get(position).getPortpath();
                    String pfload = "http://" + portpath;
                    Intent PF_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pfload));
                    PF_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    applicationContext.startActivity(PF_intent);
                }

            }
        });


        return v;
    }
}
