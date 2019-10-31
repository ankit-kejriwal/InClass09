package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayMail extends AppCompatActivity {

    TextView textViewname;
    TextView textViewSubj;
    EditText editTextMessage;
    TextView textViewcreated;
    Button buttonclose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);
        Email email= (Email) getIntent().getSerializableExtra("email");
        Log.d("demo",email.toString());
        textViewname = findViewById(R.id.textViewSenderName);
        textViewSubj = findViewById(R.id.textViewSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        textViewcreated = findViewById(R.id.textViewCreatedAt);
        buttonclose = findViewById(R.id.buttonClose);
        textViewname.setText(email.getSender_fname()+" "+email.getSender_lname());
        textViewSubj.setText(email.getSubject());
        editTextMessage.setText(email.getMessage());
        textViewcreated.setText(email.getCreated_at());
        buttonclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
