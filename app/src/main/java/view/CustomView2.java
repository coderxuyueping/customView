package view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tiange.myview.R;

/**
 * User: xyp
 * Date: 2017/3/24
 * Time: 11:03
 */

//重写已有控件
public class CustomView2 extends LinearLayout{
    public CustomView2(Context context) {
        this(context,null);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.custom_view2,this);
//        this.addView(view);
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onclick();
                }
            }
        });
        view.findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.star();
                }
            }
        });
    }
    public interface OnClickListener{
        void onclick();
        void star();
    }
    private OnClickListener listener;

    public void setBtnClickListener(OnClickListener listener){
        this.listener=listener;
    }

}
