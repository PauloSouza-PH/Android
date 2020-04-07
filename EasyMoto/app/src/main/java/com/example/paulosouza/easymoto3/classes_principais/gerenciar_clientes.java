package com.example.paulosouza.easymoto3.classes_principais;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.paulosouza.easymoto3.R;
import com.example.paulosouza.easymoto3.objetos.Adaptador;
import com.example.paulosouza.easymoto3.objetos.Cliente;
import com.example.paulosouza.easymoto3.objetos.Contas;
import com.example.paulosouza.easymoto3.objetos.Data_atual;
import com.example.paulosouza.easymoto3.objetos.Formato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulo.souza on 04/08/2017.
 * Projeto Moto Facil
 */

public class gerenciar_clientes extends AppCompatActivity{
    private List<Cliente> clientes = new ArrayList<>();
    private String user;
    private FirebaseDatabase database;
    private DatabaseReference database_cliente,database_Movimentos;
    private Boolean lCliente = false;
    private ProgressDialog dialog;

    protected void onCreate(Bundle instance){
        super.onCreate(instance);
        setContentView(R.layout.gerenciar_clientes);

        Intent itGet = getIntent();
        user = itGet.getStringExtra("USER");

        dialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        database_cliente = database.getReference().child("Usuarios").child(user).child("Clientes_Avulso");
        new carregaDados().execute();
    }

    private class carregaDados extends AsyncTask<Boolean,Boolean,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Carregando clientes do banco de dados");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            carregaClientes();

