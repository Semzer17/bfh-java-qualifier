package com.example.bfhqualifier;

import com.example.bfhqualifier.util.RegNoUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmokeTest {
    @Test
    void testLastTwoDigits() {
        assertEquals(47, RegNoUtils.lastTwoDigits("REG12347"));
        assertEquals(7, RegNoUtils.lastTwoDigits("REG7"));
        assertEquals(0, RegNoUtils.lastTwoDigits("REG"));
        assertEquals(12, RegNoUtils.lastTwoDigits("21BIT0012"));
    }
}
