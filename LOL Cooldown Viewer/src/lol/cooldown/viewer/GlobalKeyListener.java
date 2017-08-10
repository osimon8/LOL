/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.cooldown.viewer;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListener implements NativeKeyListener {
        int count = 0;
        View v;
        public GlobalKeyListener(View v){
            this.v = v;
        }
        
	public void nativeKeyPressed(NativeKeyEvent e) {
		//System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
                //System.out.println("hit");
                if (count == 0 && e.getKeyCode() == NativeKeyEvent.VC_TAB){
                    count++;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GlobalKeyListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ImageHandler.screenshot();
                }
                
//		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
//                    try {
//                        GlobalScreen.unregisterNativeHook();
//                    } catch (NativeHookException ex) {
//                       // Logger.getLogger(GlobalKeyListener.class.getName()).log(Level.SEVERE, null, ex);
//                    }
		//}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		//System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
                if (count != 0 && e.getKeyCode() == NativeKeyEvent.VC_TAB){
                    count = 0;
                    System.out.println("hey");
                    v.newScreenshot();
                }

	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		//System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
	}

	public void listen(){
                Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
                logger.setLevel(Level.OFF);
                logger.setUseParentHandlers(false);
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(new GlobalKeyListener(v));
	}
}
