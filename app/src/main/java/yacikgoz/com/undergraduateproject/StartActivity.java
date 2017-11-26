package yacikgoz.com.undergraduateproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * Created by yacikgoz on 23.11.2017.
 */

public class StartActivity extends Activity implements View.OnClickListener{

    public static Socket socket;
    public static PrintWriter printWriter_socket;
    public static BufferedReader in;
    volatile boolean isConnected = false;
    EditText ipTxt, portTxt;
    Button btn, btn2;
    String ip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int a;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ipTxt = findViewById(R.id.ip_text);
        portTxt = findViewById(R.id.port_text);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(this);
        ip = getIpAddress();
        if(ip != null) {
            a = ip.lastIndexOf('.');
            if (a != -1) {
                ip = ip.substring(0,a);
                ipTxt.setText(ip + ".20");
            }
        }

    }
    public static String getIpAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress=inetAddress.getHostAddress();
                        Log.e("IP address",""+ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Socket exception", ex.toString());
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                SocketCreator socketCreator = new SocketCreator();
                socketCreator.execute((Void) null);
                while (!isConnected) {
                    try {
                        Thread.sleep(100); //ms
                    } catch (Exception e) {
                        break;
                    }
                }

                if(isConnected) {
                    Toast.makeText(getApplicationContext(), "baglandi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, DrawerActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (printWriter_socket != null) {

            printWriter_socket.print("e");
            printWriter_socket.flush();
            printWriter_socket.close();
        }

        try {
            socket.close();
        } catch (IOException e) {
            Log.e("Closing socket", e.getMessage());
        }
    }

    public class SocketCreator extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected Void doInBackground(Void... params) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket(ipTxt.getText().toString(), Integer.parseInt(portTxt.getText().toString()));
                            if(socket.isConnected()) {
                                printWriter_socket = new PrintWriter(socket.getOutputStream());
                                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                                isConnected = true;
                            } else{
                                Toast.makeText(StartActivity.this, "Yanlış IP veya Port Numarası!!!", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {

                            Log.e("Thread", e.getMessage());
                            System.exit(0);
                        }
                    }
                }).start();
                isConnected = true;

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
        }
    }
}
