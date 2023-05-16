package com.example.pingpongtest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.DashPathEffect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private float mPlayerPaddleX, mPlayerPaddleY, mPlayerPaddleWidth, mPlayerPaddleHeight;
    private float mEnemyPaddleX, mEnemyPaddleY, mEnemyPaddleWidth, mEnemyPaddleHeight;

    private float mBallX, mBallY, mBallRadius, mBallSpeedX, mBallSpeedY;
    private float mScreenWidth, mScreenHeight;
    private static final int PADDLE_COLOR = Color.WHITE;
    private static final int BALL_COLOR = Color.RED;

    private int mPlayerScore = 0;
    private int mEnemyScore = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSurfaceView = new SurfaceView(this);
        setContentView(mSurfaceView);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        mSurfaceView.setFocusable(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            mPlayerPaddleWidth = mScreenWidth * 0.1f;
            mPlayerPaddleHeight = mScreenHeight * 0.05f;
            mPlayerPaddleX = (mScreenWidth - mPlayerPaddleWidth) / 2f;
            mPlayerPaddleY = mScreenHeight * 0.9f;

            mEnemyPaddleWidth = mScreenWidth * 0.1f;
            mEnemyPaddleHeight = mScreenHeight * 0.05f;
            mEnemyPaddleX = (mScreenWidth - mEnemyPaddleWidth) / 2f;
            mEnemyPaddleY = mScreenHeight * 0.1f;
        } else {
            // Portrait mode
            mPlayerPaddleWidth = mScreenWidth * 0.2f;
            mPlayerPaddleHeight = mScreenHeight * 0.05f;
            mPlayerPaddleX = (mScreenWidth - mPlayerPaddleWidth) / 2f;
            mPlayerPaddleY = mScreenHeight * 0.9f;

            mEnemyPaddleWidth = mScreenWidth * 0.2f;
            mEnemyPaddleHeight = mScreenHeight * 0.05f;
            mEnemyPaddleX = (mScreenWidth - mEnemyPaddleWidth) / 2f;
            mEnemyPaddleY = mScreenHeight * 0.1f;
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mScreenWidth = mSurfaceView.getWidth();
        mScreenHeight = mSurfaceView.getHeight();


        mBallRadius = mScreenWidth * 0.03f;
        mBallX = mScreenWidth / 2f;
        mBallY = mScreenHeight / 2f;
        mBallSpeedX = mScreenWidth * 0.005f;
        mBallSpeedY = mScreenHeight * 0.005f;

        mPlayerPaddleWidth = mScreenWidth * 0.2f;
        mPlayerPaddleHeight = mScreenHeight * 0.05f;
        mPlayerPaddleX = (mScreenWidth - mPlayerPaddleWidth) / 2f;
        mPlayerPaddleY = mScreenHeight * 0.9f;

        mEnemyPaddleWidth = mScreenWidth * 0.2f;
        mEnemyPaddleHeight = mScreenHeight * 0.05f;
        mEnemyPaddleX = (mScreenWidth - mEnemyPaddleWidth) / 2f;
        mEnemyPaddleY = mScreenHeight * 0.1f;
        startGameLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Do nothing
    }

    private void startGameLoop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    update();
                    draw();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void draw() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);

            mPaint.setTextSize(50);
            mPaint.setColor(Color.WHITE);
            canvas.drawText("Jogador: " + mPlayerScore, 50, 100, mPaint);
            canvas.drawText("Inimigo: " + mEnemyScore, 50, mScreenHeight - 50, mPaint);

            Paint linePaint = new Paint();
            linePaint.setColor(Color.WHITE);
            linePaint.setStrokeWidth(10);

            // Desenha a linha no topo
            canvas.drawLine(0, 0, mScreenWidth, 0, linePaint);
            // Desenha a linha inferior
            canvas.drawLine(0, mScreenHeight, mScreenWidth, mScreenHeight, linePaint);
            // Desenha a linha na esquerda da tela
            canvas.drawLine(0, 0, 0, mScreenHeight, linePaint);
            // Desenha a linha na direita da tela
            canvas.drawLine(mScreenWidth, 0, mScreenWidth, mScreenHeight, linePaint);

            mPaint.setColor(PADDLE_COLOR);
            canvas.drawRect(mPlayerPaddleX, mPlayerPaddleY, mPlayerPaddleX + mPlayerPaddleWidth, mPlayerPaddleY + mPlayerPaddleHeight, mPaint);
            canvas.drawRect(mEnemyPaddleX, mEnemyPaddleY, mEnemyPaddleX + mEnemyPaddleWidth, mEnemyPaddleY + mEnemyPaddleHeight, mPaint);

            mPaint.setColor(BALL_COLOR);
            canvas.drawCircle(mBallX, mBallY, mBallRadius, mPaint);



            linePaint.setColor(Color.WHITE);
            linePaint.setStrokeWidth(10);

            float[] intervals = {20, 20};
            float phase = 0;
            DashPathEffect dashPathEffect = new DashPathEffect(intervals, phase);
            linePaint.setPathEffect(dashPathEffect);

            canvas.drawLine(0, mScreenHeight/2, mScreenWidth, mScreenHeight/2, linePaint);
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void resetBall() {
        mBallX = mScreenWidth / 2f;
        mBallY = mScreenHeight / 2f;
        mBallSpeedX = mBallSpeedX * (new Random().nextBoolean() ? 1 : -1);
        mBallSpeedY = mBallSpeedY * (new Random().nextBoolean() ? 1 : -1);
    }

    private void update() {
        mBallX += mBallSpeedX;
        mBallY += mBallSpeedY;

        // Verifica se a bola bateu na parede esquerda ou na parede direita
        if (mBallX - mBallRadius < 0 || mBallX + mBallRadius > mScreenWidth) {
            mBallSpeedX = -mBallSpeedX;
        }

        // Atualiza a posição da paleta inimiga de acordo com a posição da bolinha
        // mEnemyPaddle pondera a velocidade com que a paleta inimiga se dirije a bola no eixo X
        float enemyPaddleCenterX = mEnemyPaddleX + mEnemyPaddleWidth / 2f;
        float ballCenterX = mBallX;

        if (ballCenterX > enemyPaddleCenterX) {
            mEnemyPaddleX += 3;
        } else if (ballCenterX < enemyPaddleCenterX) {
            mEnemyPaddleX -= 3;
        }

        // Garantir que a posição da paleta inimiga nunca saia da tela
        if (mEnemyPaddleX < 0) {
            mEnemyPaddleX = 0;
        } else if (mEnemyPaddleX + mEnemyPaddleWidth > mScreenWidth) {
            mEnemyPaddleX = mScreenWidth - mEnemyPaddleWidth;
        }

        // Aumenta a pontuação do player se a bolinha bater no topo da tela
        if (mBallY - mBallRadius < 0) {
            mPlayerScore++;
            resetBall();
        }

        // Aumenta a pontuação do enemy se a bolinha bater na linha inferior da tela
        if (mBallY + mBallRadius > mScreenHeight) {
            mEnemyScore++;
            resetBall();
        }


        // Verifica se a bolinha bateu na paleta do jogador ou na paleta do inimigo.
        if (mBallY + mBallRadius >= mPlayerPaddleY && mBallY - mBallRadius <= mPlayerPaddleY + mPlayerPaddleHeight) {
            if (mBallX + mBallRadius >= mPlayerPaddleX && mBallX - mBallRadius <= mPlayerPaddleX + mPlayerPaddleWidth) {
                mBallSpeedY = -mBallSpeedY;
                mBallY = mPlayerPaddleY - mBallRadius;
            }
        } else if (mBallY - mBallRadius <= mEnemyPaddleY + mEnemyPaddleHeight && mBallY + mBallRadius >= mEnemyPaddleY) {
            if (mBallX + mBallRadius >= mEnemyPaddleX && mBallX - mBallRadius <= mEnemyPaddleX + mEnemyPaddleWidth) {
                mBallSpeedY = -mBallSpeedY;
                mBallY = mEnemyPaddleY + mEnemyPaddleHeight + mBallRadius;
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mPlayerPaddleX = event.getX() - mPlayerPaddleWidth / 2f;
                break;
        }
        return true;

    }
}