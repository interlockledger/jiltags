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
package io.interlockledger.iltags.io;

import io.interlockledger.iltags.ILTagException;
import io.interlockledger.iltags.ilint.ILIntCodec;
import io.interlockledger.iltags.ilint.ILIntException;

/**
 * Implementation of the ILIntCodec's InputHandler for ILTagDataReader
 * instances.
 * 
 * @author Fabio Jun Takada Chino
 * @since 2019.06.14
 */
public class ILTagDataReaderHandler implements ILIntCodec.InputHandler<ILTagDataReader> {

	public static final ILTagDataReaderHandler INSTANCE = new ILTagDataReaderHandler();

	@Override
	public int get(ILTagDataReader in) throws ILIntException {
		try {
			return in.readByte() & 0xFF;
		} catch (ILTagException e) {
			throw new ILIntException(e.getMessage(), e);
		}
	}
}
