/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.cooldown.viewer;

/**
 *
 * @author Owen
 */

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import com.google.gson.*;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.endpoints.static_data.constant.ChampionListTags;
import net.rithms.riot.api.endpoints.static_data.constant.ChampionTags;
import net.rithms.riot.api.endpoints.static_data.constant.ItemListTags;
import net.rithms.riot.api.endpoints.static_data.constant.Locale;
import net.rithms.riot.api.endpoints.static_data.constant.MasteryListTags;
import net.rithms.riot.api.endpoints.static_data.constant.RuneListTags;
import net.rithms.riot.api.endpoints.static_data.constant.SpellListTags;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionList;
import net.rithms.riot.api.endpoints.static_data.dto.Item;
import net.rithms.riot.api.endpoints.static_data.dto.ItemList;
import net.rithms.riot.constant.Platform;



public class LOLCooldownViewer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //GlobalMouseListenerExample.listen();
        try{
            View v = new View();
            JFrame f = new JFrame("LOL Cooldown Viewer");
            f.getContentPane().add(v);
            //f.getContentPane().add(new JLabel("loading... ", loading, JLabel.CENTER));
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //ImageIcon i = new ImageIcon("flash-icon.jpg");
            try{
                f.setIconImage(ImageIO.read(LOLCooldownViewer.class.getResource("flash-icon.jpg")));
            }catch(IOException e){System.out.println("no");}
            f.pack();
            f.setVisible(true);
            GlobalKeyListener listener = new GlobalKeyListener(v);
            listener.listen();
//        try {
//            RiotApi api = new RiotApi("21190d18-af0f-48ec-afe9-926f9fe237a4");
//            
//            Summoner summoner=null;
//            summoner = api.getSummonerByName(Region.NA, "Oomni");
//            ChampionList champRaw = api.getDataChampionList();
//            Map<String, Champion> champs = champRaw.getData();
//            long id = summoner.getId();
//            long enemyTeamId=0;
//            try{
//            CurrentGameInfo gameInfo = api.getCurrentGameInfo(PlatformId.NA, id);
//            List<Participant> participants = gameInfo.getParticipants();
//            List<Enemy> enemies = new ArrayList<>();
//            int ctr=1;
//            for(Participant p : participants){
//                if(p.getSummonerId()==id){
//                   enemyTeamId =(ctr<=5)?200:100;
//                }
//                ctr++;
//            }
//            for(Participant p : participants){
//                if(p.getTeamId()==enemyTeamId){
//                    System.out.println(p.getSummonerName());
//                    System.out.println(api.getDataChampion((int)p.getChampionId()).getName());
//                    enemies.add(new Enemy(p,api));
//                }
//            }
//            v.setEnemies(enemies);
//            }
//            catch(RiotApiException e){
//                System.out.println(summoner.getName()+" is not currently in a game.");
//            }
//        } catch (RiotApiException ex) {
//            Logger.getLogger(LOLCooldownViewer.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("fail");
//        }
        }catch(RiotApiException ex){Logger.getLogger(LOLCooldownViewer.class.getName()).log(Level.SEVERE, null, ex);}
    }
   
    
    
    public static void updateCache() throws RiotApiException{
        ApiConfig config = new ApiConfig().setKey("RGAPI-80e44638-93fe-4db5-b498-2bab841759ba");
        RiotApi api = new RiotApi(config);
        Cacher.store(api.getDataVersions(Platform.NA), "cache/versions.ser");
        String vsn = ((List<String>)Cacher.read("cache/versions.ser")).get(0);
        Cacher.store(api.getDataSummonerSpellList(Platform.NA, Locale.EN_US, vsn, true, SpellListTags.ALL), "cache/spells.ser");
        Cacher.store(api.getDataChampionList(Platform.NA, Locale.EN_US, vsn, true, ChampionListTags.ALL), "cache/champions.ser");
        Cacher.store(api.getDataRuneList(Platform.NA, Locale.EN_US, vsn, RuneListTags.ALL), "cache/runes.ser");
        Cacher.store(api.getDataMasteryList(Platform.NA, Locale.EN_US, vsn, MasteryListTags.ALL), "cache/masteries.ser");
        Cacher.store(api.getDataItemList(Platform.NA, Locale.EN_US, vsn, ItemListTags.ALL), "cache/items.ser");
        
        ItemList iList = (ItemList)Cacher.read("cache/items.ser");
        ChampionList cList = (ChampionList)Cacher.read("cache/champions.ser");
        //String imgLoc = "xd";
        //System.out.println(iList.getData().get("3082").getImage().getFull());
        for(String key : iList.getData().keySet()){
            Item i = iList.getData().get(key);
            //System.out.println(i.getEffect());
            String imgLoc = i.getImage().getFull();
            String s = "http://ddragon.leagueoflegends.com/cdn/" + vsn+  "/img/item/" + imgLoc;
            BufferedImage image = null;
            try {
                URL url = new URL(s);
                image = ImageIO.read(url);
            }  catch (IOException e) {
            }
            try {
                ImageIO.write(image, "png", new File("cache/images/items/" + i.getImage().getFull()));
            } catch (IOException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(String key : cList.getData().keySet()){
            Champion c = cList.getData().get(key);
            //System.out.println(i.getEffect());
            String imgLoc = c.getImage().getFull();
            String s = "http://ddragon.leagueoflegends.com/cdn/" + vsn+  "/img/champion/" + imgLoc;
            BufferedImage image = null;
            try {
                URL url = new URL(s);
                image = ImageIO.read(url);
            }  catch (IOException e) {
            }
            try {
                ImageIO.write(image, "png", new File("cache/images/champions/" + c.getImage().getFull()));
            } catch (IOException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
