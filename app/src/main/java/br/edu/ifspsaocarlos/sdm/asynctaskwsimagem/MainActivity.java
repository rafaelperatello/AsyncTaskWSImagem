package br.edu.ifspsaocarlos.sdm.asynctaskwsimagem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button btAcessarWs;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarWs.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    public void onClick(View v) {
        if (v == btAcessarWs) {
//            buscarImagem("http://www.nobile.pro.br/sdm/logo_ifsp.png");
            buscarImagem("https://static.pexels.com/photos/2394/lights-clouds-dark-car.jpg");
        }
    }

    private void buscarImagem(final String url) {
        AsyncTask<String, Void, Bitmap> tarefa = new AsyncTask<String, Void, Bitmap>() {
            protected void onPreExecute() {
                String[] message = url.split("/");

                progressDialog.setMessage("Downloading " + message[message.length - 1]);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }

            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap = null;

                try {
                    URL url = new URL(params[0]);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                } catch (IOException e) {
                    Log.e((String) getText(R.string.app_name), "Erro na recuperação de imagem");
                }
                return bitmap;
            }

            protected void onPostExecute(Bitmap bitmap) {
                progressDialog.dismiss();

                if (bitmap != null) {
                    ImageView ivImagem = (ImageView) findViewById(R.id.iv_imagem);
                    ivImagem.setImageBitmap(bitmap);
                }
            }
        };
        tarefa.execute(url);
    }
}