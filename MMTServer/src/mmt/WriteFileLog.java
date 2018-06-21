/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keven
 */
enum TYPELOG {
    ERROR, INFORMATION, WARNING
};

public class WriteFileLog {

    private static String filename = "log.log";
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void deleteFileLog() {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void writeFileLog(String username, TYPELOG type, String content) {
        Date date = new Date();
        StringBuilder strbd = new StringBuilder();
        strbd.append(dateFormat.format(date)).append(" - ");
        if (type == TYPELOG.ERROR) {
            strbd.append("ERORR - ");
        } else if (type == TYPELOG.INFORMATION) {
            strbd.append("INFORMATION - ");
        } else {
            strbd.append("WARNING - ");
        }

        strbd.append(username).append(" - ").append(content);

        try (FileWriter fw = new FileWriter(filename, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(strbd.toString());

        } catch (IOException ex) {
            Logger.getLogger(WriteFileLog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
