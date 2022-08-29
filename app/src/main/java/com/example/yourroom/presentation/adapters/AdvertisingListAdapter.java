package com.example.yourroom.presentation.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourroom.R;
import com.example.yourroom.domain.Announcement;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdvertisingListAdapter extends RecyclerView.Adapter<AdvertisingListAdapter.AdvertisingViewHolder> {
    private Context mContext;
    private List<Announcement> mAnnouncement;
    private AdvertisingListAdapter.OnItemClickListener mListener;

    public AdvertisingListAdapter(Context context, List<Announcement> announcements) {
        mContext = context;
        mAnnouncement = announcements;
    }

    @NonNull
    @Override
    public AdvertisingListAdapter.AdvertisingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.advertising_item_add_fragment, parent, false);
        return new AdvertisingListAdapter.AdvertisingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdvertisingListAdapter.AdvertisingViewHolder holder, int position) {
        Announcement announcementCurrent = mAnnouncement.get(position);

        holder.textViewPrice.setText(announcementCurrent.getPrice());
        holder.textViewAddress.setText(announcementCurrent.getAddress());

        Picasso.get()
                .load(announcementCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher_logo)
                .fit()
                .centerInside()
                .into(holder.imageViewAnnouncement);
    }

    @Override
    public int getItemCount() {
        return mAnnouncement.size();
    }

    public class AdvertisingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewPrice, textViewAddress;
        public ImageView imageViewAnnouncement;

        public AdvertisingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPrice = itemView.findViewById(R.id.text_view_price_advertising);
            textViewAddress = itemView.findViewById(R.id.text_view_address_advertising);

            imageViewAnnouncement = itemView.findViewById(R.id.image_view_advertising);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    if (item.getItemId() == 1) {
                        mListener.onWhatEverClick(position);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Выберите действие");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Избранное");

            doWhatever.setOnMenuItemClickListener(this);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onWhatEverClick(int position);

    }

    public void setOnItemClickListener(AdvertisingListAdapter.OnItemClickListener listener){
        mListener = listener;
    }
}
