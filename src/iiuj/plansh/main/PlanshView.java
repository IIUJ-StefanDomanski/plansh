/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iiuj.plansh.main;

import android.content.Context;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import org.cocos2d.opengl.CCGLSurfaceView;

/**
 *
 * @author Stefan
 */
public class PlanshView extends CCGLSurfaceView {

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    public PlanshView(Context context, GameViewLayer gvl) {
        super(context);
        //setOnTouchListener(gvl);
    }

    public PlanshView(Context context) {
        super(context);
    }
    private GameViewLayer gml;

    public void setGameViewLayer(GameViewLayer in) {
        gml = in;
    }

    /** Determine the space between the first two fingers */
   private float spacing(MotionEvent event) {
      // ...
      float x = event.getX(0) - event.getX(1);
      float y = event.getY(0) - event.getY(1);
      return FloatMath.sqrt(x * x + y * y);
   }

   /** Calculate the mid point of the first two fingers */
   private void midPoint(PointF point, MotionEvent event) {
      // ...
      float x = event.getX(0) + event.getX(1);
      float y = event.getY(0) + event.getY(1);
      point.set(x / 2, y / 2);
   }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gml == null) {
            return true;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                gml.touchBegin(event.getX(), event.getY());
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                    gml.touchDown(event.getX(1), event.getY(1));
                }
                break;
            case MotionEvent.ACTION_UP:
                gml.touchEnd(event.getX(), event.getY());
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    gml.moveCamera((event.getX() - start.x), (event.getY() - start.y));
                    start.set(event.getX(), event.getY());
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        float scale = newDist / oldDist;
                        gml.scaleCamera(scale, mid.x, mid.y);
                        oldDist = newDist;
                        midPoint(mid, event);
                    }
                }
                break;
        }
        return true;
    }
}
