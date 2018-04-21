// -*- Java -*-
/*
 * <copyright>
 * 
 *  Copyright (c) 2002
 *  Institute for Information Processing and Computer Supported New Media (IICM),
 *  Graz University of Technology, Austria.
 * 
 * </copyright>
 * 
 * <file>
 * 
 *  Name:    Input.java
 * 
 *  Purpose: Input reads and parses the KWIC input file
 * 
 *  Created: 05 Nov 2002 
 * 
 *  $Id$
 * 
 *  Description:
 *    Input reads and parses the KWIC input file
 * </file>
*/

package kwic.es;

/*
 * $Log$
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 *  An object of the Input class is responsible for reading and parsing the content of 
 *  a KWIC input file. The format of the KWIC input file is as follows:
 *  <ul>
 *  <li>Lines are separated by the line separator character(s) (on Unix '\n', on Windows '\r\n')
 *  <li>Each line consists of a number of words. Words are delimited by any number and combination
 *  of the space chracter (' ') and the horizontal tabulation chracter ('\t').
 *  </ul>
 *  The entered data is parsed and stored in memory as an instance of the LineStorageWrapper class. The data
 *  is parsed in the following way:
 *  <ul>
 *  <li>All line separators are removed from the data; for each new line in the file a new line
 *  is added to the LineStorageWrapper instance
 *  <li>All horizontal tabulation word delimiters are removed
 *  <li>All space character word delimiters are removed
 *  <li>From characters between any two word delimiters a new string is created; the new string
 *  is added to the LineStorageWrapper instance.
 *  </ul>
 *  @author  dhelic
 *  @version $Id$
*/

public class Input{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Constructors
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * This method reads and parses a KWIC input file. If an I/O exception occurs
 * during the execution of this method, an error message is shown and program
 * exits.
 * @param file name of KWIC input file
 * @param line_storage holds the parsed data
 */

  public void parse(String file, LineStorageWrapper line_storage){
    try{
      BufferedReader reader = new BufferedReader(new FileReader(file));

      String line = reader.readLine();
      while(line != null){
        StringTokenizer tokenizer = new StringTokenizer(line); // whitespaces are default delimiters
        if(tokenizer.countTokens() > 0){
          ArrayList words = new ArrayList();          
          while(tokenizer.hasMoreTokens())
            words.add(tokenizer.nextToken());

          String[] line_rep = new String[words.size()];
          for(int i = 0; i < line_rep.length; i++)
            line_rep[i] = (String) words.get(i);

          line_storage.addLine(line_rep);
        }

        line = reader.readLine();
      }
      
    }catch(FileNotFoundException exc){
      exc.printStackTrace();
      System.err.println("KWIC Error: Could not open " + file + "file.");
      System.exit(1);
    }catch(IOException exc){
      exc.printStackTrace();
      System.err.println("KWIC Error: Could not read " + file + "file.");
      System.exit(1);
    }
  }


  public void parseCommandAdd(String name, LineStorageWrapper line_storage){
      String[] line_rep = tokenizeString(name);
      if(line_rep != null)
          line_storage.addLine(line_rep);

  }

  public void parseCommandDelete(String name, LineStorageWrapper line_storage){

      int index = -1;
      if(name != null)
        for(int i=0; i<line_storage.getLineCount(); i++){
          if(line_storage.getLineAsString(i).equals(name)){
            index = i;
            break;
          }
        }

      if(index != -1)
        line_storage.deleteLine(index);
  }

  public String[] tokenizeString(String name) {
    StringTokenizer tokenizer = new StringTokenizer(name); // whitespaces are default delimiters
    if (tokenizer.countTokens() > 0) {
      ArrayList words = new ArrayList();
      while (tokenizer.hasMoreTokens())
        words.add(tokenizer.nextToken());

      String[] line_rep = new String[words.size()];
      for (int i = 0; i < line_rep.length; i++)
        line_rep[i] = (String) words.get(i);

      return line_rep;
    }
    else
      return null;
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
