package com.skhu.cse.promiss.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.skhu.cse.promiss.AddAppointmentActivity;
import com.skhu.cse.promiss.MainActivity;
import com.skhu.cse.promiss.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Appointment_Fragment_3 extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    TextView date;
    TextView time;
    Fragment main;

    String hourOfDay;
    public Add_Appointment_Fragment_3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__appointment__fragment_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        main=this;
        date=view.findViewById(R.id.frg_appointment_3_t5);
        time=view.findViewById(R.id.frg_appointment_3_t6);

        date.setOnClickListener(view1 -> {

            if(getFragmentManager().findFragmentByTag("Datepickerdialog")==null) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        (DatePickerDialog.OnDateSetListener) main,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.setAccentColor(getResources().getColor(R.color.mainColor1));
// If you're calling this from a support Fragment
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        time.setOnClickListener(view1->{

            if(getFragmentManager().findFragmentByTag("Timepickerdialog")==null) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        (TimePickerDialog.OnTimeSetListener) main,
                        now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
                );
// If you're calling this from a support Fragment
                dpd.setAccentColor(getResources().getColor(R.color.mainColor1));
                dpd.show(getFragmentManager(), "Timepickerdialog");

            }
        });
        view.findViewById(R.id.frg_appointment_3_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //넘어가는 부분
                ((AddAppointmentActivity)getActivity()).setAppointment_Date(date.getText().toString(),hourOfDay);
                ((AddAppointmentActivity)getActivity()).Next();
            }
        });
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date_S ;

        date_S=""+year+"."+(monthOfYear+1)+"."+dayOfMonth;

        date.setText(date_S);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time_S ;

        this.hourOfDay=hourOfDay+":"+minute;
        if(hourOfDay>12)
            time_S="오후 "+(hourOfDay-12);
        else if(hourOfDay==12)
            time_S="오후 "+hourOfDay;
        else
            time_S="오전 "+hourOfDay;

        time_S+=":"+minute;

        time.setText(time_S);
    }
}
