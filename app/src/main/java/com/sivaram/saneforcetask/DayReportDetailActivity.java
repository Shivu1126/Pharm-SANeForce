package com.sivaram.saneforcetask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sivaram.saneforcetask.adapter.WorkersDetailAdapter;
import com.sivaram.saneforcetask.model.StaffDetail;
import com.sivaram.saneforcetask.model.WorkersDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DayReportDetailActivity extends AppCompatActivity {

    private String aCodeId, staffName;
    private ImageView back;
    private int docCount, chemistCount, stockCount, hosCount, cipCount, allCount;
    private Context context;
    private RecyclerView recyclerView;
    private TextView allCountTv, docCountTv, chemistCountTv, stockCountTv, hosCountTv, cipCountTv, staffNameTv;
    private EditText searchEt;
    private List<WorkersDetail> workersDetailList;
    private WorkersDetailAdapter workersDetailAdapter;
    private VolleyRequest volleyRequest;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_report_detail);
        aCodeId = getIntent().getStringExtra("aCodeId");
        staffName = getIntent().getStringExtra("staffName");
        docCount = getIntent().getIntExtra("docCount",0);
        chemistCount = getIntent().getIntExtra("chemistCount",0);
        stockCount = getIntent().getIntExtra("stockCount",0);
        hosCount = getIntent().getIntExtra("hosCount",0);
        cipCount = getIntent().getIntExtra("cipCount",0);
        allCount = docCount+chemistCount+stockCount+hosCount+cipCount;

        init();

        allCountTv.setText(allCount+"");
        docCountTv.setText(docCount+"");
        chemistCountTv.setText(chemistCount+"");
        stockCountTv.setText(stockCount+"");
        hosCountTv.setText(hosCount+"");
        cipCountTv.setText(cipCount+"");
        staffNameTv.setText(staffName);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchWorker(editable.toString());
            }
        });

        if(Common.getNetWorkStatus((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)))
        {
            getWorkerDetail();
        }
        else{
            Common.makeToast("Please On the Data and Go Back", context);
            progressBar.setVisibility(View.VISIBLE);
        }


    }
    private void init(){
        context = DayReportDetailActivity.this;
        back = findViewById(R.id.back);
        allCountTv = findViewById(R.id.all_count);
        docCountTv = findViewById(R.id.doctor_count);
        chemistCountTv = findViewById(R.id.chemist_count);
        stockCountTv = findViewById(R.id.stockiest_count);
        hosCountTv = findViewById(R.id.hospital_count);
        cipCountTv = findViewById(R.id.cip_count);
        staffNameTv = findViewById(R.id.staff_name);
        searchEt = findViewById(R.id.search_text);
        recyclerView = findViewById(R.id.workers_rv);
        workersDetailList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        workersDetailAdapter = new WorkersDetailAdapter(context, workersDetailList);
        recyclerView.setAdapter(workersDetailAdapter);
        volleyRequest = new VolleyRequest(context);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void getWorkerDetail(){
        String urlPath = "http://sanffa.info/server/db_native_app.php?divisionCode=25&ACd="+aCodeId+"&rSF=MGR2240&axn=get/vwVstDet&typ=1&sfCode=MGR2240";
        volleyRequest.getRequest(urlPath, new VolleyCallback() {        //API Call
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.d("worker-details-page", response);
                JSONArray jsonArray = new JSONArray(response);
                if(jsonArray.length()!=0){
                    setDetails(jsonArray);
                    progressBar.setVisibility(View.GONE);
                }
                else Common.makeToast("Empty Detail", context);
            }
            @Override
            public void onError(VolleyError error) throws JSONException {
//                error.printStackTrace();
                progressBar.setVisibility(View.VISIBLE);
                Common.makeToast("Please check the API", context);
            }
        });
    }

    private void setDetails(JSONArray jsonArray){
        try{
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                WorkersDetail workersDetail = new WorkersDetail();
                workersDetail.setName(obj.getString("name"));
                workersDetail.setVisitTime(obj.getString("visitTime"));
                workersDetail.setModifiedTime(obj.getString("ModTime"));
                workersDetail.setCluster(obj.getString("Territory"));
                workersDetail.setPob(obj.get("pob_value").toString());
                if(obj.getString("call_feedback").equals("null") || obj.getString("call_feedback").isEmpty())
                    workersDetail.setFeedBack("NA");
                else workersDetail.setFeedBack(obj.getString("call_feedback"));

                if(obj.getString("WWith").equals("null") || obj.getString("WWith").isEmpty())
                    workersDetail.setJointWith("NONE");
                else  workersDetail.setJointWith(obj.getString("WWith")+" HQ Designation");

                if(obj.getString("remarks").equals("null") || obj.getString("remarks").isEmpty())
                    workersDetail.setRemarks("NA");
                else workersDetail.setRemarks(obj.getString("remarks"));

                workersDetailList.add(workersDetail);
            }
            workersDetailAdapter.updateList(workersDetailList);
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    private void searchWorker(String searchStr){
        List<WorkersDetail> searchResultList = new ArrayList<>();
        for(WorkersDetail w: workersDetailList){
            if(w.getName().toLowerCase().contains(searchStr.toLowerCase())){
                searchResultList.add(w);
            }
        }
        if(! searchResultList.isEmpty()) workersDetailAdapter.updateList(searchResultList);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}