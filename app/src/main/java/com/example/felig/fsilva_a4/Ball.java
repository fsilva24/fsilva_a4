package com.example.felig.fsilva_a4;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class Ball extends Activity implements SensorEventListener
{
    /** Called when the activity is first created. */
    CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    ShapeDrawable mTarget = new ShapeDrawable();
    public static float x;
    public static float y;
    private static int targetX;
    private static int targetY;

    private float mDispWidth;
    private float mDispHeight;
    private Display disp;
    Sensor mSensorAcceler;
    Sensor mSensorLight;
    BallTarget targetBall;
    int mMonsterScore;
    int mPatrolScore;

    private SensorManager sensorManager = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSensorAcceler = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, mSensorAcceler, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_FASTEST);

        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
        // setContentView(R.layout.main);

        targetBall = createTarget();
        disp = getWindowManager().getDefaultDisplay();
        mDispWidth = disp.getWidth();
        mDispHeight = disp.getHeight();
        x = mDispWidth / 2;
        y = 50;



    }

    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // the values you were calculating originally here were over 10000!
                if (sensorEvent.values[0] > 0){
                    x -= sensorEvent.values[0];
                    targetX += sensorEvent.values[0];
                    targetBall.setX(targetX);
                    checkBound();
                    //checkCollision(targetX, targetY, targetBall.getDiameter()/2, (int)x,(int)y,50);
                }else{
                    x -= sensorEvent.values[0];
                    targetY += sensorEvent.values[0];
                    targetBall.setY(targetY);
                    checkBound();

                }
                //x = (int) Math.pow(sensorEvent.values[1], 2);
                if (sensorEvent.values[1] < 0){
                    y = (int) Math.pow(sensorEvent.values[1], 2)*2;
                    targetX += sensorEvent.values[0];
                    targetBall.setX(targetX);
                    checkBound();
                    checkCollision(targetX, targetY, targetBall.getDiameter()/2, (int)x,(int)y,50);
                }else {
                    targetY += sensorEvent.values[0];
                    targetBall.setY(targetY);
                    checkBound();
                    //checkCollision(targetX, targetY, targetBall.getDiameter()/2, (int)x,(int)y,50);
                }
            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {

                float maxLight = mSensorLight.getMaximumRange();
                float lightVal = sensorEvent.values[0];
                if (lightVal > maxLight * 0.75){
                    mCustomDrawableView.setBackgroundColor(Color.BLUE);
                    mDrawable.getPaint().setColor(Color.WHITE);
                } else if (lightVal > maxLight * 0.5){
                    mCustomDrawableView.setBackgroundColor(Color.GREEN);
                }  else if (lightVal > maxLight * 0.25){
                    mCustomDrawableView.setBackgroundColor(Color.YELLOW);
                } else {
                    mCustomDrawableView.setBackgroundColor(Color.WHITE);
                }
            }
        }

        createTarget();
        /*Runnable helloRunnable = new Runnable() {
            public void run() {
                createTarget();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);*/
        mCustomDrawableView.invalidate();
    }

    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
               SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public class CustomDrawableView extends View
    {
        static final int width = 50;
        static final int height = 50;

        public CustomDrawableView(Context context)
        {
            super(context);

            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xff74AC23);
            mDrawable.setBounds((int)x, (int)y, (int)(x + width), (int)(y + height));

            mTarget = new ShapeDrawable(new OvalShape());
            mTarget.getPaint().setColor(0xff74AC23);
            mTarget.setBounds((int)targetX, (int)targetY, (int)(targetX + width), (int)(targetY + height));


        }

        protected void onDraw(Canvas canvas)
        {
            RectF target = new RectF( targetBall.getX(),targetBall.getY(),
                    targetBall.getX() +targetBall.getDiameter(),
                    targetBall.getY()+ targetBall.getDiameter());
            RectF oval = new RectF(Ball.x, Ball.y, Ball.x + width, Ball.y
                    + height);
            RectF village = new RectF(0, (mDispHeight / 20), mDispWidth,(mDispHeight / 20) + 20);
            // set bounds of rectangle
            Paint black = new Paint(); // set some paint options
            Paint t = new Paint();
            Paint greenPaint = new Paint();
            greenPaint.setColor(Color.GREEN);
            black.setColor(Color.BLACK);
            t.setColor(targetBall.getColor());
            canvas.drawOval(oval, black);
            canvas.drawOval(target,t);
            canvas.drawOval(village, greenPaint);
            //invalidate();
        }
    }
    public void checkBound () {
        final float rightBound = mDispWidth - 50;
        final float leftBound = 0;
        if (x >= rightBound){
            x = rightBound;
        }
        if (x <= leftBound){
            x = leftBound;
        }
        if (targetX >= rightBound){
            targetX = 100;
        }
        if (targetX <= leftBound){
            targetY = 1700;
        }
        if (targetY >= mDispHeight - 100){
            targetY = 100;
        }
        if (targetY <= mDispHeight / 20){
            targetY = 1700;
            mMonsterScore += 1;
            Toast.makeText(this, "Monsters: " + mMonsterScore + " | " + "Saves: " + mPatrolScore, Toast.LENGTH_SHORT).show();
        }
    }

    public void checkCollision(int x1, int y1, int r1, int x2, int y2, int r2){
        if (Math.pow((x2-x1),2) + Math.pow((y1-y2),2) <= (Math.pow((r1+r2),2))){
            targetY = 1700;
            mPatrolScore += 1;
            Toast.makeText(this, "Monsters: " + mMonsterScore + " | " + "Saves: " + mPatrolScore, Toast.LENGTH_SHORT).show();
        }
    }
    public BallTarget createTarget(){
        int diameter = 100 ;
        int minX = 50;
        int maxX = (int)mDispWidth - 80;
        int minY = (int)(mDispHeight * 0.5);
        int maxY = (int)(mDispHeight * 0.95);
        int targetColor;

        Random rand = new Random();
        int randomX = rand.nextInt() * (700 - 200) + 200;
        int randomY = rand.nextInt() * (1700 - 900) + 900;
        randomX = 400;
        randomY = 1700;
        int colorVal = 9;
            if (colorVal < 8) {
                targetColor = Color.GREEN;
            } else {
                targetColor = Color.RED;
            }

        BallTarget target = new BallTarget(diameter, targetColor, randomX, randomY);
            /*
        RectF oval = new RectF(target.getX(), target.getY(), randomX, randomY); // set bounds of rectangle
        Paint p = new Paint(); // set some paint options
        p.setColor(Color.BLACK);
        //canvas.drawOval(oval, p);
        */
            return target;


    }
}