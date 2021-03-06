package com.example.englishtester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.englishtester.adapter.ViewPagerAdapter;
import com.example.englishtester.model.Answer;
import com.example.englishtester.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.englishtester.LoginActivity.urlGetDataB1;
import static com.example.englishtester.MainActivity.answerArrayList;
import static com.example.englishtester.MainActivity.questionArrayList;
import static com.example.englishtester.MainActivity.TYPE_QUESTION;


public class TimeActivity extends AppCompatActivity {
    Button btnStart, btnListQues, btnSubmit;
    TextView txtTimer;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private long timeSeconds = 900000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        setWidget();

        Intent intent = getIntent();
        TYPE_QUESTION = intent.getStringExtra("typeQuestion");
        if (questionArrayList.isEmpty()) {
            getDataB1(urlGetDataB1);
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setOffscreenPageLimit(1);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setCurrentItem(0);
                btnStart.setVisibility(View.GONE);
                btnListQues.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                startTimer();
            }
        });

        btnListQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogListQuestion();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultSubmit();
            }
        });
    }

    void resultSubmit(){
        int right = 0;
        try {
            for (int i=0; i< questionArrayList.size(); i++){
                Question question = questionArrayList.get(i);
                Answer answer = answerArrayList.get(i);
                Toast.makeText(TimeActivity.this, question.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(TimeActivity.this, answer.toString(), Toast.LENGTH_SHORT).show();

                if (question.getRightAnswer().equals(answer.getAnswer())){
                    right += 1;
                }
            }
        }catch (Exception e){
            Toast.makeText(TimeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(TimeActivity.this, Integer.toString(right), Toast.LENGTH_SHORT).show();
    }

    void startTimer(){
        new CountDownTimer(timeSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeSeconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) timeSeconds / 60000;
        int seconds = (int) timeSeconds % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "" + minutes + ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        txtTimer.setText(timeLeftText);
    }

    void setWidget() {
        btnStart = findViewById(R.id.btn_start);
        viewPager = findViewById(R.id.view_pager);
        btnListQues = findViewById(R.id.btn_list_ques);
        btnSubmit = findViewById(R.id.btn_submit);
        txtTimer = findViewById(R.id.tvTimer);
    }

    void getDataB1(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(TimeActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cauhoi");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                questionArrayList.add(new Question(
                                        jsonObject.getString("STT"),
                                        jsonObject.getString("cauhoi"),
                                        jsonObject.getString("theloai"),
                                        jsonObject.getString("dapanA"),
                                        jsonObject.getString("dapanB"),
                                        jsonObject.getString("dapanC"),
                                        jsonObject.getString("dapanD"),
                                        jsonObject.getString("ketqua")
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TimeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", TYPE_QUESTION);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    void dialogListQuestion(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_list_question);
        Button btnQues1 = (Button)dialog.findViewById(R.id.btn_1);
        setDialogListQuestionButton(btnQues1, 0, dialog);
        Button btnQues2 = (Button)dialog.findViewById(R.id.btn_2);
        setDialogListQuestionButton(btnQues2, 1, dialog);
        Button btnQues3 = (Button)dialog.findViewById(R.id.btn_3);
        setDialogListQuestionButton(btnQues3, 2, dialog);
        Button btnQues4 = (Button)dialog.findViewById(R.id.btn_4);
        setDialogListQuestionButton(btnQues4, 3, dialog);
        Button btnQues5 = (Button)dialog.findViewById(R.id.btn_5);
        setDialogListQuestionButton(btnQues5, 4, dialog);
        Button btnQues6 = (Button)dialog.findViewById(R.id.btn_6);
        setDialogListQuestionButton(btnQues6, 5, dialog);
        Button btnQues7 = (Button)dialog.findViewById(R.id.btn_7);
        setDialogListQuestionButton(btnQues7, 6, dialog);
        Button btnQues8 = (Button)dialog.findViewById(R.id.btn_8);
        setDialogListQuestionButton(btnQues8, 7, dialog);
        Button btnQues9 = (Button)dialog.findViewById(R.id.btn_9);
        setDialogListQuestionButton(btnQues9, 8, dialog);
        Button btnQues10 = (Button)dialog.findViewById(R.id.btn_10);
        setDialogListQuestionButton(btnQues10, 9, dialog);
        dialog.show();
    }

    void setDialogListQuestionButton(View view, final int pos, final Dialog dia){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setOffscreenPageLimit(1);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                viewPager.setCurrentItem(pos);
                btnStart.setVisibility(View.GONE);
                dia.cancel();
            }
        });
    }
}