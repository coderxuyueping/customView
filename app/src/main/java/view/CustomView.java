package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.example.tiange.myview.R;


/**
 * User: xyp
 * Date: 2017/3/23
 * Time: 18:10
 */
//通过继承View里自定义，主要有onMeasure，onDraw
public class CustomView extends View{
    private Paint mPaint,mPaint2;
    private int textSize;
    private String text;
    private int textColor;
    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
        init();
    }
    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray t=context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        text=t.getString(R.styleable.CustomView_text);
        textColor=t.getColor(R.styleable.CustomView_textColor,0x0000ff);
        textSize=t.getInt(R.styleable.CustomView_textSize,12);
        t.recycle();
    }
    private void init(){
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL);//设置填充
        mPaint.setAntiAlias(true);//抗锯齿，看起来更圆滑
        mPaint.setColor(textColor);//设置画笔的颜色
        int textSizeSp=(int)(textSize*getResources().getDisplayMetrics().scaledDensity+0.5);//转换为sp单位
        mPaint.setTextSize(textSizeSp);


        mPaint2=new Paint();
        mPaint2.setStyle(Paint.Style.STROKE);//设置填充
        mPaint2.setAntiAlias(true);//抗锯齿，看起来更圆滑

    }

    //对于view来说就是根据父view提供的测量值来测量自己，并设置自己的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=measureDimension(360,widthMeasureSpec);
        int height=measureDimension(360,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    private int measureDimension(int defaultSize,int measureSpec){
        int size=0;
        int mode=MeasureSpec.getMode(measureSpec);
        int measureSize=MeasureSpec.getSize(measureSpec);
        if(mode==MeasureSpec.EXACTLY){//精确值
            size=measureSize;
        }else if(mode==MeasureSpec.AT_MOST){//wrap_content,需要计算
            //因为像TextView继承View，处理wrap_content这类情况是直接计算文字的宽高的，measureText，所以我们这里直接给默认值了
            size=defaultSize;
        }
        return size;
    }
    private float getTextWidth(String text){
        return mPaint.measureText(text);
    }

    //得到字体的高度
    private int getFontHeight(float fontSize){
        Paint paint=new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm=paint.getFontMetrics();
        return (int) Math.ceil(fm.descent-fm.top)+2;
    }

    private float x=0;
    private float sweepAngle=30;
    private boolean isAddAngle=true;
    private int percent=0;
    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //把text画在view的中心
        canvas.drawText(text,this.getMeasuredWidth()/2-getTextWidth(text)/2,this.getMeasuredHeight()/2,mPaint);
        //滚动
        canvas.drawText(text,x,this.getMeasuredHeight()/2,mPaint);

        //画圆
        canvas.drawCircle(60,this.getMeasuredHeight()/2,55,mPaint);

        //画矩形
        RectF rect=new RectF(110,0,410,300);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect,mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        //画扇形
        //rectf圆弧所在的矩形，第二个参数是起始角度，第三个参数是圆弧的角度,顺时针画的,第四个参数是指半径那条线是否连接
        canvas.drawArc(rect,0,sweepAngle,true,mPaint);
        if(sweepAngle>360){
            isAddAngle=false;
        }else if(sweepAngle<0){
            isAddAngle=true;
        }

        //画圆角矩形
//        canvas.drawRoundRect(rect,20,20,mPaint);

        //画一个带百分百进度条的圆环
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setColor(Color.BLACK);//先画一个底部的黑圆
        mPaint2.setStrokeWidth(4);//设置描边宽度
        canvas.drawCircle(this.getMeasuredWidth()/2+getTextWidth(text)+100,this.getMeasuredHeight()/2,100,mPaint2);
        //画文字
        int fontSize=(int)(textSize*getResources().getDisplayMetrics().scaledDensity+0.5);
        mPaint2.setTextSize(fontSize);
        canvas.drawText(percent+"%",this.getMeasuredWidth()/2+getTextWidth(text)+100-getTextWidth(percent+"%")/2,this.getMeasuredHeight()/2+getFontHeight(fontSize)/2,mPaint);

        //画一个会变得圆形进度条
        mPaint2.setColor(textColor);
        //构建一个矩形，然后根据这个矩形画圆弧
        RectF percentRectF=new RectF(this.getMeasuredWidth()/2+getTextWidth(text),this.getMeasuredHeight()/2-100,this.getMeasuredWidth()/2+getTextWidth(text)+200,this.getMeasuredHeight()/2+100);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(percentRectF,mPaint);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawArc(percentRectF,0,(float)(percent*3.6),false,mPaint2);

    }
    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x+=4;
                if(x>getMeasuredWidth()){
                    x=-getTextWidth(text);
                }

                if(isAddAngle){
                    sweepAngle++;
                }else{
                    sweepAngle--;
                }

                if(percent==100){
                    percent=0;
                }
                percent++;
                postInvalidate();//在异步线程调用ondraw，invalidate是在主线程调用ondraw的
            }
        }
    }
    MyThread myThread=null;
    public void starThread(){
        if(myThread==null){
            myThread=new MyThread();
            myThread.start();
        }
    }
}
