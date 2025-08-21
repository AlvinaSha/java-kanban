package tasktracker.manager.task;

import tasktracker.manager.exception.TimeConflictException;
import tasktracker.manager.history.HistoryManager;
import tasktracker.manager.history.InMemoryHistoryManager;
import tasktracker.model.Status;
import tasktracker.model.Epic;
import tasktracker.model.Subtask;
import tasktracker.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, Subtask> subtasks;
    private int nextId;
    private final HistoryManager historyManager;
    private final TreeSet<Task> prioritizedTasks;

    protected void setNextId(int nextId) {
        this.nextId = nextId;
    }

    protected Map<Integer, Task> getTasks() {
        return tasks;
    }

    protected Map<Integer, Epic> getEpics() {
        return epics;
    }

    protected Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    protected int getNextId() {
        return nextId;
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.nextId = 1;
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(
                Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
    }

    public InMemoryTaskManager() {
        this.historyManager = new InMemoryHistoryManager();
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.nextId = 1;
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(
                Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
    }

    private void addToHistory(Task task) {
        historyManager.add(task);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            addToHistory(task);
        }
        return task;
    }

    @Override
    public Task createTask(Task task) {
        if (hasTimeOverlap(task)) {
            throw new TimeConflictException("Задача пересекается по времени с существующими задачами");
        }

        int id = nextId++;
        task.setId(id);
        tasks.put(id, task);
        addToPrioritizedTasks(task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (hasTimeOverlap(task)) {
                throw new TimeConflictException("Задача пересекается по времени с существующими задачами");
            }

            removeFromPrioritizedTasks(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            addToPrioritizedTasks(task);
            return task;
        }
        return null;

    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            addToHistory(epic);
        }
        return epic;
    }

    @Override
    public Epic createEpic(Epic epic) {
        int id = nextId++;
        epic.setId(id);
        epics.put(id, epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic existingEpic = epics.get(epic.getId());
            existingEpic.setName(epic.getName());
            existingEpic.setDescription(epic.getDescription());
            return existingEpic;
        }
        return null;
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            addToHistory(subtask);
        }
        return subtask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (hasTimeOverlap(subtask)) {
            throw new TimeConflictException("Подзадача пересекается по времени с существующими задачами");
        }

        int epicId = subtask.getEpicId();
        if (!epics.containsKey(epicId)) {
            throw new IllegalArgumentException("Эпик с ID " + epicId + " не существует");
        }

        int id = nextId++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        addToPrioritizedTasks(subtask);

        Epic epic = epics.get(epicId);
        epic.addSubtaskId(id);
        updateEpicStatus(epicId);
        updateEpicTimings(epicId);

        return subtask;
    }
    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            if (hasTimeOverlap(subtask)) {
                throw new TimeConflictException("Подзадача пересекается по времени с существующими задачами");
            }
            removeFromPrioritizedTasks(subtasks.get(subtask.getId()));
            Subtask existingSubtask = subtasks.get(subtask.getId());
            int oldEpicId = existingSubtask.getEpicId();
            int newEpicId = subtask.getEpicId();

            if (oldEpicId != newEpicId) {
                Epic oldEpic = epics.get(oldEpicId);
                if (oldEpic != null) {
                    oldEpic.removeSubtaskId(subtask.getId());
                    updateEpicStatus(oldEpicId);
                }

                Epic newEpic = epics.get(newEpicId);
                if (newEpic != null) {
                    newEpic.addSubtaskId(subtask.getId());
                }
            }

            subtasks.put(subtask.getId(), subtask);
            addToPrioritizedTasks(subtask);
            updateEpicStatus(subtask.getEpicId());
            updateEpicTimings(subtask.getEpicId());
            return subtask;
        }
        return null;
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            removeFromPrioritizedTasks(subtask);
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epicId);
                updateEpicTimings(epicId);
            }
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        return epic.getSubtaskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .toList();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
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

    private void updateEpicTimings(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Subtask> epicSubtasks = getSubtasksByEpicId(epicId);
        epic.updateTimings(epicSubtasks);
    }

    private void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private void removeFromPrioritizedTasks(Task task) {
        prioritizedTasks.remove(task);
    }

    public boolean hasTimeOverlap(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) {
            return false;
        }
        return prioritizedTasks.stream()
                .filter(existingTask -> !existingTask.equals(task))
                .anyMatch(existingTask -> existingTask.isOverlapping(task));
    }


}
