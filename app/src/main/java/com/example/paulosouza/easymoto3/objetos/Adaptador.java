package com.example.paulosouza.easymoto3.objetos;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.paulosouza.easymoto3.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by paulo.souza on 04/08/2017.
 */

public class Adaptador extends BaseAdapter {
    private List<Cliente> itensFiltrados;
    private List<Cliente> clientes;
    private Activity activity;

    public Adaptador(List<Cliente> clientes, Activity activity) {
        this.clientes = clientes;
        this.activity = activity;
        this.itensFiltrados = clientes;
    }

    @Override
    public int getCount() {
        return itensFiltrados.size();
    }

    @Override
    public Object getItem(int i) {
        return itensFiltrados.get(i);
    }

    public void remove(Cliente i) {
        itensFiltrados.remove(i);
        clientes.remove(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vW  = activity.getLayoutInflater().inflate(R.layout.view_visualizar_clientes,viewGroup,false);
        Cliente cliente = itensFiltrados.get(i);

        if(cliente.getValor() < 0){
            vW.setBackgroundResource(R.drawable.animation_red);
        }else if(cliente.getValor() > 0){
            vW.setBackgroundResource(R.drawable.animation_green);
        }

        TextView v2 = (TextView) vW.findViewById(R.id.v2);

        if(cliente.getFavorito()){
            v2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nome_do_cliente,0,R.drawable.ic_favorito,0);
        }

        TextView v3 = (TextView) vW.findViewById(R.id.v3);
        TextView v4 = (TextView) vW.findViewById(R.id.v4);

        v2.setText(cliente.getNome());
        v3.setText(cliente.getNumero());
        v4.setText(Double.toString(cliente.getValor()));

        return vW;
    }

    public android.widget.Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = clientes.size();
                    results.values = clientes;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    List<Cliente> itens_filtrados = new ArrayList<Cliente>();

                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < clientes.size(); i++) {
                        Cliente data = clientes.get(i);

                        filtro = filtro.toString().toLowerCase();
                        String condicao = data.getNome().toLowerCase();
                        String condicao2 = data.getNumero();

                        if (condicao.contains(filtro)) {
                            //se conter adiciona na lista de itens filtrados.
                            itens_filtrados.add(data);
                        }else if(condicao2.contains(filtro)){
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
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                itensFiltrados = (List<Cliente>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }
        };
    }
}
