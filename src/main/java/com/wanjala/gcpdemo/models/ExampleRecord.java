package com.wanjala.gcpdemo.models;

import java.util.UUID;

public record ExampleRecord(int Id, String name, UUID parentId) {
}
