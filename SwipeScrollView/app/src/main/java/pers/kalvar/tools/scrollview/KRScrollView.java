package pers.kalvar.tools.scrollview;

import java.util.ArrayList;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class KRScrollView extends HorizontalScrollView {

    private int currentX              = -1;
    private int firstX                = -1;
    private int currentPage           = 0;
    //private int screenWidth           = 0;
    private int subviewCount          = 0;
    private ViewGroup firstSubview    = null;
    private ArrayList<Integer> points = new ArrayList<Integer>();

    private void initialize() {
        setHorizontalScrollBarEnabled(false);
    }

    public KRScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public KRScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public KRScrollView(Context context) {
        super(context);
        initialize();
    }

    private enum Swipes{
        //Right to Left
        TO_LEFT(0),
        //Left to Right
        TO_RIGHT(1),
        //Top to Down
        TO_DOWN(2),
        //Down to Top
        TO_TOP(3),
        //Never Move
        NO_MOVED(4);
        private int value;
        private Swipes(int value) {
            this.value = value;
        }
    }

    private Swipes getSwipeDirection()
    {
        Swipes swipeDirection = Swipes.NO_MOVED;
        if (currentX < firstX)
        {
            //Log.d("ACTION", "Right to Left");
            swipeDirection = Swipes.TO_LEFT;
        }
        else if( currentX > firstX )
        {
            //Log.d("ACTION", "Left to Right");
            swipeDirection = Swipes.TO_RIGHT;
        }
        else
        {
            //Log.d("ACTION", "No moved");
            swipeDirection = Swipes.NO_MOVED;
        }
        return swipeDirection;
    }

    private void resetSwipeParams(){
        currentX = -1;
        firstX   = -1;
    }

    public void receiveChildInfo() {
        firstSubview     = (ViewGroup) getChildAt(0);
        if(firstSubview != null)
        {
            subviewCount = firstSubview.getChildCount();
            for( int i=0; i<subviewCount; i++ )
            {
                if( ((View) firstSubview.getChildAt(i)).getWidth() > 0 )
                {
                    points.add( ( (View) firstSubview.getChildAt(i) ).getLeft() );
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        receiveChildInfo();
        //screenWidth = getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        /*
        * @ 2015.01.20 01:21
        * @ Notes
        *   - ev.getX() 取到的值是手指「按在螢幕上的最一開始位置」，所以 ... 偵測判斷的方式要再多想想
        *   - MotionEvent.ACTION_DOWN 不一定都偵測的到，至少在 Sony Z3 上沒有一次成功過 XD
        *   - MotionEvent.ACTION_MOVE 是指一直在 Dragging 和 Moving 時
        *   - 越往左移動 ( 越靠近 0 ) 代表要翻至下一頁
        *     越往右移動 ( 越靠近 getWidth() 螢幕解析度 ) 代表要翻至上一頁
        *   - 第 1 頁 只有 toNext / toCurrent 的模式
        *     第 2 頁 以後才有 toPre / toNext 的判斷
        *     最後頁  只有 toPre 的模式
        * */
        switch ( event.getActionMasked() )
        {
            //In some cases, this touch event won't be fired, the Google document didn't say the solution.
            case MotionEvent.ACTION_DOWN:
                //Log.d("ACTION", "DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
            {
                //Log.d("ACTION", "MOVE");
                currentX = (int) event.getX();
                if( firstX == -1 )
                {
                    firstX = currentX;
                }
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            {
                //Log.d("ACTION", "currentX " + currentX + ", firstX " + firstX);
                //int swipeLine = ( currentPage == 0 ) ? screenWidth / 4 : screenWidth / 2;
                //if( Math.abs( currentX - firstX ) >= swipeLine )
                {
                    switch( this.getSwipeDirection() )
                    {
                        case TO_LEFT:
                            smoothScrollToNextPage();
                            break;
                        case TO_RIGHT:
                            smoothScrollToPrePage();
                            break;
                        case TO_DOWN:

                            break;
                        case TO_TOP:

                            break;
                        case NO_MOVED:
                            smoothScrollToCurrent();
                            break;
                        default: break;
                    }
                }

                resetSwipeParams();
                return true;
            }
            default: break;
        }

        return super.onTouchEvent(event);
    }

    private void smoothScrollToCurrent() {
        smoothScrollTo(points.get(currentPage), 0);
    }

    private void smoothScrollToNextPage()
    {
        //Log.d("PAGE", currentPage + " / " + subviewCount);
        if(currentPage < subviewCount - 1)
        {
            currentPage++;
            smoothScrollTo(points.get(currentPage), 0);
        }
        else if(currentPage == subviewCount - 1)
        {
            smoothScrollToCurrent();
        }
    }

    private void smoothScrollToPrePage()
    {
        if(currentPage == 0)
        {
            smoothScrollToCurrent();
        }
        else if(currentPage > 0)
        {
            currentPage--;
            smoothScrollTo(points.get(currentPage), 0);
        }
    }

    public void nextPage(){
        smoothScrollToNextPage();
    }

    public void previousPage(){
        smoothScrollToPrePage();
    }

    public boolean gotoPage(int page){
        if(page > 0 && page < subviewCount - 1)
        {
            smoothScrollTo(points.get(page), 0);
            currentPage = page;
            return true;
        }
        return false;
    }
}