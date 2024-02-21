package com.sivaram.saneforcetask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sivaram.saneforcetask.OnStaffDetailClickListener;
import com.sivaram.saneforcetask.R;
import com.sivaram.saneforcetask.model.StaffDetail;

import java.util.List;

public class StaffDetailsAdapter extends RecyclerView.Adapter<StaffDetailsAdapter.StaffDetailsHolder> {

    private Context context;
    private List<StaffDetail> staffDetailList;
    private OnStaffDetailClickListener onStaffDetailClickListener;

    public StaffDetailsAdapter(Context context, List<StaffDetail> staffDetailList, OnStaffDetailClickListener onStaffDetailClickListener) {
        this.context = context;
        this.staffDetailList = staffDetailList;
        this.onStaffDetailClickListener = onStaffDetailClickListener;
    }

    @NonNull
    @Override
    public StaffDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_report_detail_rv, parent, false);
        return new StaffDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffDetailsHolder holder, int position) {
        holder.staffNameTv.setText(staffDetailList.get(position).getStaffName());
        holder.workTypeTv.setText(staffDetailList.get(position).getWorkType());
        holder.checkInTimeTv.setText(staffDetailList.get(position).getCheckInTime());
        holder.checkInLocationTv.setText(staffDetailList.get(position).getCheckInLocation());
//        holder.viewLocationTv.setText(staffDetailList.get(position).get());
        holder.remarksContentTv.setText(staffDetailList.get(position).getRemarksContent());

        holder.submittedDateTv.setText(staffDetailList.get(position).getSubmittedDate());
        holder.checkOutTimeTv.setText(staffDetailList.get(position).getCheckOutTime());
        holder.checkOutLocationTv.setText(staffDetailList.get(position).getCheckOutLocation());
//        holder.viewLocationTv2.setText(staffDetailList.get(position).getStaffName());
        holder.statusTv.setText(staffDetailList.get(position).getStatus());

        if(staffDetailList.get(position).getDoctorsCount()!=0)
            holder.doctorsCountTv.setText(staffDetailList.get(position).getDoctorsCount()+"");
        else
            holder.doctorLayout.setVisibility(View.GONE);

        if(staffDetailList.get(position).getChemistCount()!=0)
            holder.chemistCountTv.setText(staffDetailList.get(position).getChemistCount()+"");
        else
            holder.chemistLayout.setVisibility(View.GONE);

        if(staffDetailList.get(position).getStockiestCount()!=0)
            holder.stockiestCountTv.setText(staffDetailList.get(position).getStockiestCount()+"");
        else
            holder.stockiestLayout.setVisibility(View.GONE);

        if(staffDetailList.get(position).getCipCount()!=0)
            holder.cipCountTv.setText(staffDetailList.get(position).getCipCount()+"");
        else
            holder.cipLayout.setVisibility(View.GONE);

        if(staffDetailList.get(position).getHospitalCount()!=0)
            holder.hospitalCountTv.setText(staffDetailList.get(position).getHospitalCount()+"");
        else
            holder.hospitalLayout.setVisibility(View.GONE);


        holder.nextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStaffDetailClickListener.onClick(staffDetailList.get(position).getaCodeId(),
                        staffDetailList.get(position).getDoctorsCount(), staffDetailList.get(position).getChemistCount(),
                        staffDetailList.get(position).getStockiestCount(), staffDetailList.get(position).getHospitalCount(),
                        staffDetailList.get(position).getCipCount(), staffDetailList.get(position).getStaffName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffDetailList.size();
    }

    public void updateList(List<StaffDetail> list){
        staffDetailList = list;
        notifyDataSetChanged();
    }

    public class StaffDetailsHolder extends RecyclerView.ViewHolder{

        private TextView staffNameTv, workTypeTv, checkInTimeTv, checkInLocationTv, viewLocationTv, remarksContentTv;
        private TextView submittedDateTv, checkOutTimeTv, checkOutLocationTv, viewLocationTv2, statusTv;
        private ImageView locationViewIcon, locationViewIcon2;
        private TextView doctorsCountTv, chemistCountTv, stockiestCountTv, cipCountTv, hospitalCountTv;
        private LinearLayout doctorLayout, chemistLayout, stockiestLayout, cipLayout, hospitalLayout;
        private ImageView nextIv;
        public StaffDetailsHolder(@NonNull View itemView) {
            super(itemView);
            nextIv = itemView.findViewById(R.id.next);

            staffNameTv = itemView.findViewById(R.id.staff_name);
            workTypeTv = itemView.findViewById(R.id.work_type);
            checkInTimeTv = itemView.findViewById(R.id.checkin_time_date);
            checkInLocationTv = itemView.findViewById(R.id.checkin_location);
            viewLocationTv = itemView.findViewById(R.id.view_location);
            locationViewIcon = itemView.findViewById(R.id.location_icon);
            remarksContentTv = itemView.findViewById(R.id.remarks_content);

            submittedDateTv = itemView.findViewById(R.id.submitted_date);
            checkOutTimeTv = itemView.findViewById(R.id.checkout_time_date);
            checkOutLocationTv = itemView.findViewById(R.id.checkout_location);
            viewLocationTv2 = itemView.findViewById(R.id.view_location_);
            locationViewIcon2 = itemView.findViewById(R.id.location_icon_);
            statusTv = itemView.findViewById(R.id.status_text);

            doctorsCountTv = itemView.findViewById(R.id.doctor_count);
            chemistCountTv = itemView.findViewById(R.id.chemist_count);
            stockiestCountTv = itemView.findViewById(R.id.stockiest_count);
            cipCountTv = itemView.findViewById(R.id.cip_count);
            hospitalCountTv = itemView.findViewById(R.id.hospital_count);

            doctorLayout = itemView.findViewById(R.id.doctors_layout);
            chemistLayout = itemView.findViewById(R.id.chemist_layout);
            stockiestLayout = itemView.findViewById(R.id.stockiest_layout);
            cipLayout = itemView.findViewById(R.id.cip_layout);
            hospitalLayout = itemView.findViewById(R.id.hospital_layout);
        }
    }
}
