package org.lyl.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.Supplier;

@Getter
@AllArgsConstructor
public enum VictoryRateEnum {

    AAAA(new BigDecimal("0.0"), new BigDecimal("0.6"), () -> new BigDecimal(Math.random()));

    private BigDecimal startVal;
    private BigDecimal endVal;
    private Supplier<BigDecimal> supplier;


    public static BigDecimal getVictoryRate(BigDecimal rightRate) {
        BigDecimal victoryRate = null;
        for (VictoryRateEnum e : VictoryRateEnum.values()) {
            if(rightRate.compareTo(e.endVal) > 0){
                victoryRate = e.getSupplier().get();
                break;
            }
        }
        return victoryRate;
    }
    //SecureRandom.

}
