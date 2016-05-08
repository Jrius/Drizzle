/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

/**
 *
 * @author user
 */
public abstract class Interactor
{
    //used for general user questioning at runtime.


    //just an ok/cancel question
    public abstract boolean AskOkCancel(String question);

    //just an question with a string response.  Can be "" but never null.
    public abstract String AskQuestion(String question);

    public String AskQuestionDefault(String question, String defaultValue)
    {
        String response = AskQuestion(question);
        if(response.equals("")) response = defaultValue;
        return response;
    }

}
