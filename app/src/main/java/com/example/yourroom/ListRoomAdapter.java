package com.example.yourroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourroom.Announcement.Announcement;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.RoomViewHolder>{
    private Context mContext;
    private List<Announcement> mAnnouncement;


    public ListRoomAdapter(Context context, List<Announcement> announcements) {
        mContext = context;
        mAnnouncement = announcements;
    }


    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item_add_fragment, parent, false);
        return new RoomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Announcement announcementCurrent = mAnnouncement.get(position);
        holder.textViewPrice.setText(announcementCurrent.getPrice());
        holder.textViewAddress.setText(announcementCurrent.getAddress());
        holder.textViewDescription.setText(announcementCurrent.getDescription());
        holder.textViewPhone.setText(announcementCurrent.getPhone());
        holder.textViewEmail.setText(announcementCurrent.getEmail());
        Picasso.get().
                load(announcementCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageViewAnnouncement);
    }

    @Override
    public int getItemCount() {
        return mAnnouncement.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewPrice, textViewAddress,
                textViewDescription, textViewPhone, textViewEmail;
        public ImageView imageViewAnnouncement;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            textViewAddress = itemView.findViewById(R.id.text_view_address);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPhone = itemView.findViewById(R.id.text_view_phone);
            textViewEmail = itemView.findViewById(R.id.text_view_email);

            imageViewAnnouncement = itemView.findViewById(R.id.image_view_announcement);
        }
    }
}
