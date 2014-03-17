/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.util;

import java.util.ResourceBundle;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author mido
 */
public class Resources {
    private static final ResourceBundle rb;
    
    static    {
    rb= ResourceBundle.getBundle("sg.edu.astar.taxi360.util.resource");
    }
    
    public static String get(String key){
        return rb.getString(key);
    }
    
    public static synchronized  String getAccessKey(){
        return RandomStringUtils.randomAlphanumeric(31);
    }
    
}
