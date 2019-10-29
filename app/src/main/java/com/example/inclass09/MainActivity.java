package com.example.inclass09;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int SIGN_UP_REQ = 123;
    EditText editTextemail;
    EditText editTextpassword;
    Button buttonlogin;
    Button buttonsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextemail = findViewById(R.id.editTextLoginEmail);
        editTextpassword = findViewById(R.id.editTextLoginPassword);
        buttonlogin = findViewById(R.id.buttonLogin);
        buttonsignup = findViewById(R.id.buttonmainSignup);
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivityForResult(intent,SIGN_UP_REQ);
            }
        });
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextemail.getText().toString();
                String password = editTextpassword.getText().toString();
                new getAsync().execute(email,password);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == SIGN_UP_REQ) {
            if(resultCode == RESULT_OK){
                if(data.getStringExtra("token") == null){
                    System.out.println("Not found");
                } else {
                    String token =  data.getStringExtra("token");
                    Log.d("demo",token);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,Inbox.class);
                    startActivity(intent);

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class getAsync extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            final OkHttpClient client = new OkHttpClient();
            try { RequestBody formBody = new FormBody.Builder()
                    .add("email", strings[0])
                    .add("password", strings[1])
                    .build();
                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login")
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
            String token="", fname="", lname="";
            if(s!=null){
                JSONObject Jobject = null;
                try {
                    Jobject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    token = Jobject.getString("token");
                    fname = Jobject.getString("user_fname");
                    lname = Jobject.getString("user_lname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "User logged in", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", token);
                editor.commit();
                Intent intent = new Intent(MainActivity.this,Inbox.class);
                intent.putExtra("fname",fname);
                intent.putExtra("lname",lname);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Error in login", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

}
