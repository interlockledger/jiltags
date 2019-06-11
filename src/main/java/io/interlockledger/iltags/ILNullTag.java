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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This class implements the Null ILTag.
 *
 * @author Fabio Jun Takada Chino
 * @since 2019.06.10
 */
public class ILNullTag extends ILTag {

	public ILNullTag() {
		super(ILTagStandardTags.TAG_NULL);
	}

	@Override
	protected void serializeValue(DataOutputStream out) throws ILTagException, IOException {
	}

	@Override
	public long getValueSize() {
		return 0;
	}

	@Override
	public void deserializeValue(ILTagFactory factory, long tagSize, DataInputStream in) throws ILTagException, IOException {
		if (tagSize != this.getValueSize()) {
			throw new ILTagException("Invalid value.");
		}		
	}
}