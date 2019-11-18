package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.kroy.Kroy;

class KroyTests {

    private final Kroy kroy = new Kroy();

    @Test
    void example() {
        assertEquals(kroy.getClass().getName(), kroy.getClass().getName());
    }

}