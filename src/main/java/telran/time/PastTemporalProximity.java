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
        Temporal finalDate = null;
        boolean temporalDateOk = isOkWithDateTem(temporal);
        boolean temporalArrDateOk = isOkWithDateAr(dates, temporal);
        if (temporalArrDateOk && temporalDateOk) {
            convert(dates, temporal);
            // removeNullsFromArray(dates);
            Arrays.sort(dates);
            finalDate = nearestNegative(dates, temporal);
        }
        return finalDate;
    }

    private void convert(Temporal[] ar, Temporal temp) {
        for (int i = 0; i < ar.length; i++) {
            try {
                long between = temp.until(dates[i], ChronoUnit.DAYS);
                
            ar[i] = temp.plus(between, ChronoUnit.DAYS);
        }
            catch(RuntimeException e) {
                ar[i] = temp;
            }
        }
    }

    private Temporal nearestNegative(Temporal[] ar, Temporal temp) {

        int start = 0;
        int end = ar.length - 1;
        int middle = (start + end) / 2;
        while (start <= end) {
            long diff = temp.until(dates[middle], ChronoUnit.DAYS);
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
}
