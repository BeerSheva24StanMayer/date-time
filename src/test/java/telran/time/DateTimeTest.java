package telran.time;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.*;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Locale;

import org.junit.jupiter.api.Test;

public class DateTimeTest {
    @Test
    void localDateTest() {
        LocalDate current = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
        ZonedDateTime currentZonedTime = ZonedDateTime.now();
        Instant currentInstant = Instant.now();
        LocalTime currentLtime = LocalTime.now();
        System.out.printf("Current date %s in ISO format \n", current);
        System.out.printf("Current date & time %s in ISO format \n", currentTime);
        System.out.printf("Current zoned date and time %s in ISO format \n", currentZonedTime);
        System.out.printf("Current instant of date & time %s in ISO format \n", currentInstant);
        System.out.printf("Local current of date & time %s in ISO format \n", currentLtime);
        System.out.printf("Current date is %s in dd/mm/yyyy \n",
                current.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    @Test
    void nextFridayTest() {
        LocalDate current = LocalDate.of(2024, 8, 11);
        LocalDate expected = LocalDate.of(2024, 9, 13);
        NextFriday13 adjuster = new NextFriday13();
        assertEquals(expected, current.with(adjuster));
        assertThrows(RuntimeException.class, () -> LocalTime.now().with(adjuster));
    }

    @Test
    void pastTempProxTest() {

        Temporal[] datesWithPast = { LocalDateTime.of(2024, 7, 8, 12, 10, 10),
                ZonedDateTime.of(2024, 8, 9, 10, 4, 5, 5, ZoneId.of("Europe/Berlin")),
                LocalDate.of(2023, 9, 13), MinguoDate.of(2024, 8, 8), LocalDate.of(1915, 9, 10), LocalTime.of(10, 15) };

        Temporal[] datesWithoutPast = { LocalDateTime.of(2025, 7, 8, 12, 10, 10),
                ZonedDateTime.of(2024, 9, 9, 10, 4, 5, 5, ZoneId.of("Europe/Berlin")),
                LocalDate.of(2024, 12, 13), MinguoDate.of(2024, 10, 8),
                LocalDate.of(2027, 9, 10) };

        PastTemporalProximity adjasterWithNegative = new PastTemporalProximity(datesWithPast);
        PastTemporalProximity adjusterWithoutNegative = new PastTemporalProximity(datesWithoutPast);

        // //Test for localDate as comparable
        LocalDate localDate = LocalDate.of(2024, 8, 12);
        assertEquals(LocalDate.of(2024, 8, 9), localDate.with(adjasterWithNegative));
        assertEquals(null, localDate.with(adjusterWithoutNegative));

        // LocalTime doesn't have yy-mm-dd and gets null as result
        LocalTime localTime = LocalTime.of(10, 5, 10);
        assertEquals(null, localTime.with(adjasterWithNegative));
        assertEquals(null, localTime.with(adjusterWithoutNegative));


        // //Test for MinguoDate as comparable
        MinguoDate minguoDate = MinguoDate.of(113, 8, 12);
        assertEquals(MinguoDate.of(113, 8, 9), minguoDate.with(adjasterWithNegative));
        assertThrows(RuntimeException.class, () -> minguoDate.with(adjusterWithoutNegative)); // I MinguoDate can't be
                                                                                              // Null

        // Test for LocalDateTime as comparable
        LocalDateTime localDateTime = LocalDateTime.of(2024, 8, 12, 23, 00, 15);
        assertEquals(LocalDateTime.of(2024, 8, 9, 10, 4, 6), localDateTime.with(adjasterWithNegative));
        assertEquals(null, localDateTime.with(adjusterWithoutNegative));


            // for Array with 1 and 0 elements
        Temporal[] datesWithPast1 = {LocalDateTime.of(2024, 7, 8, 12, 10, 10)};
        Temporal[] datesWithoutPast1 = {LocalDateTime.of(2024, 8, 15, 12, 10, 10)};
        Temporal[] datesWithPast2 = {};

        PastTemporalProximity adjasterWithNegative1 = new PastTemporalProximity(datesWithPast1);
        PastTemporalProximity adjasterWithoutNegative1 = new PastTemporalProximity(datesWithoutPast1);
        PastTemporalProximity adjasterWithNegative2 = new PastTemporalProximity(datesWithPast2);

        assertEquals(LocalDate.of(2024, 7, 8), localDate.with(adjasterWithNegative1));
        assertEquals(null, localDate.with(adjasterWithoutNegative1));

        assertEquals(null, localDate.with(adjasterWithNegative2));
        


    }

}
