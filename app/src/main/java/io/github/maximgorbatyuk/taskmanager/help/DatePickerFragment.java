package io.github.maximgorbatyuk.taskmanager.help;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Maxim on 10.04.2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DateTimeInterface delegate;

    public void setDelegate(DateTimeInterface delegate){
        this.delegate = delegate;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int date = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);


        return new DatePickerDialog(getActivity(), this, year, month, date);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String day;
        String month;
        if ((dayOfMonth + 1) < 10)
            day = "0" + (dayOfMonth + 1);
        else
            day = "" + (dayOfMonth + 1);
        //--------------------------
        if ((monthOfYear + 1) < 10)
            month = "0" + (monthOfYear+ 1);
        else
            month = "" + (monthOfYear + 1);

        String result = "" + day + "." + month + "." + year;
        delegate.getDateTimeString(result);
    }
}
