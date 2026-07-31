package com.albusxing.showcase.model;

/**
 *  jdk14以后的新特性，记录类record = entity + lombok
 * @author Albusxing
 * @created 2026/6/22
 */
public record StudentRecord(String id, String name, String major, String email) {
}
