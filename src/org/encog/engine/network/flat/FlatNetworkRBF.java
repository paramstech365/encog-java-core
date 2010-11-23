/*
 * Encog(tm) Core v2.6 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.engine.network.flat;

import java.io.Serializable;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.rbf.RadialBasisFunction;
import org.encog.engine.util.BoundMath;
import org.encog.engine.util.EngineArray;

/**
 * A flat network designed to handle an RBF.
 */
public class FlatNetworkRBF extends FlatNetwork implements Serializable {

	/**
	 * The RBF's used.
	 */
	private RadialBasisFunction[] rbf;
	
	/**
	 * Default constructor.
	 */
	public FlatNetworkRBF() {

	}

	/**
	 * Construct an RBF flat network.
	 * 
	 * @param inputCount
	 *            The number of input neurons. (also the number of dimensions)
	 * @param hiddenCount
	 *            The number of hidden neurons.
	 * @param outputCount
	 *            The number of output neurons.
	 * @param rbf 
	 * 				The radial basis functions to use.
	 */
	public FlatNetworkRBF(final int inputCount, final int hiddenCount,
			final int outputCount, final RadialBasisFunction[] rbf) {

		FlatLayer[] layers = new FlatLayer[3];
		this.rbf = rbf;
		
		double[] slope = new double[1];
		slope[0] = 1.0;

		layers[0] = new FlatLayer(new ActivationLinear(), inputCount, 0.0,
				slope);
		layers[1] = new FlatLayer(new ActivationLinear(), hiddenCount, 0.0,
				slope);
		layers[2] = new FlatLayer(new ActivationLinear(), outputCount, 0.0,
				slope);

		init(layers);
	}

	/**
	 * Clone the network.
	 * 
	 * @return A clone of the network.
	 */
	@Override
	public FlatNetworkRBF clone() {
		final FlatNetworkRBF result = new FlatNetworkRBF();
		cloneFlatNetwork(result);
		result.rbf = this.rbf;
		return result;
	}

	/**
	 * Calculate the output for the given input.
	 * 
	 * @param x
	 *            The input.
	 * @param output
	 *            Output will be placed here.
	 */
	@Override
	public void compute(final double[] x, final double[] output) {

		int outputIndex = this.getLayerIndex()[1];

		for (int i = 0; i < rbf.length; i++) {
			double o = this.rbf[i].calculate(x);
			this.getLayerOutput()[outputIndex + i] = o;
		}

		// now compute the output
		computeLayer(1);
		EngineArray.arrayCopy(this.getLayerOutput(), 0, output, 0, this
				.getOutputCount());
	}

}
