file://<WORKSPACE>/src/isabelle/jedit/helpers.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -Document.
	 -scala/Predef.Document.
offset: 184
uri: file://<WORKSPACE>/src/isabelle/jedit/helpers.scala
text:
```scala
import org.gjt.sp.jedit.buffer.JEditBuffer
import org.gjt.sp.jedit.textarea.JEditTextArea
import org.gjt.sp.jedit.View

def collectSnapshots(views: Array[View]): Map[JEditBuffer, Docum@@ent.Snapshot] =
    views.flatMap { v =>
    val ta = v.getTextArea
    val buffer = ta.getBuffer
    Document_View.get(ta)
      .map(docView => buffer -> Document_View.rendering(docView).snapshot)
  }.toMap


```


#### Short summary: 

empty definition using pc, found symbol in pc: 