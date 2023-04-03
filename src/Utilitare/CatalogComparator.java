//Clasa utilitara pentru a sorta obiecte de tip Catalog.
package Utilitare;

import java.util.Comparator;
import Persoane.Catalog;
public class CatalogComparator implements Comparator<Catalog>{

    private boolean crescator;

    public CatalogComparator(boolean crescator) {
        this.crescator = crescator;
    }

    @Override
    public int compare(Catalog c1, Catalog c2) {
        int rezultat = c1.getNumarPersoane() - c2.getNumarPersoane() ;
        return crescator ? rezultat : -rezultat;
    }

}
