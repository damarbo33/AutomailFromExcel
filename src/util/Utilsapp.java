/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author dmarcobo
 */
public class Utilsapp {
    
    /**
     * 
     * @param date
     * @param format
     * @return 
     */
    public static String dateToStr(Date date, String format){
        DateFormat dateFormat = new SimpleDateFormat(format); 
        return dateFormat.format(date);
    }
    
    /**
     * 
     * @param date
     * @param format
     * @return 
     */
    public static Date strToDate(String date, String format) throws ParseException{
        // Setting the pattern
        SimpleDateFormat sm = new SimpleDateFormat(format);
        //Converting the String back to java.util.Date
        return sm.parse(date);
    }
    
}
