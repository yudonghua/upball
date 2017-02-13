package com.example.pc.upball2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{

    private Thread newThread;
    static boolean touch=false;
    static boolean live=true;
    private SoundPool sp;
    private int music;
    private long time=0;
    static float upv=0,hv=0,downv=0;
    static float ballsize=0,wallsize=0,width=1980,height=1080;
    static int x=100,y=500,k=0,i=0,num=1,best=0,score=0;
    static float t1=0,t2=0,datx=0,daty=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout main = (LinearLayout)findViewById(R.id.root) ;


        final DrawView draw = new DrawView(this) ;
        sp= new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.jump, 1);
//        View home = findViewById(R.id.root);
//
//        home.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("home",":");
//                sp.play(music, 1, 1, 0, 0, 1);
//            }
//
//        });

        SharedPreferences best0 = getSharedPreferences("best",
                Activity.MODE_PRIVATE);
        int history = best0.getInt("best", 0);
        TextView besttext = (TextView)findViewById(R.id.best);
        besttext.setText("最高分："+history);
        best=history;
        draw.setMinimumWidth(300) ;
        draw.setMinimumHeight(500) ;
        final RelativeLayout re = (RelativeLayout) findViewById(R.id.re) ;
        main.post(new Runnable() {
            @Override
            public void run() {
                width=re.getWidth();
                height=re.getHeight();
                ballsize=height/80;
                wallsize=height/100;
                draw.ballsize=ballsize;
                draw.backwidth=(int)width;
                draw.backheight=(int)height;
                draw.currentx=width/2;
                draw.currenty=height-height/10;
                draw.y[0]=height-wallsize;
                draw.y2[0]=draw.y[0]+wallsize;
                for(int i = 1; i<5;i++){
                    draw.y[i]=draw.y[i-1]-height/5;
                    draw.y2[i]=draw.y2[i-1]-height/5;
                }
                for(int i = 0; i<5;i++){
                    draw.x[i]=1+width/6+(float)Math.random()*(width/3);
                    draw.x2[i]=draw.x[i]+width/3;
                    draw.x3[i]=draw.x2[i]+width/3;
                    draw.x4[i]=draw.x3[i]+width/3;
                }
            }
        });



        draw.setOnTouchListener(new View.OnTouchListener()
        {


            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if(System.currentTimeMillis()-time>75){
                    sp.play(music, 1, 1, 0, 0, 1);
                }
                time=System.currentTimeMillis();
                if(event.getX()<width/2)hv=(float)-height/3000;
                else hv=(float)height/3000;
                if(live){
                    upv=(float) -height/900;
                }
                touch = true;
                return true;

            }

        }) ;
        main.addView(draw) ;


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    draw.currenty+=upv;
                    draw.currentx+=hv;
                    if(touch)upv+= height/60000;
                    for(int i = 0; i<5;i++){

                        if(i%2==0){
                            if(draw.x2[i]>0){
                                draw.x[i]-= height/6000;
                                draw.x2[i]-= height/6000;
                            }
                            else {
                                draw.x[i]=width;
                                draw.x2[i]=draw.x[i]+width/3;
                            }

                            if(draw.x4[i]>0){
                                draw.x3[i]-= height/6000;
                                draw.x4[i]-= height/6000;
                            }
                            else {
                                draw.x3[i]=width;
                                draw.x4[i]=draw.x3[i]+width/3;
                            }
                        }
                        else{
                            if(draw.x[i]<width){
                                draw.x[i]+= height/6000;
                                draw.x2[i]+= height/6000;
                            }
                            else {
                                draw.x[i]=-width/3;
                                draw.x2[i]=0;
                            }

                            if(draw.x3[i]<width){
                                draw.x3[i]+=(float) height/6000;
                                draw.x4[i]+=(float) height/6000;
                            }
                            else {
                                draw.x3[i]=-width/3;
                                draw.x4[i]=0;
                            }
                        }

                    }

                    if (touch){
                        while(draw.currenty<height/3){
                            draw.currenty++;
                            for (int i = 0;i<5;i++){
                                draw.y[i]++;
                                draw.y2[i]++;
                            }
                            if(draw.y2[i]>=height){
                                draw.y[i]=0;
                                draw.y2[i]=draw.y[i]+wallsize;
                                downv= height/1000*(10+score)/(score+40);
                                score++;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView currentscore = (TextView)findViewById(R.id.score);
                                        currentscore.setText("得分："+score);
                                    }
                                });
                            }
                        }

                        for(int i = 0; i<5;i++){
                            draw.y[i]+=downv;
                            if(draw.y2[i]<height)draw.y2[i]+=downv;
                            else {
                                draw.y[i]=0;
                                draw.y2[i]=draw.y[i]+wallsize;
                                downv= height/1000*(10+score)/(score+40);
                                score++;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView currentscore = (TextView)findViewById(R.id.score);
                                        currentscore.setText("得分："+score);
                                    }
                                });
                            }
                        }
                        draw.currenty+=downv;
                    }


                    draw.postInvalidate();
                    for (int i = 0;i<5;i++){
                        if(draw.currentx>=draw.x[i]-ballsize&&draw.currentx<=draw.x2[i]+ballsize)
                            if(draw.currenty>=draw.y[i]-(ballsize+wallsize)&&draw.currenty<=draw.y2[i]+(ballsize+wallsize)){
                                live=false;
                                hv=0;
                                upv=(float) height/3000;
                                draw.postInvalidate();
                                main.post(MainActivity.this);
                                break;
                            }
                    }
                    for (int i = 0;i<5;i++){
                        if(draw.currentx>=draw.x3[i]-ballsize&&draw.currentx<=draw.x4[i]+ballsize)
                            if(draw.currenty>=draw.y[i]-(ballsize+wallsize)&&draw.currenty<=draw.y2[i]+(ballsize+wallsize)){
                                live=false;
                                hv=0;
                                upv=(float) height/1000;
                                draw.postInvalidate();
                                main.post(MainActivity.this);
                                break;
                            }
                    }

                    if(draw.currentx<ballsize){
                        draw.postInvalidate();
                        main.post(MainActivity.this);
                        break;

                    }
                    if(draw.currentx>width-ballsize){
                        draw.postInvalidate();
                        main.post(MainActivity.this);
                        break;
                    }
                    if(draw.currenty<ballsize){
                        draw.postInvalidate();
                        main.post(MainActivity.this);
                        break;
                    }
                    if(draw.currenty>height-ballsize){
                        draw.postInvalidate();
                        main.post(MainActivity.this);
                        break;
                    }



                }
            }
        }).start();


    }









    @Override
    public void run() {
        if(best<score)best=score;
        SharedPreferences best0 = getSharedPreferences("best", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = best0.edit();
        editor.putInt("best", best);
        editor.commit();
        TextView bestscore = (TextView)findViewById(R.id.best2);
        bestscore.setVisibility(View.VISIBLE);
        bestscore.setText("最高分："+best);
        TextView currentscore = (TextView)findViewById(R.id.score2);
        currentscore.setVisibility(View.VISIBLE);
        currentscore.setText("得分："+score);
        Button button=(Button)findViewById(R.id.button);
        button.setVisibility(View.VISIBLE);
    }
    public void replay(View view){
        upv=0;
        hv=0;
        score=0;
        live=true;
        touch=false;
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


}
