see readme.txt in src/gui

>54,520 lines of code as of Dec 2 2008 :P

In order to show the state retention gui components in Netbeans:
  right-click the gui component palette, and go to the palette manager.
  create a new category.
  "add from project" all the components in shared.State
  restart netbeans.

If when you first try to open the project in Netbeans, it gives a bunch of errors about components not found:
  Do a "Clean and Build"
  Restart netbeans.

If the buttons come out squished vertically, try changing the minimum size on one of them, and recompiling: just this reprocessing seems to fix it, then you can change it back.