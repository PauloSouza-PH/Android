package com.example.paulosouza.easymoto3.classes_principais;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulosouza.easymoto3.R;
import com.example.paulosouza.easymoto3.objetos.Cliente;
import com.example.paulosouza.easymoto3.objetos.Contas;
import com.example.paulosouza.easymoto3.objetos.Usuario;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Usuario user;
    private long lastBackPressTime = 0;
    private Toast toast;
    private ArrayList<Cliente> clientes;
    private ArrayList<Contas> contas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView nome = (TextView) headerView.findViewById(R.id.nome_conta);
        TextView mail = (TextView) headerView.findViewById(R.id.account_email);
        ImageView foto = (ImageView) headerView.findViewById(R.id.imageView);

        user = (Usuario) getIntent().getSerializableExtra("Usuario");

        nome.setText(user.getNome());
        mail.setText(user.getEmail());
        Picasso.with(getApplication()).load(user.getFoto()).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                int size = Math.min(source.getWidth(), source.getHeight());

                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }

                Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap,
                        BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);

                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "circle";
            }
        }).resize(256, 256).centerCrop().into(foto);

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {

            if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
                toast = Toast.makeText(this, "Pressione o Botão Voltar novamente para fechar o Aplicativo.", Toast.LENGTH_LONG);
                toast.show();
                this.lastBackPressTime = System.currentTimeMillis();
            } else {
                if (toast != null) {
                    toast.cancel();
                }
                super.onBackPressed();
            }
        }
    }

    public void showMenu(View view) {
        MenuBuilder menu = new MenuBuilder(view.getContext());
        MenuInflater inflater = new MenuInflater(view.getContext());
        inflater.inflate(R.menu.menu_add, menu);
        MenuPopupHelper optionMenu = new MenuPopupHelper(view.getContext(),menu,view);
        optionMenu.setForceShowIcon(true);

        menu.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_avulso:
                        Intent a = new Intent(MainActivity.this, CorridaAvulsa.class);
                        a.putExtra("USER",user.getUid());
                        startActivity(a);
                        return true;
                    case R.id.add_despesa:
                        Intent b = new Intent(MainActivity.this, Despesas.class);
                        b.putExtra("USER",user.getUid());
                        startActivity(b);
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) { }
        });

        optionMenu.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_clientes) {
            // Handle the camera action
            Intent c = new Intent(MainActivity.this,gerenciar_clientes.class);
            c.putExtra("USER",user.getUid());
            startActivity(c);
        } else if (id == R.id.nav_contas) {
            Intent d = new Intent(MainActivity.this,Gerenciar_Contas.class);
            d.putExtra("USER",user.getUid());
            startActivity(d);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
