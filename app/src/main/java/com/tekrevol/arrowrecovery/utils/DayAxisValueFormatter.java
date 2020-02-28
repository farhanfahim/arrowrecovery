package com.tekrevol.arrowrecovery.utils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter extends ValueFormatter
{
    ArrayList<Date> date = new ArrayList<>();
    private final String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private final BarLineChartBase<?> chart;



    public DayAxisValueFormatter(BarLineChartBase<?> chart, ArrayList<Date> dates) {
        this.chart = chart;
        this.date = dates;
    }

    @Override
    public String getFormattedValue(float value) {


        int pos = (int) value;
        Date date2 = date.get(pos);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(date2);
        return date;


    }

}
