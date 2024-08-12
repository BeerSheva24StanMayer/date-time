package telran.time;

import java.lang.reflect.Array;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;

public class PastTemporalProximity implements TemporalAdjuster {
    // TODO some encapsulation
    // array of temporals supporting Day, Month, Year (Dates)
    Temporal[] dates;

    public PastTemporalProximity(Temporal[] dates) {
        this.dates = dates;
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        // TODO Auto-generated method stub
        // return the temporal for the encapsulated array
        // that is a nearest in past
        Temporal[] dates2 = Arrays.copyOf(dates, dates.length);
        Temporal finalDate = null;
        dates2 = convert(dates2, temporal);
        if(dates2 != null) {
        Arrays.sort(dates2);
        finalDate = nearestNegative(dates2, temporal);
        }
        return finalDate != temporal ? finalDate : null;
    }

    private Temporal[] convert(Temporal[] ar, Temporal temp) {
        boolean isDateOk = isOkWithDateTem(temp);
        boolean isDateOkArr = isOkWithDateAr(dates, temp);
        if (isDateOk && isDateOkArr) {
            conditionsToConvert(ar, temp);
        }
        else {
            ar = null;
        }
        return ar;
    }

    private void conditionsToConvert(Temporal[] ar, Temporal temp) {
        boolean isTimeTemp = isTimeTemp(temp);
        if (isTimeTemp) {
            convertWithTime(ar, temp);
        } else {
            convertBasic(ar, temp);
        }
    }

    private void convertWithTime(Temporal[] ar, Temporal temp) {
        for (int i = 0; i < ar.length; i++) {
            try {
                long between = temp.until(ar[i], ChronoUnit.SECONDS);

                ar[i] = temp.plus(between, ChronoUnit.SECONDS);
            } catch (RuntimeException e) {
                ar[i] = temp;
            }
        }

    }

    private void convertBasic(Temporal[] ar, Temporal temp) {
        for (int i = 0; i < ar.length; i++) {
            try {
                long between = temp.until(ar[i], ChronoUnit.DAYS);

                ar[i] = temp.plus(between, ChronoUnit.DAYS);
            } catch (RuntimeException e) {
                ar[i] = temp;
            }
        }
    }

    private Temporal nearestNegative(Temporal[] ar, Temporal temp) {
        int start = 0;
        int end = ar.length - 1;
        int middle = (start + end) / 2;
        while (start <= end) {
            long diff = temp.until(ar[middle], ChronoUnit.DAYS);
            if (diff >= 0) {
                end = middle - 1;
            } else {
                start = middle + 1;
            }
            middle = (start + end) / 2;
        }
        return end == -1 ? null : ar[end];
    }

    private boolean isOkWithDateAr(Temporal[] ar, Temporal temp) {
        for (int i = 0; i < ar.length; i++) {
            boolean year = ar[i].isSupported(ChronoUnit.YEARS);
            boolean month = ar[i].isSupported(ChronoUnit.MONTHS);
            boolean day = ar[i].isSupported(ChronoUnit.DAYS);
            if (!year && !month && !day) {
                ar[i] = temp;
            }
        }
        return ar.length > 0;

    }

    private boolean isOkWithDateTem(Temporal temporal) {
        boolean year = temporal.isSupported(ChronoUnit.YEARS);
        boolean month = temporal.isSupported(ChronoUnit.MONTHS);
        boolean day = temporal.isSupported(ChronoUnit.DAYS);
        return year & month & day;
    }

    private boolean isTimeTemp(Temporal temp) {
        boolean hour = temp.isSupported(ChronoUnit.HOURS);
        boolean minutes = temp.isSupported(ChronoUnit.MINUTES);
        boolean seconds = temp.isSupported(ChronoUnit.SECONDS);
        return hour & minutes & seconds;
    }
}
