package org.soonerrobotics;

import java.util.concurrent.TimeUnit;

import org.soonerrobotics.ROSTypes.ROSMessage;

public class Main {

    public static class GPSFeedback {
        public double latitude;
        public double longitude;
    }

    public static class MotorInput {
        public double forward_velocity;
        public double angular_velocity;
    }

    public static void main(String[] args) throws Exception {
        Scrabby client = new Scrabby("127.0.0.1", 9092);

        client.subscribe("/autonav/MotorFeedback", new ScrabbySubscriber() {
            @Override
            public void onReceive(ROSMessage message) {
                GPSFeedback gps_msg = Scrabby.parseData(message, GPSFeedback.class);
                // System.out.println("Lat: " + gps_msg.latitude + ", Lon: " + gps_msg.longitude);
            }
        });

        client.start();

        MotorInput motorInput = new MotorInput();
        motorInput.forward_velocity = 1;
        motorInput.angular_velocity = 0;

        while (true) {
            TimeUnit.MILLISECONDS.sleep(100);
            client.publish("/onboarding/MotorInput", motorInput, MotorInput.class);
        }
    }
}