            while(!lCliente){
                SystemClock.sleep(2000);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            setView();
        }
    }

    public void carregaClientes(){
        database_cliente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clientes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cliente cliente = snapshot.getValue(Cliente.class);
                    clientes.add(cliente);
                }
                lCliente = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.setMessage("Erro ao carregar banco de dados");
            }
        });
    }

    private void setView() {
        final ListView view = (ListView) findViewById(R.id.listview_clientes);
        final Adaptador adaptador = new Adaptador(clientes,this);
        view.setAdapter(adaptador);

        EditText texto = (EditText) findViewById(R.id.auto_complete_cliente);
        texto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
               final Cliente cliente = clientes.get(position);
               database = FirebaseDatabase.getInstance();
               database_cliente = database.getReference().child("Usuarios").child(user).child("Clientes_Avulso").child(cliente.getId());
               database_Movimentos = database.getReference().child("Usuarios").child(user).child("Movimentos").push();

               MenuBuilder menu = new MenuBuilder(view.getContext());
               MenuInflater inflater = new MenuInflater(view.getContext());
               inflater.inflate(R.menu.menu_cliente,menu);

               MenuPopupHelper optionsMenu = new MenuPopupHelper(view.getContext(),menu,view);
               optionsMenu.setForceShowIcon(true);

               menu.setCallback(new MenuBuilder.Callback() {
                   @Override
                   public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                       switch(item.getItemId()){
                           case R.id.add_corrida_cliente:
                               final EditText insere = new EditText(view.getContext());
                               insere.setGravity(Gravity.CENTER);
                               insere.setInputType(InputType.TYPE_CLASS_NUMBER);
                               insere.addTextChangedListener(new Formato(insere));
                               AlertDialog.Builder alerta = new AlertDialog.Builder(view.getContext());
                               alerta.setMessage("Informe o valor da corrida");
                               alerta.setView(insere);
                               alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       Double nvlr = cliente.getValor();
                                       String xtexto = insere.getText().toString().replaceAll("[R$]"," ");
                                       xtexto = xtexto.replaceAll("[,]",".");
                                       final Double nVlrAdd = Double.parseDouble(xtexto);

                                       final Double saldoRestante = nvlr - nVlrAdd;

                                       if((saldoRestante) <= 0){
                                           AlertDialog.Builder confirma = new AlertDialog.Builder(view.getContext());
                                           confirma.setMessage("Debitar assim mesmo?");
                                           confirma.setTitle("O cliente esta sem saldo");
                                           confirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   cliente.setValor(saldoRestante);
                                                   database_cliente.setValue(cliente);
                                                   Data_atual data = new Data_atual();
                                                   Contas contas = new Contas(database_Movimentos.getKey(), cliente.getId(),nVlrAdd, 5, "Corrida realizado com cliente em divida");
                                                   database_Movimentos.setValue(contas);
                                                   Toast.makeText(view.getContext(),"Saldo do cliente atualizado!!",Toast.LENGTH_LONG).show();
                                               }
                                           });
                                           confirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   Toast.makeText(view.getContext(),"Procedimento cancelado!!",Toast.LENGTH_LONG).show();
                                               }
                                           });
                                           confirma.create();
                                           confirma.show();
                                       }else {
                                           cliente.setValor(saldoRestante);
                                           database_cliente.setValue(cliente);
                                           Data_atual data = new Data_atual();
                                           Contas contas = new Contas(database_Movimentos.getKey(), cliente.getId(), nVlrAdd, 7, "Corrida realizada para o cliente em dia");
                                           database_Movimentos.setValue(contas);
                                           Toast.makeText(view.getContext(),"Saldo atualizado!!",Toast.LENGTH_LONG).show();
                                       }

                                   }
                               });
                               alerta.setIcon(R.drawable.ic_valor);
                               alerta.setTitle("Debitar corrida");
                               alerta.create();
                               alerta.show();
                               break;

                           case R.id.add_fundos:
                               final EditText edt = new EditText(view.getContext());
                               edt.setGravity(Gravity.CENTER);
                               edt.setInputType(InputType.TYPE_CLASS_NUMBER);
                               edt.addTextChangedListener(new Formato(edt));
                               AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                               alert.setMessage("Informe o valor para adicionar");
                               alert.setView(edt);
                               alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       Double nvlr = cliente.getValor();
                                       String xtexto = edt.getText().toString().replaceAll("[R$]"," ");
                                       xtexto = xtexto.replaceAll("[,]",".");
                                       Double nVlrAdd = Double.parseDouble(xtexto);
                                       Double saldoAtualizado = nVlrAdd + nvlr;
                                       cliente.setValor(saldoAtualizado);
                                       database_cliente.setValue(cliente);
                                       Data_atual  data = new Data_atual();

                                       String cMsg = "";
                                       int tipo;

                                       if(nvlr < 0 && saldoAtualizado > 0) {
                                           cMsg = "Quitou divida e ficou com credito";
                                           tipo = 6;
                                       }else{
                                           cMsg = "Adição de fundo monetário ao cliente";
                                           tipo = 0;
                                       }

                                       Contas contas = new Contas(database_Movimentos.getKey(), cliente.getId(), nVlrAdd, tipo, cMsg);
                                       database_Movimentos.setValue(contas);
                                       Toast.makeText(view.getContext(),"Saldo atualizado!!",Toast.LENGTH_LONG).show();
                                   }
                               });
                               alert.setIcon(R.drawable.ic_valor);
                               alert.setTitle("Creditar valor");
                               alert.create();
                               alert.show();
                               break;
                           case R.id.remove_cliente:
                               AlertDialog.Builder confirmacao = new AlertDialog.Builder(view.getContext());
                               confirmacao.setMessage("Deletar cliente " + cliente.getNome().trim() + "?");
                               confirmacao.setTitle("Confirmar remoção");
                               confirmacao.setIcon(R.drawable.ic_remover);
                               confirmacao.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       adaptador.remove(cliente);
                                       database_cliente.removeValue();
                                   }
                               });
                               confirmacao.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {  }
                               });
                               confirmacao.create();
                               confirmacao.show();
                               break;
                       }
                       return true;
                   }
                   @Override
                   public void onMenuModeChange(MenuBuilder menu) { }
               });
               optionsMenu.show();
               return true;
           }
       });

    }

}
