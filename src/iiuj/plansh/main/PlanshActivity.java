package iiuj.plansh.main;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CGPoint;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class PlanshActivity extends Activity {
    private PlanshView mGLSurfaceView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set the window status, no tile, full screen and don't sleep
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //, (GameViewLayer) scene.getChildren().get(0)
        //mGLSurfaceView = new PlanshView(this);
        mGLSurfaceView = new PlanshView(this);

        setContentView(mGLSurfaceView);

        // attach the OpenGL view to a window
        CCDirector.sharedDirector().attachInView(mGLSurfaceView);
        //CCDirector.sharedDirector().set

        // no effect here because device orientation is controlled by manifest
        CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
        
        // show FPS
        // set false to disable FPS display, but don't delete fps_images.png!!
        CCDirector.sharedDirector().setDisplayFPS(true);

        // frames per second
        CCDirector.sharedDirector().setAnimationInterval(1.0f / 60);
        CCScene scene = GameViewLayer.scene();

        mGLSurfaceView.setGameViewLayer((GameViewLayer) scene.getChildren().get(0));

        // Make the Scene active
        CCDirector.sharedDirector().runWithScene(scene);
    }
    
    @Override
    public void onStart() {
        super.onStart();        
    }

    @Override
    public void onPause() {
        super.onPause();

        CCDirector.sharedDirector().pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        CCDirector.sharedDirector().resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        CCDirector.sharedDirector().end();
    }
    
    
    

}