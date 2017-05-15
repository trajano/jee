package net.trajano.jee.domain.nlp.test

import net.trajano.jee.nlp.impl.ModelProvider
import net.trajano.jee.nlp.test.FakeLobDAO
import org.junit.Test

class ModelProviderTest {
	@Test
	fun testProvider() {
		val provider = ModelProvider()
		provider.setLobDAO(FakeLobDAO())
		provider.loadFromDatabase()

		// Reloading should not be an issue
		provider.loadFromDatabase()
		provider.persistCurrentGraph()
	}
}