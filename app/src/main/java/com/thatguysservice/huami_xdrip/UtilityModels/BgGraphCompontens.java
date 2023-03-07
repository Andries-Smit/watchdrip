package com.thatguysservice.huami_xdrip.UtilityModels;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.eveningoutpost.dexdrip.services.broadcastservice.models.GraphLine;
import com.eveningoutpost.dexdrip.services.broadcastservice.models.GraphPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;

public class BgGraphCompontens {

    public static final int FUZZER = 500 * 15 * 5;
    private final int pointSize = 3;
    private final GraphLine inRangeValues;
    private final GraphLine lowValues;
    private final GraphLine highValues;
    private final GraphLine iobValues;

    public GraphLine getTreatmentValues() {
        return treatmentValues;
    }

    private final GraphLine treatmentValues;
    private final GraphLine predictedBgValues;
    private final GraphLine cobValues;
    private final GraphLine polyBgValues;
    private final GraphLine lowLineValues;
    private final GraphLine highLineValues;
    public boolean doMgdl;
    private int fuzzer;
    private Bundle bundle;
    private int axisTextSize = 12;
    private Context context;
    private long start;
    private long end;

    public BgGraphCompontens(Bundle bundle, Context context) {
        this.bundle = bundle;
        this.context = context;

        fuzzer = bundle.getInt("fuzzer");
        if (fuzzer == 0) {
            fuzzer = FUZZER;
        }
        start = bundle.getLong("start");
        end = bundle.getLong("end");
        if (end == 0) {
            end = new Date().getTime() / fuzzer;
        }
        if (start == 0) {
            start = end - (60000 * 180 / fuzzer); // 3h
        }
        doMgdl = bundle.getBoolean("doMgdl", true);
        lowLineValues = parseGraphLine("graph.lowLine");
        highLineValues = parseGraphLine("graph.highLine");
        inRangeValues = parseGraphLine("graph.inRange");
        lowValues = parseGraphLine("graph.low");
        highValues = parseGraphLine("graph.high");
        iobValues = parseGraphLine("graph.iob");
        treatmentValues = parseGraphLine("graph.treatment");
        predictedBgValues = parseGraphLine("graph.predictedBg");
        cobValues = parseGraphLine("graph.cob");
        polyBgValues = parseGraphLine("graph.polyBg");
        this.bundle = null;
    }

    public GraphLine getInRangeValues() {
        return inRangeValues;
    }

    public GraphLine getLowValues() {
        return lowValues;
    }

    public GraphLine getHighValues() {
        return highValues;
    }

    public GraphLine getPredictedBgValues() {
        return predictedBgValues;
    }

    public GraphLine getLowLineValues() {
        return lowLineValues;
    }

    public GraphLine getHighLineValues() {
        return highLineValues;
    }

    public int getFuzzer() {
        return fuzzer;
    }

    public long getStart() {
        return start / fuzzer;
    }


    public long getEnd() {
        return end / fuzzer;
    }


    public GraphLine parseGraphLine(String key) {
        GraphLine graphLine = bundle.getParcelable(key);
        if (graphLine == null) {
            graphLine = new GraphLine();
        }
        return graphLine;
    }

    private Line getFormattedLine(GraphLine graphLine) {
        List<PointValue> values = new ArrayList<>();
        for (GraphPoint point : graphLine.getValues()) {
            values.add(new PointValue(point.getX(), point.getY()));
        }

        Line line = new Line(values);
        line.setColor(graphLine.getColor());
        return line;
    }

    public Line lowLine() {
        Line line = getFormattedLine(lowLineValues);
        line.setHasPoints(false);
        line.setAreaTransparency(50);
        line.setStrokeWidth(1);
        line.setFilled(true);
        return line;
    }

    public Line highLine() {
        Line line = getFormattedLine(highLineValues);
        line.setHasPoints(false);
        line.setStrokeWidth(1);
        return line;
    }

    public Line inRangeValuesLine() {
        Line line = getFormattedLine(inRangeValues);
        line.setHasLines(false);
        line.setPointRadius(pointSize);
        line.setHasPoints(true);
        return line;
    }

    public Line lowValuesLine() {
        Line line = getFormattedLine(lowValues);
        line.setHasLines(false);
        line.setPointRadius(pointSize);
        line.setHasPoints(true);
        return line;
    }

    public Line highValuesLine() {
        Line line = getFormattedLine(highValues);
        //highValuesLine.setColor(ChartUtils.COLOR_ORANGE);
        line.setHasLines(false);
        line.setPointRadius(pointSize);
        line.setHasPoints(true);
        return line;
    }

