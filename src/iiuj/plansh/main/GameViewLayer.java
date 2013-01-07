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

                layer.addImage("icons/rzut_koscia.png", 320, 160);
                layer.addImage("icons/nastepny_gracz.png", 100, 50);
                layer.addImage("icons/odtworz_dzwiek.png", 450, 150);

    		return scene;
    	}

        protected GameViewLayer() {
                hud = new HudNode();
                hud.init(4, 8);
                hud.hide();

                //timer  = new CCTimer(this, "startup", 5);
        }

        public void addImage(String image, int x, int y){
            int i;
            if(this.getChildren() == null)
                i = 0;
            else
                i = this.getChildren().size();
            CCSprite sprite = CCSprite.sprite(image);
            addChild(sprite, i+1);
            getChild(i).setPosition(x, y);
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
        private CCNode modNode;
        private boolean tap = false;

        public void touchBegin(float x, float y){
            tap = true;
            startT = CGPoint.ccp(x, y);
            CCScheduler.sharedScheduler().schedule("startup", this, 0.1f, false);
            if(!hud.isHidden()&&CCDirector.sharedDirector().winSize().height-y<80)
                touchState = SLIDE;
            else{
                touchState = 0;
                CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(x,y));

                for(CCNode child : getChildren()){
                    CGPoint ul = this.convertToWorldSpace(child.getPosition().x - child.getBoundingBox().size.width/2*child.getScale(),
                            child.getPosition().y + child.getBoundingBox().size.height/2*child.getScale());
                    CGPoint lr = this.convertToWorldSpace(child.getPosition().x + child.getBoundingBox().size.width/2*child.getScale(),
                            child.getPosition().y - child.getBoundingBox().size.height/2*child.getScale());
                    if(spacing(ul, location)<30 || spacing(lr, location)<30){
                        touchState = READY;
                        modNode = child;
                    }
                }
            }
        }

        public void touchDown(float x, float y){
            if(touchState == READY){
                CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(x,y));

                CGPoint ul = this.convertToWorldSpace(modNode.getPosition().x - modNode.getBoundingBox().size.width/2*modNode.getScale(),
                        modNode.getPosition().y + modNode.getBoundingBox().size.height/2*modNode.getScale());
                CGPoint lr = this.convertToWorldSpace(modNode.getPosition().x + modNode.getBoundingBox().size.width/2*modNode.getScale(),
                        modNode.getPosition().y - modNode.getBoundingBox().size.height/2*modNode.getScale());
                if(spacing(ul, location)<30 || spacing(lr, location)<30){
                    touchState = SET;
                }
            }
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
            if(touchState != SET){
                CGPoint translate = CGPoint.ccpSub(location, getPosition());
                move(translate);

                setScale(getScale()*scale);
                move(CGPoint.ccpMult(translate, -1*scale));
            }else{
                modNode.setPosition(this.convertToNodeSpace(location.x, location.y));
                modNode.setScale(scale);
            }
        }

    @Override
        public CCNode getChild(int i){
           return this.getChildren().get(i);
        }
    }
