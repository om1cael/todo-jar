package com.om1cael.todo;

public record Task(int id, String description, String dueDate, boolean completed) {
}