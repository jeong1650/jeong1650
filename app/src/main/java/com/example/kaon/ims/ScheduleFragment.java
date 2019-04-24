package com.example.kaon.ims;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScheduleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ScheduleActivity";
    ArrayList<HashMap<String, String>> contactList;
    TextView inter_time;
    HorizontalCalendar horizontalCalendar;
    Retrofit retrofit;
    ApiService apiService;
    Calendar date;
    AddCookiesInterceptor in1;
    OkHttpClient httpClient;

    TextView start_time, finish_time, interview_place, s_name, s_position;

    String starttime;
    String endtime;
    String s_place;
    String int_date;
    String name_sch;
    String pot_sch;
    String strt;

    String refreshdate;

    Boolean Todaycheck = true;

    int project;
    ArrayList<Integer> projectlist;
    String et;
    ArrayList<String> namelist;
    ArrayList<String> polist;
    ArrayList<String> s_timelist;
    ArrayList<String> compar_starttime;
    ArrayList<String> e_timelist;
    ArrayList<String> compar_endtime;

    LinearLayout infotime;

    ScrollView scv;

    LinearLayout re_view;
    FrameLayout frm;
    String username;
    String idvalue;
    String cutstarttime;
    String cutendtime;
    ArrayList<String> placelist;
    ArrayList<String> prnamelist;
    ArrayList<String> prnamelist2;
    ArrayList<String> reprnamelist;

    String selectedDateStr;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String p_place;
    String Todaydate;

    String project_name;
    String plusdate;
    String minusdate;
    String arraymdate;
    int INDEX_ID;

    Calendar cal;
    ArrayList<String> minusdatelist;


    Boolean isExistdata = false;

    HorizontalCalendar.Builder builder = null;
    int height;
    int width;
    public ScheduleFragment(){

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIdvalue(String idvalue) {
        this.idvalue = idvalue;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        new loading().execute();

        minusdatelist = new ArrayList<>();
        height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        final View view = inflater.inflate(R.layout.interview_schedule,container,false);

        in1 = new AddCookiesInterceptor(getActivity());
        httpClient = new OkHttpClient.Builder().addInterceptor(in1)
                .build();

        mSwipeRefreshLayout  = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
//        Bundle extras = getArguments();
//        username = extras.getString("NAME");
//        idvalue = extras.getString("id");

//        Intent intent = getIntent();
//        username = intent.getStringExtra("NAME");
//        idvalue = intent.getStringExtra("id");
//        re_view = (LinearLayout) view.findViewById(R.id.re_view);

//        listView = (ListView) findViewById(R.id.listview_schedule);

        start_time = (TextView) view.findViewById(R.id.in_time);
        finish_time = (TextView) view.findViewById(R.id.end_time);
        interview_place = (TextView) view.findViewById(R.id.inter_place);
        s_name = (TextView) view.findViewById(R.id.s_name);
        s_position = (TextView) view.findViewById(R.id.s_position);
        infotime = (LinearLayout) view.findViewById(R.id.info_time);
        builder = new HorizontalCalendar.Builder(view, R.id.calendarView);
//        frm = (FrameLayout) findViewById(R.id.frame);

        scv = (ScrollView) view.findViewById(R.id.mainscroll);



        s_timelist = new ArrayList<>();
//        ArrayList<ScheduleVO> sList = new ArrayList<>();
        namelist = new ArrayList<>();
        polist = new ArrayList<>();
        contactList = new ArrayList<>();
        compar_starttime = new ArrayList<>();
        e_timelist = new ArrayList<>();
        compar_endtime = new ArrayList<>();
        prnamelist = new ArrayList<>();
        prnamelist2 = new ArrayList<>();

        reprnamelist = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        /* start 2 months ago from now */
        final Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -2);

        /* end after 2 months from now */
         final Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        // Default Date set to Today.
         final Calendar defaultSelectedDate = Calendar.getInstance();
         Log.d(TAG, String.valueOf(defaultSelectedDate));

        for(int c= 1; c<=7; c++) {
            cal  = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -c);
            arraymdate = format.format(cal.getTime());
            minusdatelist.add(arraymdate);

        }
