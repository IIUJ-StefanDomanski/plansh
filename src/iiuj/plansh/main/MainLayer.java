/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package iiuj.plansh.main;

import android.view.MotionEvent;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.*;
import org.cocos2d.nodes.*;
import org.cocos2d.types.CGPoint;

/**
 *
 * @author Stefan
 */
public class MainLayer extends CCLayer {
        CCLabel lbl;

    	public static CCScene scene() {
    		CCScene scene = CCScene.node();
    		CCLayer layer = new MainLayer();

    		scene.addChild(layer);

    		return scene;
    	}

        protected MainLayer() {

        	this.setIsTouchEnabled(true);

        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {

            return CCTouchDispatcher.kEventHandled;
        }

    }
