package com.example.cctv_tmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cctv_tmap.Search_map.SearchEntity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private onItemClickListener mlistener = null;
    private ArrayList<SearchEntity> itemLists = new ArrayList<>();
    private RecyclerViewAdapterCallback callback;
    private double lat, longi;
    private boolean flag = true;

    public void setOnItemClickListener(RecyclerViewAdapter.onItemClickListener listener) {
        this.mlistener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int ItemPosition = position;

        if (holder instanceof CustomViewHolder) {
            CustomViewHolder viewHolder = (CustomViewHolder) holder;

            SearchEntity searchEntity = itemLists.get(position);

            viewHolder.title.setText(searchEntity.getTitle());
            viewHolder.address.setText(searchEntity.getAddress());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.Transportedit(searchEntity.getTitle());
                    setClear();
                    flag = false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public void setData(ArrayList<SearchEntity> itemLists) {
        this.itemLists = itemLists;
    }

    public void setCallback(RecyclerViewAdapterCallback callback) {
        this.callback = callback;
    }

    public void setLocation(double lat, double longi) {
        this.lat = lat;
        this.longi = longi;
    }

    public void filter(String keyword) {
        if (keyword.length() >= 2 && flag) {
            try {
                AutoCompleteParse parser = new AutoCompleteParse(this, lat, longi);
                itemLists.addAll(parser.execute(keyword).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else if (keyword.length() < 2 && !flag) {
            flag = true;
        }
    }

    public void setClear() {
        itemLists.clear();
        notifyDataSetChanged();
    }

    public interface onItemClickListener {
        void onItemClick(View v, int position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView address;

        public CustomViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            address = itemView.findViewById(R.id.tv_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        if (mlistener != null) {
                            mlistener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }
}
