package Utilitare;

public enum Clase {
    ELEV_SCOALA_GENERALA(1),
    ELEV_LICEU(2),
    FMI_INFO(3),
    FMI_MATEMATICA(4),
    FMI_CTI(5),
    LOCATIE(6),
    MATERIE(7);

    private final int cod;

    Clase(int cod) {
        this.cod=cod;
    }
}
