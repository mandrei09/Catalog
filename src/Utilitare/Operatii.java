package Utilitare;

public enum Operatii {
    CREATE("CREATE"),READ("READ"),UPDATE("UPDATE"),DELETE("DELETE");

    private final String valoare;

    Operatii(String valoare) {
        this.valoare = valoare;
    }
}
