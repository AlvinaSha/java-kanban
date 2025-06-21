import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;
    private List<Epic> epics;
    private List<Subtask> subtasks;
    private int nextId;

    public TaskManager() {
        tasks = new ArrayList<>();
        epics = new ArrayList<>();
        subtasks = new ArrayList<>();
        nextId = 1;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public int createTask(Task task) {
        int id = nextId++;
        task.setId(id);
        tasks.add(task);
        return id;
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask);
                return;
            }
        }
    }

    public void deleteTaskById(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics);
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public Epic getEpicById(int id) {
        for (Epic epic : epics) {
            if (epic.getId() == id) {
                return epic;
            }
        }
        return null;
    }

    public int createEpic(Epic epic) {
        int id = nextId++;
        epic.setId(id);
        epics.add(epic);
        return id;
    }

    public void updateEpic(Epic updatedEpic) {
        for (int i = 0; i < epics.size(); i++) {
            if (epics.get(i).getId() == updatedEpic.getId()) {
                Epic existingEpic = epics.get(i);
                existingEpic.setName(updatedEpic.getName());
                existingEpic.setDescription(updatedEpic.getDescription());
                return;
            }
        }
    }

    public void deleteEpicById(int id) {
        Epic epicToRemove = null;
        for (Epic epic : epics) {
            if (epic.getId() == id) {
                epicToRemove = epic;
                break;
            }
        }

        if (epicToRemove != null) {
            subtasks.removeIf(subtask -> subtask.getEpicId() == id);
            epics.remove(epicToRemove);
        }
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.getId());
        }
    }

    public Subtask getSubtaskById(int id) {
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == id) {
                return subtask;
            }
        }
        return null;
    }

    public int createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (getEpicById(epicId) == null) {
            return -1; // Эпик не существует
        }

        int id = nextId++;
        subtask.setId(id);
        subtasks.add(subtask);

        Epic epic = getEpicById(epicId);
        epic.addSubtaskId(id);

        updateEpicStatus(epicId);

        return id;
    }

    public void updateSubtask(Subtask updatedSubtask) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == updatedSubtask.getId()) {
                Subtask existingSubtask = subtasks.get(i);
                int oldEpicId = existingSubtask.getEpicId();
                int newEpicId = updatedSubtask.getEpicId();

                if (oldEpicId != newEpicId) {
                    // Если эпик изменился, обновляем связи
                    Epic oldEpic = getEpicById(oldEpicId);
                    if (oldEpic != null) {
                        oldEpic.removeSubtaskId(updatedSubtask.getId());
                        updateEpicStatus(oldEpicId);
                    }

                    Epic newEpic = getEpicById(newEpicId);
                    if (newEpic != null) {
                        newEpic.addSubtaskId(updatedSubtask.getId());
                    }
                }

                subtasks.set(i, updatedSubtask);
                updateEpicStatus(updatedSubtask.getEpicId());
                return;
            }
        }
    }

    public void deleteSubtaskById(int id) {
        Subtask subtaskToRemove = null;
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == id) {
                subtaskToRemove = subtask;
                break;
            }
        }

        if (subtaskToRemove != null) {
            int epicId = subtaskToRemove.getEpicId();
            Epic epic = getEpicById(epicId);
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epicId);
            }
            subtasks.remove(subtaskToRemove);
        }
    }

    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> result = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (subtask.getEpicId() == epicId) {
                result.add(subtask);
            }
        }
        return result;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = getEpicById(epicId);
        if (epic == null) return;

        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask == null) continue;

            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }

            if (!allDone && !allNew) {
                break;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}

