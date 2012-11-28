/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package iiuj.plansh.main;

import android.util.Log;
import org.cocos2d.layers.*;
import org.cocos2d.nodes.*;
import org.cocos2d.types.CGPoint;

/**
 *
 * @author Stefan
 */
public class GameViewLayer extends CCLayer {
        CCSprite sprite;


    	public static CCScene scene() {
    		CCScene scene = CCScene.node();
    		CCLayer layer = new GameViewLayer();
                layer.setAnchorPoint(0f, 0f);

    		scene.addChild(layer);

    		return scene;
    	}

        protected GameViewLayer() {
                sprite = CCSprite.sprite("back.png");

                addChild(sprite, 1);

        }

        public void moveCamera(float nx, float ny){
            this.setPosition(CGPoint.ccp(this.getPosition().x+nx, this.getPosition().y-ny));
        }


        public void move(CGPoint cgp){
            setPosition(CGPoint.ccpAdd(getPosition(), cgp));
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
