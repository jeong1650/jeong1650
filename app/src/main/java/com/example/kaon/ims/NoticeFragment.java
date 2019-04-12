package com.example.kaon.ims;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

public class NoticeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView listView;

    //Retrofit
    Retrofit retrofit;
    ApiService apiService;
    List<Listitem> NoticeList;

    ArrayList<HashMap<String, String>> Gonglist;

    ScrollView n_scrollView;

    private static final String TAG = "NoticeActivity";

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    TextView notnotice,title;

    String id;
    String username;
    Context context;

    SwipeRefreshLayout mSwipeRefreshLayout;

    AddCookiesInterceptor in1;
    OkHttpClient httpClient;
    public NoticeFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new loading().execute();

        View view = inflater.inflate(R.layout.person_info,container,false);
        in1 = new AddCookiesInterceptor(getActivity());
        httpClient = new OkHttpClient.Builder().addInterceptor(in1)
                .build();

        listView = (ListView) view.findViewById(R.id.listview);
        listView.setFocusable(false);

        notnotice = (TextView) view.findViewById(R.id.noticenot);
        Gonglist = new ArrayList<>();

        n_scrollView = view.findViewById(R.id.notice_scroll);
        n_scrollView.requestFocus(View.FOCUS_UP);
        n_scrollView.scrollTo(0,0);

        AddCookiesInterceptor in1 = new AddCookiesInterceptor(getActivity());



        httpClient = new OkHttpClient.Builder()

                .addNetworkInterceptor(in1)



                .build();

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_notice);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        retrofit = new Retrofit.Builder().
                baseUrl(ApiService.API_URL)
                .client(httpClient)
                .build();

        apiService = retrofit.create(ApiService.class);

        final HashMap<String, String> notice = new HashMap<>();
        notice.put("Name", id);

        apiService.postData(notice).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d(TAG, result);
                    if (!result.equals("[]")) {
                        notnotice.setVisibility(View.GONE);
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
                        NoticeAdapter adapter = new NoticeAdapter(getActivity(), R.layout.list_gonggo, NoticeList);
                        listView.setAdapter(adapter);
                    } else{
                        notnotice.setVisibility(View.VISIBLE);
                        notnotice.setText("등록된 공고가 없습니다.");
                    }
                } catch (final NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json parsing error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (final JSONException e) {
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String NAME = Gonglist.get(position).get("NAME");
                String NAME = NoticeList.get(position).getNAME();

                Intent intent = new Intent(getActivity(),PersonInfoActivity.class);

                intent.putExtra("NAME", NAME);
                intent.putExtra("username", username);

                getActivity().startActivity(intent);


            }
        });

        return view;

    }


    @Override
    public void onRefresh() {

        retrofit = new Retrofit.Builder().
                baseUrl(ApiService.API_URL)
                .client(httpClient)
                .build();

        apiService = retrofit.create(ApiService.class);

        final HashMap<String, String> notice = new HashMap<>();
        notice.put("Name", id);

        apiService.postData(notice).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d(TAG, result);
                    if (!result.equals("[]")) {
                        notnotice.setVisibility(View.GONE);
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
                        NoticeAdapter adapter = new NoticeAdapter(getActivity(), R.layout.list_gonggo, NoticeList);
                        listView.setAdapter(adapter);
                    } else{
                        notnotice.setVisibility(View.VISIBLE);
                        notnotice.setText("등록된 공고가 없습니다.");
                    }
                } catch (final NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json parsing error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (final JSONException e) {
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
