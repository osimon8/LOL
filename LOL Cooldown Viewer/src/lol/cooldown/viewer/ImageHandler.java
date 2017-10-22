/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.cooldown.viewer;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.rithms.riot.api.endpoints.static_data.dto.Item;
import net.rithms.riot.api.endpoints.static_data.dto.ItemList;

/**
 *
 * @author Owen
 */
public class ImageHandler {
    
    public static void screenshot(){
        try {
            Robot robot = new Robot();
            String format = "png";
            String fileName = "PartialScreenshot." + format;

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureRect = new Rectangle(0, 0, screenSize.width, screenSize.height);
            System.out.println(screenSize.width + ", " + screenSize.height);
            BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
           // BufferedImage subImage = screenFullImage.getSubimage(1287, 418, 48, 48);
            ImageIO.write(screenFullImage, format, new File(fileName));
            

            System.out.println("Screenshot captured");
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }
    
    public static BufferedImage crop(int x, int y, int w, int h, String filepath, String newName){
        try {
            BufferedImage img = ImageIO.read(new File(filepath));
            BufferedImage subImage = img.getSubimage(x, y, w, h);
            subImage.equals(img);
            if (newName != null)
                ImageIO.write(subImage, "png", new File(newName));
            return subImage;
        } catch (IOException ex) {
            Logger.getLogger(LOLCooldownViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static double compare(BufferedImage img1, BufferedImage img2, double region){
        int width = img1.getWidth();
        int height = img1.getHeight();
        if ((width != img2.getWidth()) || (height != img2.getHeight()))
            return 0;
        double num = 0;
        //System.out.println(width);
        int startX = (int)(width * region);
        int startY = (int) (width * region);
        int endX = (int)(width - region * width);
        int endY = (int)(height - region * height);
        endX = endX >= width ? width - 1: endX;
        endY = endY >= height ? height - 1 : endY;
        
        for(int y = startY; y <= endY; y++){
            for(int x = startX; x <= endX; x++){
                //System.out.println("(" + x + ", " + y + ")");
                //System.out.println(distance(new Color(img1.getRGB(j,i)), new Color(img2.getRGB(j, i))));
                if (distance(new Color(img1.getRGB(x,y)), new Color(img2.getRGB(x, y))) <= 45){
                    //System.out.println(new Color(img1.getRGB(j, i)));
                    num++;
                }
            }
        }
        
        return num / ((width - 2 * region * width) * (height - 2 * region * height));
    } 
    
    
    public static double compare(BufferedImage img1, BufferedImage img2){
            return compare(img1, img2, 0); 
            
        }
    
    
    public static boolean imageEqual(BufferedImage img1, BufferedImage img2, double tolerance){
     return compare(img1, img2, .35) >= tolerance;   
    }
    
    public static List<BufferedImage> getSummonerItemPics(String filepath, int summoner){
     ArrayList<BufferedImage> pics = new ArrayList<>();   
     //int startX = 1287 + 33 * (summoner  -1);
     int y = 342 + 76 * (summoner - 1);
     for(int x = 1286; x < 1286 + 34 * 6; x += 34){
         BufferedImage temp = ImageHandler.crop(x, y, 34, 34, filepath, null);
         if(!isEmpty(temp))
            pics.add(temp);
         else
             break;
     }
     return pics;
    }
    
    public static List<BufferedImage> getSummonerItemPics(int summoner){
        return getSummonerItemPics("in-game4.png", summoner);
    }
    
    public static List<List<BufferedImage>> getAllSummonerItemPics(){
        long time1 = System.currentTimeMillis();
        ArrayList<List<BufferedImage>> pics = new ArrayList<>();
        for(int i = 1; i<=5; i++)
            pics.add(getSummonerItemPics(i));
        System.out.println((System.currentTimeMillis() - time1));
        return pics;  
    }
    
    public static boolean isEmpty(BufferedImage img){
        try {
            return imageEqual(img, ImageIO.read(new File("empty_slot.png")), 0.9);
        } catch (IOException ex) {
            Logger.getLogger(ImageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }
    
    public static Item parseItem(BufferedImage img){
        String version = ((List<String>) Cacher.read("cache/versions.ser")).get(0);
        ItemList iList = (ItemList)Cacher.read("cache/items.ser");
        //String imgLoc = "xd";
        //System.out.println(iList.getData().get("3082").getImage().getFull());
        for(String key : iList.getData().keySet()){
            Item i = iList.getData().get(key);
            if((i.getTags() != null && i.getTags().contains("CooldownReduction"))){
                String imgLoc = i.getImage().getFull();
                String s = "http://ddragon.leagueoflegends.com/cdn/" + version+  "/img/item/" + imgLoc;
                BufferedImage image = null;
                try {
                    URL url = new URL(s);
                    image = ImageIO.read(url);
                }  catch (IOException e) {
                }
                BufferedImage img1 = new BufferedImage(34, 34, BufferedImage.TYPE_INT_RGB);
                Graphics g2 = img1.createGraphics();
                g2.drawImage(image, 0, 0, 34, 34, null);
                g2.dispose();
                //BufferedImage img1 = image.getScaledInstance(33, 35, Image.SCALE_REPLICATE);
                //BufferedImage img2 = ImageHandler.crop(1286, 342, 33, 32, "in-game4.png", null);
                
                //g.drawImage(img1, 100, 200, null);
                //g.drawImage(img2, 300, 200, null);
                //System.out.println(ImageHandler.compare(img1, img2));
                if(ImageHandler.imageEqual(img1, img, 0.8)){
                    System.out.println(i.getName());
                    return i;
                    //break;
                }
            }
        }
        return null;
    }
    
    
    private static double distance(Color c1, Color c2){
        
     return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2));   
    }
    
}