//          for(int b=0; b<minusdatelist.size();b++){
//              minusdate = minusdatelist.get(b);
//
//              retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
//                      .client(httpClient)
//                      .build();
//              apiService = retrofit.create(ApiService.class);
//
//              final HashMap<String, String> Schedule = new HashMap<>();
//              Schedule.put("USER_ID", idvalue);
//              Schedule.put("INT_DATE", minusdate);
//
//              final int finalB = b;
//
//              apiService.Schedule(Schedule).enqueue(new Callback<ResponseBody>() {
//                  @Override
//                  public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                      try {
//                          String result = response.body().string();
//
//                          if (!result.equals("[]")) {
//                              isExistdata = true;
//
//
//                          } else  {
//                              isExistdata = false;
//
//                          }
//
//                      if(isExistdata = true){
//
//                      }
//
//                      } catch (IOException e) {
//                          e.printStackTrace();
//                      }
//                      catch (NullPointerException e) {
//                          e.printStackTrace();
//                          Toast.makeText(getActivity(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
//                      }
//
//                  }
//
//                  @Override
//                  public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                      final ErrorDialog errorDialog = new ErrorDialog(getActivity());
//                      errorDialog.setErrorDialogListener(new ErrorDialog.ErrorDialogListener() {
//                          @Override
//                          public void checkClick() {
//                              errorDialog.cancel();
//
//                          }
//                      });
//                      errorDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                      errorDialog.show();
//                  }
//              });
//          }


        Todaydate = format.format(System.currentTimeMillis());

        horizontalCalendar = builder
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .colorTextMiddle(Color.LTGRAY, Color.parseColor("#ffd54f"))
                .end()
                .defaultSelectedDate(defaultSelectedDate)
                .addEvents(new CalendarEventsPredicate() {

                    @Override
                    public List<CalendarEvent> events(final Calendar date) {
                        final List<CalendarEvent> events = new ArrayList<>();
                        for(int b=0; b<minusdatelist.size();b++){
                            minusdate = minusdatelist.get(b);

                            retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                                    .client(httpClient)
                                    .build();
                            apiService = retrofit.create(ApiService.class);

                            final HashMap<String, String> Schedule = new HashMap<>();
                            Schedule.put("USER_ID", idvalue);
                            Schedule.put("INT_DATE", minusdate);

                            final int finalB = b;

                            apiService.Schedule(Schedule).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    try {
                                        String result = response.body().string();

//                                        LinearLayout.LayoutParams viewlayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                                height);
//
//
//                                        if (!result.equals("[]")) {
//
//                                        } else  {
//
//                                        }
//
//                                        if(isExistdata = true){
//
//                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    catch (NullPointerException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
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
                        }



                        return events;
                    }
                })
                .build();


        Log.d(TAG, Todaydate);
        Log.i("Default Date", DateFormat.format("yyyy-MM-dd", defaultSelectedDate).toString());

        infotime.removeAllViews();

        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                .client(httpClient)
                .build();
        apiService = retrofit.create(ApiService.class);

        final HashMap<String, String> Schedule = new HashMap<>();
        Schedule.put("USER_ID", idvalue);
        Schedule.put("INT_DATE", Todaydate);

        apiService.Schedule(Schedule).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        re_view.setVisibility(View.VISIBLE);

                s_timelist.clear();
                compar_starttime.clear();
                e_timelist.clear();
                compar_endtime.clear();
                placelist = new ArrayList<>();

                try {
                    String result = response.body().string();

                    if (!result.equals("[]")) {
                        isExistdata = true;

                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            starttime = c.getString("START_TIME");
                            cutstarttime = starttime.substring(0, 5);
                            endtime = c.getString("END_TIME");
                            cutendtime = endtime.substring(0, 5);
                            s_place = c.getString("PLACE");
                            int_date = c.getString("INT_DATE");


                            compar_starttime.add(cutstarttime);
                            compar_endtime.add(cutendtime);
                            placelist.add(s_place);

                            for (int b = 0; b < compar_starttime.size(); b++) {
                                if (!s_timelist.contains(compar_starttime.get(b))) {
                                    s_timelist.add(compar_starttime.get(b));
                                }
                            }
                            for (int g = 0; g < compar_endtime.size(); g++) {
                                if (!e_timelist.contains(compar_endtime.get(g))) {
                                    e_timelist.add(compar_endtime.get(g));
                                }
                            }
                        }
                    } else  {
                        LinearLayout.LayoutParams nottotal = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                       TextView notdata = new TextView(getActivity());
                        notdata.setText("면접일정이 없습니다. ");
                        notdata.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        infotime.addView(notdata,nottotal);
                        TextView notdata2 = new TextView(getActivity());
                        notdata2.setText("다른 일정을 확인해보세요.");
                        notdata2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        infotime.addView(notdata2,nottotal);
                    }

                    for (int j = 0; j < s_timelist.size(); j++) {
                        LinearLayout total = new LinearLayout(getActivity());
                        total.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout ori = new LinearLayout(getActivity());
                        ori.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout hori = new LinearLayout(getActivity());
                        hori.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout info = new LinearLayout(getActivity());
                        info.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout twohori = new LinearLayout(getActivity());
                        twohori.setOrientation(LinearLayout.HORIZONTAL);


                        LinearLayout.LayoutParams totalparm = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams bigmargin = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams placemargin = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams smallmargin = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams viewlayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                               height);

                        layoutParams.setMargins(30, 40, 0, 0);
                        bigmargin.setMargins(40, 40, 0, 0);
                        smallmargin.setMargins(20, 20, 0, 0);
                        placemargin.setMargins(30, 40, 0, 0);

                        String st = s_timelist.get(j);
                        et = e_timelist.get(j);
                        Log.d(TAG, String.valueOf(placelist));
                        p_place = placelist.get(j);

                        infotime.addView(total, totalparm);

                        total.addView(hori);

                        start_time = new TextView(getActivity());
                        start_time.setText(st);
                        start_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
                        hori.addView(start_time, layoutParams);
                        View time = new View(getActivity());
                        time.setBackground(getResources().getDrawable(R.drawable.background));
                        total.addView(time,viewlayout);
//                        hori.setBackgroundResource(R.drawable.rounded_edittext);

                        finish_time = new TextView(getActivity());
                        finish_time.setText(" ~ " + et + " 까지");
                        finish_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        hori.addView(ori);
                        ori.addView(finish_time, bigmargin);
                        interview_place = new TextView(getActivity());
                        ori.addView(interview_place, placemargin);

                        interview_place.setText("면접장소 : " + p_place);
                        interview_place.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                        projectlist = new ArrayList<>();
                        JSONArray comarray = new JSONArray(result);
                        for (int z = 0; z < comarray.length(); z++) {
                            JSONObject comp = comarray.getJSONObject(z);

                            name_sch = comp.getString("NAME");
                            pot_sch = comp.getString("POSITION");
                            strt = comp.getString("START_TIME");
                            INDEX_ID = comp.getInt("INDEX_ID");
                            project = comp.getInt("PROJECT_INDEX_ID");
                            project_name = comp.getString("PROJECT_NAME");
                            String cutstr = strt.substring(0, 5);
                            projectlist.add(project);
                            prnamelist.add(project_name);
                            if (st.equals(cutstr)) {
                                s_name = new TextView(getActivity());
                                s_name.setId(z);
                                s_name.setText(name_sch + " " + pot_sch + " " + " " + "⇨");
                                final int finalZ = z;

                                s_name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent schduleinfo = new Intent(getActivity(),PersonInfoActivity.class);
                                        schduleinfo.putExtra("id", idvalue);
                                        schduleinfo.putExtra("INDEX_ID", projectlist.get(finalZ));
                                        schduleinfo.putExtra("NAME",prnamelist.get(finalZ));
                                        getActivity().startActivity(schduleinfo);

                                    }
                                });
                                s_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                                total.addView(s_name, layoutParams);
                                View infoview = new View(getActivity());
                                infoview.setBackground(getResources().getDrawable(R.drawable.background));
                                total.addView(infoview,viewlayout);
