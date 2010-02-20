package com.rapidftr.services;

import net.rim.device.api.system.EncodedImage;

public interface PhotoServiceListener {
	void handlePhoto(EncodedImage encodedImage);
}
