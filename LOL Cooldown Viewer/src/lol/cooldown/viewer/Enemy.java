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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JSlider;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.league.dto.LeagueList;
import net.rithms.riot.api.endpoints.match.dto.Mastery;
import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.match.dto.Rune;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;
import net.rithms.riot.api.endpoints.static_data.constant.ChampionTags;
import net.rithms.riot.api.endpoints.static_data.constant.Locale;
import net.rithms.riot.api.endpoints.static_data.constant.SpellTags;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionSpell;
import net.rithms.riot.api.endpoints.static_data.dto.Passive;
import net.rithms.riot.api.endpoints.static_data.dto.SummonerSpell;
import net.rithms.riot.constant.Platform;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionList;
import net.rithms.riot.api.endpoints.static_data.dto.Item;
import net.rithms.riot.api.endpoints.static_data.dto.MasteryList;
import net.rithms.riot.api.endpoints.static_data.dto.RuneList;
import net.rithms.riot.api.endpoints.static_data.dto.SummonerSpellList;
//import net.rithms.riot.constant.Region;
//import net.rithms.riot.constant.staticdata.ChampData;
//import net.rithms.riot.constant.staticdata.RuneData;
//import net.rithms.riot.constant.staticdata.SpellData;
//import net.rithms.riot.dto.CurrentGame.Mastery;
//import net.rithms.riot.dto.CurrentGame.Participant;
//import net.rithms.riot.dto.Static.Rune;
//import net.rithms.riot.dto.League.League;
//import net.rithms.riot.dto.Static.Champion;
//import net.rithms.riot.dto.Static.ChampionSpell;
//import net.rithms.riot.dto.Static.Passive;
//import net.rithms.riot.dto.Static.SummonerSpell;


/**
 *
 * @author Owen
 */


