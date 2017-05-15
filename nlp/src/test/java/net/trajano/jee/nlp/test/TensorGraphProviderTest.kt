package net.trajano.jee.nlp.test

import org.junit.Assert.assertEquals
import org.junit.Test
import org.tensorflow.Session
import org.tensorflow.Tensor
import net.trajano.jee.nlp.impl.TensorGraphProvider

class TensorGraphProviderTest {
	@Test
	fun testProvider() {
		val provider = TensorGraphProvider()
		provider.setLobDAO(FakeLobDAO())
		provider.loadFromDatabase()
		Tensor.create("Hello world".toByteArray()).use {
			provider.opBuilder("Const", "MyConst").setAttr("dtype", it.dataType()).setAttr("value", it).build()
		}
		provider.persistCurrentGraph()

		provider.newSession().use {
			it.runner().fetch("MyConst").run().get(0).use {
				assertEquals("Hello world", String(it.bytesValue()))
			}
		}
		provider.closeGraph()
	}
}