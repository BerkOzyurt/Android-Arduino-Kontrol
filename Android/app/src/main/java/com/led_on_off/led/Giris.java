package com.led_on_off.led;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by BiruskSterk on 12/7/2017.
 */

public class Giris extends ActionBarActivity {

    Button girisYap, Devam;
    EditText kullanici, sifre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        Ileri();
        Kaydet();

    }

    public void Ileri(){
        Devam = (Button) findViewById(R.id.goOn);
        Devam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "KAYIT EKLENDI", Toast.LENGTH_SHORT).show();//uyari mesaji
                Intent intent = new Intent(Giris.this, CihazListesi.class);
                startActivity(intent);
            }
        });
    }

    public void Kaydet(){
        kullanici = (EditText) findViewById(R.id.kullanici);
        sifre = (EditText) findViewById(R.id.sifre);
        girisYap = (Button) findViewById(R.id.girisYap);
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ad, pass;
                ad = kullanici.getText().toString();
                pass = sifre.getText().toString();

                if (ad.matches("") || pass.matches("")) {
                    Toast.makeText(getApplicationContext(), "Tüm Bilgileri Eksiksiz Doldurunuz", Toast.LENGTH_LONG).show();
                } else {
                    Database db = new Database(getApplicationContext());
                    db.kullaniciEkle(ad, pass);//kullanıcı ekledik
                    db.close();


                }


            }
        });
    }
}