package isabelle.jedit

import isabelle.GUI_Thread
import javax.swing.JOptionPane
import org.gjt.sp.jedit.{View, jEdit}
import org.gjt.sp.jedit.textarea.JEditTextArea

object Rename_POC {
  def hello(view: View): Unit =
    GUI_Thread.later {
      JOptionPane.showMessageDialog(view, "Hello from plugin action!")
    }

  def show_context(view: View): Unit =
    GUI_Thread.later {
      val ta: JEditTextArea = view.getTextArea
      val caret = ta.getCaretPosition
      val atCaret = ta.getSelectionAtOffset(caret)
      val (selStart, selEnd) =
        if (atCaret != null) (atCaret.getStart, atCaret.getEnd) else (caret, caret)
      val path = Option(view.getBuffer.getPath).getOrElse("<no path>")
      JOptionPane.showMessageDialog(
        view,
        s"path: $path\ncaret: $caret\nsel: [$selStart, $selEnd)"
      )
    }
}

