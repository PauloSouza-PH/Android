package com.example.paulosouza.easymoto3.classes_principais;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.paulosouza.easymoto3.R;
import com.example.paulosouza.easymoto3.objetos.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.Serializable;

/**
 * Created by Paulo on 26/08/2017.
 * New File
 */

public class Splash extends AppCompatActivity implements Serializable {
    private static final int RC_SIGN_IN = 9001;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private GoogleApiClient GoogleApiClient;
    private TextView textoView;
    private Boolean lSignResult = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_splash);
        textoView = (TextView) findViewById(R.id.textSplashScreen);
        CarregaAPI();
        new EfetuarLogin().execute();
    }

    private class EfetuarLogin extends AsyncTask<Boolean,Boolean,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user = mAuth.getCurrentUser();
            textoView.setText("Realizando login na conta do Google");
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            while(user == null) {
                if(!lSignResult) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(GoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    lSignResult = true;
                }else{
                    SystemClock.sleep(5000);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Intent principal = new Intent(Splash.this,MainActivity.class);
            Usuario usuario = new Usuario(user.getDisplayName(),user.getEmail(),user.getUid(),user.getPhotoUrl().toString());
            principal.putExtra("Usuario",usuario);
            startActivity(principal);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                ConectandoFirebase(account);
            }else{
                lSignResult = false;
                textoView.setText("Erro ao realizado Login no Google\nTentando novamente");
            }
        }
    }

    public void CarregaAPI(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleApiClient = new GoogleApiClient.Builder(Splash.this)
                .enableAutoManage(Splash.this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        textoView.setText("Erro ao realizar login Google");
                    }
                } )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
    }

    public void ConectandoFirebase(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                user = mAuth.getCurrentUser();
                textoView.setText("Login realizado, carregando dados");
            }
        });
    }

}
