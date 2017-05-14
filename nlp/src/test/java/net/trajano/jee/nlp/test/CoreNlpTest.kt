package net.trajano.jee.domain.nlp.test

import net.trajano.jee.nlp.AnalysisResult
import net.trajano.jee.nlp.impl.CoreNlp
import org.junit.Assert
import org.junit.Test

class CoreNlpTest {
	/**
	 * Ensure Kotlin is setup correctly.
	 */
	@Test
	fun sanityTest() {
		Assert.assertTrue(1 == 1)
	}

	@Test
	fun testAnalyze() {
		val c = CoreNlp()
		val r = c.analyze("Archie loves Janet very much.  That much is QUITE true.");
		Assert.assertEquals(2, r.sentences.size)
	}
}