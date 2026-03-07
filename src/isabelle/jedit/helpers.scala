package isabelle.jedit

import org.gjt.sp.jedit.buffer.JEditBuffer
import org.gjt.sp.jedit.textarea.JEditTextArea
import org.gjt.sp.jedit.View

def collectSnapshots(views: Array[View]): Map[JEditBuffer, Document.Snapshot] =
    views.flatMap { v =>
    val ta = v.getTextArea
    val buffer = ta.getBuffer
    Document_View.get(ta)
      .map(docView => buffer -> Document_View.rendering(docView).snapshot)
  }.toMap