//                                s_position = new TextView(ScheduleActivity.this);
//                                s_position.setText(pot_sch);
//                                s_position.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
//                                total.addView(s_position, bigmargin);

                            }

                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
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

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Todaycheck = false;
               selectedDateStr = DateFormat.format("yyyy-MM-dd", date).toString();
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);

                infotime.removeAllViews();
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                        .client(httpClient)
                        .build();
                apiService = retrofit.create(ApiService.class);

                final HashMap<String, String> Schedule = new HashMap<>();
                Schedule.put("USER_ID", idvalue);
                Schedule.put("INT_DATE", selectedDateStr);

                apiService.Schedule(Schedule).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        re_view.setVisibility(View.VISIBLE);
                        Calendar cal = Calendar.getInstance();
                        s_timelist.clear();
                        compar_starttime.clear();
                        e_timelist.clear();
                        compar_endtime.clear();
                        placelist = new ArrayList<>();
                        try {
                            String result = response.body().string();

                            if (!result.equals("[]")) {
                                isExistdata = true;

                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);

                                    starttime = c.getString("START_TIME");
                                    cutstarttime = starttime.substring(0, 5);
                                    endtime = c.getString("END_TIME");
                                    cutendtime = endtime.substring(0, 5);
                                    s_place = c.getString("PLACE");
                                    int_date = c.getString("INT_DATE");

                                    compar_starttime.add(cutstarttime);
                                    compar_endtime.add(cutendtime);
                                    placelist.add(s_place);
                                    for (int b = 0; b < compar_starttime.size(); b++) {
                                        if (!s_timelist.contains(compar_starttime.get(b))) {
                                            s_timelist.add(compar_starttime.get(b));
                                        }
                                    }
                                    for (int g = 0; g < compar_endtime.size(); g++) {
                                        if (!e_timelist.contains(compar_endtime.get(g))) {
                                            e_timelist.add(compar_endtime.get(g));
                                        }
                                    }
                                }
                            } else{
                                LinearLayout.LayoutParams nottotal = new LinearLayout.LayoutParams
                                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                TextView notdata = new TextView(getActivity());
                                notdata.setText("면접일정이 없습니다. ");
                                notdata.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                                infotime.addView(notdata,nottotal);
                                TextView notdata2 = new TextView(getActivity());
                                notdata2.setText("다른 일정을 확인해보세요.");
                                notdata2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                                infotime.addView(notdata2,nottotal);
                            }
                            for (int j = 0; j < s_timelist.size(); j++) {
                                LinearLayout total = new LinearLayout(getActivity());
                                total.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout ori = new LinearLayout(getActivity());
                                ori.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout hori = new LinearLayout(getActivity());
                                hori.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout twohori = new LinearLayout(getActivity());
                                twohori.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout verti = new LinearLayout(getActivity());
                                verti.setOrientation(LinearLayout.VERTICAL);

                                LinearLayout.LayoutParams totalparm = new LinearLayout.LayoutParams
                                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams bigmargin = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams placemargin = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams smallmargin = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams viewlayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        height);


                                String st = s_timelist.get(j);
                                et = e_timelist.get(j);
                                p_place = placelist.get(j);
                                layoutParams.setMargins(30, 20, 0, 0);
                                bigmargin.setMargins(40, 20, 0, 0);
                                smallmargin.setMargins(20, 20, 0, 0);
                                placemargin.setMargins(30, 20, 0, 0);
                                infotime.addView(total, totalparm);


                                total.addView(hori);


                                start_time = new TextView(getActivity());
                                start_time.setText(st);
                                start_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
                                hori.addView(start_time, layoutParams);
                                View time = new View(getActivity());
                                time.setBackground(getResources().getDrawable(R.drawable.background));
                                total.addView(time,viewlayout);
