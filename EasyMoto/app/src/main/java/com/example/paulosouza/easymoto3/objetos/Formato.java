package com.example.paulosouza.easymoto3.objetos;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by paulosouza on 03/08/2017.
 */

public class Formato implements TextWatcher {
    final EditText campo;
    private boolean isUpdate = false;
    private NumberFormat nf = NumberFormat.getCurrencyInstance();

    public Formato(EditText campo) {
        super();
        this.campo = campo;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isUpdate) {
            isUpdate = false;
            return;
        }

        isUpdate = true;
        String str = s.toString();
        boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) && (str.indexOf(".") > -1 || str.indexOf(",") > -1));

        if (hasMask) {
            str = str.replaceAll("[R$]", "").replaceAll("[,]", "").replaceAll("[.]", "");
        }

        try {
            str = nf.format(Double.parseDouble(str) / 100);
            campo.setText(str);
            campo.setSelection(campo.getText().length());
        } catch (NumberFormatException e) {
            s = "";
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
