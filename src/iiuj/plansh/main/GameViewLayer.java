/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package iiuj.plansh.main;

import android.util.FloatMath;
import android.util.Log;
import org.cocos2d.actions.CCScheduler;
import org.cocos2d.actions.CCTimer;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.layers.*;
import org.cocos2d.nodes.*;
import org.cocos2d.types.*;

/**
 *
 * @author Stefan
 */
public class GameViewLayer extends CCLayer {
        CCSprite sprite;
        HudNode hud;

        class HudNode extends CCNode {
            CCNode leftBar;
            CCNode downBar;
            private boolean hidden;
            final String leftIco = "lico.png";
            final String downIco = "rico.png";
            final CGPoint downPos = CGPoint.ccp(100, 0);
            CGPoint upPos = CGPoint.ccp(100, 0);

            public HudNode (){
                super();
            }

            public void init(int nol, int nod){
                leftBar = CCNode.node();
                for(int i=0; i<nol; i++){
                    CCSprite spr = CCSprite.sprite(leftIco);
                    CGSize sz = spr.getBoundingBox().size;
                    spr.setPosition(4+sz.width/2,
                            CCDirector.sharedDirector().displaySize().height
                            - ((i+1)*(8+sz.height)-4-sz.height/2));
                    leftBar.addChild(spr);
                }
                downBar = CCNode.node();
                int i;
                for(i=0; i<nod; i++){
                    CCSprite spr = CCSprite.sprite(downIco);
                    CGSize sz = spr.getBoundingBox().size;
                    spr.setPosition((i+1)*(8+sz.width)-4-sz.width/2,
                            4+sz.height/2);
                    downBar.addChild(spr);
                }
                upPos = CGPoint.ccp( CCDirector.sharedDirector().displaySize().width
                        - i*(8+CCSprite.sprite(downIco).getBoundingBox().size.width), 0);
                downBar.setPosition(downPos);
                addChild(leftBar, 5);
                addChild(downBar, 1);
            }
            public void hide(){
                if(!hidden){
                    leftBar.runAction(CCMoveTo.action(1, CGPoint.ccp(-72, 0)));
                    downBar.runAction(CCMoveTo.action(1, CGPoint.ccp(downPos.x, -72)));
                    hidden=!hidden;
                }
            }
            public void show(){
                if(hidden){
                    leftBar.runAction(CCMoveTo.action(1, CGPoint.ccp(0, 0)));
                    downBar.runAction(CCMoveTo.action(1, downPos));
                    hidden=!hidden;
                }
            }

            public boolean isHidden(){
                return hidden;
            }
        }

        
    	public static CCScene scene() {
    		CCScene scene = CCScene.node();
    		GameViewLayer layer = new GameViewLayer();

                layer.setAnchorPoint(0f, 0f);

    		scene.addChild(layer);
    		scene.addChild(layer.hud);

    		return scene;
    	}

        protected GameViewLayer() {
                hud = new HudNode();
                hud.init(4, 8);
                hud.hide();
                sprite = CCSprite.sprite("back.png");

                addChild(sprite, 1);

                getChild(0).setPosition(320, 160);

                //timer  = new CCTimer(this, "startup", 5);
        }

        public void startup(float n){
            tap = false;
            CCScheduler.sharedScheduler().unschedule("startup", this);
        }

        public void moveCamera(float nx, float ny){
            if(touchState == SLIDE){
                hud.downBar.setPosition(hud.downBar.getPosition().x+nx, 0);
                if(hud.downBar.getPosition().x > hud.downPos.x)
                    hud.downBar.runAction(CCMoveTo.action(0.3f, hud.downPos));
                if(hud.downBar.getPosition().x < hud.upPos.x)
                    hud.downBar.runAction(CCMoveTo.action(0.3f, hud.upPos));
            } else
                this.setPosition(CGPoint.ccp(this.getPosition().x+nx, this.getPosition().y-ny));
        }


        public void move(CGPoint cgp){
            setPosition(CGPoint.ccpAdd(getPosition(), cgp));
        }

        private float spacing(CGPoint cp1, CGPoint cp2) {
          // ...
          float x = cp1.x - cp2.x;
          float y = cp1.y - cp2.y;
          return FloatMath.sqrt(x * x + y * y);
       }

        static final int READY = 1;
        static final int SET = 2;
        static final int SLIDE = 3;

        private int touchState = 0;
        private CGPoint startT;
        private boolean tap = false;

        public void touchBegin(float x, float y){
            tap = true;
            startT = CGPoint.ccp(x, y);
            if(!hud.isHidden()&&CCDirector.sharedDirector().winSize().height-y<80)
                touchState = SLIDE;
            else touchState = 0;
            CCScheduler.sharedScheduler().schedule("startup", this, 0.1f, false);
            CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(x,y));
            CGPoint translate = CGPoint.ccpSub(location, getPosition());
            for(CCNode child : getChildren()){
                CGPoint cp = convertToWorldSpace(child.convertToWorldSpace(child.getBoundingBox().size.width, 0).x,
                        child.convertToWorldSpace(child.getBoundingBox().size.width, 0).y);
                //Log.v("Space lower-right: ", ""+spacing(cp, CGPoint.ccp(x, y)));
                cp = child.convertToWorldSpace(child.getBoundingBox().size.width/2,
                            -child.getBoundingBox().size.height/2);

                //Log.v("Space upper-left: ", ""+spacing(cp, CGPoint.ccp(x, y)));
                //Log.v("Child: ", "X: "+cp.x+" Y: "+cp.y);
                //Log.v("Child: ", "X: "+cp.x+" Y: "+cp.y);
                //Log.v("Child dim: ", "X: "+child.getBoundingBox().size.width+" Y: "+child.getBoundingBox().size.height);
            }
            //Log.v("Touch: ", "X: "+translate.x+" Y: "+translate.y);
        }

        public void touchEnd(float x, float y){
            if(tap&&spacing(startT, CGPoint.ccp(x, y))<30){
                if(hud.isHidden())
                    hud.show();
                else
                    hud.hide();
            }
            CCScheduler.sharedScheduler().unschedule("startup", this);
        }

        public void scaleCamera(float scale, float nx, float ny){
            CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(nx,ny));
            CGPoint translate = CGPoint.ccpSub(location, getPosition());
            move(translate);

            setScale(getScale()*scale);
            move(CGPoint.ccpMult(translate, -1*scale));
        }

    @Override
        public CCNode getChild(int i){
           return this.getChildren().get(i);
        }
    }
