package com.sundolls.epbackend.config.util;

public enum SearchOption {
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year"),
    ALL("all")
    ;

    private String option;

    SearchOption(String option){
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
