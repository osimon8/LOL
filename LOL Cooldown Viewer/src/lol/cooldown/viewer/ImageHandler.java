/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.cooldown.viewer;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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
            BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
            BufferedImage subImage = screenFullImage.getSubimage(1287, 418, 48, 48);
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
            ImageIO.write(subImage, "png", new File(newName));
            return subImage;
        } catch (IOException ex) {
            Logger.getLogger(LOLCooldownViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static double compare(BufferedImage img1, BufferedImage img2){
        if ((img1.getWidth() != img2.getWidth()) || (img1.getHeight() != img2.getHeight()))
            return 0;
        double num = 0;
        for(int i = 0; i < img1.getHeight(); i++){
            //System.out.println(i);
            for(int j = 0;j < img1.getWidth(); j++){
                //System.out.println(j);
                //System.out.println(distance(new Color(img1.getRGB(j,i)), new Color(img2.getRGB(j, i))));
                if (distance(new Color(img1.getRGB(j,i)), new Color(img2.getRGB(j, i))) <= 50){
                    //System.out.println(new Color(img1.getRGB(j, i)));
                    num++;
                }
            }
        }
        
        return num / (img1.getWidth() * img1.getHeight());
    } 
    
    
    public static boolean imageEqual(BufferedImage img1, BufferedImage img2, double tolerance){
     return compare(img1, img2) >= tolerance;   
    }
    
    private static double distance(Color c1, Color c2){
        
     return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2));   
    }
    
}
