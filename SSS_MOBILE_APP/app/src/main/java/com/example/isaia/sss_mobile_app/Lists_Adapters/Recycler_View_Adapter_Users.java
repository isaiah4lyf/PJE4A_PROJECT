package com.example.isaia.sss_mobile_app.Lists_Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.R;

import java.util.List;

public class Recycler_View_Adapter_Users extends RecyclerView.Adapter<Recycler_View_Adapter_Users.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private Recycler_View_Adapter_Users thisAdapter;

    // data is passed into the constructor
    public Recycler_View_Adapter_Users(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        thisAdapter = this;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_users, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView remove_User;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.user_name);
            remove_User = itemView.findViewById(R.id.remove);
            remove_User.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DBHelper mydb = new DBHelper(context);
                    Toast.makeText(context,mData.get(getAdapterPosition())+ " was successfully removed from list!", Toast.LENGTH_LONG).show();
                    mydb.Remove_User(mData.get(getAdapterPosition()));
                    mData.remove(getAdapterPosition());
                    thisAdapter.notifyDataSetChanged();
            }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}