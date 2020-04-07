package com.example.paulosouza.easymoto3.objetos;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.example.paulosouza.easymoto3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo on 20/08/2017.
 * New File
 */

public class AdapterContas extends BaseAdapter {
    private List<Contas> full_contas;
    private List<Contas> filter_contas;
    private Activity activity;

    public AdapterContas(List<Contas> full_contas, Activity activity) {
        this.full_contas = full_contas;
        this.filter_contas = full_contas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return filter_contas.size();
    }

    @Override
    public Object getItem(int position) {
        return filter_contas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void remove(Cliente i) {
        filter_contas.remove(i);
        full_contas.remove(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vW  = activity.getLayoutInflater().inflate(R.layout.visao_contas,parent,false);
        Contas contas = filter_contas.get(position);

        TextView time = (TextView) vW.findViewById(R.id.hora_viewcontas);
        TextView valor = (TextView) vW.findViewById(R.id.valor_viewContas);
        TextView tipo = (TextView) vW.findViewById(R.id.tipo);
        Button remover = (Button) vW.findViewById(R.id.btnRemove_viewContas);

        time.setText(contas.getHora());
        valor.setText(Double.toString(contas.getValor()));
        switch (contas.getTipo()){
            case 0:
                tipo.setText("Receita");
                tipo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_receita,0,0,0);
                break;
            case 1:
                tipo.setText("Abastec.");
                tipo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_despesa,0,0,0);
                break;
            case 2:
                tipo.setText("Peças");
                tipo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_despesa,0,0,0);
                break;
            case 3:
                tipo.setText("Outros");
                tipo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_despesa,0,0,0);
                break;
            case 5:
                tipo.setText("Corrida.D");
                tipo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_corrida_no_debito,0,0,0);
                break;

            case 6:
                tipo.setText("PG.Divida");
                tipo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_quitou_divida,0,0,0);
                break;

            case 7:
                tipo.setText("Corrida.C");
                tipo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_corrida_no_credito,0,0,0);
                break;

        }

        return vW;
    }

    public android.widget.Filter getFilter(){

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                if (filtro == null || filtro.length() == 0) {
                    results.count = full_contas.size();
                    results.values = full_contas;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    List<Contas> itens_filtrados = new ArrayList<>();
                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < full_contas.size(); i++) {
                        Contas data = full_contas.get(i);

                        String date = data.getData();

                        if(date.equals(filtro)){
                            itens_filtrados.add(data);
                        }

                    }
                    // Define o resultado do filtro na variavel FilterResults
                    results.count = itens_filtrados.size();
                    results.values = itens_filtrados;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filter_contas = (List<Contas>) results.values; // Valores filtrados.
                notifyDataSetChanged();
            }
        };
    }


}
