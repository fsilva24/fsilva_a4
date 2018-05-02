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

import java.util.Random;

/**
 * Created by felig on 5/1/2018.
 */

class BallTarget  {
    private int mDiameter;
    private int mColor;
    private int mX;
    private int mY;

    public BallTarget(int diam, int color,int x, int y){
        mDiameter = diam;
        mColor= color;
        mX = x;
        mY = y;
    }

    public int getDiameter() {
        return mDiameter;
    }

    public void setDiameter(int diameter) {
        mDiameter = diameter;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public float getX() {
        return mX;
    }

    public void setX(int x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(int y) {
        mY = y;
    }

}
