package com.ldd.e_noticeboarduma;

import static com.parse.Parse.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class NoticeRVAdapter extends RecyclerView.Adapter<NoticeRVAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Notice> noticeArrayList;
    private ParseUser user;

    // creating a constructor class.
    public NoticeRVAdapter(Context context, ArrayList<Notice> noticeArrayList) {
        this.context = context;
        this.noticeArrayList = noticeArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // setting data to our text views from our modal class.
        final Notice notices = noticeArrayList.get(position);
        holder.image.setImageBitmap(notices.getPostImage());
        holder.description.setText(notices.getPostDescription());


        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), AddNotice.class);
                intent.putExtra("des", notices.getPostDescription());
//                intent.putExtra("image", notices.getPostImage());
                Utility.bitImage = notices.getPostImage();
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

        //delete item on recycler view
        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = ParseUser.getCurrentUser();
                //getting league name from user
//                league = user.getString("League");
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Notice");
//                query.whereEqualTo("PostImage", notices.getPostImage());
                query.whereEqualTo("PostDescription", notices.getPostDescription());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context.getApplicationContext(), AdminNoticeActivity.class));
                                    ((Activity)context).finish();
                                } else {
                                    e.printStackTrace();
                                    Toast.makeText(context.getApplicationContext(), " " + e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final ImageView image;
        private final TextView description;
        private final Button del_btn;
        private final Button edit_btn;
//        private AdView adView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            image = itemView.findViewById(R.id.league_logo);
            description = itemView.findViewById(R.id.league_name);
            del_btn = itemView.findViewById(R.id.btnDel);
            edit_btn = itemView.findViewById(R.id.edit);

        }
    }
}
