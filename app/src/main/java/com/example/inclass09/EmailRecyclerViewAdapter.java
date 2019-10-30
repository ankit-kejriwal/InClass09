package com.example.inclass09;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class EmailRecyclerViewAdapter extends RecyclerView.Adapter<EmailRecyclerViewAdapter.ViewHolder> {
    @NonNull

    ArrayList<Email> mData;
    String token;




    public EmailRecyclerViewAdapter(@NonNull ArrayList<Email> mData, Context ctx) {
        this.mData = mData;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        this.token = sharedPreferences.getString("token", null);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.email_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Email email = mData.get(position);
        holder.textViewSubject.setText(email.getSubject());
        holder.textViewCreatedate.setText(email.getCreated_at());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSubject;
        TextView textViewCreatedate;
        ImageView imageViewDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            textViewCreatedate = itemView.findViewById(R.id.textViewCreatedate);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.d("demo",trackValue.getArtworkUrl100());
//                    Context context = view.getContext();
//                    Intent intent = new Intent(context,DisplayActivity.class);
//                    intent.putExtra("track",trackValue);
//                    context.startActivity(intent);
                }
            });

            //372
            //376

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/delete/" + removeItem(getAdapterPosition()))
                            .addHeader("Authorization","BEARER "+token)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                            }
                        }
                    });

                }
            });
        }
        public String removeItem(int position){
            Email removedEmail = mData.get(position);
            removedEmail.toString();
            String id = removedEmail.getId();
            mData.remove(position);
            notifyItemRemoved(position);
            Log.d("heyo",id);
            return id;
        }


    }
}
