package com.example.usermanagement.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;

    }
}
