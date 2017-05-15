package net.trajano.jee.nlp.test

import org.junit.Test
import org.tensorflow.Graph
import org.tensorflow.Session
import org.tensorflow.Tensor
import org.tensorflow.TensorFlow
import org.junit.Assert

class TensorFlowExampleTest {
	@Test
	fun exampleConvertedToKotlin() {
		Graph().use {

			val g = it
			val value = "Hello from " + TensorFlow.version();

			// Construct the computation graph with a single operation, a constant
			// named "MyConst" with a value "value".

			Tensor.create(value.toByteArray()).use {
				// The Java API doesn't yet include convenience functions for adding operations.
				g.opBuilder("Const", "MyConst").setAttr("dtype", it.dataType()).setAttr("value", it).build();

			}

			// Execute the "MyConst" operation in a Session.
			Session(g).use {
				it.runner().fetch("MyConst").run().get(0).use {
					Assert.assertEquals(value, String(it.bytesValue()))
				}
			}
		}
	}
}