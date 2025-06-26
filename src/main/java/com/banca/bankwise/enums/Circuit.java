package com.banca.bankwise.enums;

import lombok.Getter;

public enum Circuit {
    VISA("4", 16),
    MASTERCARD("51", 16),
    AMERICAN_EXPRESS("34", 15);


    @Getter
    private final String prefix;

    @Getter
    private final int length;

    Circuit(String prefix, int length) {
        this.prefix = String.valueOf(prefix);
        this.length = length;
    }

}
