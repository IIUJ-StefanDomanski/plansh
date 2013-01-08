/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package iiuj.plansh.main;

import android.util.Log;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

/**
 *
 * @author Stefan
 */
public class WindowsLayer extends CCNode {
    class Window extends CCNode {
        CGSize size;

        CCSprite rectangle(CGSize size, ccColor3B color){
            CCSprite sprite = CCSprite.sprite("blank.png");
            sprite.setTextureRect(0, 0, size.width, size.height);
            sprite.setColor(color);
            return sprite;
        }

        public Window(int x, int y, String title){
            super();
            addChild(rectangle(CGSize.make(x, y), ccColor3B.ccc3(235, 210, 85)), 1);
            addChild(rectangle(CGSize.make(x, y), ccColor3B.ccc3(250, 225, 100)), 2);
            addChild(rectangle(CGSize.make(x - 8, 16), ccColor3B.ccc3(210, 185, 60)), 3);
            addChild(CCLabel.makeLabel(title, "Arial", 12), 4);
            getChildren().get(0).setPosition(2, -2);
            getChildren().get(2).setPosition(0, y/2 - 12);
            getChildren().get(3).setPosition(0, y/2 - 12);
            setPosition(CCDirector.sharedDirector().winSize().width/2, CCDirector.sharedDirector().winSize().height/2);
            size = CGSize.make(x, y);
        }


        public void title(String name){
            ((CCLabel)getChildren().get(3)).setString(name);
        }

        public void beginTouch(float x, float y){
            
        }
    }

    class WindowList {
        class Node {
            Window window;
            Node next;
        }
        Node root;
        int size = 0;

        public Window get(int i){
            if(i<=size && i>0){
                Node curr = root;
                int n = 0;
                while(++n<size)
                    curr = curr.next;
                return curr.window;
            }else return null;
        }

        public void push(Window window){
            Node neu = new Node();
            neu.window = window;
            neu.next = root;
            root = neu;
            window.setVertexZ(++size);
        }

        public void remove(Window window){
            Node node = root;
            if(node.window != window){
                while(node.next.window != window){
                    node.window.setVertexZ(node.window.getVertexZ()-1);
                    node = node.next;
                }
                node.window.setVertexZ(node.window.getVertexZ()-1);
                node.next = node.next.next;
            }else{
                root = root.next;
            }
            size--;
        }

        public void bring(Window window){
            Node node = root;
            if(node.window != window){
                while(node.next.window != window){
                    node.window.setVertexZ(node.window.getVertexZ()-1);
                    node = node.next;
                }
                node.window.setVertexZ(node.window.getVertexZ()-1);
                Node node1 = node.next.next;
                node.next.next = root;
                root = node.next;
                node.next = node1;
                root.window.setVertexZ(size);
            }
        }
    }
    
    WindowList windowList = new WindowList();

    Window activeWindow;
    boolean movState = false;

    public void openWindow(int x, int y, String title){
        Window window = new Window(x, y, title);
        addChild(window);
        windowList.push(window);
    }

    public boolean touchBegin(float x, float y){
        CGPoint p = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(x,y));
        WindowList.Node node = windowList.root;
        while(node != null){
            Window window = node.window;
            Log.d("mouse", "x: "+p.x+" y: "+p.y);
            Log.d("LR", "x: "+(window.getPosition().x + window.size.width/2)+
                    " y: "+(window.getPosition().y - window.size.height/2));
            Log.d("UL", "x: "+(window.getPosition().x - window.size.width/2)+
                    " y: "+(window.getPosition().y + window.size.height/2));
            if(window.getPosition().x - window.size.width/2 < p.x &&
                    window.getPosition().x + window.size.width/2 > p.x &&
                    window.getPosition().y - window.size.height/2 < p.y &&
                    window.getPosition().y + window.size.height/2 > p.y)
            {
                windowList.bring(window);
                window.beginTouch(x, y);
                activeWindow = window;
                if(p.y > window.getPosition().y + window.size.height/2 - 20)
                    movState = true;
                else movState = false;
                return true;
            }
            node = node.next;
        }
        return false;
    }

    public void touchMove(float nx, float ny){
        if(activeWindow != null && movState)
            activeWindow.setPosition(activeWindow.getPosition().x + nx, activeWindow.getPosition().y - ny);
    }

    public void touchEnd(float x, float y){
        activeWindow = null;
    }
}
