package com.jsxl.selfexam.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import android.view.View;


/**
 * Created by Charles on 2018/6/26.
 */

public class ScoreView extends View {
    private Paint paint;
    private Paint paint2;
    private Paint paint3,paint4;
    private int count = 0;
    private int score;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    invalidate();
                    break;
            }

        }
    };

    public ScoreView(Context context) {
        super(context);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();

    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(Color.parseColor("#b8dbb4"));//b8dbb4
        paint2.setStrokeWidth(2);

        paint3 = new Paint();
        paint3.setTextAlign(Paint.Align.CENTER);
        paint3.setStrokeWidth(5);
        paint3.setTextSize(120);
        //设置绘图样式 为填充
        paint3.setStyle(Paint.Style.FILL);
        paint3.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawText(score+"",getWidth()/2,getHeight()/2+40,paint3);
        paint3.setTextSize(30);
        canvas.drawText("正确率",getWidth()/2,getHeight()/2+100,paint3);
        if((score+"").length()==1){
            canvas.drawText("%",getWidth()/2+46,getHeight()/2+35,paint3);
        }
        if((score+"").length()==2){
            canvas.drawText("%",getWidth()/2+76,getHeight()/2+35,paint3);
        }
        if((score+"").length()==3){
            canvas.drawText("%",getWidth()/2+116,getHeight()/2+35,paint3);
        }

        paint3.setTextSize(120);
        canvas.rotate(45, getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < 100; i++) {
            canvas.rotate(2.73f, getWidth() / 2, getHeight() / 2);
            if (i < count) {
                canvas.drawLine(getWidth() / 2, getHeight() - 40, getWidth() / 2, getHeight() - 70, paint);
            }
            if (i >= count) {
                canvas.drawLine(getWidth() / 2, getHeight() - 40, getWidth() / 2, getHeight() - 70, paint2);
            }
            //如果成绩为0 则指示器在第一根线上
            if (score == 0) {
                if (i == count) {
                    canvas.drawRect(getWidth() / 2 - 10, getHeight() - 20, getWidth() / 2 + 10, getHeight() - 4, paint);
                    canvas.drawLine(getWidth() / 2 - 10, getHeight() - 20, getWidth() / 2 + 10, getHeight() - 20, paint);
                    Path path = new Path();
                    path.moveTo(getWidth() / 2, getHeight() - 36);// 此点为多边形的起点
                    path.lineTo(getWidth() / 2 - 10, getHeight() - 20);
                    path.lineTo(getWidth() / 2 + 10, getHeight() - 20);
                    path.close(); // 使这些点构成封闭的多边形
                    canvas.drawPath(path, paint);
                }
            }//如果成绩不为0 则指示器在第一根线开始
            if (score > 0) {
                if (i == count - 1) {
                    canvas.drawRect(getWidth() / 2 - 10, getHeight() - 20, getWidth() / 2 + 10, getHeight() - 4, paint);
                    canvas.drawLine(getWidth() / 2 - 10, getHeight() - 20, getWidth() / 2 + 10, getHeight() - 20, paint);
                    Path path = new Path();
                    path.moveTo(getWidth() / 2, getHeight() - 36);// 此点为多边形的起点
                    path.lineTo(getWidth() / 2 - 10, getHeight() - 20);
                    path.lineTo(getWidth() / 2 + 10, getHeight() - 20);
                    path.close(); // 使这些点构成封闭的多边形
                    canvas.drawPath(path, paint);
                }
            }
        }
        canvas.rotate(315, getWidth() / 2, getHeight() / 2);
        Message msg = handler.obtainMessage();
        msg.what = 0;
        msg.arg1 = count;
        count += 1;
        if (count <= score)
            handler.sendMessageDelayed(msg, 10);

    }


    public void setCount(int score) {
        this.score = score;
    }
}