public class Enemy {
    CurrentGameParticipant p;
    Champion c;
    RiotApi api;
    double cdr;
    double minCdr;
    double insight;
    ChampionSpell q, w, e, r;
    SummonerSpell s1,s2;
    Passive passive;
    Map passives;
    Image icon,s1Icon,s2Icon,lIcon;
    boolean intelligence;
    JSlider s;
    LeagueList l;
    String version;
    List<Item> items;
    public Enemy(CurrentGameParticipant p, RiotApi api,long gameId, Map passives, String vsn) throws RiotApiException, InterruptedException{
        cdr=0;
        items=null;
        minCdr=0;
        insight=1;
        s=null;
        this.api=api;
        this.p=p;
        version = vsn;
        //ChampionList ChampData = api.getDataChampionList(Platform.NA);
        //c=api.getDataChampion(Platform.NA,(int)p.getChampionId(),Locale.EN_US,version, ChampionTags.PASSIVE, ChampionTags.SPELLS);
        c = ((ChampionList)Cacher.read("cache/champions.ser")).getData().get(""+p.getChampionId());
        List<ChampionSpell> spells =c.getSpells();
        Map<String, SummonerSpell> spellList = ((SummonerSpellList)Cacher.read("cache/spells.ser")).getData();
//        s1 = api.getDataSummonerSpell(Platform.NA, (int)p.getSpell1Id(), Locale.EN_US, version, SpellTags.COOLDOWN);
//        s2 = api.getDataSummonerSpell(Platform.NA, (int)p.getSpell2Id(), Locale.EN_US, version, SpellTags.COOLDOWN);
        s1  = spellList.get(""+p.getSpell1Id());
        s2 = spellList.get(""+p.getSpell2Id());
        System.out.println(s1.toString());
        passive=c.getPassive(); 
        q=spells.get(0);
        w=spells.get(1);
        e=spells.get(2);
        r=spells.get(3);
        icon = getIcon(0);
        s1Icon= getIcon(1);
        s2Icon = getIcon(2);
        this.passives=passives;
        
        try{
        l=api.getLeagueBySummonerId(Platform.NA, p.getSummonerId()).get(0);
        }
        catch(RiotApiException | IndexOutOfBoundsException e){
            l=null;
        }
        lIcon = getIcon(3);
        
        updateCdr();
        
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
    public CurrentGameParticipant getParticipant(){
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
    
    public double updateCdr(){
     minCdr = 0;
     if (items != null){
        for(Item i : items){
           String s = i.getDescription();
           String newS = s.substring(s.indexOf("Cooldown Reduction") - 4, s.indexOf("Cooldown Reduction") - 2);
           if(newS.contains("+"))
               newS = newS.substring(1);
           minCdr += Double.parseDouble(newS);
        }
    }
     //insight is id #6241, intelligence is id #6352
      for(net.rithms.riot.api.endpoints.spectator.dto.Mastery m : p.getMasteries()){
            if(m.getMasteryId()==6352){
                intelligence = true;
                minCdr+=5;
            }
            if(m.getMasteryId()==6241){
                insight=0.85;
            }
        }

        for(int i = 0; i<p.getRunes().size();i++){
            RuneList rList = (RuneList)Cacher.read("cache/runes.ser");
            net.rithms.riot.api.endpoints.static_data.dto.Rune r = rList.getData().get(""+p.getRunes().get(i).getRuneId());
            int count = p.getRunes().get(i).getCount();
           minCdr += (-100*count*r.getStats().getPercentCooldownMod());  
        }
        cdr = minCdr;
     
     return cdr;   
    }
    
    public void setItems(List<Item> items){
        this.items = items;
    }
    
    
    public void draw(Graphics g,int x, int y, int width, int height){
        this.getSlider().setLocation(x-20, y+460);
        this.getSlider().setVisible(true);
        g.setColor(Color.WHITE);
        if(s.getValue()<(int)minCdr){
            s.setValue((int)minCdr);
            cdr=minCdr;
        }
        double roc = 0.0392, roc2 = .1961, roc3 = 0.08772, roc4 = .2353, roc5=.10526;//20,100h,100w,120h,120w
        if(l!=null){
            //l.getEntries().get
            if(l.getTier().equals("MASTER") || l.getTier().equals("CHALLENGER"))
                g.drawString(l.getTier(),x,y+100);
            else
                g.drawString(l.getTier() +" "+ l.getEntryBySummonerId(this.p.getSummonerId()).getRank(),x,y+100);
            if(lIcon != null)
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
        if(passives.containsKey(c.getName()))
            g.drawString("P: " + passive.getName() + ": " + passives.get(c.getName()) + " secs", x, y+280);
        else
            g.drawString("P: " + passive.getName(),x,y+280);
        g.drawString("Q: " +df.format(qcd/((100.0+cdr)/100))+" secs",x,y+300);
        g.drawString("W: " +df.format(wcd/((100.0+cdr)/100))+" secs",x,y+320);
        g.drawString("E: " +df.format(ecd/((100.0+cdr)/100))+" secs",x,y+340);
        g.drawString("R: " +df.format(rcd/((100.0+cdr)/100))+" secs",x,y+360);
        df.applyPattern("#");
        double cooldown1, cooldown2;
        if (s1.getId() == 12)
            cooldown1 = 300.0;
        else
            cooldown1 = s1.getCooldown().get(0);
        if (s2.getId() == 12)
            cooldown2 = 300.0;
        else
            cooldown2 = s2.getCooldown().get(0);
        g.drawString(s1.getName()+": "+df.format(insight * cooldown1)+" secs",x,y+380);
        g.drawImage(s1Icon.getScaledInstance(30, 30, Image.SCALE_REPLICATE),x+125,y+355,null);
        g.drawString(s2.getName()+": "+df.format(insight * cooldown2)+" secs",x,y+400);
        g.drawImage(s2Icon.getScaledInstance(30, 30, Image.SCALE_REPLICATE),x+125,y+385,null);
        df.applyPattern("#.#");
        g.drawString("CDR: " + df.format(cdr) + "%",x,y+420);
        
    }
    
    private Image getIcon(int i) throws RiotApiException{
        String s="",name="";
        switch(i){
            case(0):
                name  = c.getKey();
                s="https://ddragon.leagueoflegends.com/cdn/"+version+"/img/champion/" +name+".png";
                Image image = null;
                try {
                    image = ImageIO.read(new File("cache/images/champions/" + c.getKey() + ".png"));
                } catch (IOException ex) {
                    Logger.getLogger(Enemy.class.getName()).log(Level.SEVERE, null, ex);
                }
                return image;
        
                //break;
            case(1):
                name = s1.getKey();
                s="https://ddragon.leagueoflegends.com/cdn/"+version+"/img/spell/" +name+".png";
                break;
             case(2):
                 name = s2.getKey();
                 s="https://ddragon.leagueoflegends.com/cdn/"+version+"/img/spell/" +name+".png";
                 break;
             case(3):
                 if(l!=null){
                    System.out.println(l.getEntryBySummonerId(this.p.getSummonerId()).getRank());
                    name=l.getTier().toLowerCase()+"_"+numeralToNumber(l.getEntryBySummonerId(this.p.getSummonerId()).getRank());
                    s="https://opgg-static.akamaized.net/images/medals/" + name + ".png";
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
