package pl.cba.gibcode.modelLibrary.model;

public enum CategoryEnum {
    ACCESSORIES,
    HOUSEHOLD,
    ENTERTAINMENT,
    CULTURE,
    FASHION,
    SPORT,
    GROCERIES,
    SERVICES,
    GAMES,
    HEALTH,
    RESTAURANTS;

    private static CategoryEnum[] allValues = values();

    public static CategoryEnum fromOrdinal(int n) {
        if (n < 0 || n >= allValues.length) {
            throw new RuntimeException();
        } else {
            return allValues[n];
        }
    }
}
