package com.example.paulosouza.easymoto3.classes_principais;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulosouza.easymoto3.R;
import com.example.paulosouza.easymoto3.objetos.Contas;
import com.example.paulosouza.easymoto3.objetos.Data_atual;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by paulosouza on 03/08/2017.
 * New File
 */

public class Despesas extends AppCompatActivity {
    private String user;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.adicionar_despesa);
        Intent itGet = getIntent();
        user = itGet.getStringExtra("USER");


        setInicial();
    }

    private void setInicial() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_despesa);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, new String[]{"Combustivel", "Peças", "Outras despesas"});
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int imgResource;
                TextView tTexto = (TextView) findViewById(R.id.textview_view1);
                EditText edtTex = (EditText) findViewById(R.id.id_edt_texto);
                switch ((int) id) {
                    case 0:
                        tTexto.setText(R.string.desc1);
                        imgResource = R.drawable.ic_lgasolina;
                        edtTex.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edtTex.setText("0");
                        break;
                    case 1:
                        tTexto.setText(R.string.desc2);
                        imgResource = R.drawable.ic_menu_manage;
                        edtTex.setInputType(InputType.TYPE_CLASS_TEXT);
                        edtTex.setText("");
                        break;
                    default:
                        tTexto.setText(R.string.desc3);
                        imgResource = R.drawable.ic_observacao;
                        edtTex.setInputType(InputType.TYPE_CLASS_TEXT);
                        edtTex.setText("");
                        break;
                }

                tTexto.setCompoundDrawablesRelativeWithIntrinsicBounds(imgResource,0,0,0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Button add_despesa = (Button) findViewById(R.id.btn_add_despesa);
        add_despesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinn_select = (Spinner) findViewById(R.id.spinner_despesa);
                EditText valor = (EditText) findViewById(R.id.id_valor_despesa);
                EditText Desc = (EditText) findViewById(R.id.id_edt_texto);

                if(!(valor.getText().toString().isEmpty() || Desc.getText().toString().isEmpty())){
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference database_Movimentos = db.getReference().child("Usuarios").child(user).child("Movimentos").push();

                    Double nVlr = Double.parseDouble(valor.getText().toString());
                    String descricao = Desc.getText().toString();

                    if(spinn_select.getSelectedItemPosition() == 0){
                        descricao += " Litros";
                    }

                    Data_atual data = new Data_atual();

                    Contas contas = new Contas(database_Movimentos.getKey(),user,nVlr,spinn_select.getSelectedItemPosition()+1,descricao);
                    database_Movimentos.setValue(contas);

                    Toast.makeText(Despesas.this,"Despesa adicionada",Toast.LENGTH_LONG).show();
                    valor.setText("");
                    Desc.setText("");
                }else{
                    Toast.makeText(Despesas.this,"Ainda existem campos em branco",Toast.LENGTH_LONG).show();
                }

            }
        });

    }


}
