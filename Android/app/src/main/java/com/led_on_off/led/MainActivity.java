package com.led_on_off.led;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {


    Button Ses, Isik, Kes, Hakkinda;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(CihazListesi.EXTRA_ADDRESS); //Bluetooth'un adresini alır.

        setContentView(R.layout.activity_kontrol);


        Ses = (Button) findViewById(R.id.Ses);
        Isik = (Button) findViewById(R.id.Isik);
        Kes = (Button) findViewById(R.id.Sicaklik);
        Hakkinda = (Button) findViewById(R.id.Hakkinda);

        new ConnectBT().execute(); //Bağlan sınıfını çağır

        //Veri göndermek için gerekli setOnClickler.
        Ses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sesCalis();
            }
        });

        Isik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isikCalis();
            }
        });

        Kes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baglantiSonu();
            }
        });
    }

    private void isikCalis() {
        if (btSocket!=null) {
            try {
                btSocket.getOutputStream().write("0".toString().getBytes());
                KaydetIsik();
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sesCalis() {
        if (btSocket!=null) {
            try {
                btSocket.getOutputStream().write("1".toString().getBytes());
                KaydetSes();
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void baglantiSonu() {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout
    }




    // Toast oluşturma metodu
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_kontrol, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private class ConnectBT extends AsyncTask<Void, Void, Void> { // UI thread
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Bağlanıyor...", "Lütfen Bekleyin");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter(); //Mobilin bluetooth'u
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address); //Adres ile bluetootha bağlanır
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID); //Bir SSP bağlantısı oluşturur
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Bağlantı Hatası Oluştu. Lütfen Tekrar Deneyin.");
                finish();
            }
            else {
                msg("Bağlandı.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    public void KaydetSes(){
        Ses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ad, durum;
                ad ="Ses Sensörü";
                durum = "Açık";
                if (ad.matches("") || durum.matches("")) {
                    Toast.makeText(getApplicationContext(), "Bir hata oluştu", Toast.LENGTH_LONG).show();
                } else {
                    Database db = new Database(getApplicationContext());
                    db.SensorDurumEkle(ad, durum);
                    db.close();
                }
            }
        });
    }

    public void KaydetIsik(){
        Ses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ad, durum;
                ad ="Işık Sensörü";
                durum = "Açık";
                if (ad.matches("") || durum.matches("")) {
                    Toast.makeText(getApplicationContext(), "Bir hata oluştu", Toast.LENGTH_LONG).show();
                } else {
                    Database db = new Database(getApplicationContext());
                    db.SensorDurumEkle(ad, durum);
                    db.close();
                }
            }
        });
    }
}
