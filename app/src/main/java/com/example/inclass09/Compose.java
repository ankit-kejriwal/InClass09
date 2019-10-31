package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Compose extends AppCompatActivity {

    Spinner spinner;
    EditText editTextsubject;
    EditText editTextbody;
    Button buttonSend;
    Button buttoncancel;
    String token;
    String userid ="";
    String userName;
    Map<String,String> users = new LinkedHashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        spinner = findViewById(R.id.spinner);
        editTextsubject = findViewById(R.id.editTextSubj);
        editTextbody = findViewById(R.id.editTextBody);
        buttonSend = findViewById(R.id.buttonSend);
        buttoncancel = findViewById(R.id.buttonCancel);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Compose.this);
        token = sharedPreferences.getString("token", null);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = userid;
                String subj =editTextsubject.getText().toString();
                String body =editTextbody.getText().toString();
                if(id.equals("user")) {
                    Toast.makeText(Compose.this, "Please select User", Toast.LENGTH_SHORT).show();
                }
                else if(subj.equals(""))
                {
                    editTextsubject.setError("Enter subject. It cannot be blank");

                } else if(body.equals("")){
                    editTextbody.setError("Enter message. It cannot be empty");
                }else {
                    new getAsync().execute(id,subj,body);
                }
            }
        });
        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getUserData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userid = (String) users.keySet().toArray()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private class getAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            final OkHttpClient client = new OkHttpClient();
            try { RequestBody formBody = new FormBody.Builder()
                    .add("receiver_id", strings[0])
                    .add("subject", strings[1])
                    .add("message", strings[2])
                    .build();
                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/add")
                        .addHeader("Authorization","BEARER "+token)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                else{
                    String  obj = response.body().string();
                    return obj;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Compose.this, "Email sent", Toast.LENGTH_SHORT).show();
            Intent returnIntent = getIntent();
            setResult(RESULT_OK,returnIntent);
            finish();
        }
    }
    public void getUserData(){
        users.clear();
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/users")
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
                    JSONArray usersArray = Jobject.getJSONArray("users");
                    users.put("user","Select User");
                    for(int i=0;i<usersArray.length();i++){
                        JSONObject emailJSON = usersArray.getJSONObject(i);
                        String fname = emailJSON.getString("fname");
                        String lname = emailJSON.getString("lname");
                        String id = emailJSON.getString("id");
                        users.put(id,fname+" "+lname);
                    }
                    final List userList = new ArrayList(users.values());
                    Compose.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Compose.this,android.R.layout.simple_spinner_item, userList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
