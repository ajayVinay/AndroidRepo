package com.example.usermanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.usermanagement.R;
import com.example.usermanagement.model.UserInfo;

import java.util.List;

public class UserRecyclerAdpater extends RecyclerView.Adapter<UserRecyclerAdpater.ViewHolder> {

    // interface for callback
    public interface ActionCallback{
        void onLongClickListener(UserInfo userInfo);
    }

    private Context context;
    private List<UserInfo> userInfoList;
    private ActionCallback mActionCallbacks;

    public UserRecyclerAdpater(Context context, List<UserInfo> userInfos) {
        this.context = context;
        this.userInfoList = userInfos;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(context).inflate(R.layout.activity_profile_item,parent,false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindDate(position);

    }
    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    public void updateData(List<UserInfo> userInfo) {
        this.userInfoList = userInfo;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView tvName,tvDesignation,tvFatherName,tvMotherName,tvDateOfBirth,tvAddress,tvContact,tvEmail;
        private TextView tvId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            tvId = itemView.findViewById(R.id.tv_emp_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvId = itemView.findViewById(R.id.tv_emp_id);
            tvDesignation =itemView.findViewById(R.id.tv_designation);
            tvFatherName = itemView.findViewById(R.id.tv_fname);
            tvMotherName =itemView.findViewById(R.id.tv_mname);
            tvDateOfBirth =itemView.findViewById(R.id.tv_dob);
            tvContact =itemView.findViewById(R.id.tv_contact);
            tvAddress =itemView.findViewById(R.id.tv_address);
            tvEmail = itemView.findViewById(R.id.tv_email);
        }
        public void bindDate(int position) {
            UserInfo userInfo = userInfoList.get(position);
            //int id =userInfo.getEmployeeID();

           // tvId.setText(" " + "UI900" +Integer.toString(userInfoList.get(position).getEmployeeID()));

            tvId.setText(" " + "UI900"+(userInfoList.get(position).getEmployeeID()));
            Log.d("idEmp", "bindDate: "+tvId);
            String name =  userInfo.getName();
            tvName.setText(name);
            String designation = userInfo.getDesignation();
            tvDesignation.setText(designation);
            Log.d("designation_value", "bindDate: "+designation);
            String fatherName = userInfo.getFatherName();
            tvFatherName.setText(fatherName);
            String motherName = userInfo.getMotherName();
            tvMotherName.setText(motherName);
            String dob =userInfo.getDateOfBirth();
            tvDateOfBirth.setText(dob);
            String contact =userInfo.getContactNo();
            Log.d("dfgfghjhjk", "bindDate: "+contact);
            tvContact.setText(contact);
            String add = userInfo.getAddress();
            tvAddress.setText(add);
            String email = userInfo.getEmailId();
            tvEmail.setText(email);
        }
        @Override
        public boolean onLongClick(View v) {
            if (mActionCallbacks != null) {
                mActionCallbacks.onLongClickListener(userInfoList.get(getAdapterPosition()));
            }
            return true;
        }
    }
    public void addActionCallback(ActionCallback actionCallbacks) {
        mActionCallbacks = actionCallbacks;
    }
}
