package isabelle.jedit

import isabelle.{GUI_Thread, Markup, Rendering, Text, XML}
import javax.swing.JOptionPane
import org.gjt.sp.jedit.View
import org.gjt.sp.jedit.textarea.JEditTextArea

object Refactor {
  def rename(view: View): Unit =
    GUI_Thread.now {
      val view = org.gjt.sp.jedit.jEdit.getActiveView()
      val text_area = view.getTextArea()
      Document_View.get(text_area).foreach { doc_view =>
        val rendering = Document_View.rendering(doc_view)
        val snapshot = rendering.snapshot

        // Find entity ID under caret
        val caret_range = JEdit_Lib.caret_range(text_area)
        val matches = snapshot.cumulate[List[(String, String, Option[Long])]](
          caret_range, Nil, Rendering.entity_elements, _ => {
            case (acc, Text.Info(_, XML.Elem(markup, _))) =>
              (Markup.Entity.unapply(markup), Markup.Entity.Occ.unapply(markup)) match {
                case (Some((kind, name)), occ_id) => Some((kind, name, occ_id) :: acc)
                case _ => None
              }
          })

        // Extract Long ID from the "constant" entry
        val canonical_id: Option[Long] =
          matches.flatMap(_.info)
            .collectFirst { case ("constant", _, Some(id)) => id }

        // Find all occurrences of entities matching ID in buffer
        val rangeList: List[Text.Range] = canonical_id.map { target_id =>
          val buffer_range = JEdit_Lib.buffer_range(text_area.getBuffer)
          val ranges = snapshot.cumulate[List[Text.Range]](
            buffer_range, Nil, Rendering.entity_elements, _ => {
              case (acc, Text.Info(range, XML.Elem(markup, _)))
                  if Markup.Entity.Occ.unapply(markup) == Some(target_id) =>
                Some(range :: acc)
              case _ => None
            })
          ranges.flatMap(_.info)
        }.getOrElse(Nil)

        // Display in popup
        val rangesStr =
          if (rangeList.isEmpty) "  (none)"
          else rangeList.map(r => s"  [${r.start}, ${r.stop})").mkString("\n")

        JOptionPane.showMessageDialog(
          view,
          s"ID: ${canonical_id.getOrElse("none")}\nMatches:\n$rangesStr"
        )
      }
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
