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
 *  Name:    Alphabetizer.java
 * 
 *  Purpose: Sorts circular shifts alphabetically
 * 
 *  Created: 05 Nov 2002 
 * 
 *  $Id$
 * 
 *  Description:
 *    Sorts circular shifts alphabetically
 * </file>
*/

package kwic.es;

/*
 * $Log$
*/

import java.util.Observable;
import java.util.Observer;

/**
 *  Similarly to CircularShifter class, Alphabetizer class implemets the "Observer" 
 *  part of the standard "Observable"-"Observer" mechanism. However, an object
 *  of Alphabetizer class "observes" a LineStorageWrapper object, which keeps
 *  circular shifts, whereas an object of CircularShifter class "observes" a LineStorageWrapper
 *  object, which keeps original lines from a KWIC input file. Any event produced and sent
 *  by the observed LineStorageWrapper object (e.g. whenever a new circular shift
 *  has been added) is catched by Alphabetizer object. In turn, this leads to 
 *  sorting the circular shifts alphabetically.
 *  @author  dhelic
 *  @version $Id$
*/

public class Alphabetizer implements Observer{

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
 */

  public void update(Observable observable, Object arg){
    LineStorageWrapper shifts = (LineStorageWrapper) observable;
    LineStorageChangeEvent event = (LineStorageChangeEvent) arg;

    switch(event.getType()){
    case LineStorageChangeEvent.ADD:
      int count = shifts.getLineCount();
      String shift = shifts.getLineAsString(count - 1);
      for(int i = 0; i < (count - 1); i++){
        if(shift.compareTo(shifts.getLineAsString(i)) <= 0){
           shifts.insertLine(shifts.getLine(count - 1), i);
           shifts.deleteLine(count);
           break;
        }           
      }
      break;

    default:
      break;      
    }
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
