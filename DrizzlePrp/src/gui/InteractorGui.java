/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import shared.GuiUtils;

public class InteractorGui extends Interactor
{

    public boolean AskOkCancel(String question)
    {
        boolean r = GuiUtils.getOKorCancelFromUser(question, "Question");
        return r;
    }

    public String AskQuestion(String question)
    {
        String r = GuiUtils.getStringFromUser(question, "Question");
        return r;
    }

}
