package com.example.paulosouza.easymoto3.classes_principais;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paulosouza.easymoto3.R;
import com.example.paulosouza.easymoto3.objetos.Cliente;
import com.example.paulosouza.easymoto3.objetos.Contas;
import com.example.paulosouza.easymoto3.objetos.Data_atual;
import com.example.paulosouza.easymoto3.objetos.Formato;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by paulosouza on 03/08/2017.
 * Projeto Moto Facil
 */

public class CorridaAvulsa extends AppCompatActivity {
    private String user;
    private EditText nome,numero,valor;
    private Boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.adicionar_corrida_avulsa);

        nome   = (EditText) findViewById(R.id.id_nome_cliente);
        numero = (EditText) findViewById(R.id.id_telefone_cliente);
        valor  = (EditText) findViewById(R.id.id_valor_corrida);

        valor.addTextChangedListener(new Formato(valor));
        valor.setText(R.string.valor_padrao);

        Intent ab = getIntent();
        user = ab.getStringExtra("USER");

        final Button btnOk = (Button) findViewById(R.id.imageButton);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String xtexto = valor.getText().toString().replaceAll("[R$]"," ");
                xtexto = xtexto.replaceAll("[,]",".");
                Double nVlr = Double.parseDouble(xtexto);

                if(!(nome.getText().toString().isEmpty() || numero.getText().toString().isEmpty() || nVlr == 0) ) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference base_cliente = database.getReference().child("Usuarios").child(user).child("Clientes_Avulso").push();
                    DatabaseReference base_contas = database.getReference().child("Usuarios").child(user).child("Movimentos").push();

                    Data_atual data = new Data_atual();

                    Cliente cliente = new Cliente(base_cliente.getKey(), nome.getText().toString(), numero.getText().toString(), 0.0,false);
                    Contas contas = new Contas(base_contas.getKey(),cliente.getId(),nVlr,0,"Inclusão de corrida avulsa");

                    base_cliente.setValue(cliente);
                    base_contas.setValue(contas);

                    nome.setText("");
                    numero.setText("");

                    Toast.makeText(btnOk.getContext(), "Corrida adicionada e cliente salvo", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(btnOk.getContext(), "Ainda existem campos em branco", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
