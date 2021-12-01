package com.mindhub.homebanking.utils;

public final class Utils {
    private Utils() {
    }

    public static int getCVV(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int randomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String getCardNumber(int min, int max){
        return randomNumber(min, max) + "" + randomNumber(min, max) + "" + randomNumber(min, max) + "" + randomNumber(min, max);
    }

    public static String accNumber(){
        String accountNumber = ("VIN" + (int) ((Math.random() * (99999 - 10000) + 10000)));
        return accountNumber;
    }

}
