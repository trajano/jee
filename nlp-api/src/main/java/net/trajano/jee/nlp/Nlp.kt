package net.trajano.jee.nlp

import javax.ejb.Local

@Local
interface Nlp {
	fun analyze(text: String)
}