package org.lyl.common.util.encrypt;

import java.util.Random;

public class SeedGenerator {

    public static int getSeedByRandom() {
        Random random = new Random(100L);
        int randomData = random.nextInt();
        System.out.println("first get seed by random data =" + randomData);

        random = new Random(10001L);
        randomData = random.nextInt();
        System.out.println("second get seed by random data =" + randomData);
        return randomData;
    }

}
