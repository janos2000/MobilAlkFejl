package com.example.mvm;

import android.app.ListActivity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> implements Filterable {
    private ArrayList<Bills> billsData;
    private ArrayList<Bills> billsDataAll;
    private Context context;
    private int lastPosition = -1;

    public BillAdapter(Context context, ArrayList<Bills> bills) {
        this.billsData = bills;
        this.billsDataAll = bills;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_bills, parent, false));
    }

    @Override
    public void onBindViewHolder(BillAdapter.ViewHolder holder, int position) {
        Bills currentItem = billsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return billsData.size();
    }

    private Filter billFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Bills> filteredlist = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = billsDataAll.size();
                results.values = billsDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Bills bill : billsDataAll){
                    if(bill.getName().toLowerCase().contains(filterPattern)){
                        filteredlist.add(bill);
                    }
                }
                results.count = filteredlist.size();
                results.values = filteredlist;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            billsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return billFilter;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private TextView dateText;
        private TextView priceText;
        private ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name);
            dateText  = itemView.findViewById(R.id.date);
            priceText = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);

            itemView.findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("asd", "asd");
                }
            });
        }

        public void bindTo(Bills currentItem) {
            nameText.setText(currentItem.getName());
            dateText.setText(currentItem.getDate());
            priceText.setText(currentItem.getPrice());

            Glide.with(context).load(currentItem.getImage()).into(image);

            itemView.findViewById(R.id.delete).setOnClickListener(view -> ((com.example.mvm.ListActivity)context).deleteItem(currentItem));

        }
    }
}
