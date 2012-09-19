/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mindseye;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

/**
 *
 * @author tanvi
 */
public class test {
    
    
    
    public static void main(String args[]){
        String pattern = "[\\s]+";
        String colours = "Red White      Blue    Green        Yellow    Orange";
 
        Pattern splitter = Pattern.compile(pattern);
        String[] result = splitter.split(colours);
 
        for (String colour : result) {
            System.out.println("Colour = \"" + colour + "\"");
        }
        File f = new File("/Users/girish/Dropbox/Tanvi_code/data_files/new_object_list_no_hyphen.txt");
        System.out.println("name " + f.getName());
        
        String current = "ball";
        if (current.charAt(current.length()-1)=='s') System.out.println("worked");
    }
    
    
    
}