    public Axis yAxis() {
        Axis yAxis = new Axis();
        yAxis.setAutoGenerated(false);
        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for (int j = 1; j <= 12; j += 1) {
            if (doMgdl) {
                axisValues.add(new AxisValue(j * 50));
            } else {
                axisValues.add(new AxisValue(j * 2));
            }
        }
        yAxis.setValues(axisValues);
        yAxis.setMaxLabelChars(5);
        yAxis.setInside(true);
        yAxis.setTextSize(axisTextSize);
        yAxis.setHasLines(false);
        return yAxis;
    }

    public Axis xAxis() {
        Axis xAxis = new Axis();
        xAxis.setAutoGenerated(false);
        List<AxisValue> xAxisValues = new ArrayList<AxisValue>();
        //GregorianCalendar now = new GregorianCalendar();
        //GregorianCalendar today = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        final java.text.DateFormat timeFormat = hourFormat();
        timeFormat.setTimeZone(TimeZone.getDefault());
        // double start_hour_block = today.getTime().getTime();
        //double timeNow = new Date().getTime();
        //for (int l = 0; l <= 24; l++) {
        //    if ((start_hour_block + (60000 * 60 * (l))) < timeNow) {
        //        if ((start_hour_block + (60000 * 60 * (l + 1))) >= timeNow) {
        //            endHour = start_hour_block + (60000 * 60 * (l));
        //            l = 25;
        //         }
        //    }
        // }


        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis((long) (start));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < (start)) {
            calendar.add(Calendar.HOUR, 1);
        }
        while (calendar.getTimeInMillis() < end) {
            xAxisValues.add(new AxisValue((calendar.getTimeInMillis() / fuzzer), (timeFormat.format(calendar.getTimeInMillis())).toCharArray()));
            calendar.add(Calendar.HOUR, 1);
        }

        //for (int l = 0; l <= (24 + predictivehours); l++) {
        //    double timestamp = (endHour + ((predictivehours) * 60 * 1000 * 60) - (60000 * 60 * l));
        //    xAxisValues.add(new AxisValue((long) (timestamp / FUZZER), (timeFormat.format(timestamp)).toCharArray()));
        // }
        xAxis.setValues(xAxisValues);
        return xAxis;
    }

    private SimpleDateFormat hourFormat() {
        return new SimpleDateFormat(DateFormat.is24HourFormat(context) ? "HH" : "h a");
    }

    public Axis chartXAxis() {
        Axis xAxis = xAxis();
        xAxis.setHasLines(false);
        xAxis.setTextSize(axisTextSize);
        return xAxis;
    }

    public Line predictedBg() {
        Line line = getFormattedLine(predictedBgValues);
        line.setHasLines(false);
        line.setCubic(false);
        line.setStrokeWidth(1);
        line.setFilled(false);
        line.setPointRadius(2);
        line.setHasPoints(true);
        line.setHasLabels(false);
        return line;
    }

    public Line cobValues() {
        Line line = getFormattedLine(cobValues);
        line.setHasLines(false);
        line.setCubic(false);
        line.setFilled(false);
        line.setPointRadius(1);
        line.setHasPoints(true);
        line.setHasLabels(false);
        return line;
    }

    public Line polyBg() {
        Line line = getFormattedLine(polyBgValues);
        line.setCubic(false);
        line.setStrokeWidth(1);
        line.setFilled(false);
        line.setPointRadius(1);
        line.setHasLabels(false);
        line.setHasLines(true);
        line.setHasPoints(true);
        return line;
    }

    public Line iobValues() {
        Line line = getFormattedLine(iobValues);
        line.setHasLines(true);
        line.setCubic(false);
        line.setFilled(true);
        line.setAreaTransparency(35);
        line.setFilled(true);
        line.setPointRadius(1);
        line.setHasPoints(true);
        line.setStrokeWidth(1); //bolus line
        return line;
    }

    public Line bolusValues() {
        Line line = getFormattedLine(treatmentValues);
        line.setHasLines(false);
        line.setHasPoints(true);
        line.setShape(ValueShape.DIAMOND);

        LineChartValueFormatter formatter = new SimpleLineChartValueFormatter(1);
        line.setFormatter(formatter);

        line.setFilled(false); //bolus dots
        line.setHasLabels(false);
        line.setPointRadius(2);
        return line;
    }
}
