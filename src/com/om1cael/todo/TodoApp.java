package com.om1cael.todo;

import com.om1cael.todo.handlers.FileSystemHandler;

import java.util.List;

public class TodoApp {
    private List<Task> taskList;
    private final FileSystemHandler fileSystemHandler;

    public TodoApp() {
        fileSystemHandler = new FileSystemHandler();
    }

    public void addTask(String description, String dueDate) {
        Task task = new Task(getTasks().size(), description, dueDate, false);

        if(fileSystemHandler.handleTaskWriting(task)) {
            System.out.println("Task " + task.getDescription() + " was added successfully.");
            return;
        }

        System.out.println("Could not add your task.");
    }

    public void changeTaskStatus(int id) {
        if(fileSystemHandler.handleTaskEditing(id)) {
            System.out.println("Task status changed successfully.");
        }
    }

    public void listTasks() {
        if(!getTasks().isEmpty()) {
            System.out.println("List of tasks:");
            for(Task task : getTasks()) {
                System.out.println("Task [" + task.getId() + "]: " + task.getDescription());
                System.out.println(" Due date: " + task.getDueDate());
                System.out.println(" Completed: " + task.isCompleted());
            }
        }
    }

    public List<Task> getTasks() {
        this.taskList = fileSystemHandler.loadTasksFromFileSystem();
        return this.taskList;
    }
}
