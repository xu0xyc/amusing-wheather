package com.charlie.wheather;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Z1 z1 = new Z1(5);
        Z2 z2 = new Z2();

        assertEquals(z1.getB(), z2.getB());

    }


    private static class P {
        protected static int b = 10;

    }

    private class Z1 extends P {

        public Z1(int b){
            this.b = b;
        }

        public int getB() {
            return b;
        }
    }

    private class Z2 extends P {

        public int getB() {
            return b;
        }
    }
}