package net.trajano.jee.nlp.impl

import edu.stanford.nlp.simple.Document
import net.trajano.jee.nlp.Nlp
import javax.ejb.Local
import javax.ejb.Stateless

@Local
@Stateless
class CoreNlp : Nlp {
	override fun analyze(text: String) {
		val doc = Document(text)
		for (sent in doc.sentences()) {
			// Will iterate over two sentences
			// We're only asking for words -- no need to load any models yet
			println("The second word of the sentence '" + sent + "' is " + sent.word(1))
			// When we ask for the lemma, it will load and run the part of speech tagger
			println("The third lemma of the sentence '" + sent + "' is " + sent.lemma(2))
			// When we ask for the parse, it will load and run the parser
			println("The parse of the sentence '" + sent + "' is " + sent.parse())
			// ...

		}
	}
}
