package com.example.kaon.ims;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class WaitpersonFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    String username;

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

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new loading().execute();
        View view = inflater.inflate(R.layout.waitperson,container,false);
        in1 = new AddCookiesInterceptor(getActivity());
        httpClient = new OkHttpClient.Builder().addInterceptor(in1)
                .build();

        mTitle = (TextView) view.findViewById(R.id.w_title);
        mPerson = (ListView) view.findViewById(R.id.w_list);

        mTitle.setText("서류대기자");

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_wait);
        mSwipeRefreshLayout.setOnRefreshListener(this);

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

                            WaitpersonAdapter adpater = new WaitpersonAdapter(getActivity(),R.layout.waitlist,infoList);

                            mPerson.setAdapter(adpater);

                        }


                    }
                }catch (final NullPointerException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Json parsing Error: :" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (final JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Json parsing Error:" + e.getMessage());
                    Toast.makeText(getActivity(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                final ErrorDialog errorDialog = new ErrorDialog(getActivity());
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




        return view;
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

                            WaitpersonAdapter adpater = new WaitpersonAdapter(getActivity(),R.layout.waitlist,infoList);

                            mPerson.setAdapter(adpater);

                        }


                    }
                }catch (final NullPointerException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Json parsing Error: :" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (final JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Json parsing Error:" + e.getMessage());
                    Toast.makeText(getActivity(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                final ErrorDialog errorDialog = new ErrorDialog(getActivity());
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

    private class loading extends AsyncTask<Void, Void, Void> {
        CustomProgressDialog progressDialog = new CustomProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }

    }
}
