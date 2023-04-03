package Utilitare;

//Interfata pentru afisarea unor date specifice atat elevilor, cat si studentilor.
public interface Functii {
    StringBuilder listaMaterii(int clasa); //lista care se va furniza

    //Functie pentru elevi - acestia pot avea mai multe note la o materie
    default void carnet(int an, Materie[][] cursuri, int [][][] note){
        int index=1;
        if(an>8)
            index=9;
        if(getClass().getName().equals("ElevScoalaGenerala") && (an<0 ||an>8))
            System.out.print("\n\t!!!Elevul trebuie sa fie intr-o clasa de la 1 la 8!!!\n");
        else
            if(getClass().getName().equals("ElevLiceu") && (an<9 ||an>12))
                System.out.print("\n\t!!!Elevul trebuie sa fie intr-o clasa de la 9 la 12!!!\n");
            else{
                System.out.println("\n\t!!!Afisarea carnenului!!!" );
                int i=0,j;
                while(i<cursuri[an-index].length && cursuri[an-index][i]!=null) {
                    System.out.print("\nNotele la " + cursuri[an - index][i].getDenumire() + ": ");
                    j = 0;
                    while (j < note[an - index][i].length && note[an - index][i] != null){
                        System.out.print(note[an - index][i][j] + " ");
                        j++;
                    }
                    i++;
                }
            }
    }

    //Functie pentru studenti - au o singura nota la o materie;
    default void carnet(int an, Materie[][] cursuri, int [][] note){
        System.out.println("\n\t!!!Afisarea carnenului!!!" );
        int i=0;
        while(i<cursuri[an-1].length && cursuri[an-1][i]!=null) {
            System.out.print("\nNota la " + cursuri[an - 1][i].getDenumire()
                    + ": " + note[an-1][i]) ;
            i++;
        }
    }

    //Functie pentru a valida CNP-ul introdus
    static int validatorCNP(String CNP){
        char sex;
        int lunaNastere,ziNastere;
        //Regex pentru a verifica daca CNP-ul are 13 caractere si toate cifre.
        if(!CNP.matches("[0-9]{13}"))
            return -1;
        else{
            sex=CNP.charAt(0);
            //Prima cifra din CNP trebuie sa fie 1,2,5 sau 6.
            if (sex!='1' && sex!='2' && sex!='5' && sex!='6')
                return -2;
            lunaNastere=Integer.parseInt(CNP.substring(3,5));
            ziNastere=Integer.parseInt(CNP.substring(5,7));
            //Validam ziua de nastere
            if(ziNastere<0 || ziNastere>31)
                return -3;
            else
                //Validam luna
                if (lunaNastere<0 || lunaNastere>12)
                    return -4;
        }
        return 1;
    }
}
