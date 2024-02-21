package com.sivaram.saneforcetask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sivaram.saneforcetask.R;
import com.sivaram.saneforcetask.model.StaffDetail;
import com.sivaram.saneforcetask.model.WorkersDetail;

import java.util.List;

public class WorkersDetailAdapter extends RecyclerView.Adapter<WorkersDetailAdapter.WorkersDetailHolder> {

    private Context context;
    private List<WorkersDetail> workersDetailList;

    public WorkersDetailAdapter(Context context, List<WorkersDetail> workersDetailList) {
        this.context = context;
        this.workersDetailList = workersDetailList;
    }

    @NonNull
    @Override
    public WorkersDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.workers_detail_rv, parent, false);
        return new WorkersDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkersDetailHolder holder, int position) {
        holder.nameTv.setText(workersDetailList.get(position).getName());
        holder.visitTimeTv.setText(workersDetailList.get(position).getVisitTime());
        holder.modifiedTimeTv.setText(workersDetailList.get(position).getModifiedTime());
        holder.clusterTv.setText(workersDetailList.get(position).getCluster());
        holder.pobTv.setText(workersDetailList.get(position).getPob());
        holder.feedbackTv.setText(workersDetailList.get(position).getFeedBack());
        holder.jointWithTv.setText(workersDetailList.get(position).getJointWith());
        holder.remarksTv.setText(workersDetailList.get(position).getRemarks());
    }

    @Override
    public int getItemCount() {
        return workersDetailList.size();
    }

    public void updateList(List<WorkersDetail> list){
        workersDetailList = list;
        notifyDataSetChanged();
    }

    public class WorkersDetailHolder extends RecyclerView.ViewHolder{

        private TextView nameTv, visitTimeTv, modifiedTimeTv, clusterTv, pobTv, feedbackTv, jointWithTv, remarksTv;
        public WorkersDetailHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.worker_name);
            visitTimeTv = itemView.findViewById(R.id.visit_time);
            modifiedTimeTv = itemView.findViewById(R.id.modify_time);
            clusterTv = itemView.findViewById(R.id.cluster);
            pobTv = itemView.findViewById(R.id.pob);
            feedbackTv = itemView.findViewById(R.id.feedback);
            jointWithTv = itemView.findViewById(R.id.jointwith);
            remarksTv = itemView.findViewById(R.id.remark_content);
        }
    }
}
