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

/**
 * This is the basic tag factories implementation. It can only handle standard
 * tags. Factories with support for the new tags must extend this class.
 * 
 * @author Fabio Jun Takada Chino
 */
public class ILTagFactory {

	/**
	 * Creates a new standard tag if it is supported.
	 * 
	 * @param id The tag id.
	 * @return The standard tag or null if the tag is not supported.
	 * @since 2019.06.22
	 */
	public static ILTag createStandard(ILStandardTags tag) {
		switch (tag) {
		case TAG_NULL:
			return ILNullTag.NULL;
		case TAG_BOOL:
			return new ILBooleanTag();
		case TAG_INT8:
			return new ILInt8Tag();
		case TAG_UINT8:
			return new ILUInt8Tag();
		case TAG_INT16:
			return new ILInt16Tag();
		case TAG_UINT16:
			return new ILUInt16Tag();
		case TAG_INT32:
			return new ILInt32Tag();
		case TAG_UINT32:
			return new ILUInt32Tag();
		case TAG_INT64:
			return new ILInt64Tag();
		case TAG_UINT64:
			return new ILUInt64Tag();
		case TAG_ILINT64:
			return new ILILIntTag();
		case TAG_BINARY32:
			return new ILBinary32Tag();
		case TAG_BINARY64:
			return new ILBinary64Tag();
		case TAG_BINARY128:
			return new ILBinary128Tag();
		case TAG_BYTE_ARRAY:
			return new ILByteArrayTag();
		case TAG_STRING:
			return new ILStringTag();
		case TAG_BINT:
			return new ILBigIntTag();
		case TAG_BDEC:
			return new ILBigDecimalTag();
		case TAG_ILINT64_ARRAY:
			return new ILILIntArrayTag();
		case TAG_ILTAG_ARRAY:
			return new ILTagArrayTag();
		case TAG_ILTAG_SEQ:
			return new ILTagSequenceTag();
		case TAG_RANGE:
			return new ILRangeTag();
		case TAG_VERSION:
			return new ILVersionTag();
		case TAG_OID:
			return new ILOIDTag();
		default:
			return null;
		}
	}

	private boolean strictMode;

	/**
	 * Creates a new tag based on a give id. The default implementation handles all
	 * defined standard classes. Override this method to add more tags whenever
	 * necessary.
	 * 
	 * @param tagId The tag ID.
	 * @return The new tag instance or null if the tag is unknown.
	 */
	public ILTag create(long tagId) {
		return createStandard(ILStandardTags.parseId(tagId));
	}

	/**
	 * Deserializes the first ILTag found inside the input. It uses create
	 *
	 * @param[in] The input buffer.
	 * @return The extracted tag.
	 */
	public ILTag deserialize(ILTagDataReader in) throws ILTagException {
		long tagId;
		long tagSize;

		// Read the basic tag info
		tagId = in.readILInt();
		if (ILTag.isImplicity(tagId)) {
			tagSize = ILTag.getImplicitValueSize(tagId);
		} else {
			tagSize = in.readILInt();
		}

		// Create the tag and handle unknown tags if any.
		ILTag tag = create(tagId);
		if (tag == null) {
			if (this.isStrictMode()) {
				throw new ILUnknownTagException(tagId);
			}
			if (tagSize < 0) {
				throw new ILTagException(String.format("Cannot handle unknown implicit tag %1$X.", tagId));
			}
			tag = new ILByteArrayTag(tagId);
		}
		try {
			// Deserialize the value
			if (tagSize >= 0) {
				in.pushLimit(tagSize);
				tag.deserializeValue(this, tagSize, in);
				in.popLimit(true);
			} else {
				tag.deserializeValue(this, tagSize, in);
			}
			return tag;
		} catch (IllegalArgumentException e) {
			throw new ILTagException(e.getMessage(), e);
		}
	}

	/**
	 * Determines if this factory will or will not handle unknown tags.
	 * 
	 * @return true if unknown tags generate errors or false otherwise.
	 */
	public boolean isStrictMode() {
		return strictMode;
	}

	/**
	 * Sets how this factory will handle unknown tags. If strict mode is set to
	 * true, unknown classes will generate errors. Otherwise, they will be created
	 * as instances of ILTagArrayTag with the corresponding tagId.
	 * 
	 * @param strictMode true to activate the strict mode or false otherwise.
	 */
	public void setStrictMode(boolean strictMode) {
		this.strictMode = strictMode;
	}
}
