package com.example.usuario.digiinvanders;
/**
 * @author Martin Calderon Blanco
 */



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;


//Clase Que crear los metodos de boton, censor y el cliente Socket
public class MainActivity extends AppCompatActivity implements SensorEventListener {

EditText el;


//Variables de la Clase MainActivity
    private static Socket s;
    private static java.io.PrintWriter printWriter;
    String message="";
    String message2="";
    private static String ip="192.168.100.10";
    private SensorManager sensorManager;
    private Sensor sensor;


    /**
     * Metodo que obtiene los valores del Sensor y los mensajes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



    }

    /**
     *Metodo del boton que envia un mensaje al socket en java
     * @param v
     */
    public void Disparar(View v){
        message="dispara";
        myTask mt = new myTask();
        mt.execute();

    }
    //Envia los Mensajes al SOcket en java
    class myTask extends AsyncTask<String,Void,Void>{
        /**
         * Envia los mensajes al socket
         * @param voids
         * @return null
         */
        @Override
        protected Void doInBackground(String... voids) {
            try{



                s=new Socket(ip,5000);//Se conecta al socket en el puerto 50000
                printWriter=new PrintWriter(s.getOutputStream());
                //printWriter.write(message);
                printWriter.write(message);
                printWriter.flush();
                printWriter.close();
                s.close();

            }catch(IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Obtiene los valores del acelerometro
     * @param event Movimiento del celular
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        if (Math.abs(y) > Math.abs(x)) {


            if (x >5) {
                message="Izquierda";
                Log.d("Lol",x+"");
                myTask mt = new myTask();
                mt.execute();
            }
            if (x < -5) {
                message="Derecha";
                Log.d("Lol",x+"");
                myTask mt = new myTask();
                mt.execute();
            }
        }
        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {

        }
    }


    /**
     *
     * @param sensor El censor del celular
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Registra los valores del acelerometro
     */
    @Override
    protected  void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Pausa la obetencion de valores
     */
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}


