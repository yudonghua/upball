package com.example.pc.upball2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Administrator on 2016/7/16.
 */
public class DrawView extends View {
    public float currentx = 400;
    public  float currenty = 500 ;
    public float ballsize = 25;
    int backwidth=1080,backheight=1920;
    public float x[];
    public  float y[];
    public float x2[];
    public  float y2[];
    public float x3[];
    public  float y3[];
    public float x4[];
    public  float y4[];
    /**
     *
     * @param context
     */
    public DrawView(Context context) {
        super(context);
        x= new float[5];
        y= new float[5];
        x2= new float[5];
        y2= new float[5];
        x3= new float[5];
        y3= new float[5];
        x4= new float[5];
        y4= new float[5];
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //创建画笔 ;

        Paint p = new Paint() ;
        //绘制一个小球 ；
        drawball(canvas);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ci);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = backwidth/3;
        int newHeight = (int)(ballsize*0.8);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        for(int i = 0; i<5;i++){
            drawci(canvas,bitmap,(int)x[i],(int)y[i]);
            drawci(canvas,bitmap,(int)x3[i],(int)y[i]);
        }


    }
    public void drawci(Canvas canvas,Bitmap bitmap,int x,int y){
        canvas.drawBitmap(bitmap,x,y,null);
    }
    public void drawball(Canvas canvas){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ball);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (int)ballsize*2;
        int newHeight = (int)ballsize*2;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(bitmap,currentx-ballsize,currenty-ballsize,null);
    }

}

