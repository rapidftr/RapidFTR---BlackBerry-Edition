package com.rapidftr.services;

import com.rapidftr.model.Child;

public class ChildService {
    public Child[] getAllChildren() {
        return new Child[] { new Child("Tom"), new Child("Dave") };
    }
}
