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
package io.interlockledger.iltags;

import java.util.Arrays;

import io.interlockledger.iltags.io.ILTagDataReader;
import io.interlockledger.iltags.io.ILTagDataWriter;

/**
 * This class implements the standard float 128 array tag but can also be used
 * to implement other variants.
 * 
 * @author Fabio Jun Takada Chino
 * @since 2019.06.14
 */
public class ILBinary128Tag extends ILFixedSizeTag {

	private byte[] value;

	public ILBinary128Tag() {
		this(ILStandardTags.TAG_BINARY128.ordinal());
	}

	public ILBinary128Tag(long id) {
		super(id, 16);
		this.value = new byte[16];
	}

	@Override
	protected void deserializeValueCore(ILTagFactory factory, ILTagDataReader in) throws ILTagException {
		in.readBytes(this.value);
	}

	public byte[] getValue() {
		return value;
	}

	@Override
	protected int getValueHashCode() {
		return Arrays.hashCode(this.getValue());
	}

	@Override
	protected boolean sameValue(ILTag other) {
		ILBinary128Tag t = (ILBinary128Tag) other;
		return Arrays.equals(this.getValue(), t.getValue());
	}

	@Override
	protected void serializeValue(ILTagDataWriter out) throws ILTagException {
		out.writeBytes(this.value);
	}

	public void setValue(byte[] value) {
		if (value.length != this.value.length) {
			throw new IllegalArgumentException("value must have 16 bytes.");
		}
		System.arraycopy(value, 0, this.value, 0, this.value.length);
	}
}
