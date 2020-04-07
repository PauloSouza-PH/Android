package com.example.paulosouza.easymoto3.objetos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Paulo on 20/08/2017.
 * New File
 */

public class Data_atual {
    private Date data_atual;
    private Calendar cal;
    private SimpleDateFormat completo = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat hora = new SimpleDateFormat("HH:mm");

    public Data_atual(){
        Date data = new Date();
        cal = Calendar.getInstance();
        cal.setTime(data);
        data_atual = cal.getTime();
    }

    public Data_atual(Date dtDefinida){
        data_atual = dtDefinida;
    }

    public String getData() {
        return completo.format(data_atual);
    }

    public Date getDataCompleta(){
        return data_atual;
    }
    public String getHora(){
        return hora.format(data_atual);
    }
    public int getAno(){
        return cal.get(Calendar.YEAR);
    }
    public int getMes(){
        return cal.get(Calendar.MONTH);
    }
    public int getDia(){
        return cal.get(Calendar.DAY_OF_MONTH);
    }

}
