/*
 * Copyright 2019 InterlockLedger Network
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
 */
package io.interlockledger.iltags.ilint;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of the ILIntCodec's InputHandler for InputStream instances.
 * 
 * @author Fabio Jun Takada Chino
 * @since 2019.06.10
 */
public class InputStreamHandler implements ILIntCodec.InputHandler<InputStream> {

	/**
	 * Preallocated instance of this class.
	 */
	public static final InputStreamHandler INSTANCE = new InputStreamHandler();

	@Override
	public int get(InputStream in) throws ILIntException {
		try {
			int r = in.read();
			if (r < 0) {
				throw new ILIntException("Premature end of stream.");
			}
			return r;
		} catch (IOException e) {
			throw new ILIntException(e);
		}
	}
}
