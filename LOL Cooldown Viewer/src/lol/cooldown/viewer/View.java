/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.cooldown.viewer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameInfo;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionList;
import net.rithms.riot.api.endpoints.static_data.dto.Item;
import net.rithms.riot.api.endpoints.static_data.dto.ItemList;
import net.rithms.riot.api.endpoints.static_data.dto.SummonerSpell;
import net.rithms.riot.api.endpoints.static_data.dto.SummonerSpellList;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;
import sun.awt.image.ToolkitImage;
//import net.rithms.riot.constant.PlatformId;
//import net.rithms.riot.constant.Region;
//import net.rithms.riot.dto.CurrentGame.CurrentGameInfo;
//import net.rithms.riot.dto.CurrentGame.Participant;
//import net.rithms.riot.dto.Match.MatchDetail;
//import net.rithms.riot.dto.Static.Champion;
//import net.rithms.riot.dto.Static.ChampionList;
//import net.rithms.riot.dto.Summoner.Summoner;


/**
 *
 * @author Owen
 */
public class View extends javax.swing.JPanel {

    /**
     * Creates new form View
     */
    List<Enemy> enemies,allies;
    boolean manual,ready,allied,loading;
    RiotApi api;
    Map<String, Double> passiveCooldowns;
    ChampionList champRaw;
    int width;
    int height;
    String version;
    
