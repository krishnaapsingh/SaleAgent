package com.trio.app.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.adapters.ReportAdapter;
import com.trio.app.adapters.ReportAdapter1;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.ReportModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateWiseReportActivity extends AppCompatActivity {

    Toolbar toolbar;
    static KProgressHUD hud;
    static RecyclerView recyclerView;
    static ReportAdapter1 adapter;
    static RelativeLayout rlDateWise, rlFromDate, rlToDate, rlToday;
    static TextView tvFromDate, tvToDate;
    static Calendar myCalendar;
    static int dateClick = 0;
    static List<ReportModel> obj = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_report);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Reports");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
                DateWiseReportActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.sale_cnf_rv);
        recyclerView.setLayoutManager(manager);

        rlFromDate = findViewById(R.id.rlFromDate);
        rlToDate = findViewById(R.id.rlToDate);
        rlToDate.setVisibility(View.GONE);

        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        rlToday = findViewById(R.id.rlToday);
        myCalendar = Calendar.getInstance();

        rlToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(myCalendar.getTime());
                getReportDateWise(date, date);
            }
        });


        rlFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateClick = 1;
                datePicker(v);

            }
        });

        rlToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateClick = 2;
                datePicker(v);


            }
        });
    }


    public void datePicker(View view) {

        DialogFragment dialogfragment = new DatePickerDialogTheme();
        dialogfragment.show(getSupportFragmentManager(), "Theme");
    }

    public static class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private static void setDate(final Calendar calendar) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (dateClick == 1) {
                if (calendar.getTime().before(myCalendar.getTime())) {
                    tvFromDate.setText(sdf.format(calendar.getTime()));
                    rlToDate.setVisibility(View.VISIBLE);
                } else if (calendar.getTime().equals(myCalendar.getTime())) {
                    tvFromDate.setText("Please Select Valid Date");
                } else {
                    tvFromDate.setText("Please Select Valid Date");
                }

            } else if (dateClick == 2) {
//
                if (!calendar.getTime().after(myCalendar.getTime())) {
                    tvToDate.setText(sdf.format(calendar.getTime()));

                    getReportDateWise(tvFromDate.getText().toString().trim(), tvToDate.getText().toString().trim());
                } else {

                    obj.clear();
                    adapter = new ReportAdapter1(obj);
                    recyclerView.setAdapter(adapter);
                    tvToDate.setText("Please Select Valid Date");
                }
            }

        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    android.app.AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = new GregorianCalendar(year, month, day);
            setDate(cal);
        }
    }

    private static void getReportDateWise(String fromDate, String toDate) {

        String userType = SavePref.getLoginData().UserType;
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "";
        if (userType.equalsIgnoreCase("Sales Agent")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?PerformanceReportDaily&&" + licenceNo + "&&" + fromDate + "&&" + toDate + "&&" + emailId;

        } else if (userType.equalsIgnoreCase("Admin")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?CompanyPerformanceReportDaily&&" + licenceNo + "&&" + fromDate + "&&" + toDate;
        }else {
            url= "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?DistributorPerformanceReportDaily&&"+licenceNo+"&&"+fromDate+"&&"+toDate+"&&"+emailId;
        }
//            String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?PerformanceReportDaily&&"+licenceNo+"&&"+fromDate+"&&"+toDate+"&&"+emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<ReportModel>> call = apiInterface.getReport(url);
        call.enqueue(new Callback<List<ReportModel>>() {
            @Override
            public void onResponse(Call<List<ReportModel>> call, Response<List<ReportModel>> response) {
                obj = response.body();
                if (obj.size() != 0) {
                    adapter = new ReportAdapter1(obj);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<ReportModel>> call, Throwable t) {
                hud.dismiss();
            }
        });

    }
}
