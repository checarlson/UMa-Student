package com.ldd.e_noticeboarduma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultsRVAdapter extends RecyclerView.Adapter<ResultsRVAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Results> resultsArrayList;

    // creating a constructor class.
    public ResultsRVAdapter(Context context, ArrayList<Results> resultsArrayList) {
        this.context = context;
        this.resultsArrayList = resultsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.results_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // setting data to our text views from our modal class.
        final Results results = resultsArrayList.get(position);
        holder.sn.setText(String.valueOf(results.getSn()));
        holder.code.setText(results.getCourseCode());
        holder.title.setText(results.getCourseTitle());
        holder.cc.setText(results.getCc());
        holder.ee.setText(results.getEe());
        holder.tpe.setText(results.getTpe());
        holder.total.setText(results.getTotal());
        holder.moy.setText(results.getMoyen());
        holder.dec.setText(results.getDecision());
        holder.men.setText(results.getMension());

    }

    @Override
    public int getItemCount() {
        return resultsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView sn, code, title, cc, ee, tpe, total, moy, dec, men;
//        private AdView adView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            sn = itemView.findViewById(R.id.sn);
            code = itemView.findViewById(R.id.code);
            title = itemView.findViewById(R.id.title);
            cc = itemView.findViewById(R.id.cc);
            ee = itemView.findViewById(R.id.ee);
            tpe = itemView.findViewById(R.id.tpe);
            total = itemView.findViewById(R.id.total);
            moy = itemView.findViewById(R.id.moy);
            dec = itemView.findViewById(R.id.dec);
            men = itemView.findViewById(R.id.men);
        }
    }
}
