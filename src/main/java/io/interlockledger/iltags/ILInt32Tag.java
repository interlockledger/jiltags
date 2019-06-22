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

import io.interlockledger.iltags.io.ILTagDataReader;
import io.interlockledger.iltags.io.ILTagDataWriter;

/**
 * This class implements the standard int16 tag and its variants.
 * 
 * @author Fabio Jun Takada Chino
 * @since 2019.06.12
 */
public class ILInt32Tag extends ILFixedSizeTag {
	
	protected int value;

	public ILInt32Tag(long id) {
		super(id, 4);
	}
	
	public ILInt32Tag() {
		this(ILStandardTags.TAG_INT32.ordinal());
	}

	@Override
	protected void serializeValue(ILTagDataWriter out) throws ILTagException {
		out.writeInt(this.value);
	}

	@Override
	public void deserializeValueCore(ILTagFactory factory, ILTagDataReader in)
			throws ILTagException {
		this.value = in.readInt();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	protected boolean sameValue(ILTag other) {
		ILInt32Tag t = (ILInt32Tag)other;
		return this.getValue() == t.getValue();
	}
}
