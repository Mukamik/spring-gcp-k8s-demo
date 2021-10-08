package com.wanjala.gcpdemo.models;

import java.util.UUID;

public record DemoRecord(int Id, String name, UUID parentId) {
    }
