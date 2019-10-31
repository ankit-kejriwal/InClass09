package com.example.inclass09;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {

    EditText editTextfname;
    EditText editTextlname;
    EditText editTextemail;
    EditText editTextcpassword;
    EditText editTextrpassword;
    Button buttonsignup;
    Button buttoncancel;
    String fname;
    String lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextfname = findViewById(R.id.editTextfname);
        editTextlname = findViewById(R.id.editTextLastName);
        editTextemail = findViewById(R.id.editTextemail);
        editTextcpassword = findViewById(R.id.editTextcpassword);
        editTextrpassword = findViewById(R.id.editTextrpassword);
        buttonsignup = findViewById(R.id.buttonsignup);
        buttoncancel = findViewById(R.id.buttoncancel);
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = editTextfname.getText().toString();
                String lname = editTextlname.getText().toString();
                String email = editTextemail.getText().toString();
                if(editTextcpassword.getText().toString().equals(editTextrpassword.getText().toString())){
                    String password = editTextcpassword.getText().toString();
                    new getAsync().execute(fname,lname,email,password);
                } else {
                    Toast.makeText(SignUp.this, "Password do not match", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private class getAsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            final OkHttpClient client = new OkHttpClient();
                try { RequestBody formBody = new FormBody.Builder()
                        .add("fname", strings[0])
                        .add("lname", strings[1])
                        .add("email", strings[2])
                        .add("password", strings[3])
                        .build();
                    Request request = new Request.Builder()
                            .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String  obj = response.body().string();
                    JSONObject Jobject = new JSONObject(obj);
                    String token = Jobject.getString("token");
                    fname = Jobject.getString("user_fname");
                    lname = Jobject.getString("user_lname");

                    return token;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.length() >0){
                Toast.makeText(SignUp.this, "User created", Toast.LENGTH_SHORT).show();
                Intent returnIntent = getIntent();
                returnIntent.putExtra("token",s);
                returnIntent.putExtra("fname",fname);
                returnIntent.putExtra("lname",lname);
                setResult(RESULT_OK,returnIntent);
                finish();
            } else {
                Toast.makeText(SignUp.this, "Error in creating user", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }
}
