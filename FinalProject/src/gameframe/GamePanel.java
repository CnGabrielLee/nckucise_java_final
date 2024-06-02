package gameframe;

import controller.GameController;
import controller.ObjectController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.*;
import model.gamecharacter.Player;
import model.gameobject.SuperObject;
import resourceloader.Resourceloader;
import thread.GameKeyListener;
import thread.GameThread;

public class GamePanel extends JPanel implements Runnable{
    
    private boolean running;
    private GameKeyListener keyListener;
    private JButton magicJiu;
    private JButton magicJiu2;
    public GamePanel() {
        super();
        running = true;

        this.setFocusable(true);
        init();
        running = true;

        keyListener = new GameKeyListener();
        this.addGameKeyListener();
        //System.err.println("start game key listener");

    }

    public void paint(Graphics g){
        super.paint(g);
        Gamepaint(g);
    }

    private void  init(){

         magicJiu = new JButton();
         ImageIcon jiuImg = Resourceloader.getResourceloader().getimageInfo().get("jiuming");
         jiuImg.setImage(jiuImg.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
         magicJiu.setIcon(jiuImg);
         magicJiu.setBounds(950, 420, 70, 70);
         magicJiu.addActionListener(e -> jiuButtonActionPerformed(e));

         magicJiu2 = new JButton();
         ImageIcon jiuImg2 = Resourceloader.getResourceloader().getimageInfo().get("jiuming");
         jiuImg2.setImage(jiuImg2.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
         magicJiu2.setIcon(jiuImg2);
         magicJiu2.setBounds(950, 320, 70, 70);
         magicJiu2.addActionListener(e -> jiuButton2ActionPerformed(e));

         this.setLayout(null);
         //this.add(magicWei);
         //this.add(magicJiu);
         //this.add(magicWei2);
         //this.add(magicJiu2);
    }

    public void jiuButtonActionPerformed(ActionEvent e){ // oh my god what a save
        getFocus();
        List<SuperObject> playerList = ObjectController.getObjController().getMap().get("player");
        Player player = (Player) playerList.get(0);
        if(player.getmagicSaveCount()>0){
            player.setmagicSaveCount(player.getmagicSaveCount() - 1);
            player.setDying(false);
            player.setNormalImg();
        }
    }

    public void jiuButton2ActionPerformed(ActionEvent e){
        getFocus();
        List<SuperObject> playerList = ObjectController.getObjController().getMap().get("player");
        Player player = (Player) playerList.get(1);
        if(player.getmagicSaveCount()>0){
            player.setmagicSaveCount(player.getmagicSaveCount() - 1);
            player.setDying(false);
            player.setNormalImg();
        }
    }
    /*public void gameCtrlActionPerformed(ActionEvent e){
        getFocus();
        if(GameController.isGameRunning()){
            GameController.setGameRunning(false);
        }else {
            GameController.setGameRunning(true);
        }
    }*/

    @Override
    public void run() {
        while (running){
            try{
                Thread.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(GameController.isGameRunning()){
                this.repaint();
            }
        }
    }

    public void Gamepaint(Graphics g){
        HashMap<String, List<SuperObject>> map = ObjectController.getObjController().getMap();
        Set<String> sortitems = new TreeSet<String>(ObjectController.getObjController().getPriorty());
        sortitems.addAll(map.keySet());
        for(String key:sortitems){
            List<SuperObject> list = map.get(key);
            for(int i=0;i<list.size();i++){
                list.get(i).showObject(g);
            }
        }
        if(map.get("player").size() > 0){
            Player player = (Player) map.get("player").get(0);
            Player player2 = (Player) map.get("player").get(1);

            g.setFont(new Font("宋体", Font.BOLD, 24));
            g.drawString("P1泡泡威力:   " + String.valueOf(player.getbubblepower()), 950, 210);

            g.setFont(new Font("宋体", Font.BOLD, 24));
            g.drawString("P1数量:  "+String.valueOf(player.getmagicSaveCount()), 950, 260);

            g.setFont(new Font("宋体", Font.BOLD, 24));
            g.drawString("P2泡泡威力:   " + String.valueOf(player2.getbubblepower()), 950, 310);

            g.setFont(new Font("宋体", Font.BOLD, 24));
            g.drawString("P2数量:  "+String.valueOf(player2.getmagicSaveCount()), 950, 360);

            int gameTime = GameThread.getGameTime()/1000;
            int minute = gameTime / 60;
            int seconds = gameTime % 60;
            String min = "0" + String.valueOf(minute);
            String sec;
            if(seconds < 10){
                sec = "0" + String.valueOf(seconds);
            }else {
                sec = String.valueOf(seconds);
            }
            g.setFont(new Font("Times New Roman", Font.BOLD, 36));
            g.drawString("Time: "+ min + ":" + sec, 950, 650);

        }
    }

    public void addGameKeyListener(){
        if(keyListener != null){
            this.removeKeyListener(keyListener);
            this.addKeyListener(keyListener);
        }
    }
    
    public void removeGameKeyListener(){
        this.removeKeyListener(keyListener);
    }
    public void getFocus(){
        this.requestFocus();
    }

}