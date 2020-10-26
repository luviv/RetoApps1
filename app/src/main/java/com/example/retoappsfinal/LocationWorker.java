package com.example.retoappsfinal;

import com.google.gson.Gson;

public class LocationWorker extends Thread {

    private MapsActivity ref;
    private boolean isAlive;

    public LocationWorker(MapsActivity ref) {
        this.ref = ref;
        isAlive = true;
    }

    @Override
    public void run() {
        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive) {
            //Hacer PUT de mi posición
            if (ref.getCurrentPos() != null) {
                utilDomi.PUTrequest("https://retoappsfinal.firebaseio.com/users/"+ref.getUser()+"/location.json", gson.toJson(ref.getCurrentPos()));
            }
            delay(5000);
        }

    }

    public void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void finish() {

    }
}
