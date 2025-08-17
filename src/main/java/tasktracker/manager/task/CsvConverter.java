package tasktracker.manager.task;

import tasktracker.model.*;

public class CsvConverter {
    public static final String CSV_HEADER = "id,type,name,status,description,epic\n";
    private static final String TASK_FORMAT = "%d,%s,%s,%s,%s,%s\n";

    public static String toString(Task task) {
        return String.format(TASK_FORMAT,
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                "");
    }

    public static String toString(Subtask subtask) {
        return String.format(TASK_FORMAT,
                subtask.getId(),
                "SUBTASK",
                subtask.getName(),
                subtask.getStatus(),
                subtask.getDescription(),
                subtask.getEpicId());
    }


    public static Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 6) return null;

        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        String epicId = parts[5];

        switch (type) {
            case TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                if (epicId.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Subtask с id=" + id + " не может быть создан без epicId"
                    );
                }
                return new Subtask(id, name, description, status, Integer.parseInt(epicId));
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}