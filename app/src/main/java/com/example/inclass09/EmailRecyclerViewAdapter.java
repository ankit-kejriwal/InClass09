package com.example.inclass09;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmailRecyclerViewAdapter extends RecyclerView.Adapter<EmailRecyclerViewAdapter.ViewHolder> {
    @NonNull

    ArrayList<Email> mData;

    public EmailRecyclerViewAdapter(@NonNull ArrayList<Email> mData) {
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.email_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Email email = mData.get(position);
        holder.textViewSubject.setText(email.getSubject());
        holder.textViewCreatedate.setText(email.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSubject;
        TextView textViewCreatedate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            textViewCreatedate = itemView.findViewById(R.id.textViewCreatedate);
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

        }
    }
}
