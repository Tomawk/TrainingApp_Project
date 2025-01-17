package it.dii.unipi.trainerapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Transactions {
    private static final String TAG = Transactions.class.getSimpleName();

    public static final String TRANSACTION_ACTION = "write-transaction";
    public static final String TRANSACTION_TYPES = "transaction-type";
    public static final String DATA = "data";

    public enum TRANSACTION_TYPE {
        NAME,
        HEART_RATE,
        SPEED,
        STEPS,
        ACTIVITY,
        PACE,
        DISTANCE
    }

    private static int writeSentOnHR = 0;
    private static int writeSentOnSpeed = 0;

    public static void writeAthleteName(Context context, String athleteName){
        broadcastMessage(context, TRANSACTION_TYPE.NAME, athleteName.getBytes(StandardCharsets.UTF_8));
        return;
    }

    public static void writeHeartRate(Context context, int heartRate) {
        BigInteger bigIntHR = BigInteger.valueOf(heartRate);
        broadcastMessage(context, TRANSACTION_TYPE.HEART_RATE, bigIntHR.toByteArray());
        return;
    }

    public static void writeSpeed(Context context, double speed) {
        double speedRounded = Math.round(speed * 100.0) / 100.0;
        byte[] speedBytes = ByteBuffer.allocate(8).putDouble(speedRounded).array();
        broadcastMessage(context, TRANSACTION_TYPE.SPEED, speedBytes);
        return;
    }

    public static void writeSteps(Context context, float steps){
        BigInteger bigIntSteps = BigInteger.valueOf((int) steps);
        broadcastMessage(context, TRANSACTION_TYPE.STEPS, bigIntSteps.toByteArray());
        return;
    }

    public static void writeActivity(Context context, String activityName, int activityInt){
        BigInteger bigIntActivity = BigInteger.valueOf(activityInt);
        broadcastMessage(context, TRANSACTION_TYPE.ACTIVITY, bigIntActivity.toByteArray());
        return;
    }

    public static void writePace(Context context, double pace) {
        double paceRounded = Math.round(pace * 10.0) / 10.0;
        byte[] paceBytes = ByteBuffer.allocate(8).putDouble(paceRounded).array();
        broadcastMessage(context, TRANSACTION_TYPE.PACE, paceBytes);
        return;
    }

    public static void writeDistance(Context context, double distance){
        double distanceRounded = Math.round(distance * 10.0) / 10.0;
        byte[] distanceBytes = ByteBuffer.allocate(8).putDouble(distanceRounded).array();
        broadcastMessage(context, TRANSACTION_TYPE.DISTANCE, distanceBytes);
        return;
    }

    private static void broadcastMessage(Context context, TRANSACTION_TYPE transaction_type, byte[] data){
        final Intent intent = new Intent(TRANSACTION_ACTION);
        intent.putExtra(TRANSACTION_TYPES, transaction_type);
        intent.putExtra(DATA, data);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        if(transaction_type == TRANSACTION_TYPE.HEART_RATE){
            writeSentOnHR++;
        }else if(transaction_type == TRANSACTION_TYPE.SPEED){
            writeSentOnSpeed++;
        }
        Log.v(TAG, "sending msg for " + transaction_type.name() + " | total write sent for HR: " + writeSentOnHR + " | for Speed: " + writeSentOnSpeed);
    }

}
