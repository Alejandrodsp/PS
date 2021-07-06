/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carregador;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import montador.Montador;
import logic.String24;
import logic.Memory.*;
import logic.CPU;

/**
 *
 * @author Alejandro
 */
public class Carregador {

    public static void carregador(File bin) {
        var starts = Montador.getStart();
        try {
            FileReader fr = new FileReader(bin);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int i = 0;
            int j;
            while ((line = br.readLine()) != null) {
                if (line.length() == 8) { // INSTRUÇÃO DE 1 BYTE
                    CPU.mem.mem_write(starts.get(0) + i, 1, new String24(line));
                } else if (line.length() == 16) {//INSTRUÇÃO DE 2 BYTES
                    CPU.mem.mem_write(starts.get(0) + i, 2, new String24(line));
                } else if (line.length() == 24) {//INSTRUÇÃO DE 3 BYTES
                    if (line.charAt(6) == '0' && line.charAt(7) == '1') {//VERIFICA SE É IMEDIATO
                        CPU.mem.mem_write(starts.get(0) + i, 3, new String24(line));
                    }  else {//SE NÃO SOMA O VALOR DO START
                        String24 s = new String24(line);
                        s.setBits(s.toInt() + starts.get(0));
                        CPU.mem.mem_write(starts.get(0) + i, 3, s);
                    }
                } else {//DE 4 BYTES
                     if (line.charAt(6) == '0' && line.charAt(7) == '1') {//IMEDIATO
                        CPU.mem.mem_write(starts.get(0) + i, 4, new String24(line));
                    } else if (line.startsWith("W")) { // TIRA OS WS DAS INSTRUÇÕES WORDS
                        line = line.replace("W", "");
                        CPU.mem.mem_write(starts.get(0) + i, 3, new String24(line));
                    } else {
                        String24 s = new String24(line);
                        s.setBits(s.toInt() + starts.get(0));
                        CPU.mem.mem_write(starts.get(0) + i, 4, s);
                    }
                }
                i += line.length()/8;         
            }
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Carregador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
