package tasktracker.manager.task;

import tasktracker.model.Epic;
import tasktracker.model.Status;
import tasktracker.model.Subtask;
import tasktracker.model.Task;

public class CsvConverter {
    public static final String CSV_HEADER = "id,type,name,status,description,epic\n";

    public static String toString(Task task) {
        return String.format("%d,TASK,%s,%s,%s,\n",
                task.getId(),
                task.getName(),
                task.getStatus(),
                task.getDescription());
    }

    public static String toString(Epic epic) {
        return String.format("%d,EPIC,%s,%s,%s,\n",
                epic.getId(),
                epic.getName(),
                epic.getStatus(),
                epic.getDescription());
    }

    public static String toString(Subtask subtask) {
        return String.format("%d,SUBTASK,%s,%s,%s,%d\n",
                subtask.getId(),
                subtask.getName(),
                subtask.getStatus(),
                subtask.getDescription(),
                subtask.getEpicId());
    }

    public static Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 6) return null;

        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        String epicId = parts[5];

        return switch (type) {
            case "TASK" -> new Task(id, name, description, status);
            case "EPIC" -> new Epic(id, name, description, status);
            case "SUBTASK" -> new Subtask(id, name, description, status,
                    epicId.isEmpty() ? 0 : Integer.parseInt(epicId));
            default -> null;
        };
    }
}