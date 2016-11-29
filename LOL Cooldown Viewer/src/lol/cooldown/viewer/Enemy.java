/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.cooldown.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JSlider;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.constant.Region;
import net.rithms.riot.constant.staticdata.ChampData;
import net.rithms.riot.constant.staticdata.RuneData;
import net.rithms.riot.constant.staticdata.SpellData;
import net.rithms.riot.dto.CurrentGame.Mastery;
import net.rithms.riot.dto.CurrentGame.Participant;
import net.rithms.riot.dto.Static.Rune;
import net.rithms.riot.dto.League.League;
import net.rithms.riot.dto.Static.Champion;
import net.rithms.riot.dto.Static.ChampionSpell;
import net.rithms.riot.dto.Static.Passive;
import net.rithms.riot.dto.Static.SummonerSpell;


/**
 *
 * @author Owen
 */


public class Enemy {
    Participant p;
    Champion c;
    RiotApi api;
    double cdr;
    double minCdr;
    double insight;
    ChampionSpell q, w, e, r;
    SummonerSpell s1,s2;
    Passive passive;
    Image icon,s1Icon,s2Icon,lIcon;
    boolean intelligence;
    JSlider s;
    League l;
    public Enemy(Participant p, RiotApi api,long gameId) throws RiotApiException{
        cdr=0;
        minCdr=0;
        insight=1;
        s=null;
        this.api=api;
        this.p=p;
        c=api.getDataChampion((int)p.getChampionId(),null,null,ChampData.SPELLS,ChampData.PASSIVE);
        List<ChampionSpell> spells =c.getSpells();
        s1 = api.getDataSummonerSpell((int)p.getSpell1Id(), null, null, SpellData.COOLDOWN);
        s2 = api.getDataSummonerSpell((int)p.getSpell2Id(), null, null, SpellData.COOLDOWN);
        passive=c.getPassive();
        q=spells.get(0);
        w=spells.get(1);
        e=spells.get(2);
        r=spells.get(3);
        icon = getIcon(0);
        s1Icon= getIcon(1);
        s2Icon = getIcon(2);
        
        try{
        l=api.getLeagueBySummoner(p.getSummonerId()).get(0);
        }
        catch(RiotApiException e){
        l=null;
        }
        lIcon = getIcon(3);
        //insight is id #6241, intelligence is id #6352
        for(Mastery m : p.getMasteries()){
            if(m.getMasteryId()==6352){
                intelligence = true;
                minCdr+=5;
            }
            if(m.getMasteryId()==6241){
                insight=0.85;
            }
        
        
        }
        for(int i = 0; i<p.getRunes().size();i++){
            Rune r = api.getDataRune((int)(p.getRunes().get(i).getRuneId()),null,null,RuneData.ALL);
            int count = p.getRunes().get(i).getCount();
            //System.out.println(-100*count*r.getStats().getrPercentCooldownMod());
            minCdr += (-100*count*r.getStats().getrPercentCooldownMod());  
        }
        //if((int)minCdr%5!=0)
            //s.setSnapToTicks(false);
        
    }
    
    
    public double getCdr(){
        return cdr;
    }
    public void setCdr(int num){
        cdr = num;
//        if(intelligence&&cdr<5){
//           cdr=5; 
//        }
    }
    public Participant getParticipant(){
        return p;
    }
    
    public void setSlider(JSlider s){
        this.s=s;
    }
    
    public JSlider getSlider(){
        return s;
    }
    
    public boolean hasIntelligence(){
        return intelligence;   
    }
    
