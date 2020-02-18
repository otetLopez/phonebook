package com.f20.phonebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> mDataset;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout cell;
        public TextView tvName;
        public TextView tvDetail;
        public ImageView imageView;
        public ViewHolder(View v) {
            super(v);

            cell = v.findViewById(R.id.cell_contact);
            tvName = v.findViewById(R.id.tv_name);
            tvDetail = v.findViewById(R.id.tv_details);
        }
    }

    public ContactAdapter(Context context, List<Contact> myDataset) {
        this.context = context;
        this.mDataset = myDataset;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.i("MainActivity: ", "onBindViewHolder: " + "Getting position " + String.valueOf(position));
        final Contact contact = mDataset.get(position);
        holder.tvName.setText(contact.getFname() + " " + contact.getLname());
        holder.tvDetail.setText(contact.getAddress() + " - " + String.valueOf(contact.getPhone()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContactDetailActivity.class);
                intent.putExtra("id", contact.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void removeItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Contact contact, int position) {
        mDataset.add(position, contact);
        notifyItemInserted(position);
    }

    public List<Contact> getData() {
        return mDataset;
    }
}
