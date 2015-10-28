package com.example.android.customviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {

    public static final int MINIMUM_HEIGHT = 800;
    public static final int MINIMUM_WIDTH = 800;

    public static final int BORDER_WIDTH = 5;

    public static final int LETTERS_SIZE = 60;

    private Paint mPaintCircle;
    private Paint mPaintLine;
    private Paint mPaintLetters;

    private Rect textBounds;

    public MyView(Context context) {
        super(context);
        initialize();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     * Inicializamos los Paints. Esto se hace al instanciar la View, de forma
     * que no se haga en cada redraw (afectaria rendimiento)
     */
    private void initialize(){
        //Los paints contienen informacion de coloers, borde, rellenado, etc.
        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.BLACK);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeWidth(BORDER_WIDTH);

        mPaintLine = new Paint();
        mPaintLine.setColor(Color.BLACK);
        mPaintLine.setStrokeWidth(BORDER_WIDTH);

        mPaintLetters = new Paint();
        mPaintLetters.setTextSize(LETTERS_SIZE);
        mPaintLetters.setFakeBoldText(true);
        mPaintLetters.setTextAlign(Paint.Align.CENTER);

        //Rectangulo con los limites del texto (N, S, E, O)
        textBounds = new Rect();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //Obtenemos dimension y modo de la altura
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int myHeight = hSpecSize;

        //Obtenemos dimension y modo de la anchura
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myWidth = wSpecSize;

        if(hSpecMode == MeasureSpec.EXACTLY){
            //Si es Match Parent o si se le ha dado un valor fijo
            //Si es mayor que nuestra View devolvemos ese valor.
            myHeight = hSpecSize;
        }else if(hSpecMode == MeasureSpec.AT_MOST){
            //Wrap Content
            //Si fuese menor que nuestra View, podríamos devolver el
            // mínimo valor de nuestra View, confiando en que el View
            // Padre recortara o Scrollara si es necesario.
            myHeight = MINIMUM_HEIGHT;
        }

        if(wSpecMode == MeasureSpec.EXACTLY){
            //Si es Match Parent o si se le ha dado un valor fijo
            //Si es mayor que nuestra View devolvemos ese valor.
            myWidth = wSpecSize;
        }else if(wSpecMode == MeasureSpec.AT_MOST){
            //Wrap Content
            //Si fuese menor que nuestra View, podríamos devolver el
            // mínimo valor de nuestra View, confiando en que el View
            // Padre recortara o Scrollara si es necesario.
            myWidth = MINIMUM_WIDTH;
        }

        //Para que la figura sea redonda no permitimos una dimension distinta de la otra
        if(myHeight>myWidth){
            myHeight=myWidth;
        }else if(myWidth>myHeight){
            myWidth=myHeight;
        }

        setMeasuredDimension(myWidth, myHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        //Circulo exterior
        canvas.drawCircle(getWidth()/2, getHeight() / 2, (getWidth() / 2) - BORDER_WIDTH, mPaintCircle);

        //Distancia entre el fin de las lineas y el borde del circulo
        float linesPadding = getWidth()/5;

        //Lineas interiores en cruz
        canvas.drawLine(0+linesPadding, getHeight()/2, getWidth()-linesPadding, getHeight()/2, mPaintLine);
        canvas.drawLine(getWidth() / 2, getHeight() - linesPadding, getWidth() / 2, 0 + linesPadding, mPaintLine);

        drawTextCentred(canvas, mPaintLetters, "N", getWidth() / 2, 0 + (linesPadding / 2));
        drawTextCentred(canvas, mPaintLetters, "S", getWidth() / 2, getHeight()-(linesPadding/2));
        drawTextCentred(canvas, mPaintLetters, "E", getWidth()-(linesPadding/2), getHeight()/2);
        drawTextCentred(canvas, mPaintLetters, "O", 0+(linesPadding/2), getHeight()/2);

    }

    /**
     * Metodo para centrar texto verticalmente, ya que en el valor Y que le damos pone la base de
     * las letras
     * @param canvas Canvas
     * @param paint Paint
     * @param text Texto a escribir
     * @param cx coordenada central X, si esta centrado no hace falta cambiarlo
     * @param cy coordenada donde queremos el CENTRO vertical de la letra
     */
    public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        //Para la coordenada Y nos centra bien, pero la X no. La comentamos y dejamos el valor pasado
        //Probablemente es porque hemos hecho "mPaintLetters.setTextAlign(Paint.Align.CENTER);"
        canvas.drawText(text, /*cx - textBounds.exactCenterX()*/cx, cy - textBounds.exactCenterY(), paint);
    }
}
