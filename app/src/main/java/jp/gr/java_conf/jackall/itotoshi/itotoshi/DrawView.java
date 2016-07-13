package jp.gr.java_conf.jackall.itotoshi.itotoshi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// SurfaceViewを描画するクラス
public class DrawView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    Thread thread;
    boolean scorejudge = false;
    boolean touch = false;
    SurfaceHolder surfaceHolder;
    Paint paint;
    float aue,asita;
    int score=0;
    float px=0, py;
    float randomy=800;
    float randomy2=500;
    float count = 0;
    Bitmap bmp1= BitmapFactory.decodeResource(getResources(),R.drawable.needle1);
    Bitmap bmp2= BitmapFactory.decodeResource(getResources(),R.drawable.needle2);
    float r1x,r2x;
    float r1y,r2y;
    float tuuroysize;//=200;
    float tuuroxsize;//12;
    float width, height;
    float y[];
    float vy = (float)-0.5;
    
    int i,j;

    public DrawView(Context context, AttributeSet atr) {
        super(context, atr);
        setFocusable(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        width = getWidth();
        height = getHeight();
        count=0;
        score=0;
        r1x=getWidth()/5*6;
        r2x=(float)((getWidth()/5*6)*1.5);
        r1y=getHeight();
        vy = (float)-0.3;
        r2y=500;
        tuuroysize=(float)(getWidth()/5.37);
        tuuroxsize=(float)(getWidth()/71.6);
        aue=(getHeight()/3*2)/36;
        asita=(getHeight()/3*2)/23;//24;
        bmp1 = Bitmap.createScaledBitmap(bmp1,40,getHeight()/3*2,false);
        bmp2 = Bitmap.createScaledBitmap(bmp2,40,getHeight()/3*2,false);
        y = new float[getWidth() / 2];
        for (i = 0; i < getWidth() / 2; i++) {
            y[i] = 0;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    public double getRadian(float r) {
        return r * Math.PI / 180;
    }

    public void doDraw(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        //if(first=false) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        paint.setStrokeWidth(8);
        //canvas.drawCircle(getWidth()/2+px,getHeight()/2+py,50,paint);
        //}
        paint.setColor(Color.RED);
        canvas.drawBitmap(bmp1,r1x,randomy,paint);
        canvas.drawBitmap(bmp1,r2x,randomy2,paint);
        for (i = 0; i < getWidth() / 2 -1; i+=1) {
            canvas.drawPoint(i,getHeight()/2+y[i],paint);
        }
        canvas.drawBitmap(bmp2,r1x,randomy,paint);

        canvas.drawBitmap(bmp2,r2x,randomy2,paint);
        paint.setTextSize(100);
        canvas.drawText(String.valueOf(score),getWidth()-(getWidth()/8),getHeight()/10,paint);
        paint.setColor(Color.GREEN);
        //canvas.drawRect(r1x+(getWidth()/80),randomy+aue,r1x+(getWidth()/160)+tuuroxsize,randomy+tuuroysize-asita,paint);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN)
            touch = true;
        if (e.getAction() == MotionEvent.ACTION_UP)
            touch = false;
        return true;
    }

    @Override
    public void run() {
        while (thread != null) {
            try {

                for(j=0; j<3; j++) {
                    py = (float) ((getHeight()*3 / 5) * Math.sin(getRadian(count)));
                    y[getWidth() / 2 - 1] = py;
                    for (i = 0; i < getWidth() / 2 - 1; i++) {
                        y[i] = y[i + 1];
                    }


                    if (touch == false) {
                        if (vy < 0.4)
                            vy += 0.005;
                        count += vy;
                    } else if (touch == true) {
                        if (vy > -0.4) {
                            vy -= 0.005;
                        }
                        count += vy;
                    }
                }
                if(getHeight()/2<=Math.abs(py))
                    surfaceCreated(getHolder());
                r1x-=3;
                r2x-=3;

                if((getWidth()/2-1>r1x+(getWidth()/80)&&getWidth()/2-1<r1x+(getWidth()/50)+tuuroxsize)&&
                        ((0<getHeight()/2+py&&randomy+aue>getHeight()/2+py)||
                        (randomy+tuuroysize-asita<getHeight()/2+py&&getHeight()>getHeight()/2+py))) {
                    //surfaceDestroyed(getHolder());
                    surfaceCreated(getHolder());
                }

                if((getWidth()/2-1>r2x+(getWidth()/60)&&getWidth()/2-1<r2x+(getWidth()/60)+tuuroxsize)&&
                        ((0<getHeight()/2+py&&randomy2+aue>getHeight()/2+py)||
                                (randomy2+tuuroysize-asita<getHeight()/2+py&&getHeight()>getHeight()/2+py))) {
                    //surfaceDestroyed(getHolder());
                    surfaceCreated(getHolder());
                }

                if(scorejudge==false && r1x+(getWidth()/130)<getWidth()/2-20){
                    score++;
                    scorejudge=true;
                }

                if(scorejudge==false && r2x+(getWidth()/130)<getWidth()/2-20){
                    score++;
                    scorejudge=true;
                }

                if(r1x<-50) {
                    randomy = (float)((getHeight()-(getHeight()/6))*Math.random()+getHeight()/10);
                    r1x = getWidth();
                    scorejudge=false;
                }
                if(r2x<-50) {
                    randomy2 = (float)((getHeight()-(getHeight()/6))*Math.random()+getHeight()/10);
                    r2x = getWidth();
                    scorejudge=false;
                }
                doDraw(getHolder());
                thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }
}
