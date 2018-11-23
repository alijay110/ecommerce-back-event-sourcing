package pl.cba.gibcode.modelLibrary.model;

public enum PriceRangeEnum {
    ZERO_FIFTY,
    FIFTY_HUNDRED,
    HUNDRED_FIVEHUNDRED,
    FIVEHUNDRED_MORE;


    private static PriceRangeEnum[] allValues = values();

    public static PriceRangeEnum fromOrdinal(int n) {
        if (n < 0 || n >= allValues.length) {
            throw new RuntimeException();
        } else {
            return allValues[n];
        }
    }

    public static PriceRangeEnum decideRangeFrom(Double value){
        if( value < 50){
            return ZERO_FIFTY;
        } else if( value < 100){
            return FIFTY_HUNDRED;
        } else if (value < 500){
            return  HUNDRED_FIVEHUNDRED;
        } else if( value > 500){
            return  FIVEHUNDRED_MORE;
        }
        throw new IllegalStateException("Cannot determine range from: " + value);
    }
}