package com.example.paulosouza.easymoto3.classes_principais;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.example.paulosouza.easymoto3.R;
import com.example.paulosouza.easymoto3.objetos.AdapterContas;
import com.example.paulosouza.easymoto3.objetos.Contas;
import com.example.paulosouza.easymoto3.objetos.Data_atual;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Paulo on 20/08/2017.
 * New File
 */

public class Gerenciar_Contas extends AppCompatActivity {
    private List<Contas> List_contas = new ArrayList<>();
    private ListView view;
    private AdapterContas adapterContas;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Boolean lContas = false;
    private Data_atual data_atual;
    private ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contas);
        view = (ListView) findViewById(R.id.visao_contas);
        setEventoBotoes();

        Intent itGet = getIntent();
        String user = itGet.getStringExtra("USER");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Usuarios").child(user).child("Movimentos");
        data_atual = new Data_atual();
        dialog = new ProgressDialog(this);

        new carregaDados().execute();
    }


    private class carregaDados extends AsyncTask<Boolean,Boolean,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Carregando movimentações");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            carregaContas();

            while(!lContas){
                SystemClock.sleep(2000);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            adapterContas = new AdapterContas(List_contas,Gerenciar_Contas.this);
            view.setAdapter(adapterContas);
            adapterContas.getFilter().filter(data_atual.getData());
            dialog.dismiss();
        }
    }

    public void carregaContas(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List_contas.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contas contas = snapshot.getValue(Contas.class);
                    List_contas.add(contas);
                }
                lContas = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setEventoBotoes() {
        final Button btnDtInicial = (Button) findViewById(R.id.btn_dt_inicial);
        final Data_atual data_atual = new Data_atual();
        btnDtInicial.setText(data_atual.getData());

        final int ano = data_atual.getAno();
        final int mes = data_atual.getMes();
        final int dia = data_atual.getDia();

        btnDtInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Gerenciar_Contas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Data_atual dataAtual = new Data_atual(myCalendar.getTime());
                        String data = dataAtual.getData();

                        btnDtInicial.setText(data);
                        adapterContas.getFilter().filter(data);

                    }
                }, ano, mes, dia);
                dialog.show();
            }
        });
    }

}
