/*
Copyright (c) 2007, Sun Microsystems, Inc.
 
All rights reserved.
 
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
 
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in
      the documentation and/or other materials provided with the
      distribution.
 * Neither the name of Sun Microsystems, Inc. nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.
 
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sun.me.web.path;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.util.Vector;

//#if WithXML
//# import org.json.me.util.XML;
//#endif

public class Result {
	public static final String IMAGE_CONTENT_TYPE_PATTERN = "image";

	public static final String JS_CONTENT_TYPE = "text/javascript";
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String PLAIN_TEXT_CONTENT_TYPE = "text/plain";
	public static final String TEXT_XML_CONTENT_TYPE = "text/xml";
	public static final String APPLICATION_XML_CONTENT_TYPE = "application/xml";

	public static final char SEPARATOR = '.';
	public static final char ARRAY_START = '[';
	public static final char ARRAY_END = ']';

	private final JSONObject json;
	private final JSONArray array;
	private final boolean isArray;

	private final byte[] data;

	// TODO: add a cache mapping subpaths to objects to improve performance

	public static Result fromContent(final byte[] data, final String contentType) throws ResultException {
		if (data == null) { throw new IllegalArgumentException("content cannot be null"); }

		return new Result(data);
	}

	public static Result fromContent(final String content, final String contentType) throws ResultException {

		if (content == null) { throw new IllegalArgumentException("content cannot be null"); }

		if (content.equals("")) {
			try {
				return new Result(new JSONObject("{}"));
			} catch (JSONException e) {
				throw new ResultException(e);
			}
		}

		if (JS_CONTENT_TYPE.equals(contentType) || JSON_CONTENT_TYPE.equals(contentType) ||
		    PLAIN_TEXT_CONTENT_TYPE.equals(contentType)) {
			try {
				return content.startsWith("[") ? new Result(new JSONArray(content)) : new Result(new JSONObject(content));
			} catch (Exception ex) {
				throw new ResultException(ex);
			}
		}

		throw new ResultException("unsupported URL");
	}

	private Result(final byte[] data) {
		if (data == null) { throw new IllegalArgumentException("data object cannot be null"); }
		isArray = false;
		this.data = data;
		this.json = null;
		this.array = null;
	}

	private Result(final JSONObject obj) {
		if (obj == null) { throw new IllegalArgumentException("json object cannot be null"); }
		isArray = false;
		this.data = null;
		this.json = obj;
		this.array = null;
	}

	private Result(final JSONArray obj) {
		if (obj == null) { throw new IllegalArgumentException("json object cannot be null"); }
		isArray = true;
		this.data = null;
		this.json = null;
		this.array = obj;
	}

	public int hashCode() {
		return isArray ? array.hashCode() : json.hashCode();
	}

	public boolean equals(final Object other) {
		return isArray ? array.equals(other) : json.equals(other);
	}

	public String toString() {
		try {
			return isArray ? ((JSONArray) array).toString(2) : ((JSONObject) json).toString(2);
		} catch (Exception jx) {
			return json.toString();
		}
	}

	// #if CLDC!="1.0"
	// # public double getAsDouble(final String path) throws ResultException {
	// # final Vector tokens = new ResultTokenizer(path).tokenize();
	// # final JSONObject obj = isArray ? apply(array, tokens, 0) : apply(json, tokens, 0);
	// # return obj == null ? 0 : obj.optDouble((String) tokens.lastElement());
	// # }
	// #endif

	public String getAsString(final String path) throws ResultException {
		final Vector tokens = new ResultTokenizer(path).tokenize();
		final JSONObject obj = isArray ? apply(array, tokens, 0) : apply(json, tokens, 0);
		return obj == null ? null : obj.optString((String) tokens.lastElement());
	}

	public JSONArray getAsArray(final String path) throws ResultException {
		final Vector tokens = new ResultTokenizer(path).tokenize();
		if (isArray && tokens.isEmpty()) { return array; }
		final JSONObject obj = isArray ? apply(array, tokens, 0) : apply(json, tokens, 0);
		return obj == null ? null : obj.optJSONArray((String) tokens.lastElement());
	}

	public byte[] getData() {
		return data;
	}

	private JSONObject apply(final JSONArray start, final Vector tokens, final int firstToken) throws ResultException {

		if (start == null) { return null; }

		final int nTokens = tokens.size();
		for (int i = firstToken; i < nTokens;) {
			final String tok1 = (String) tokens.elementAt(i);
			final char t1 = tok1.charAt(0);
			switch (t1) {
				case SEPARATOR:
					throw new ResultException("Syntax error: must start with an array: " + tok1);

				case ARRAY_START:
					if (i + 1 >= nTokens) { throw new ResultException("Syntax error: array must be followed by a dimension: " + tok1); }
					final String tok2 = (String) tokens.elementAt(i + 1);
					int dim = 0;
					try {
						dim = Integer.parseInt(tok2);
					} catch (NumberFormatException nx) {
						throw new ResultException("Syntax error: illegal array dimension: " + tok2);
					}
					if (i + 2 >= nTokens) { throw new ResultException("Syntax error: array dimension must be closed: " + tok2); }
					final String tok3 = (String) tokens.elementAt(i + 2);
					if (tok3.length() != 1 && tok3.charAt(0) != ARRAY_END) { throw new ResultException("Syntax error: illegal close of array dimension: " + tok3); }
					if (i + 3 >= nTokens) { throw new ResultException("Syntax error: array close must be followed by a separator or array open: " + tok3); }
					final String tok4 = (String) tokens.elementAt(i + 3);
					if (tok4.length() != 1 && tok4.charAt(0) != SEPARATOR && tok4.charAt(0) != ARRAY_START) { throw new ResultException("Syntax error: illegal separator after array: " + tok4); }
					i += 4;
					if (tok4.charAt(0) == SEPARATOR) {
						return apply(start.optJSONObject(dim), tokens, i);
					} else if (tok4.charAt(0) == ARRAY_START) { return apply(start.optJSONArray(dim), tokens, i); }
					throw new ResultException("Syntax error: illegal token after array: " + tok4);

				default:
					throw new ResultException("Syntax error: unknown delimiter: " + tok1);
			}
		}

		return null;
	}

	private JSONObject apply(final JSONObject start, final Vector tokens, final int firstToken) throws ResultException {

		if (start == null) { return null; }

		final int nTokens = tokens.size();
		if (firstToken >= nTokens) { return start; }

		for (int i = firstToken; i < nTokens; i++) {
			final String tok1 = (String) tokens.elementAt(i);
			if (tok1.length() == 1 && ResultTokenizer.isDelimiter(tok1.charAt(0))) { throw new ResultException("Syntax error: path cannot start with a delimiter: " + tok1); }

			if (i + 1 >= nTokens) { return start; }
			final String tok2 = (String) tokens.elementAt(i + 1);
			final char t2 = tok2.charAt(0);
			switch (t2) {
				case SEPARATOR:
					return apply(start.optJSONObject(tok1), tokens, i + 2);

				case ARRAY_START:
					if (i + 2 >= nTokens) { throw new ResultException("Syntax error: array must be followed by a dimension: " + tok1); }
					final String tok3 = (String) tokens.elementAt(i + 2);
					int dim = 0;
					try {
						dim = Integer.parseInt(tok3);
					} catch (NumberFormatException nx) {
						throw new ResultException("Syntax error: illegal array dimension: " + tok3);
					}
					if (i + 3 >= nTokens) { throw new ResultException("Syntax error: array dimension must be closed: " + tok3); }
					final String tok4 = (String) tokens.elementAt(i + 3);
					if (tok4.length() != 1 && tok4.charAt(0) != ARRAY_END) { throw new ResultException("Syntax error: illegal close of array dimension: " + tok4); }
					if (i + 4 >= nTokens) { throw new ResultException("Syntax error: array close must be followed by a separator: " + tok1); }
					final String tok5 = (String) tokens.elementAt(i + 4);
					if (tok5.length() != 1 && tok5.charAt(0) != SEPARATOR) { throw new ResultException("Syntax error: illegal separator after array: " + tok4); }
					i += 4;
					final JSONArray array = start.optJSONArray(tok1);
					return array == null ? null : apply((JSONObject) array.opt(dim), tokens, i + 1);
			}
		}

		return start;
	}
}
