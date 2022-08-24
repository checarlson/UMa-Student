package com.ldd.e_noticeboarduma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainNoticeRVAdapter extends RecyclerView.Adapter<MainNoticeRVAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Notice> noticeArrayList;

    // creating a constructor class.
    public MainNoticeRVAdapter(Context context, ArrayList<Notice> noticeArrayList) {
        this.context = context;
        this.noticeArrayList = noticeArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.main_notice_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // setting data to our text views from our modal class.
        final Notice notices = noticeArrayList.get(position);
        holder.image.setImageBitmap(notices.getPostImage());
        holder.description.setText(notices.getPostDescription());
        holder.created.setText(String.valueOf(notices.getDate()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), DisplayNotice.class);
                Utility.bitImage = notices.getPostImage();
//                intent.putExtra("image", notices.getPostImage());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

       /* holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), DisplayNotice.class);
                Utility.bitImage = notices.getPostImage();
//                intent.putExtra("image", notices.getPostImage());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final ImageView image;
        private final TextView description, created;
        private final LinearLayout layout;
//        private AdView adView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            image = itemView.findViewById(R.id.league_logo);
            description = itemView.findViewById(R.id.league_name);
            layout = itemView.findViewById(R.id.vert);
            created = itemView.findViewById(R.id.date);
        }
    }
}
