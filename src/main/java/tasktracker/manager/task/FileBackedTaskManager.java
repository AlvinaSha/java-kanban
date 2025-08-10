package tasktracker.manager.task;

import tasktracker.manager.exception.ManagerSaveException;
import tasktracker.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static tasktracker.model.TaskType.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path path;
    private static final String CSV_HEADER = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(String path) {
        super();
        this.path = Path.of(path);
    }

    private void save() {
        try {
            StringBuilder content = new StringBuilder(CSV_HEADER);

            getAllTasks().forEach(task -> content.append(toString(task)));
            getAllEpics().forEach(epic -> content.append(toString(epic)));
            getAllSubtasks().forEach(subtask -> content.append(toString(subtask)));

            Files.writeString(path, content.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла", e);
        }
    }

    private void load() {
        if (!Files.exists(path)) return;

        try {
            String content = Files.readString(path);
            String[] lines = content.split("\n");

            for (int i = 1; i < lines.length; i++) {
                Task task = fromString(lines[i]);
                if (task == null) continue;

                switch (task.getType()) {
                    case TASK -> createTask(task);
                    case EPIC -> createEpic((Epic) task);
                    case SUBTASK -> createSubtask((Subtask) task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file.toPath().toString());
        manager.load();
        return manager;
    }

    private String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                (task instanceof Subtask) ? ((Subtask) task).getEpicId() : "");
    }

    private Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 6) return null;

        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        String epicId = parts.length > 5 ? parts[5] : "";

        return switch (type) {
            case TASK -> new Task(id, name, description, status);
            case EPIC -> new Epic(id, name, description, status);
            case SUBTASK -> new Subtask(id, name, description, status,
                    epicId.isEmpty() ? 0 : Integer.parseInt(epicId));
        };
    }


    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task createTask(Task task) {
        save();
        return super.createTask(task);
    }

    @Override
    public Task updateTask(Task task) {
        save();
        return super.updateTask(task);
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Epic createEpic(Epic epic) {
        save();
        return super.createEpic(epic);
    }

    @Override
    public Epic updateEpic(Epic epic) {
        save();
        return super.updateEpic(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        save();
        return super.createSubtask(subtask);
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        save();
        return super.updateSubtask(subtask);
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }
}
