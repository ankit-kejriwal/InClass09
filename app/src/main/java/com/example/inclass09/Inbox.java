package com.example.inclass09;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Inbox extends AppCompatActivity {

    private static final int COMPOSE_REQ_CODE = 999;
    TextView textViewName;
    ImageView imageViewLogout;
    ImageView imageViewCompose;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Email> result = new ArrayList<Email>();
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        String fname = getIntent().getStringExtra("fname");
        String lname = getIntent().getStringExtra("lname");
        textViewName = findViewById(R.id.textViewName);
        textViewName.setText(fname+" "+lname);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Inbox.this);
        token = sharedPreferences.getString("token", null);

        textViewName = findViewById(R.id.textViewName);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        imageViewLogout= findViewById(R.id.imageViewLogout);
        imageViewCompose = findViewById(R.id.imageViewCompose);
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().remove("token").commit();
                finish();
            }
        });
        imageViewCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inbox.this,Compose.class);
                startActivityForResult(intent,COMPOSE_REQ_CODE);
            }
        });

        getInboxdata();


    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == COMPOSE_REQ_CODE) {
            if(resultCode == RESULT_OK){
                getInboxdata();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public void getInboxdata(){
        result.clear();
        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox")
                .addHeader("Authorization","BEARER "+token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    String  obj = response.body().string();
                    JSONObject Jobject = new JSONObject(obj);
                    JSONArray messages = Jobject.getJSONArray("messages");
                    for(int i=0;i<messages.length();i++){
                        JSONObject emailJSON = messages.getJSONObject(i);
                        Email email = new Email();
                        email.subject = emailJSON.getString("subject");
                        email.created_at = emailJSON.getString("created_at");
                        email.sender_fname = emailJSON.getString("sender_fname");
                        email.sender_lname = emailJSON.getString("sender_lname");
                        email.message = emailJSON.getString("message");
                        email.id = emailJSON.getString("id");
                        result.add(email);
                    }
                    Inbox.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Handle UI here
                            mLayoutManager = new LinearLayoutManager(Inbox.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mAdapter =  new EmailRecyclerViewAdapter(result);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
