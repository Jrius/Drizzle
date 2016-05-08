/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared.State;

//Reusable LogBox Swing component

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.BadLocationException;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.Color;

public class LogBoxStateless extends JTextPane
{
    private boolean wordwrap = false;
    private StyledDocument doc;
    Style normalStyle;
    Style warningStyle;
    Style errorStyle;

    public LogBoxStateless()
    {
        super();
        Initialize();
        //this.setBackground(Color.red);
    }
    public void ClearAll()
    {
        super.setText("");
    }
    public void setText(String s)
    {
        ClearAll();
        this.append(s);
    }
    public void Alphabetise()
    {
        //alphabetise...
        //String alltext = this.jTextArea1.getText();
        String alltext = this.getText();
        String[] lines = alltext.split("\n");
        java.util.List<String> list = java.util.Arrays.asList(lines);
        java.util.Collections.sort(list);

        int linecount = list.size();
        StringBuilder newtext = new StringBuilder();
        for(int i=0;i<linecount;i++)
        {
            newtext.append(list.get(i));
            newtext.append("\n");
        }
        //this.jTextArea1.setText(newtext.toString());
        this.setText(newtext.toString());
    }
    public void RemoveDuplicateLines()
    {
        //remove duplicate lines, even if they're not adjacent.

        //String alltext = this.jTextArea1.getText();
        String alltext = this.getText();
        String[] lines = alltext.split("\n");
        int linecount = lines.length;
        StringBuilder newtext = new StringBuilder();

        for(int i=0;i<linecount;i++)
        {
            String curline = lines[i];
            if(curline != null) //if the current line isn't empty.
            {
                newtext.append(curline);
                newtext.append("\n");

                for(int j=i+1;j<linecount;j++) //search for duplicates
                {
                    if(curline.equals(lines[j]))
                    {
                        lines[j] = null; //remove the duplicate.
                    }
                }
            }
        }

        //this.jTextArea1.setText(newtext.toString());
        this.setText(newtext.toString());
    }
    public void append(String s)
    {
        append(s, shared.m.MessageType.normal);
    }
    public void append(String s, shared.m.MessageType type)
    {
        //get the style
        Style style;
        switch(type)
        {
            case normal:
                style = normalStyle;
                break;
            case warning:
                style = warningStyle;
                break;
            case error:
                style = errorStyle;
                break;
            case console:
                style = normalStyle;
                break;
            case status:
                style = normalStyle;
                break;
            default:
                style = normalStyle;
                break;
        }

        //add the string
        try{
            doc.insertString(doc.getLength(), s, style);
        }catch(BadLocationException e){
            throw new shared.nested(e);
        }
    }

    public void setWordwrap(boolean value)
    {
        wordwrap = value;
    }

    public void setSize(java.awt.Dimension d)
    {
        //this is so that it goes as wide as it needs, otherwise there is a color issue.
        if(d.width < getParent().getSize().width)
        {
            d.width = getParent().getSize().width;
        }
        super.setSize(d);
    }

    public boolean getScrollableTracksViewportWidth()
    {
        if(wordwrap)
        {
            return super.getScrollableTracksViewportWidth();
        }
        else
        {
            return false; //disables word wrap.
        }
    }

    private void Initialize()
    {
        //add menus
        shared.State.common.addSpecialMenu(null, this);

        //set default font and background
        //Font font = UIManager.getFont("TextArea.font");
        //Color background = UIManager.getColor("TextArea.background");
        //this.setFont(font);
        //this.setBackground(background);

        //make readonly
        this.setEditable(false);
        //pane.setw

        //create normal style
        StyleContext sc = new StyleContext();
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        normalStyle = sc.addStyle("Normal", defaultStyle);
        //StyleConstants.setFontSize(normalStyle, 12); //not needed, as the font is already set.

        //create warning style
        warningStyle = sc.addStyle("Warning", normalStyle);
        Color warningColor = shared.m.warningColor;
        StyleConstants.setForeground(warningStyle, warningColor);

        //create error style
        errorStyle = sc.addStyle("Error", normalStyle);
        Color errorColor = shared.m.errorColor;
        StyleConstants.setForeground(errorStyle, errorColor);

        //test
        doc = this.getStyledDocument();
        //try{
        //    //for(int i=0;i<10000;i++){
        //    doc.insertString(doc.getLength(), "Hello! I am the very model of the modern major general.\n", normalStyle);
        //    doc.insertString(doc.getLength(), "Warning!\n", warningStyle);
        //    doc.insertString(doc.getLength(), "Error!\n", errorStyle);
        //    //}
        //}catch(BadLocationException e){
        //    throw new shared.nested(e);
        //}

        //append("hi");

    }

}
