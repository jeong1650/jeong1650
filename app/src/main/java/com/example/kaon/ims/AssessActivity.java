package com.example.kaon.ims;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    String CONTENTS;

    String MASTER_ID;
    String PARENT_ID;
    int SCORE;
    int cs = 0;
    Boolean isChecking;

    JSONObject master;
    String TYPE;
    String JOB;
    String JOB_GRADE;
    int TOTAL_EVAL;
    String TOTAL_COMMENT;
    int TOTAL_SCORE;
    String USER_ID;
    String UserName;


    String STATUS;
    String INDEX_ID;
    String interviewercheck;

    Boolean isCount;
    int check_count;
    int ref_count;
    int count;
    LinearLayout Eval_View;


    //retrofit
    Retrofit retrofit;
    ApiService apiService;

    ArrayList<String> IDList;
    ArrayList<String> ConList;
    ArrayList<Integer> compare;

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

    //ExpandableListview
    public ExpandableListView expandableListView;
    public ExpandableListviewAdapter expandableListviewAdapter;
    public ArrayList<String> parentList;
    public ArrayList<ChildListData> Inputcontents;
    public ArrayList<ChildListData> Inputcontents2;
    public ArrayList<ChildListData> Inputcontents3;
    public ArrayList<ChildListData> Inputcontents4;
    public ArrayList<ChildListData> Inputcontents5;

    public HashMap<String, ArrayList<ChildListData>> childList;

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;
    private ExpandableListView mListView;

    String Pr_name;


    //btn
    Button btn_save;
    Button btn_Cancel;
    Button btn_tem;

    FloatingActionButton fab;

    RadioGroup Group;

    ScrollView Asses_scoll;

    String getEdit;

    int q_ea;
    int checkcount;
    //score
    int project_name;
    String userid;
    int Emp_point;
    ArrayList<Integer> Scorelist;
    ArrayList<Integer> Totalscore;

    LinearLayout totalbtn;
    int ea;

    int question_ea;

    TextView totalpercent;

    private static final String TAG = "AssessActivity";

    AddCookiesInterceptor in1;
    OkHttpClient httpClient;


    ArrayList<String> questionlist;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assess_appbar);

        isCount = true;

        Window window = getWindow();


        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        window.setStatusBarColor(Color.parseColor("#D3D3D3"));
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
        questionlist = new ArrayList<>();

        compare = new ArrayList<>();

        Log.d("comparelist", String.valueOf(compare));
        totalpercent = (TextView) findViewById(R.id.total_percent);

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
        Expandable();

        radioButton1 = (RadioButton) findViewById(R.id.eval_1);
        radioButton2 = (RadioButton) findViewById(R.id.eval_2);
        radioButton3 = (RadioButton) findViewById(R.id.eval_3);
        radioButton4 = (RadioButton) findViewById(R.id.eval_4);
        radioButton5 = (RadioButton) findViewById(R.id.eval_5);

        radioButton1.setOnClickListener(this);
        radioButton2.setOnClickListener(this);
        radioButton3.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton5.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
        btn_tem.setOnClickListener(this);
        fab.setOnClickListener(this);



        for (int v = 0; v <= 22; v++) {

            compare.add(v, 0);


        }
        

        new JsonEval().execute();


    }


    private void Expandable() {
        parentList = new ArrayList<String>();
        parentList.add("반드시 채용");
        parentList.add("채용 추천");
        parentList.add("채용 가능");
        parentList.add("채용 유보");
        parentList.add("채용 불가");

        ChildListData childData = new ChildListData("꼭 함께 일하고 싶으며, 개인과 회사의 시너지 효과가 극대화 될 것임");
        Inputcontents = new ArrayList<ChildListData>();
        Inputcontents.add(childData);

        ChildListData childData2 = new ChildListData("함께 일한다면 성과 창출과 부서 발전에 기여할 것으로 기대됨");
        Inputcontents2 = new ArrayList<ChildListData>();
        Inputcontents2.add(childData2);

        ChildListData childData3 = new ChildListData("결점이 뚜렷하게 보이지 않으며 자기 역할을 무난히 수행할 것으로 기대됨");
        Inputcontents3 = new ArrayList<ChildListData>();
        Inputcontents3.add(childData3);
        ChildListData childData4 = new ChildListData("결정적인 단점은 보이지 않으나 함께 일함에 있어 꺼려짐");
        Inputcontents4 = new ArrayList<ChildListData>();
        Inputcontents4.add(childData4);
        ChildListData childData5 = new ChildListData("결격 사유가 보이며 반드시 불합격 시켜야 함");
        Inputcontents5 = new ArrayList<ChildListData>();
        Inputcontents5.add(childData5);
        childList = new HashMap<String, ArrayList<ChildListData>>();
        childList.put(parentList.get(0), Inputcontents);
        childList.put(parentList.get(1), Inputcontents2);
        childList.put(parentList.get(2), Inputcontents3);
        childList.put(parentList.get(3), Inputcontents4);
        childList.put(parentList.get(4), Inputcontents5);
        expandableListView = (ExpandableListView) findViewById(R.id.ex_list);

        expandableListviewAdapter = new ExpandableListviewAdapter(this, parentList, childList);
        expandableListView.setAdapter(expandableListviewAdapter);

    }


    @Override
    public void onClick(View v) {
        getEdit = Opnion.getText().toString();
        int id = v.getId();
        if (id == R.id.eval_1) {
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
            Emp_point = 5;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
            Log.d(TAG, String.valueOf(ea));
        } else if (id == R.id.eval_2) {
            radioButton1.setChecked(false);
            radioButton3.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
            Emp_point = 4;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
        } else if (id == R.id.eval_3) {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton4.setChecked(false);
            radioButton5.setChecked(false);
            Emp_point = 3;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
        } else if (id == R.id.eval_4) {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            radioButton5.setChecked(false);
            Emp_point = 2;
            ea = 1;
            Log.d(TAG, String.valueOf(Emp_point));
        } else if (id == R.id.eval_5) {
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

            SharedPreferences pref = getSharedPreferences("pref", AssessActivity.this.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("check_count", check_count);

            editor.commit();

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

                            TempDialog tempDialog = new TempDialog(AssessActivity.this);
                            tempDialog.setTempDialogListener(new TempDialog.TempDialogListener() {
                                @Override
                                public void checkClick() {
                                    Intent intent = new Intent(AssessActivity.this, PersonInfoActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("NAME",Pr_name);
                                    intent.putExtra("INDEX_ID", project_name);
                                    intent.putExtra("id", userid);
                                    AssessActivity.this.startActivity(intent);
                                }
                            });
                            tempDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            tempDialog.show();

                        } else if (result == 0) {

                            OnefinishedDialog onefinishedDialog = new OnefinishedDialog(AssessActivity.this);
                            onefinishedDialog.setOnefinishedDialogListener(new OnefinishedDialog.OnefinishedDialogListener() {
                                @Override
                                public void checkClick() {
                                    Intent intent = new Intent(AssessActivity.this, PersonInfoActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("NAME",Pr_name);
                                    intent.putExtra("INDEX_ID", project_name);
                                    intent.putExtra("id", userid);
                                    AssessActivity.this.startActivity(intent);
                                }
                            });
                            onefinishedDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            onefinishedDialog.show();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    final ErrorDialog errorDialog = new ErrorDialog(AssessActivity.this);
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


        } else if (id == R.id.Btn_save) {

            if (q_ea != count && q_ea != checkcount) {

                final Tablecheck tablecheck = new Tablecheck(AssessActivity.this);
                tablecheck.setTablecheckListener(new Tablecheck.TablecheckListener() {
                    @Override
                    public void checkClick() {
                        tablecheck.cancel();
                    }
                });
                tablecheck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                tablecheck.show();
            }
            else if (getEdit.equals("")) {

                final Tablecheck tablecheck = new Tablecheck(AssessActivity.this);
                tablecheck.setTablecheckListener(new Tablecheck.TablecheckListener() {
                    @Override
                    public void checkClick() {
                        tablecheck.cancel();
                    }
                });
                tablecheck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                tablecheck.show();
            }
            else if (ea != 1) {
                final Tablecheck tablecheck = new Tablecheck(AssessActivity.this);
                tablecheck.setTablecheckListener(new Tablecheck.TablecheckListener() {
                    @Override
                    public void checkClick() {
                        tablecheck.cancel();
                    }
                });
                tablecheck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                tablecheck.show();
            }
            else {
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

                                        final InfocheckDialog infocheckDialog = new InfocheckDialog(AssessActivity.this);
                                        infocheckDialog.setInfocheckDialogListener(new InfocheckDialog.InfocheckDialogListener() {
                                            @Override
                                            public void checkClick() {
                                                infocheckDialog.dismiss();
                                            }
                                        });
                                        infocheckDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                        infocheckDialog.show();
                                    } else {
//
                                        final InfocheckDialog infocheckDialog = new InfocheckDialog(AssessActivity.this);
                                        infocheckDialog.setInfocheckDialogListener(new InfocheckDialog.InfocheckDialogListener() {
                                            @Override
                                            public void checkClick() {
                                                infocheckDialog.dismiss();
                                            }
                                        });
                                        infocheckDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                        infocheckDialog.show();
                                    }


                                } else if (result == 1) {
                                    Datasend();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {

                                final RightCheckdialog rightCheckdialog = new RightCheckdialog(AssessActivity.this);
                                rightCheckdialog.setRightCheckdialogListener(new RightCheckdialog.RightCheckdialogListener() {
                                    @Override
                                    public void checkClick() {
                                        rightCheckdialog.dismiss();
                                    }
                                });
                                rightCheckdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                rightCheckdialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            final ErrorDialog errorDialog = new ErrorDialog(AssessActivity.this);
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

                } else {

                    final Checkdialog checkdialog = new Checkdialog(AssessActivity.this);
                    checkdialog.setcheckDialogListener(new Checkdialog.CheckDialogListener() {
                        @Override
                        public void checkClick() {
                            checkdialog.dismiss();
                        }
                    });
                    checkdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    checkdialog.show();
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
                                        Pr_name = c.getString("PROJECT_NAME");
                                        master = c.getJSONObject("master");
                                        JSONObject ma = new JSONObject(String.valueOf(master));
                                        TOTAL_SCORE = ma.getInt("TOTAL_SCORE");
                                        final Submitdialog submitdialog = new Submitdialog(AssessActivity.this);
                                        submitdialog.setSubmitdialogListener(new Submitdialog.SubmitdialogListener() {
                                            @Override
                                            public void checkClick() {
                                                Intent intent = new Intent(AssessActivity.this, PersonInfoActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("NAME",Pr_name);
                                                intent.putExtra("INDEX_ID", project_name);
                                                intent.putExtra("id", userid);
                                                AssessActivity.this.startActivity(intent);
                                            }
                                        });
                                        submitdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                        submitdialog.show();


                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                final ErrorDialog errorDialog = new ErrorDialog(AssessActivity.this);
                                errorDialog.setErrorDialogListener(new ErrorDialog.ErrorDialogListener() {
                                    @Override
                                    public void checkClick() {
                                        errorDialog.cancel();
                                    }
                                });
                                errorDialog.show();
                            }
                        });


                    } else if (result == 0) {

                        OnefinishedDialog onefinishedDialog = new OnefinishedDialog(AssessActivity.this);
                        onefinishedDialog.setOnefinishedDialogListener(new OnefinishedDialog.OnefinishedDialogListener() {
                            @Override
                            public void checkClick() {
                                Intent intent = new Intent(AssessActivity.this, PersonInfoActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("NAME",Pr_name);
                                intent.putExtra("INDEX_ID", project_name);
                                intent.putExtra("id", userid);
                                AssessActivity.this.startActivity(intent);
                            }
                        });
                        onefinishedDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        onefinishedDialog.show();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                final ErrorDialog errorDialog = new ErrorDialog(AssessActivity.this);
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
                            Pr_name = c.getString("PROJECT_NAME");
                            master = c.getJSONObject("master");
                            project_name = c.getInt("PROJECT_ID");
                            JSONObject ma = new JSONObject(String.valueOf(master));
                            USER_ID = ma.getString("USER_ID");
                            UserName = ma.getString("USER_NAME");
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
                            interviewer.setText("면접관 : " + UserName);
                            SharedPreferences pref = getSharedPreferences("pref", AssessActivity.this.MODE_PRIVATE);
                            if(STATUS.equals("1")){
                                ref_count = pref.getInt("check_count", 0);
                                Pr_percent.setText("평가 한 문항수 :" + ref_count);
                            } else{
                                int startcount = 0;
                                Pr_percent.setText("평가 한 문항수 :" + startcount);
                            }



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
                            if(STATUS.equals("2")){
                                totalpercent.setVisibility(View.GONE);
                                Pr_percent.setText("평가완료");
                            }

                            q_ea = 0;
                            Log.e("STATUS",STATUS);
                            if(STATUS.equals("1")){
                                isCount = false;
                                check_count = ref_count;
                            } else if(STATUS.equals("2")) {
                                check_count = ref_count;
                            } else{
                                check_count = 0;
                            }
                            for (int n = 0; n < IDList.size(); n++) {
                                int x = Totalscore.get(n);
                                Log.d(TAG, String.valueOf(Totalscore));
                                String id = IDList.get(n);
                                final String con = ConList.get(n);
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
                                    questionlist.add(con);
                                    eval_question.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                                    Eval_View.addView(eval_question);
                                    question_ea = questionlist.size();
                                    cs++;
                                    totalpercent.setText("/"+String.valueOf(cs));
//                                    Pr_percent.setText("평가 한 문항수 :" + "0");

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




                                            if (isCount == false) {

                                                for (int j = tag; j <= tag; j++) {
                                                    if (compare.get(tag) != 1) {
                                                        isCount = true;
                                                    }
                                                }
                                            }

                                            if (isCount == true) {
                                                check_count++;
                                            }

                                            for (int i = tag; i <= tag; i++) {
                                                if (Scorelist.get(i) != 0) {
                                                    compare.set(tag, 1);
                                                    isCount = false;
                                                } else {

                                                    break;
                                                }

                                            }

                                            Pr_percent.setText("평가 한 문항수 :" + check_count);

                                            checkcount = 0;
                                            for (int f = 0; f < Scorelist.size(); f++) {
                                                if (Scorelist.get(f) == 0) {
                                                    checkcount++;
                                                }
                                            }

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
                                    interviewer.setText("평가자 : " + UserName);
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


                            Opnion.setText(TOTAL_COMMENT);


                            Log.d(TAG, String.valueOf(Scorelist));
                            Log.d(TAG, String.valueOf(check_count));


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
                    final ErrorDialog errorDialog = new ErrorDialog(AssessActivity.this);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//

        }

    }


    @Override
    protected void onStop() {

        super.onStop();


    }
}
