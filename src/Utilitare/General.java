package Utilitare;

import java.text.SimpleDateFormat;

public interface General {

    static int calculVarsta(DataNastere d) {
        String dataSiOra = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        int varsta=Integer.parseInt(dataSiOra.substring(0,4))-d.getAn();
        if(Integer.parseInt(dataSiOra.substring(5,7))<d.getLuna())
            varsta--;
        else
        if(Integer.parseInt(dataSiOra.substring(5,7))==d.getLuna())
            if(Integer.parseInt(dataSiOra.substring(8,10))<d.getZi())
                varsta--;
        return varsta;
    }

}
