package com.example.retoappsfinal;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class HoleWorker extends Thread {

    private MapsActivity ref;
    private boolean isAlive;

    public HoleWorker(MapsActivity ref) {
        this.ref = ref;
        isAlive = true;
    }

    public void run() {
        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive) {
            delay(3000);
            String response = utilDomi.GETrequest(Constants.BASEURL+"holes.json");
            Type type = new TypeToken<HashMap<String, PositionContainer>>(){}.getType();
            HashMap<String, PositionContainer> holes = gson.fromJson(response, type);
            ArrayList<Position> positions = new ArrayList<>();
            if (holes != null) {
                holes.forEach(
                        (key, value) -> {
                            PositionContainer posCon = value;
                            double lat = posCon.getPos().getLat();
                            double lng = posCon.getPos().getLng();
                            positions.add(new Position(lat, lng));
                        }
                );
            }
            ref.updateHoles(positions);
        }
    }

    public void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
