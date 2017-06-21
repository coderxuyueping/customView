package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * User: xyp
 * Date: 2017/4/24
 * Time: 14:50
 * 一个自定义的viewgroup,效果是把4个子view分布在4个角
 */

public class CustomViewGroup extends ViewGroup {
    public CustomViewGroup(Context context) {
        this(context, null);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //测量子view
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //计算viewGroup的大小
        measureViewGroup(widthMeasureSpec, heightMeasureSpec);
    }

    private void measureViewGroup(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //warp模式下viewGroup的真实大小
        int wrapWidth, wrapHeight;

        //在warp模式下上面2个child的宽
        int tWrapWidth = 0;
        //在warp模式下下面2个child的宽
        int bWrapWidth = 0;
        //在warp模式下左边2个child的高
        int lWrapHeight = 0;
        //在warp模式下右边2个child的高
        int rWrapHeight = 0;

        //wrap模式下遍历child
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //得到child的宽度和高度
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getHeight();
            //得到child的LayoutParams
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

            if (i == 0 || i == 1) {
                //上面两个child的宽度和
                tWrapWidth += childWidth + params.leftMargin + params.rightMargin;

            }

            if (i == 2 || i == 3) {
                //下面两个child的宽度和
                bWrapWidth += childWidth + params.leftMargin + params.rightMargin;
            }

            if (i == 0 || i == 2) {
                //左边两个child的高度和
                lWrapHeight += childHeight + params.topMargin + params.bottomMargin;
            }

            if (i == 1 || i == 3) {
                //右边两个child的高度和
                rWrapHeight += childHeight + params.topMargin + params.bottomMargin;
            }
        }

        //取最大值
        wrapWidth = Math.max(bWrapWidth, tWrapWidth);
        wrapHeight = Math.max(lWrapHeight, rWrapHeight);

        //给这个viewGroup设置大小
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : wrapWidth, heightMode == MeasureSpec.EXACTLY ? heightSize : wrapHeight);
    }

    //只支持Margin的LayoutParams
    //addView的时候需要给子view一个LayoutParams让父容器知道怎么摆放他的位置
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cl = 0, ct = 0, cr = 0, cb = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            switch (i) {
                case 0:
                    cl = params.leftMargin;
                    ct = params.topMargin;
                    cr = child.getMeasuredWidth() + params.leftMargin;
                    cb = child.getMeasuredHeight() + params.topMargin;
                    break;
                case 1:
                    cl = getWidth() - child.getMeasuredWidth() - params.rightMargin - params.leftMargin;
                    ct = params.topMargin;
                    cr = cl + child.getMeasuredWidth();
                    cb = child.getMeasuredHeight() + params.topMargin;
                    break;
                case 2:
                    cl = params.leftMargin;
                    ct = getHeight() - child.getMeasuredHeight() - params.topMargin - params.bottomMargin;
                    cr = child.getMeasuredWidth() + params.leftMargin;
                    cb = ct + child.getMeasuredHeight();
                    break;
                case 3:
                    cl = getWidth() - child.getMeasuredWidth() - params.leftMargin - params.rightMargin;
                    ct = getHeight() - child.getMeasuredHeight() - params.topMargin - params.bottomMargin;
                    cr = cl + child.getMeasuredWidth();
                    cb = ct + child.getMeasuredWidth();
                    break;
            }
            child.layout(cl, ct, cr, cb);
        }

    }
}
