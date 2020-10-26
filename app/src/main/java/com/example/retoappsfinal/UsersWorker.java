package com.example.retoappsfinal;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class UsersWorker extends Thread{

    private MapsActivity ref;
    private boolean isAlive;

    public UsersWorker(MapsActivity ref) {
        this.ref = ref;
        isAlive = true;
    }

    public void run() {
        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive) {
            delay(3000);
            String response = utilDomi.GETrequest(Constants.BASEURL+"users.json");
            Type type = new TypeToken<HashMap<String, PositionContainer>>(){}.getType();
            HashMap<String, PositionContainer> users = gson.fromJson(response, type);
            ArrayList<Position> positions = new ArrayList<>();
            if (users != null) {
                users.forEach(
                        (key, value) -> {
                            PositionContainer posCon = value;
                            double lat = posCon.getPos().getLat();
                            double lng = posCon.getPos().getLng();
                            if (!key.equals(ref.getUser())) {
                                positions.add(new Position(lat, lng));
                            }
                        }
                );
            }
            ref.updateUsers(positions);
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
