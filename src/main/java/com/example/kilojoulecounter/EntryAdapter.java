package com.example.kilojoulecounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {

    EntryBook obj = new EntryBook();
    ArrayList<String> entrylist = obj.getEntry();
    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView dateView;
        public TextView kjView;

        public ViewHolder(View entryView, final onItemClickListener listener){

            super(entryView);

            dateView = (TextView) entryView.findViewById(R.id.selected_date);
            kjView = (TextView) entryView.findViewById(R.id.textView3);

            entryView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        //inflate custom layout
        View contactView = inflater.inflate(R.layout.entry_item, parent, false);

        //return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView, mListener);
        return viewHolder;
    }

    //involves populating data into the item through the holder
    @Override
    public void onBindViewHolder(EntryAdapter.ViewHolder viewHolder, int position){

        String entry = entrylist.get(position);
        String[] items = entry.split(",");
        String date = items[0] + "," + items[1];
        String netcal = items[4];

        TextView textView = viewHolder.dateView;
        textView.setText(date);

        TextView textView1 = viewHolder.kjView;
        textView1.setText(netcal);
    }

    @Override
    public int getItemCount(){
        return obj.size();
    }
}