//                                hori.setBackgroundResource(R.drawable.rounded_edittext);

                                finish_time = new TextView(getActivity());
                                finish_time.setText(" ~ " + et + " 까지");
                                finish_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                                hori.addView(ori);
                                ori.addView(finish_time, bigmargin);
                                interview_place = new TextView(getActivity());
                                interview_place.setText("면접장소 : " + p_place);
                                interview_place.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                                ori.addView(interview_place, placemargin);

                                projectlist = new ArrayList<>();
                                JSONArray comarray = new JSONArray(result);
                                for (int z = 0; z < comarray.length(); z++) {
                                    JSONObject comp = comarray.getJSONObject(z);

                                    name_sch = comp.getString("NAME");
                                    pot_sch = comp.getString("POSITION");
                                    strt = comp.getString("START_TIME");
                                    project = comp.getInt("PROJECT_INDEX_ID");
                                    project_name = comp.getString("PROJECT_NAME");
                                    String cutstr = strt.substring(0, 5);
                                    projectlist.add(project);
                                    prnamelist2.add(project_name);
                                    if (st.equals(cutstr)) {
                                        s_name = new TextView(getActivity());
                                        s_name.setId(z);
                                        s_name.setText(name_sch + " " + pot_sch + " " + " " + "⇨");
                                        final int finalZ = z;
                                        s_name.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent schduleinfo = new Intent(getActivity(), PersonInfoActivity.class);
                                                schduleinfo.putExtra("id", idvalue);
                                                schduleinfo.putExtra("INDEX_ID", projectlist.get(finalZ));
                                                schduleinfo.putExtra("NAME",prnamelist2.get(finalZ));
                                                getActivity().startActivity(schduleinfo);

                                            }
                                        });
                                        s_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                                        total.addView(s_name, layoutParams);
                                        View infoview = new View(getActivity());
                                        infoview.setBackground(getResources().getDrawable(R.drawable.background));
                                        total.addView(infoview,viewlayout);
                                    }






                                }
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        catch (NullPointerException e) {
//                            e.printStackTrace();
//
//                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
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


            }

        });

        return view;
    }

    @Override
    public void onRefresh() {

        if(Todaycheck == true){
           refreshdate = Todaydate;
        } else{
            refreshdate = selectedDateStr;
        }

        infotime.removeAllViews();
        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL)
                .client(httpClient)
                .build();
        apiService = retrofit.create(ApiService.class);

        final HashMap<String, String> Schedule = new HashMap<>();
        Schedule.put("USER_ID", idvalue);
        Schedule.put("INT_DATE", refreshdate);

        apiService.Schedule(Schedule).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        re_view.setVisibility(View.VISIBLE);
                Calendar cal = Calendar.getInstance();
                s_timelist.clear();
                compar_starttime.clear();
                e_timelist.clear();
                compar_endtime.clear();
                placelist = new ArrayList<>();
                try {
                    String result = response.body().string();

                    if (!result.equals("[]")) {

                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);


                            starttime = c.getString("START_TIME");
                            cutstarttime = starttime.substring(0, 5);
                            endtime = c.getString("END_TIME");
                            cutendtime = endtime.substring(0, 5);
                            s_place = c.getString("PLACE");
                            int_date = c.getString("INT_DATE");


                            compar_starttime.add(cutstarttime);
                            compar_endtime.add(cutendtime);
                            placelist.add(s_place);
                            for (int b = 0; b < compar_starttime.size(); b++) {
                                if (!s_timelist.contains(compar_starttime.get(b))) {
                                    s_timelist.add(compar_starttime.get(b));
                                }
                            }
                            for (int g = 0; g < compar_endtime.size(); g++) {
                                if (!e_timelist.contains(compar_endtime.get(g))) {
                                    e_timelist.add(compar_endtime.get(g));
                                }
                            }
                        }
                    } else{
                        LinearLayout.LayoutParams nottotal = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                        TextView notdata = new TextView(getActivity());
                        notdata.setText("면접일정이 없습니다. ");
                        notdata.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        infotime.addView(notdata,nottotal);
                        TextView notdata2 = new TextView(getActivity());
                        notdata2.setText("다른 일정을 확인해보세요.");
                        notdata2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        infotime.addView(notdata2,nottotal);
                    }
                    for (int j = 0; j < s_timelist.size(); j++) {
                        LinearLayout total = new LinearLayout(getActivity());
                        total.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout ori = new LinearLayout(getActivity());
                        ori.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout hori = new LinearLayout(getActivity());
                        hori.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout twohori = new LinearLayout(getActivity());
                        twohori.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout verti = new LinearLayout(getActivity());
                        verti.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams totalparm = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams bigmargin = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams placemargin = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams smallmargin = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams viewlayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                height);


                        String st = s_timelist.get(j);
                        et = e_timelist.get(j);
                        p_place = placelist.get(j);
                        layoutParams.setMargins(30, 20, 0, 0);
                        bigmargin.setMargins(40, 20, 0, 0);
                        smallmargin.setMargins(20, 20, 0, 0);
                        placemargin.setMargins(30, 20, 0, 0);
                        infotime.addView(total, totalparm);


                        total.addView(hori);


                        start_time = new TextView(getActivity());
                        start_time.setText(st);
                        start_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
                        hori.addView(start_time, layoutParams);
                        View time = new View(getActivity());
                        time.setBackground(getResources().getDrawable(R.drawable.background));
                        total.addView(time,viewlayout);
