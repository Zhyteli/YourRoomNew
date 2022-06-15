package com.example.yourroom;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private OnItemClickListener mListener;

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

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
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

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
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
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Делай что угодно");
            MenuItem delete = menu.add(Menu.NONE,2, 2, "Удалить");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
