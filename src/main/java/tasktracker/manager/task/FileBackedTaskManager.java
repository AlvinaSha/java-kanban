package tasktracker.manager.task;

import tasktracker.manager.exception.ManagerSaveException;
import tasktracker.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path path;

    public FileBackedTaskManager(String path) {
        super();
        this.path = Path.of(path);
    }

    public void save() {
        try {
            StringBuilder content = new StringBuilder(CsvConverter.CSV_HEADER);

            getAllTasks().forEach(task -> content.append(CsvConverter.toString(task)));
            getAllEpics().forEach(epic -> content.append(CsvConverter.toString(epic)));
            getAllSubtasks().forEach(subtask -> content.append(CsvConverter.toString(subtask)));

            Files.writeString(path, content.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");
        }
    }

    private void load() {
        if (!Files.exists(path)) return;

        try {
            String content = Files.readString(path);
            String[] lines = content.split("\n");

            for (int i = 1; i < lines.length; i++) {
                Task task = CsvConverter.fromString(lines[i]);
                if (task == null) continue;

                switch (task.getType()) {
                    case TASK:
                        getTasks().put(task.getId(), task);
                        break;
                    case EPIC:
                        getEpics().put(task.getId(), (Epic) task);
                        break;
                    case SUBTASK:
                        Subtask subtask = (Subtask) task;
                        getSubtasks().put(subtask.getId(), subtask);
                        Epic epic = getEpics().get(subtask.getEpicId());
                        if (epic != null) {
                            epic.addSubtaskId(subtask.getId());
                        }
                        break;
                }
                updateNextId(task.getId());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла");
        }
    }

    private void updateNextId(int id) {
        if (id >= this.getNextId()) {
            this.setNextId(id + 1);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.toPath().toString());
        manager.load();
        return manager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        save();
        return updatedEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = super.updateSubtask(subtask);
        save();
        return updatedSubtask;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }
}
