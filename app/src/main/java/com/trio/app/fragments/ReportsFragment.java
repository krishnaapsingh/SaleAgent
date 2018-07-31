package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.activities.DateWiseReportActivity;
import com.trio.app.activities.MainActivity;
import com.trio.app.adapters.ReportAdapter;
import com.trio.app.adapters.RouteAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.ReportModel;
import com.trio.app.models.RouteModel;
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
import retrofit2.http.GET;

/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class ReportsFragment extends Fragment {

    public static final String TAG = ReportsFragment.class.getSimpleName();
    RecyclerView recyclerView;
    MainActivity activity;
    public static TextView tvMonth;
    //    Spinner spnMonth;
    String month;
    String month1;
    KProgressHUD hud;
    ReportAdapter adapter;
    LinearLayout llSpn;
    LinearLayout llButtons;
    TextView btnMonthly, btnDaily, tvOk;
    RelativeLayout rlSelectMonth;
    public static Calendar myCalendar;
    List<ReportModel> obj = new ArrayList<>();


    @SuppressLint("ValidFragment")
    public ReportsFragment(MainActivity context) {
        this.activity = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        recyclerView = view.findViewById(R.id.sale_cnf_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        tvMonth = view.findViewById(R.id.tvMonth);
//        spnMonth = view.findViewById(R.id.spnMonth);
        llSpn = view.findViewById(R.id.llSpn);
        llButtons = view.findViewById(R.id.llButtons);
        btnMonthly = view.findViewById(R.id.btnMonthly);
        btnDaily = view.findViewById(R.id.btnDaily);
        tvOk = view.findViewById(R.id.tvOk);
        rlSelectMonth = view.findViewById(R.id.rlSelectMonth);
        rlSelectMonth.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        myCalendar = Calendar.getInstance();

        btnMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSelectMonth.setVisibility(View.VISIBLE);
                llButtons.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }
        });

        btnDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSelectMonth.setVisibility(View.GONE);
                llButtons.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), DateWiseReportActivity.class));
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!tvMonth.getText().toString().trim().equalsIgnoreCase("MM/YYYY") &&
                        !tvMonth.getText().toString().trim().equalsIgnoreCase("Invalid date")) {
                    getMonthlyReports();
                }else {
                    obj.clear();
                    adapter = new ReportAdapter(getActivity(), obj);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(activity, "Please Select Date or Valid Date", Toast.LENGTH_SHORT).show();
                }
            }
        });


        llSpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(v);
//                spnMonth.performClick();
            }
        });

//        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                month = parent.getItemAtPosition(position).toString();
//                tvMonth.setText(month);
//                month1 = String.valueOf(position + 1);
//                getMonthlyReports();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        return view;
    }

    public void datePicker(View view) {

        DialogFragment dialogfragment = new DatePickerDialogTheme();
        dialogfragment.show(getFragmentManager(), "Theme");
    }

    public static class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private static void setDate(final Calendar calendar) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            if (dateClick == 1) {
//                if (calendar.getTime().before(myCalendar.getTime())) {
//                    tvFromDate.setText(sdf.format(calendar.getTime()));
//                    rlToDate.setVisibility(View.VISIBLE);
//                } else if (calendar.getTime().equals(myCalendar.getTime())) {
//                    tvFromDate.setText("Please Select Valid Date");
//                } else {
//                    tvFromDate.setText("Please Select Valid Date");
//                }
//
//            } else if (dateClick == 2) {
////
            if (!calendar.getTime().after(myCalendar.getTime())) {
                tvMonth.setText(sdf.format(calendar.getTime()));
            } else {
                tvMonth.setText("Invalid Date");
            }
//            }

        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
//                    android.app.AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
//            return dialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            Calendar cal = Calendar.getInstance();
            View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
            final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

            monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

            int year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(year);
            yearPicker.setMaxValue(2099);
            yearPicker.setValue(year);

            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                        }
                    });
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            getActivity().this.getDialog().cancel();
//                        }
//                    });
            return builder.create();
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
            setDate(cal);
        }

    }


    public void getMonthlyReports() {
        String userType = SavePref.getLoginData().UserType;
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.YEAR);
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "";
        if (userType.equalsIgnoreCase("Sales Agent")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?PerformanceReport&&" + licenceNo + "&&" + month1 + "&&" + year + "&&" + emailId;

        } else if (userType.equalsIgnoreCase("Admin")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?CompanyPerformanceReport&&" + licenceNo + "&&" + month1 + "&&" + year;
        }else {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?DistributorPerformanceReport&&"+licenceNo+"&&"+month1+"&&"+year+"&&"+emailId;
        }
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<ReportModel>> call = apiInterface.getReport(url);
        call.enqueue(new Callback<List<ReportModel>>() {
            @Override
            public void onResponse(Call<List<ReportModel>> call, Response<List<ReportModel>> response) {
               obj = response.body();
                if (obj.size() != 0) {
                    adapter = new ReportAdapter(activity, obj);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Reports not found ", Toast.LENGTH_SHORT).show();
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