    public View() throws RiotApiException {
        initComponents();
        ApiConfig config = new ApiConfig().setKey("RGAPI-80e44638-93fe-4db5-b498-2bab841759ba");
        api = new RiotApi(config);
        //System.out.println("Hey" + api.getSummonerByName(Platform.NA, "Dyrus").getAccountId()); //write code to test print here
        passiveCooldowns = assignCooldowns();
        //champRaw = api.getDataChampionList(Platform.NA);
        enemies=null;
        allies=null;
        allied=false;
        manual=true;
        ready=false;
        loading=false;
        height = this.getHeight();
        width = this.getWidth();
        System.out.println(width);
        //LOLCooldownViewer.updateCache();
        version = ((List<String>) Cacher.read("cache/versions.ser")).get(0);
        System.out.println(version);
        SummonerSpellList spells = (SummonerSpellList)Cacher.read("cache/spells.ser");
        

        ItemList iList = (ItemList)Cacher.read("cache/items.ser");
        //String imgLoc = "xd";
        //System.out.println(iList.getData().get("3082").getImage().getFull());
        for(String key : iList.getData().keySet()){
            Item i = iList.getData().get(key);
//            if(i.getTags() != null && i.getTags().contains("CooldownReduction")){
//                String imgLoc = i.getImage().getFull();
//                String s = "http://ddragon.leagueoflegends.com/cdn/" + version+  "/img/item/" + imgLoc;
//                BufferedImage image = null;
//                try {
//                    URL url = new URL(s);
//                    image = ImageIO.read(url);
//                }  catch (IOException e) {
//                }
//                BufferedImage img1 = new BufferedImage(33, 32, BufferedImage.TYPE_INT_RGB);
//                Graphics g2 = img1.createGraphics();
//                g2.drawImage(image, 0, 0, 33, 32, null);
//                g2.dispose();
//                //BufferedImage img1 = image.getScaledInstance(33, 35, Image.SCALE_REPLICATE);
//                BufferedImage img2 = ImageHandler.crop(1287, 418, 33, 32, "in-game2.png", "item#1.png");
//                //g.drawImage(img1, 100, 200, null);
//                //g.drawImage(img2, 300, 200, null);
//                //System.out.println(ImageHandler.compare(img1, img2));
//                if(ImageHandler.imageEqual(img1, img2, 0.55)){
//                    System.out.println(i.getName());
//                    break;
//                }
//            }
        }
        //System.out.println("done");
        //System.out.println(spells.getData());
        //System.out.println(spells.getData().get("4").getCooldown());
        //System.out.println(jSlider1);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        jSlider6 = new javax.swing.JSlider();
        jSlider7 = new javax.swing.JSlider();
        jSlider8 = new javax.swing.JSlider();
        jSlider9 = new javax.swing.JSlider();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 41, 106));
        setPreferredSize(new java.awt.Dimension(1340, 510));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jSlider1.setMaximum(45);
        jSlider1.setMinorTickSpacing(5);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.setToolTipText("Champion 1");
        jSlider1.setValue(0);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jSlider1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSlider1PropertyChange(evt);
            }
        });

        jSlider6.setMaximum(45);
        jSlider6.setMinorTickSpacing(5);
        jSlider6.setPaintTicks(true);
        jSlider6.setSnapToTicks(true);
        jSlider6.setToolTipText("Champion 2");
        jSlider6.setValue(0);
        jSlider6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider6StateChanged(evt);
            }
        });
        jSlider6.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSlider6PropertyChange(evt);
            }
        });

        jSlider7.setMaximum(45);
        jSlider7.setMinorTickSpacing(5);
        jSlider7.setPaintTicks(true);
        jSlider7.setSnapToTicks(true);
        jSlider7.setToolTipText("Champion 3");
        jSlider7.setValue(0);
        jSlider7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider7StateChanged(evt);
            }
        });
        jSlider7.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSlider7PropertyChange(evt);
            }
        });

        jSlider8.setMaximum(45);
        jSlider8.setMinorTickSpacing(5);
        jSlider8.setPaintTicks(true);
        jSlider8.setSnapToTicks(true);
        jSlider8.setToolTipText("Champion 4");
        jSlider8.setValue(0);
        jSlider8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider8StateChanged(evt);
            }
        });
        jSlider8.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSlider8PropertyChange(evt);
            }
        });

        jSlider9.setMaximum(45);
        jSlider9.setMinorTickSpacing(5);
        jSlider9.setPaintTicks(true);
        jSlider9.setSnapToTicks(true);
        jSlider9.setToolTipText("Champion 5");
        jSlider9.setValue(0);
        jSlider9.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider9StateChanged(evt);
            }
        });
        jSlider9.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSlider9PropertyChange(evt);
            }
        });

        jButton1.setText("Predicted CDR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setText("Summoner Name");
        jTextField1.setToolTipText("");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton2.setText("Go!");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Allied Team");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSlider6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jSlider7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jSlider8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSlider9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton1)))
                        .addGap(0, 223, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 400, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSlider1PropertyChange
        
    }//GEN-LAST:event_jSlider1PropertyChange

    private void jSlider6PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSlider6PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider6PropertyChange

    private void jSlider7PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSlider7PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider7PropertyChange

    private void jSlider8PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSlider8PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider8PropertyChange

    private void jSlider9PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSlider9PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider9PropertyChange

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        // TODO add your handling code here:
        if(enemies!=null){
            enemies.get(0).setCdr(jSlider1.getValue());
            repaint();
        }
        else if(allied && allies!=null){
            allies.get(0).setCdr(jSlider1.getValue());
            repaint();
        }
    }//GEN-LAST:event_jSlider1StateChanged

    private void jSlider8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider8StateChanged
        if(enemies!=null && !allied){
            enemies.get(3).setCdr(jSlider8.getValue());
            repaint();
        }
        else if(allied && allies!=null){
            allies.get(3).setCdr(jSlider8.getValue());
            repaint();
        }
    }//GEN-LAST:event_jSlider8StateChanged

    private void jSlider6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider6StateChanged
        if(enemies!=null && !allied){
            enemies.get(1).setCdr(jSlider6.getValue());
            repaint();
        }
        else if(allied && allies!=null){
            allies.get(1).setCdr(jSlider6.getValue());
            repaint();
        }
    }//GEN-LAST:event_jSlider6StateChanged

    private void jSlider7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider7StateChanged
        if(enemies!=null && !allied){
            enemies.get(2).setCdr(jSlider7.getValue());
            repaint();
        }
        else if(allied && allies!=null){
            allies.get(2).setCdr(jSlider7.getValue());
            repaint();
        }
    }//GEN-LAST:event_jSlider7StateChanged

    private void jSlider9StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider9StateChanged
        if(enemies!=null && !allied){
            enemies.get(4).setCdr(jSlider9.getValue());
            repaint();
        }
        else if(allied && allies!=null){
            allies.get(4).setCdr(jSlider9.getValue());
            repaint();
        }
    }//GEN-LAST:event_jSlider9StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        manual=!manual;
        if(enemies!=null && !allied){
            for(Enemy e : enemies){
                e.getSlider().setVisible(!e.getSlider().isVisible());     
            }
        }
        else if(allies!=null && allied){
            for(Enemy e : enemies){
                e.getSlider().setVisible(!e.getSlider().isVisible());     
            }
        }
        if(jButton1.getText().equals("Predicted CDR")){
            jButton1.setText("Manual CDR");
        }
        else{
         jButton1.setText("Predicted CDR");   
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            genTeam(true);
        } catch (InterruptedException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jButton3.setText(jButton3.getText().equals("Allied Team") ? "Enemy Team" : "Allied Team");
        allied=!allied;
        try {
            genTeam(false);
        } catch (InterruptedException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    public void paintComponent(Graphics g){
        List<Enemy> l = allied ? allies : enemies;
        super.paintComponent(g);
        height = this.getHeight();
        width = this.getWidth();
        int ctr=0;
        if(l!=null&&ready){
            for(Enemy e : l){
                e.getSlider().setBackground(this.getBackground());
                if(e.hasIntelligence() && e.getSlider().getValue()<5){
                    //e.getSlider().setValue(5);
                    repaint();
                }
                e.draw(g,100+250*ctr,0,width,height);
                ctr++;
            }
        }

        
        
        
        ItemList iList = (ItemList)Cacher.read("cache/items.ser");
        String imgLoc = iList.getData().get("3147").getImage().getFull();
        String s = "http://ddragon.leagueoflegends.com/cdn/" + version+  "/img/item/" + imgLoc;
        BufferedImage image = null;
        try {
            URL url = new URL(s);
            image = ImageIO.read(url);
        }  catch (IOException e) {
        }
        BufferedImage img1 = new BufferedImage(33, 34, BufferedImage.TYPE_INT_RGB);
        Graphics g2 = img1.createGraphics();
        g2.drawImage(image, 0, 0, 33, 34, null);
        g2.dispose();
        //BufferedImage img1 = image.getScaledInstance(33, 35, Image.SCALE_REPLICATE);
        BufferedImage img2 = ImageHandler.crop(1320, 418, 33, 34, "in-game4.png", "item#4.png");
        g.drawImage(img1, 100, 200, null);
        g.drawImage(img2, 300, 200, null);
        System.out.println(ImageHandler.compare(img1, img2));
        System.out.println(ImageHandler.imageEqual(img1, img2, 0.7));
        
        
        
    }
    public void setEnemies(List<Enemy> ens){
        enemies = ens;
        enemies.get(0).setSlider(jSlider1);
        enemies.get(1).setSlider(jSlider6);
        enemies.get(2).setSlider(jSlider7);
        enemies.get(3).setSlider(jSlider8);
        enemies.get(4).setSlider(jSlider9);
        for(Enemy e : enemies){
            e.getSlider().setValue(0);
            e.getSlider().setVisible(false);
        }
        repaint();
    }
    
    public void setAllies(List<Enemy> ens){
        allies = ens;
        allies.get(0).setSlider(jSlider1);
        allies.get(1).setSlider(jSlider6);
        allies.get(2).setSlider(jSlider7);
        allies.get(3).setSlider(jSlider8);
        allies.get(4).setSlider(jSlider9);
        for(Enemy e : allies){
            e.getSlider().setValue(0);
            e.getSlider().setVisible(false);
        }
        repaint();
    }
    
    private Map<String, Double> assignCooldowns(){ 
        Map<String, Double> list = new HashMap<>(); 
        list.put("Aatrox", 225.0);
        list.put("Anivia", 240.0);
        list.put("Blitzcrank", 90.0);
        list.put("Evelynn", 6.0);
        list.put("Garen", 9.0);
        list.put("Kindred", 90.0);
        list.put("LeBlanc", 5.0);
        list.put("Lissandra", 18.0);
        list.put("Malphite",  10.0);
        list.put("Nocturne",  10.0);
        list.put("Shen", 10.0);
        list.put("Teemo", 1.5);
        list.put("Vi", 16.0);
        list.put("Volibear", 120.0); 
        list.put("Xerath", 12.0);
        list.put("Zac", 300.0);
        list.put("Ziggs", 12.0);
        return list;   
    }
    
    
    private void genTeam(boolean override) throws InterruptedException{
        loading = true;
        if (override){
            allies=null;
            enemies=null;
        }
        if(enemies==null || allies == null || override){
            try {
                ready=false;
                Summoner summoner=null;
                System.out.println(jTextField1.getText());
                summoner = api.getSummonerByName(Platform.NA, jTextField1.getText());
                //Map<String, Champion> champs = champRaw.getData();
                long id = summoner.getId();
                long enemyTeamId=0;
                try{
                    
                //Match gameInfo = api.getMatch(Platform.NA, id);
                CurrentGameInfo gameInfo = api.getActiveGameBySummoner(Platform.NA, id);
                //api.getMa
                List<CurrentGameParticipant> participants = gameInfo.getParticipants();
                long gameId = gameInfo.getGameId();
                List<Enemy> enemies = new ArrayList<>();
                int ctr=1;
                for(CurrentGameParticipant p : participants){
                    if(p.getSummonerId()==id){
                        if(ctr<=5)
                            enemyTeamId=200;
                        else
                            enemyTeamId=100;
                    }
                    ctr++;
                }
                for(CurrentGameParticipant p : participants){
                    Summoner summ = api.getSummoner(Platform.NA, p.getSummonerId());
                    if(!allied && p.getTeamId()==enemyTeamId){
                        System.out.println(summ.getName());
                        //System.out.println(api.getDataChampion(Platform.NA, (int)p.getChampionId()).getName());
                        enemies.add(new Enemy(p,api,gameId, passiveCooldowns, version));
                    }
                    else if(allied && p.getTeamId()!=enemyTeamId ){
                        System.out.println(summ.getName());
                        //System.out.println(api.getDataChampion(Platform.NA, (int)p.getChampionId()).getName());
                        enemies.add(new Enemy(p,api,gameId, passiveCooldowns, version));
                    }
                }
                if(!allied)
                    setEnemies(enemies);
                else
                    setAllies(enemies);
                ready=true;
                }
                catch(RiotApiException e){
                    //System.out.println(summoner.getName()+" is not currently in a game.");
                    Logger.getLogger(LOLCooldownViewer.class.getName()).log(Level.SEVERE, null, e);
                }
                repaint();
            } catch (RiotApiException ex) {
                Logger.getLogger(LOLCooldownViewer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("fail");
            }
        }
        
        
        
    }
   
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider6;
    private javax.swing.JSlider jSlider7;
    private javax.swing.JSlider jSlider8;
    private javax.swing.JSlider jSlider9;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
