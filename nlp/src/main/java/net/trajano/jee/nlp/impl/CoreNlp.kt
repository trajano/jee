package net.trajano.jee.nlp.impl;

import edu.stanford.nlp.simple.Document
import edu.stanford.nlp.simple.Sentence

fun main(args: Array<String>) {
	val doc = Document("add your text here! It can contain multiple sentences. I am EXTREMELY loving Janet.");
	for (sent in doc.sentences()) {
		// Will iterate over two sentences
		// We're only asking for words -- no need to load any models yet
		println("The second word of the sentence '" + sent + "' is " + sent.word(1));
		// When we ask for the lemma, it will load and run the part of speech tagger
		println("The third lemma of the sentence '" + sent + "' is " + sent.lemma(2));
		// When we ask for the parse, it will load and run the parser
		println("The parse of the sentence '" + sent + "' is " + sent.parse());
		// ...

	}
}