    public void draw(Graphics g,int x, int y, int width, int height){
        g.setColor(Color.WHITE);
        if(s.getValue()<(int)minCdr){
            s.setValue((int)minCdr);
            cdr=minCdr;
        }
        double roc = 0.0392, roc2 = .1961, roc3 = 0.08772, roc4 = .2353, roc5=.10526;//20,100h,100w,120h,120w
        if(l!=null){
            g.drawString(l.getTier() +" "+ l.getEntries().get(0).getDivision(),x,y+100);
            g.drawImage(lIcon.getScaledInstance((int)(roc3*width),(int)(roc2*height),Image.SCALE_REPLICATE), x, y, null);
        }
        else
        g.drawString("UNRANKED",x,y+100);  
        g.drawImage(icon.getScaledInstance((int)(roc5*width),(int)(roc4*height),Image.SCALE_REPLICATE),x,y+110,null);
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        double qcd = q.getCooldown().get(0).intValue();
        double wcd = w.getCooldown().get(0).intValue(); 
        double ecd = e.getCooldown().get(0).intValue();
        double rcd = r.getCooldown().get(0).intValue(); 
        g.drawString(c.getName(),x,y+260);
        g.drawString("P: " + passive.getName(),x,y+280);
        g.drawString("Q: " +df.format(qcd/((100.0+cdr)/100))+" secs",x,y+300);
        g.drawString("W: " +df.format(wcd/((100.0+cdr)/100))+" secs",x,y+320);
        g.drawString("E: " +df.format(ecd/((100.0+cdr)/100))+" secs",x,y+340);
        g.drawString("R: " +df.format(rcd/((100.0+cdr)/100))+" secs",x,y+360);
        df.applyPattern("#");
        g.drawString(s1.getName()+": "+df.format(insight * s1.getCooldown().get(0))+" secs",x,y+380);
        g.drawImage(s1Icon.getScaledInstance(30, 30, Image.SCALE_REPLICATE),x+125,y+355,null);
        g.drawString(s2.getName()+": "+df.format(insight * s2.getCooldown().get(0))+" secs",x,y+400);
        g.drawImage(s2Icon.getScaledInstance(30, 30, Image.SCALE_REPLICATE),x+125,y+385,null);
        df.applyPattern("#.#");
        g.drawString("CDR: " + df.format(cdr) + "%",x,y+420);
        
    }
    
    private Image getIcon(int i) throws RiotApiException{
        String s="",name="", version = api.getDataVersions().get(0);
        switch(i){
            case(0):
                name  = c.getName();
                if(name.equals("Rek'Sai")||name.equals("Kog'Maw"))
                    name=name.replace("'", "");
                else if(name.contains("'"))
                    name = name.substring(0,1) + (name.substring(1).toLowerCase().replaceAll("'", ""));
                else if(name.contains("."))
                    name = name.replaceAll(". ", "");
                else if(name.contains(" "))
                    name = name.substring(0,1) + (name.substring(1).replaceAll(" ", ""));
                s="https://ddragon.leagueoflegends.com/cdn/"+version+"/img/champion/" +name+".png";
                break;
            case(1):
                name = s1.getName();
                if(name.equals("Ignite"))
                    name="Dot";
                else if(name.equals("Ghost"))
                    name="Haste";
                else if(name.equals("Mark"))
                    name="Snowball";
                else if(name.equals("Cleanse"))
                    name="Boost";
                else if(name.equals("Clarity"))
                    name="Mana";
                s="https://ddragon.leagueoflegends.com/cdn/"+version+"/img/spell/Summoner" +name+".png";
                break;
             case(2):
                 name = s2.getName();
                 if(name.equals("Ignite"))
                    name="Dot";
                else if(name.equals("Ghost"))
                    name="Haste";
                 else if(name.equals("Mark"))
                    name="Snowball";
                else if(name.equals("Cleanse"))
                    name="Boost";
                else if(name.equals("Clarity"))
                    name="Mana";
                 s="https://ddragon.leagueoflegends.com/cdn/"+version+"/img/spell/Summoner" +name+".png";
                 break;
             case(3):
                 if(l!=null){
                    name=l.getTier().toLowerCase()+"_"+numeralToNumber(l.getEntries().get(0).getDivision());
                    s="http://sk2.op.gg/images/medals/"+name+".png";
                 }
                 break;
    }
        Image image = null;
            try {
                URL url = new URL(s);
                image = ImageIO.read(url);
            }  catch (IOException e) {
            }
            return image;
    }
    
    public int numeralToNumber(String s){
        if(s==null)
            return 0;
        else if(s.equals("I"))
            return 1;
        else if(s.equals("II"))
            return 2;
        else if(s.equals("III"))
            return 3;
        else if(s.equals("IV"))
            return 4;
        else
            return 5;
        
    }
    
    public void countdown(int time){//time in milliseconds
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while (elapsedTime <time){
            elapsedTime = (new Date()).getTime() - startTime;
        }       
    }
    
}
