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
 *  Name:    KWIC.java
 * 
 *  Purpose: The Master Control class
 * 
 *  Created: 05 Nov 2002 
 * 
 *  $Id$
 * 
 *  Description:
 *    The Master Control class
 * </file>
*/

package kwic.es;

/*
 * $Log$
*/

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *  The KWIC system implemented by means of event-based architecture consists of the
 *  following modules:
 * <ul>
 *  <li>
 *  Master Control module, which is responsible for controling all other modules
 *  in the system
 *  <li>Input module, which is responsible for reading the input file, parsing it
 *  and adding lines to Original Line Storage module
 *  <li>Original Line Storage module, which stores original lines
 *  <li>Circular Shifter module, which produces all circular shifts from original lines
 *  <li>Shifted Line Storage module, which stores circular shifts 
 *  <li>Alphabetizer module, which sorts shifted lines
 *  <li>Output module, which prints out the sorted shifts in a nice format.
 * </ul>
 *  Input module reads the input file, parses it
 *  and adds lines to Original Line Storage module. Any change (e.g. a new line 
 *  was added) to Original Line Storage module causes an event (message) to be
 *  sent to all modules which declare interest in tracking these changes. In 
 *  the current system, Circular Shifter module declares such interest. Thus, 
 *  upon receiving a notification event stating that the new line was added to
 *  Original Line Storage module, Circular Shifter module processes the line and
 *  creates circular shifts for that line. The newly created circular shifts are
 *  stored in Shifted Line Storage module. Similarly to changes in Original Line
 *  Storage module, any change (e.g. a new circular shift was added) to Shifted 
 *  Line Storage module causes an event (message) to be sent to all modules which
 *  declare interest in tracking these changes. Obviously, in the current system 
 *  Alphabetizer module declares such interest. Thus, upon receiving a notification
 *  event stating that the new circular shift was added to Shifted Line Storage module, 
 *  Alphabetizer module sorts this circular shift. The sorted shifts are kept in
 *  Shifted Line Storage as well. Once when Input module has finished with parsing
 *  procedure, which implies that all circular shifts have been created and sorted
 *  (because of notification mechanism) Outpur module prints out the sorted shifts.
 *  In the current implementation an object of the KWIC class creates and initializes
 *  all other objects from the event-based KWIC implementation to achieve the desired 
 *  functionality. Thus, KWIC instance creates the following instances of other classes:
 *  <ul>
 *  <li>An instance of LineStorageWrapper class that holds the original lines
 *  <li>An instance of LineStorageWrapper class that holds the circular shifts
 *  <li>An instance of Input class which parses the input file and adds lines
 *  to the line storage for original lines
 *  <li>An instance of CircularShift class which declares its interest in
 *  changes to the line storage for original lines. Thus, whenever it receives a
 *  message that a new line has been added it produces circular shifts for that line
 *  and stores them into the line storage for circular shifts
 *  <li>An instance of Alphabetizer class which declares its interest in
 *  changes to the line storage for circular shifts. Thus, whenever it receives a
 *  message that a new circular shift has been added it sorts the line storage for
 *  circular shifts
 *  <li>An instance of Output class which prints all shifts from the line 
 *  storage for circular shifts.
 *  Further, the KWIC class provides also the main method which checks the command line
 *  arguments.
 *  </ul>
 *  @author  dhelic
 *  @version $Id$
*/

public class KWIC{

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
 * Parses the data, makes shifts and sorts them. At the end prints the
 * sorted shifts.
 * @param file name of the input file
 */

  public void execute(String file){
    LineStorageWrapper lines = new LineStorageWrapper();
    LineStorageWrapper shifts = new LineStorageWrapper();

    Input input = new Input();
    CircularShifter shifter = new CircularShifter(shifts);
    lines.addObserver(shifter);
    Alphabetizer alphabetizer = new Alphabetizer();
    shifts.addObserver(alphabetizer);
    Output output = new Output();
    input.parse(file, lines);
    output.print(shifts);
  }

//----------------------------------------------------------------------
/**
 * Main function checks the command line arguments. The program expects 
 * exactly one command line argument specifying the name of the file 
 * that contains the data. If the program has not been started with 
 * proper command line arguments, main function exits
 * with an error message. Otherwise, a KWIC instance is created and program
 * control is passed to it.
 * @param args command line arguments
 */

  public static void main(String[] args){
    if(args.length != 1){
      System.err.println("KWIC Usage: java kwic.ms.KWIC file_name");
      System.exit(1);
    }

    KWIC kwic = new KWIC();
//    kwic.execute(args[0]);
    kwic.executeInteractively();
  }

  public void executeInteractively(){
    LineStorageWrapper lines = new LineStorageWrapper();
    LineStorageWrapper shifts = new LineStorageWrapper();
    Map<String, Integer> wordsIndexMap = new HashMap<>();

    Input input = new Input();
    CircularShifter shifter = new CircularShifter(shifts);
    lines.addObserver(shifter);
    Alphabetizer alphabetizer = new Alphabetizer();
    shifts.addObserver(alphabetizer);
    WordsIndex wordsIndex = new WordsIndex(wordsIndexMap);
    lines.addObserver(wordsIndex);
    Output output = new Output();

    System.out.println("Add, Delete, Index, Print, Quit");
    Scanner sc = new Scanner(System.in);
    System.out.print("command->");
    String i = sc.nextLine();
    while(!i.equals("q")) {
      if (i.equals("a")) {
        System.out.print("name->");
        String name = sc.nextLine();
        input.parseCommandAdd(name, lines);
      }
      if (i.equals("d")) {
        System.out.print("name->");
        String name = sc.nextLine();
        input.parseCommandDelete(name, lines);
      }
      if (i.equals("p")) {
        output.print(shifts);
      }
      if (i.equals("i")) {
        output.printWordsIndexMap(wordsIndexMap);
      }
      System.out.print("command->");
      i = sc.nextLine();
    }

  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
