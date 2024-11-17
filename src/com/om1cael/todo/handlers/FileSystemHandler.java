package com.om1cael.todo.handlers;

import com.om1cael.todo.Task;
import com.om1cael.todo.TodoApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileSystemHandler {
    private Path path = Path.of("tasks.txt");

    public boolean handleTaskWriting(Task task) {
        try(BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(task.id() + "|" + task.description() + "|" + task.dueDate() + "|" + task.completed());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Could not write to file.");
            return false;
        }

        return true;
    }

    public boolean handleTaskEditing(int id) {
        List<Task> taskList = new ArrayList<Task>();
        taskList = loadTasksFromFileSystem();
        Path temporaryFile = Path.of("tempTasksFile.txt");

        try(BufferedWriter writer = Files.newBufferedWriter(temporaryFile);
            BufferedReader reader = Files.newBufferedReader(path);
        ) {
            String line;

            while((line = reader.readLine()) != null) {
                String[] taskRow = line.split("\\|");

                int taskId = Integer.parseInt(taskRow[0]);
                String taskDescription = taskRow[1];
                String taskDueDate = taskRow[2];
                boolean taskCompleted = Boolean.parseBoolean(taskRow[3]);

                if(taskId == id) {
                    writer.write(taskId + "|" + taskDescription + "|" + taskDueDate + "|" + !taskCompleted);
                    writer.newLine();
                    continue;
                }

                writer.write(taskId + "|" + taskDescription + "|" + taskDueDate + "|" + taskCompleted);
                writer.newLine();
            }

            Files.delete(path);
            Files.move(temporaryFile, path);
        } catch (IOException e) {
            System.out.println("Could not edit the task.");
            return false;
        }

        return true;
    }

    public List<Task> loadTasksFromFileSystem() {
        List<Task> taskList = new ArrayList<Task>();

        try(BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] taskRow = line.split("\\|");

                int id = Integer.parseInt(taskRow[0]);
                String description = taskRow[1];
                String dueDate = taskRow[2];
                boolean completed = Boolean.parseBoolean(taskRow[3]);

                Task task = new Task(id, description, dueDate, completed);
                taskList.add(task);
            }
        } catch (IOException e) {
            System.out.println("No tasks.");
        }

        return taskList;
    }
}
