package tasktracker.manager.task;

import tasktracker.manager.exception.NotFoundException;
import tasktracker.manager.exception.TimeConflictException;
import tasktracker.model.Epic;
import tasktracker.model.Subtask;
import tasktracker.model.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    List<Task> getAllTasks();

    void deleteAllTasks();

    Task getTaskById(int id) throws NotFoundException;

    Task createTask(Task task);

    Task updateTask(Task task) throws NotFoundException, TimeConflictException;

    void deleteTaskById(int id) throws NotFoundException;

    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpicById(int id) throws NotFoundException   ;

    Epic createEpic(Epic epic);

    Epic updateEpic(Epic epic) throws NotFoundException;

    void deleteEpicById(int id) throws NotFoundException;

    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtaskById(int id) throws NotFoundException;

    Subtask createSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask subtask) throws NotFoundException, TimeConflictException;

    void deleteSubtaskById(int id) throws NotFoundException;

    List<Subtask> getSubtasksByEpicId(int epicId) throws NotFoundException;

    List<Task> getPrioritizedTasks();
}
