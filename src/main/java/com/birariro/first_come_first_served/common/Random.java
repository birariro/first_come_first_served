package com.birariro.first_come_first_served.common;

public class Random {
  public static String code(){

//    try {
//      Thread.sleep(200);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

    int startChar = 48; // '0'
    int encChar = 122; // 'z'
    int targetStringLength = 20;
    java.util.Random random = new java.util.Random();
    String generatedString = random.ints(startChar, encChar + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    return generatedString;
  }

}
