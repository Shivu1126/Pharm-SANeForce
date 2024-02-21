package com.sivaram.saneforcetask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.card.MaterialCardView;
import com.sivaram.saneforcetask.adapter.StaffDetailsAdapter;
import com.sivaram.saneforcetask.model.StaffDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayReportActivity extends AppCompatActivity {

    private ImageView back;
    private VolleyRequest volleyRequest;
    private ProgressBar progressBar;
    private Context context;
    private View datePickerCard;
    private TextView dateText;
    private String date;
    private EditText searchEt;
    private RecyclerView recyclerView;
    private StaffDetailsAdapter staffDetailsAdapter;
    private List<StaffDetail> staffDetailList;
    private OnStaffDetailClickListener onStaffDetailClickListener = new OnStaffDetailClickListener() {
        @Override
        public void onClick(String aCode_id, int docCount, int chemistCount, int stockCount, int hosCount, int cipCount, String staffName) {
            Intent intent = new Intent(DayReportActivity.this, DayReportDetailActivity.class);
            intent.putExtra("aCodeId", aCode_id);
            intent.putExtra("docCount", docCount);
            intent.putExtra("chemistCount", chemistCount);
            intent.putExtra("stockCount", stockCount);
            intent.putExtra("hosCount", hosCount);
            intent.putExtra("cipCount", cipCount);
            intent.putExtra("staffName", staffName);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_report);
        init();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        staffDetailsAdapter =new StaffDetailsAdapter(context, staffDetailList, onStaffDetailClickListener);
        recyclerView.setAdapter(staffDetailsAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        datePickerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
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
                searchStaff(editable.toString());
            }
        });

        if(Common.getNetWorkStatus((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)))
        {
            getDetails();
        }
        else{
            Common.makeToast("Please On the Data and Go Back", context);
            progressBar.setVisibility(View.VISIBLE);
        }

    }
    private void init(){
        context = DayReportActivity.this;
        back = findViewById(R.id.back);
        volleyRequest = new VolleyRequest(context);
        progressBar = findViewById(R.id.progress_bar);
        datePickerCard = findViewById(R.id.date_picker_view);
        dateText = findViewById(R.id.date_text);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(c.getTime());
        dateText.setText(Common.changeDateFormat(date));
        staffDetailList = new ArrayList<>();
        recyclerView = findViewById(R.id.details_rv);
        searchEt = findViewById(R.id.search_text);
    }

    private void getDetails(){
        String urlPath = "http://sanffa.info/server/db_native_app.php?divisionCode=25&rptSF=MGR2240&rSF=MGR2240&axn=get/DayReports&sfCode=MGR2240&rptDt="+date;
        volleyRequest.getRequest(urlPath, new VolleyCallback() {        //API Call
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.d("details-page", response);
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
        try {
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                StaffDetail staffDetail = new StaffDetail();
                staffDetail.setaCodeId(obj.getString("ACode"));
                staffDetail.setStaffName(obj.getString("SF_Name")+" - HQ - Designation");
                staffDetail.setStaffCode(obj.getString("SF_Code"));
                staffDetail.setWorkType(obj.getString("wtype"));

                if(obj.getString("intime").isEmpty() || obj.getString("intime").equals("null")){
                    staffDetail.setCheckInTime("Not yet");
                }else{
                    staffDetail.setCheckInTime(obj.getString("intime"));
                }
                if(obj.getString("inaddress").isEmpty() || obj.getString("inaddress").equals("null")){
                    staffDetail.setCheckInLocation("N/A");
                }else{
                    staffDetail.setCheckInLocation(obj.getString("inaddress"));
                }

                if(obj.getString("outtime").isEmpty() || obj.getString("outtime").equals("null")){
                    staffDetail.setCheckOutTime("Not yet");
                }else{
                    staffDetail.setCheckOutTime(obj.getString("outtime"));
                }
                if(obj.getString("outaddress").isEmpty() || obj.getString("outaddress").equals("null")){
                    staffDetail.setCheckOutLocation("N/A");
                }else{
                    staffDetail.setCheckOutLocation(obj.getString("outaddress"));
                }

                if(obj.getString("remarks").isEmpty() || obj.getString("remarks").equals("null")){
                    staffDetail.setRemarksContent("NONE");
                }else{
                    staffDetail.setRemarksContent(obj.getString("remarks"));
                }

                int doctorCount = Integer.parseInt(obj.getString("Drs"));
                int chemistCount = Integer.parseInt(obj.getString("Chm"));
                int stockiestCount = Integer.parseInt(obj.getString("Stk"));
                int cipCount = Integer.parseInt(obj.getString("Cip"));
                int hospitalCount = Integer.parseInt(obj.getString("Hos"));

                staffDetail.setDoctorsCount(doctorCount);
                staffDetail.setChemistCount(chemistCount);
                staffDetail.setStockiestCount(stockiestCount);
                staffDetail.setCipCount(cipCount);
                staffDetail.setHospitalCount(hospitalCount);
                staffDetail.setStatus("Pending");
//                JSONObject dateObj = obj.getJSONObject("Activity_Date");
//                String dateArr[] = dateObj.getString("date").split(" ");
                staffDetail.setSubmittedDate(obj.getString("Adate"));

                staffDetailList.add(staffDetail);
            }
            staffDetailsAdapter.updateList(staffDetailList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void searchStaff(String searchStr){
        List<StaffDetail> searchResultList = new ArrayList<>();
        for(StaffDetail s: staffDetailList){
            if(s.getStaffName().toLowerCase().contains(searchStr.toLowerCase())){
                searchResultList.add(s);
            }
        }
        if(! searchResultList.isEmpty()) staffDetailsAdapter.updateList(searchResultList);

    }

    private void setDate(){
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DayReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        dateText.setText(Common.changeDateFormat(date));
                        staffDetailList.clear();
                        getDetails();
                    }
                },

                year, month, day);
        datePickerDialog.show();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}