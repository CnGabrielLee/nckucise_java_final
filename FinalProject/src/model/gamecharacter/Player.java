package model.gamecharacter;

import controller.GameMap;
import controller.ObjectController;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import model.gamecharacter.Character;
import model.gameobject.MapBubble;
import model.gameobject.MapObject;
import model.gameobject.SuperObject;
import resourceloader.Resourceloader;



public class Player extends Character{
    
    private ImageIcon img;
    private boolean isShowing;
    int moveX = 0;
    int moveY = 0;
    private boolean keyrelease;
    private int keycontroller;

    public Player(int x, int y, int width, int height, ImageIcon img,int keycontroller) {
        super(x, y, width, height);
        this.img = img;
        keyrelease = true;
        isShowing = true;
        this.keycontroller =  keycontroller;
    }

    public static Player createPlayer(int i, int j, List<String> playerInfo, int keycontroller){ // wasd -> 0, arrows -> 1
        int x = j*MapObject.PIXEL_X + GameMap.getBiasX();
        int y = i*MapObject.PIXEL_Y + GameMap.getBiasY();
        int w = MapObject.PIXEL_X;
        int h = MapObject.PIXEL_Y;

        HashMap<String, ImageIcon> imageInfo = Resourceloader.getResourceloader().getimageInfo();
        return  new Player(x, y, w, h, imageInfo.get(playerInfo.get(0)), keycontroller); //+String.valueOf(StartPanel.playerIndex)
    }

    @Override
    public void showObject(Graphics g) {
        if(isShowing==false){
            return;
        }
        g.drawImage(img.getImage(), getx(), gety(),getx()+getw(), gety()+geth(),0 , 0, img.getIconWidth(), img.getIconHeight(), null);
        //g.drawImage(img.getImage(), getx(), gety(),getx()+getw(), gety()+geth(), null);

    }
    //collision Detect

    public void plantBubble(){
        int i = ObjectController.getPosIndex(getx(), gety()).get(0);
        int j = ObjectController.getPosIndex(getx(), gety()).get(1);
        GameMap gamemap = ObjectController.getObjController().getGameMap();
        if(!gamemap.isBubble(i,j)){
            List<SuperObject> bubble = ObjectController.getObjController().getMap().get("bubble");
            List<String> bubbleinfo = Resourceloader.getResourceloader().getMapObjectInfo().get("90");
            bubble.add(MapBubble.createMapBubble(i, j, bubbleinfo, 1));
        }
    }

    //move
    public void act(int wasd){
        int tempx = getx();
        int tempy = gety();

        switch (wasd) {
            case ' ':
            case 77: //'m'
                plantBubble();
                break;
            case 87:
            case 38:
                tempy -= MapObject.PIXEL_Y;
                break;
            case 83:
            case 40:
                tempy += MapObject.PIXEL_Y;
                break;
            case 65:
            case 37:
                tempx -= MapObject.PIXEL_X;
                break;
            case 68:
            case 39:
                tempx += MapObject.PIXEL_X; 
                break;
            default:
                break;
        }

        boolean b1 = collisiondetect(tempx, tempy, ObjectController.getObjController().getMap().get("obstacle"));
        boolean b2 = collisiondetect(tempx, tempy, ObjectController.getObjController().getMap().get("fragility"));
        if(b1 && b2 && keyrelease) {
            setx(tempx);
            sety(tempy);
            keyrelease = false;
        }
    }

    private boolean collisiondetect(int tempx, int tempy, List<SuperObject> objects){
        Rectangle rect = new Rectangle(tempx, tempy, getw(), geth());
        for(SuperObject obj : objects){
            Rectangle objrect = new Rectangle(obj.getx(), obj.gety(), obj.getw(),obj.geth());
            if(rect.intersects(objrect)){
                return false;
            }
        }
        return true;
    }

    public void setkeyrelease(boolean keyrelease){
        this.keyrelease = keyrelease;
    }
    public boolean getkeyrelease(){ return keyrelease;}
}