//                        hori.setBackgroundResource(R.drawable.rounded_edittext);

                        finish_time = new TextView(getActivity());
                        finish_time.setText(" ~ " + et + " 까지");
                        finish_time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        hori.addView(ori);
                        ori.addView(finish_time, bigmargin);
                        interview_place = new TextView(getActivity());
                        interview_place.setText("면접장소 : " + p_place);
                        interview_place.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                        ori.addView(interview_place, placemargin);

                        projectlist = new ArrayList<>();
                        JSONArray comarray = new JSONArray(result);
                        for (int z = 0; z < comarray.length(); z++) {
                            JSONObject comp = comarray.getJSONObject(z);

                            name_sch = comp.getString("NAME");
                            pot_sch = comp.getString("POSITION");
                            strt = comp.getString("START_TIME");
                            project = comp.getInt("PROJECT_INDEX_ID");
                            project_name = comp.getString("PROJECT_NAME");
                            String cutstr = strt.substring(0, 5);
                            projectlist.add(project);
                            reprnamelist .add(project_name);
                            if (st.equals(cutstr)) {
                                s_name = new TextView(getActivity());
                                s_name.setId(z);
                                s_name.setText(name_sch + " " + pot_sch + " " + " " + "⇨");
                                final int finalZ = z;
                                s_name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent schduleinfo = new Intent(getActivity(), PersonInfoActivity.class);
                                        schduleinfo.putExtra("id", idvalue);
                                        schduleinfo.putExtra("INDEX_ID", projectlist.get(finalZ));
                                        schduleinfo.putExtra("NAME", reprnamelist.get(finalZ));

                                        getActivity().startActivity(schduleinfo);

                                    }
                                });
                                s_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                                total.addView(s_name, layoutParams);
                                View infoview = new View(getActivity());
                                infoview.setBackground(getResources().getDrawable(R.drawable.background));
                                total.addView(infoview,viewlayout);

                            }

                        }
                    }


//
//                            if (result.equals("[]")) {
//                               infotime.removeAllViews();
//
//                            }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
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

    private class loading extends AsyncTask<Void, Void, Void>{
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
