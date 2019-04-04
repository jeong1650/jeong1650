package com.example.kaon.ims;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class AssessActivity extends AppCompatActivity implements View.OnClickListener {
    TextView eval_title = null;
    TextView eval_question = null;
    TextView interviewer;
    TextView applicant;
    TextView Pr_percent;
    EditText Opnion;
    String apply_name;
    int que_count = 0;
    String CONTENTS;

    String MASTER_ID;
    String PARENT_ID;
    int SCORE;

    Boolean isChecking;

    JSONObject master;
    String TYPE;
    String JOB;
    String JOB_GRADE;
    int TOTAL_EVAL;
    String TOTAL_COMMENT;
    int TOTAL_SCORE;
    String USER_ID;
    String STATUS;
    String INDEX_ID;
    String interviewercheck;



    LinearLayout Eval_View;

    //retrofit
    Retrofit retrofit;
    ApiService apiService;

    ArrayList<String> IDList;
    ArrayList<String> ConList;
    ArrayList<Integer> ealist;
    LayoutInflater inflater;

    //radiobtn
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioButton radioButton5;

    RadioButton Check5;
    RadioButton Check4;
    RadioButton Check3;
    RadioButton Check2;
    RadioButton Check1;


    //btn
    Button btn_save;
    Button btn_Cancel;
    Button btn_tem;

    FloatingActionButton fab;

    RadioGroup Group;

    ScrollView Asses_scoll;

    String getEdit;
    int count;
    int q_ea;
    int checkcount;
    //score
    String project_name;
    String userid;
    int Emp_point;
    ArrayList<Integer> Scorelist;
    ArrayList<Integer> Totalscore;

    LinearLayout totalbtn;
    int ea = 0;

    int[] eaArray;

    private static final String TAG = "AssessActivity";

    AddCookiesInterceptor in1;
    OkHttpClient httpClient;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.assess_appbar);

        Window window = getWindow();



        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



        window.setStatusBarColor(Color.parseColor("#4e67c3"));
        totalbtn = (LinearLayout) findViewById(R.id.total_btn);
        in1 = new AddCookiesInterceptor(this);
        httpClient = new OkHttpClient.Builder().addInterceptor(in1)
                .build();
        Intent intent = getIntent();
        MASTER_ID = intent.getExtras().getString("MASTER_ID");
        apply_name = intent.getExtras().getString("NAME");
        IDList = new ArrayList<>();
        ConList = new ArrayList<>();
        Totalscore = new ArrayList<>();
        eaArray = new int[22];


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        Asses_scoll = (ScrollView) findViewById(R.id.topscoll);
        Asses_scoll.fullScroll(ScrollView.FOCUS_UP);

        eval_title = (TextView) findViewById(R.id.eval_title);
        eval_question = (TextView) findViewById(R.id.eval_question);
        Eval_View = (LinearLayout) findViewById(R.id.Eval_form);
        Pr_percent = (TextView) findViewById(R.id.pro_percent);

        Opnion = (EditText) findViewById(R.id.opinion);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Scorelist = new ArrayList<>();

        btn_save = (Button) findViewById(R.id.Btn_save);
        btn_Cancel = (Button) findViewById(R.id.Btn_cancel);
        btn_tem = (Button) findViewById(R.id.Btn_temporary);

        radioButton1 = (RadioButton) findViewById(R.id.eva_1);
        radioButton2 = (RadioButton) findViewById(R.id.eva_2);
        radioButton3 = (RadioButton) findViewById(R.id.eva_3);
        radioButton4 = (RadioButton) findViewById(R.id.eva_4);
        radioButton5 = (RadioButton) findViewById(R.id.eva_5);

        radioButton1.setOnClickListener(this);
        radioButton2.setOnClickListener(this);
        radioButton3.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton5.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
        btn_tem.setOnClickListener(this);
        fab.setOnClickListener(this);

        new JsonEval().execute();


    }


    @Override
    public void onClick(View v) {
        getEdit = Opnion.getText().toString();
        int id = v.getId();
        if (id == R.id.eva_1) {
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
            Emp_point = 5;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
            Log.d(TAG, String.valueOf(ea));
        } else if (id == R.id.eva_2) {
            radioButton1.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
            Emp_point = 4;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
        } else if (id == R.id.eva_3) {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
            Emp_point = 3;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
        } else if (id == R.id.eva_4) {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton5.setChecked(false);
            Emp_point = 2;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
        } else if (id == R.id.eva_5) {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            Emp_point = 1;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
        } else if (id == R.id.fab) {
            Asses_scoll.fullScroll(ScrollView.FOCUS_UP);
        } else if (id == R.id.Btn_cancel) {
            finish();

        } else if (id == R.id.Btn_temporary) {

            STATUS = "1";

            retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                    .client(httpClient)
                    .build();
            apiService = retrofit.create(ApiService.class);

            HashMap<String, Object> senddata = new HashMap<>();
            senddata.put("score", Scorelist);
            senddata.put("TOTAL_EVAL", Emp_point);
            senddata.put("TOTAL_COMMENT", Opnion.getText().toString());
            senddata.put("STATUS", STATUS);
            senddata.put("JOB", JOB);
            senddata.put("JOB_GRADE", JOB_GRADE);
            senddata.put("INDEX_ID", INDEX_ID);

            apiService.Senddata(senddata).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int result = Integer.parseInt(response.body().string());
                        if (result == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                            builder.setTitle("완료")
                                    .setMessage("임시저장하였습니다.")
                                    .setCancelable(false)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(AssessActivity.this, PersonInfoActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("NAME", project_name);
                                            intent.putExtra("username", userid);
                                            AssessActivity.this.startActivity(intent);


                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            //다이어로그 생성
                            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
                            dialog.show();

                        } else if (result == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                            builder.setTitle("제출완료")
                                    .setMessage("이미 제출이 되었습니다.")
                                    .setCancelable(false)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(AssessActivity.this, PersonInfoActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("NAME", project_name);
                                            intent.putExtra("username", userid);
                                            AssessActivity.this.startActivity(intent);

                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            //다이어로그 생성
                            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
                            dialog.show();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

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


        } else if (id == R.id.Btn_save) {

            if (q_ea != count && q_ea != checkcount) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                builder.setTitle("알림")
                        .setMessage("문항을 다 체크해주세요")
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
            } else if (ea != 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                builder.setTitle("알림")
                        .setMessage("종합평가를 체크해주세요")
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

            } else if (getEdit.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                builder.setTitle("알림")
                        .setMessage("종합의견을 작성해주세요")
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
            } else {
                checkMethod();
            }


        }

    }

    private void checkMethod() {
        final PasswordDialog passwordDialog = new PasswordDialog(this);
        passwordDialog.setPswdDiaglogListener(new PasswordDialog.PswdDiaglogListener() {
            @Override
            public void onPositiveClick(final String name, String depart, String position, String pswd, Boolean isCheck) {
                JOB = depart;
                JOB_GRADE = position;
                interviewercheck = name;
                isChecking = isCheck;

                if (isChecking == true) {

                    retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                            .client(httpClient)
                            .build();
                    apiService = retrofit.create(ApiService.class);

                    HashMap<String, String> postcheck = new HashMap<>();
                    postcheck.put("name", name);
                    postcheck.put("pw", pswd);


                    apiService.postcheck(postcheck).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                int result = Integer.parseInt(response.body().string());
                                if (result == 0) {

                                    if (!name.equals(userid)) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                                        builder.setTitle("실패")
                                                .setMessage("이름을 올바르게 입력하세요")
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
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                                        builder.setTitle("실패")
                                                .setMessage("패스워드가 틀렸습니다.")
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


                                } else if (result == 1) {
                                    Datasend();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                                builder.setTitle("입력 알림")
                                        .setMessage("정보를 올바르게 입력하세요")
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
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

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

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                    builder.setTitle("체크")
                            .setMessage("본인 확인을 체크해주세요")
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

            }

            @Override
            public void onNegativeClick() {
                passwordDialog.cancel();
            }
        });

        passwordDialog.show();
    }


    private void Datasend() {
        STATUS = "2";

        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                .client(httpClient)
                .build();
        apiService = retrofit.create(ApiService.class);

        HashMap<String, Object> senddata = new HashMap<>();
        senddata.put("score", Scorelist);
        senddata.put("TOTAL_EVAL", Emp_point);
        senddata.put("TOTAL_COMMENT", Opnion.getText().toString());
        senddata.put("STATUS", STATUS);
        senddata.put("JOB", JOB);
        senddata.put("JOB_GRADE", JOB_GRADE);
        senddata.put("INDEX_ID", INDEX_ID);

        apiService.Senddata(senddata).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    int result = Integer.parseInt(response.body().string());

                    if (result == 1) {
                        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                                .client(httpClient)
                                .build();
                        apiService = retrofit.create(ApiService.class);
                        HashMap<String, Object> complete = new HashMap<>();
                        complete.put("masterid", MASTER_ID);

                        apiService.postComplete(complete).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                try {
                                    String JsonResult = response.body().string();
                                    if (JsonResult != null) {
                                        JSONObject c = new JSONObject(JsonResult);
                                        master = c.getJSONObject("master");
                                        JSONObject ma = new JSONObject(String.valueOf(master));
                                        TOTAL_SCORE = ma.getInt("TOTAL_SCORE");

                                        AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                                        builder.setTitle("완료")
                                                .setMessage("평가표를 성공적으로 제출하였습니다. 정보 확인 후 확인버튼을 눌러주세요.")
                                                .setCancelable(false)

                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(AssessActivity.this, PersonInfoActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("NAME", project_name);
                                                        intent.putExtra("username", userid);
                                                        AssessActivity.this.startActivity(intent);

                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        //다이어로그 생성
                                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
                                        dialog.show();


                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

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


                    } else if (result == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                        builder.setTitle("제출완료")
                                .setMessage("이미 제출이 되었습니다.")
                                .setCancelable(false)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(AssessActivity.this, PersonInfoActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("NAME", project_name);
                                        intent.putExtra("username", userid);
                                        AssessActivity.this.startActivity(intent);


                                    }
                                });
                        AlertDialog dialog = builder.create();
                        //다이어로그 생성
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //dim처리
                        dialog.show();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

                builder.setTitle("응답 오류")
                        .setMessage("통신 오류가 발생하였습니다. 다시 시도해주세요")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();


                            }
                        });
            }
        });

    }


    private class JsonEval extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                    .client(httpClient)
                    .build();
            apiService = retrofit.create(ApiService.class);
            HashMap<String, Object> complete = new HashMap<>();
            complete.put("masterid", MASTER_ID);

            apiService.postComplete(complete).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String JsonResult = response.body().string();
                        if (JsonResult != null) {

                            JSONObject c = new JSONObject(JsonResult);
                            master = c.getJSONObject("master");
                            project_name = c.getString("PROJECT_ID");

                            JSONObject ma = new JSONObject(String.valueOf(master));
                            USER_ID = ma.getString("USER_ID");
                            JOB = ma.getString("JOB");
                            STATUS = ma.getString("STATUS");
                            userid = ma.getString("USER_ID");
                            JOB_GRADE = ma.getString("JOB_GRADE");
                            TOTAL_EVAL = ma.getInt("TOTAL_EVAL");
                            TOTAL_COMMENT = ma.getString("TOTAL_COMMENT");
                            if (TOTAL_COMMENT.equals("null")) {
                                TOTAL_COMMENT = "";
                            }
                            TOTAL_SCORE = ma.getInt("TOTAL_SCORE");
                            TYPE = ma.getString("TYPE");

                            INDEX_ID = ma.getString("INDEX_ID");


                            applicant = (TextView) findViewById(R.id.applicant);
                            applicant.setText("면접자 : " + apply_name);
                            interviewer = (TextView) findViewById(R.id.interviewer);
                            interviewer.setText("면접관 : " + USER_ID);

                            JSONArray jsonArray = c.getJSONArray("detail");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject de = jsonArray.getJSONObject(i);
                                {
                                    PARENT_ID = de.getString("PARENT_ID");
                                    CONTENTS = de.getString("CONTENTS");
                                    SCORE = de.getInt("SCORE");
                                    IDList.add(PARENT_ID);
                                    ConList.add(CONTENTS);
                                    Scorelist.add(SCORE);
                                    Totalscore.add(SCORE);


                                    Log.d(TAG, String.valueOf(Scorelist));

                                }
                            }

                            q_ea = 0;

                            for (int n = 0; n < IDList.size(); n++) {
                                int x = Totalscore.get(n);
                                Log.d(TAG, String.valueOf(Totalscore));
                                String id = IDList.get(n);
                                String con = ConList.get(n);
                                if (id.equals("0")) {
                                    eval_title = new TextView(AssessActivity.this);
                                    eval_title.setText(con);
                                    eval_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                                    eval_title.setTextColor(0xff000000);
                                    Eval_View.addView(eval_title);
                                    q_ea++;

                                } else {
                                    eval_question = new TextView(AssessActivity.this);
                                    eval_question.setText(con);
                                    eval_question.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

                                    Eval_View.addView(eval_question);


                                    LinearLayout radiobtn = new LinearLayout(AssessActivity.this);
                                    radiobtn.setOrientation(LinearLayout.HORIZONTAL);

                                    Group = new RadioGroup(AssessActivity.this);
                                    Group.setOrientation(LinearLayout.HORIZONTAL);
                                    TextView pointtext = new TextView(AssessActivity.this);
                                    pointtext.setText("평가점수");

                                    Check5 = new RadioButton(AssessActivity.this);
                                    Check5.setText("5");
                                    Check5.setId(R.id.check_five);


                                    if (x == 5) {
                                        Check5.setChecked(true);
                                    }
                                    Check4 = new RadioButton(AssessActivity.this);
                                    Check4.setText("4");
                                    Check4.setId(R.id.check_four);
                                    if (x == 4) {
                                        Check4.setChecked(true);
                                    }
                                    Check3 = new RadioButton(AssessActivity.this);
                                    Check3.setText("3");
                                    Check3.setId(R.id.check_three);
                                    if (x == 3) {
                                        Check3.setChecked(true);
                                    }
                                    Check2 = new RadioButton(AssessActivity.this);
                                    Check2.setText("2");
                                    Check2.setId(R.id.check_two);
                                    if (x == 2) {
                                        Check2.setChecked(true);

                                    }
                                    Check1 = new RadioButton(AssessActivity.this);
                                    Check1.setText("1");
                                    Check1.setId(R.id.check_one);
                                    if (x == 1) {
                                        Check1.setChecked(true);
                                    }

                                    Log.d(TAG, String.valueOf(n));
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    radiobtn.addView(Group, layoutParams);
                                    Group.addView(pointtext);
                                    Group.addView(Check5);
                                    Group.addView(Check4);
                                    Group.addView(Check3);
                                    Group.addView(Check2);
                                    Group.addView(Check1);


                                    Eval_View.addView(radiobtn);

                                    Group.setTag(n);


                                    Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                            int tag = (int) group.getTag();
                                            switch (checkedId) {
                                                case R.id.check_five:
                                                    Scorelist.set(tag, 5);
                                                    break;
                                                case R.id.check_four:
                                                    Scorelist.set(tag, 4);

                                                    break;
                                                case R.id.check_three:
                                                    Scorelist.set(tag, 3);

                                                    break;
                                                case R.id.check_two:
                                                    Scorelist.set(tag, 2);

                                                    break;
                                                case R.id.check_one:
                                                    Scorelist.set(tag, 1);
                                                    break;
                                            }


                                            checkcount = 0;
                                            for (int f = 0; f < Scorelist.size(); f++) {
                                                if (Scorelist.get(f) == 0) {
                                                    checkcount++;
                                                }
                                            }
                                            Log.d(TAG, String.valueOf(Scorelist));
                                            Log.d(TAG, String.valueOf(checkcount
                                            ));


                                        }


                                    });


                                }

                                if (STATUS.equals("2")) {
                                    totalbtn.removeAllViews();
                                    LinearLayout check = new LinearLayout(AssessActivity.this);
                                    check.setOrientation(LinearLayout.VERTICAL);

                                    LinearLayout checkbox = new LinearLayout(AssessActivity.this);
                                    checkbox.setOrientation(LinearLayout.HORIZONTAL);

                                    LinearLayout.LayoutParams checkparm = new LinearLayout.LayoutParams
                                            (LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                    totalbtn.addView(check, checkparm);

                                    TextView interviewer = new TextView(AssessActivity.this);
                                    interviewer.setText("평가자 : " + USER_ID);
                                    check.addView(interviewer, checkparm);
                                    TextView departcheck = new TextView(AssessActivity.this);
                                    departcheck.setText("부서 : " + JOB);
                                    check.addView(departcheck, checkparm);
                                    TextView classcheck = new TextView(AssessActivity.this);
                                    classcheck.setText("직급 : " + JOB_GRADE);
                                    check.addView(classcheck, checkparm);
                                    TextView scorecheck = new TextView(AssessActivity.this);
                                    scorecheck.setText("평가 점수 : " + TOTAL_SCORE);
                                    check.addView(scorecheck, checkparm);


                                    Button okassess = new Button(AssessActivity.this);
                                    okassess.setText("확인");

                                    okassess.setTextColor(Color.parseColor("#FFFFFF"));
                                    okassess.setBackgroundResource(R.drawable.button_round_corners);
                                    check.addView(okassess, checkparm);
                                    okassess.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();


                                        }
                                    });

                                }
                                Log.d(TAG, String.valueOf(q_ea));
                            }

                            Log.d(TAG, String.valueOf(Scorelist));
                            count = 0;
                            for (int f = 0; f < Scorelist.size(); f++) {
                                if (Scorelist.get(f) == 0) {
                                    count++;
                                }
                            }
                            Log.d(TAG, String.valueOf(count));
                            Opnion.setText(TOTAL_COMMENT);


                            if (TOTAL_EVAL == 5) {
                                radioButton1.setChecked(true);
                                ea = 1;
                            } else if (TOTAL_EVAL == 4) {
                                radioButton2.setChecked(true);
                                ea = 1;
                            } else if (TOTAL_EVAL == 3) {
                                radioButton3.setChecked(true);
                                ea = 1;
                            } else if (TOTAL_EVAL == 2) {
                                radioButton4.setChecked(true);
                                ea = 1;
                            } else if (TOTAL_EVAL == 1) {
                                radioButton5.setChecked(true);
                                ea = 1;
                            }

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (final JSONException e) {
                        e.printStackTrace();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(AssessActivity.this);

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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}
