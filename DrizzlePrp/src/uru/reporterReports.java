/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru;

import shared.reporter;
import prpobjects.Uruobjectdesc;

public abstract class reporterReports
{
    public static class refEncountered implements reporter.ReportEvent
    {
        public Uruobjectdesc from;
        public Uruobjectdesc to;
        
        public refEncountered(Uruobjectdesc from, Uruobjectdesc to)
        {
            this.from = from;
            this.to = to;
        }
    }
}
