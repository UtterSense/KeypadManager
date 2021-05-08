package net.uttersense.libkeypad;

import java.util.ArrayList;
import java.util.Iterator;



public class KeypadManager {

    private ArrayList clientListeners;
    private static KeypadManager instance = null; //Static ensures we only create one instance of this class
    private String keypad_text = null;
    static String DEBUG_LOG_TAG = "LOG";


//Load the native library:
    static {
        System.loadLibrary("keypad-man-jni");
    }
    /*
     * Declare the JNI native methods:
     */
    public native String stringFromJNI();
    public native int initialise();
    public native void close();
    public native void toggle();
    public native void switchLED(int mode);


    //Private Constructor - can not be called externally
    private KeypadManager()
    {

        //Set up array for client listeners:
        clientListeners = new ArrayList();


        System.out.println("KeypadManager: ...In Constructor");

    }


    public static KeypadManager getInstance()
    {
        synchronized (KeypadManager.class) {
            if (instance == null) {
                instance = new KeypadManager();

            }
        }
        System.out.println("KeypadManager: getInstance() called");
        //Start off timer task
        return instance;
    }

    public void registerListener(IKeypadManagerListener client)
    {
        clientListeners.add(client);
        System.out.println("The CLIENT listener has been registered...");
    }

    public void unregisterListener(IKeypadManagerListener client)
    {
        clientListeners.remove(client);
        System.out.println("CLIENT has been un-registered...");

    }

    //Callback function from native library:
    public void updateStrData(String str)
    {
        keypad_text = str;
        notifyClients(str);
    }

    private void notifyClients(String str)
    {
        for (Iterator it = clientListeners.iterator(); it.hasNext();   ) {
            ((IKeypadManagerListener) (it.next())).onKeypadEvent(str);
            System.out.println("KeypadManager: Callback has been triggered...");
        }
    }


}
