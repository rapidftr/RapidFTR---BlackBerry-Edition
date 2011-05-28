package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.util.Persistable;

import com.rapidftr.form.FormFieldAction;
import com.rapidftr.form.Forms;
import com.rapidftr.utilities.FileUtility;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.RandomStringGenerator;
import com.rapidftr.utilities.StringUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Part;
import com.sun.me.web.request.PostData;

public class Child implements Persistable {

	public static final String CREATED_AT_KEY = "created_at";
	public static final String LAST_UPDATED_KEY = "last_updated_at";
	private final Hashtable data;
	private final Hashtable changedFields;

	private ChildStatus childStatus;

	public Child(String creationDate) {
		changedFields = new Hashtable();
		data = new Hashtable();
		put("_id", RandomStringGenerator.generate(32));
		put(CREATED_AT_KEY, creationDate);
		childStatus = ChildStatus.NEW;
	}

	public String toString() {
		return (String) data.get("name");
	}

	public PostData getPostData() {

		Vector parts = new Vector();
		Enumeration keyList = isNewChild() ? data.keys() : getChangedFields();

		while (keyList.hasMoreElements()) {
			Object key = keyList.nextElement();
			Object value = data.get(key);

			if (key.equals("histories")) {
				continue;
			}

			if (key.equals("current_photo_key")) {
				if (!StringUtility.isBlank(String.valueOf(value))) {
					parts.addElement(multiPart(value, "photo",
							HttpUtility.HEADER_CONTENT_TYPE_IMAGE));
				}
				continue;
			}
			if (key.equals("recorded_audio")) {
				if (value != null
						&& !StringUtility.isBlank(String.valueOf(value))) {
					parts.addElement(multiPart(value, "audio",
							HttpUtility.HEADER_CONTENT_TYPE_AUDIO));
				}
				continue;
			}

			Arg[] headers = new Arg[1];
			headers[0] = new Arg("Content-Disposition",
					"form-data; name=\"child[" + key + "]\"");
			Part part = new Part(value.toString().getBytes(), headers);
			parts.addElement(part);
		}

		Part[] anArray = new Part[parts.size()];
		parts.copyInto(anArray);
		String boundary = "abced";

		PostData postData = new PostData(anArray, boundary);
		return postData;
	}

	private Enumeration getChangedFields() {
		return changedFields.keys();
	}

	private Part multiPart(Object value, String paramName, Arg headerContentType) {
		Arg[] headers = new Arg[2];
		headers[0] = new Arg("Content-Disposition", "form-data; name=\"child["
				+ paramName + "]\"");
		headers[1] = headerContentType;
		byte[] imageData = FileUtility.getByteArray(value.toString());

		return new Part(imageData, headers);
	}

	public void setField(String name, Object value) {
		if (!isNewChild()) {
			String oldValue = getField(name);

			if (oldValue != null && !oldValue.equals(value)
					&& !name.equals("_id")) {
				changedFields.put(name, value);
			}
		}
		put(name, value);

	}

	private void put(String name, Object value) {
		if (value != null) {
			data.put(name, value);
		}
	}

	public String getField(String key) {
		return (String) data.get(key);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((data == null) ? 0 : data.get("_id").hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Child other = (Child) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else {
			if (data.get("_id") != null && other.data.get("_id") != null
					&& data.get("_id").equals((other.data.get("_id")))) {
				return true;
			}
			if (data.get("unique_identifier") != null
					&& other.data.get("unique_identifier") != null
					&& data.get("unique_identifier").equals(
							(other.data.get("unique_identifier")))) {
				return true;
			}
		}

		return false;
	}

	public void updateField(String name) {
		if (!data.containsKey(name)) {
			put(name, "");
		}

	}

	public static Child create(Forms forms, String currentFormattedDateTime) {
		Child child = new Child(currentFormattedDateTime);
		child.update(forms);
		return child;
	}

	public void update(Forms forms) {
		forms.forEachField(new FormFieldAction() {
			public void execute(com.rapidftr.form.FormField field) {
				setField(field.getName(), field.getValue());
			}
		});
		if (isUpdated()) {
			childStatus = ChildStatus.UPDATED;
		}
	}

	public ChildHistories getHistory() {
		return new ChildHistories(getField("histories"));
	}

	public boolean hasChangesByOtherThan(final String username) {
		ChildHistories histories = getHistory();
		final boolean[] result = {false};
		histories.forEachHistory(new HistoryAction() {
			public void execute(ChildHistoryItem historyItem) {
				result[0] = !(historyItem.getUsername().equals(username));
			}
		});
		return result[0];
	}

	public boolean isNewChild() {
		return getField("unique_identifier") == null;
	}

	public boolean isUpdated() {
		return changedFields.size() > 0;
	}

	public ChildStatus childStatus() {
		return childStatus;
	}

	public void syncSuccess() {
		changedFields.clear();
		childStatus = ChildStatus.SYNCED;
	}

	public void syncFailed(String message) {
		ChildStatus status = ChildStatus.SYNC_FAILED;
		status.setSyncError(message);
		childStatus = status;
	}

	public boolean isSyncFailed() {
		return childStatus.equals(ChildStatus.SYNC_FAILED);
	}

	public boolean matches(String queryString) {
		String id = getField("unique_identifier");
		if (id == null) {
			id = "";
		}

		String name = getField("name");
		if (name == null) {
			name = "";
		}

		return ((!"".equals(queryString) && (id.equalsIgnoreCase(queryString))) || (!""
				.equals(queryString) && name.toString().toLowerCase().indexOf(
				queryString) != -1));
	}

	public String getCreatedBy() {
		return getField("created_by");
	}

	public String getImageLocation() {
		return getField("current_photo_key");
	}

	public void setUniqueIdentifier(String uniqueId) {
		setField("unique_identifier", uniqueId);
	}

	public void setId(String id) {
		setField("_id", id);
	}

	public boolean hasPhoto() {
		return !StringUtility.isBlank(getField("current_photo_key"));
	}

	public void setPhotoKey(String value) {
		setField("current_photo_key", value);
	}

	public void setPhotoKeyWithoutUpdate(String value) {
		put("current_photo_key", value);
	}

	public String getPhotoKey() {
		return getField("current_photo_key");
	}

}
