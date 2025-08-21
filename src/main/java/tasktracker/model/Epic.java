package tasktracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtaskIds =  new ArrayList<>();
    private LocalDateTime endTime;


    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.endTime = null;
    }

    public Epic(int id, String name, String description) {
        this(id, name, description, Status.NEW);
        this.endTime = null;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        if(this.getId() == subtaskId){
            System.out.println("Нельзя добавить подзадачу, у которой id совпадает с id эпика");
            return;
        }
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public void updateTimings(List<Subtask> subtasks) {
        if (subtasks == null || subtasks.isEmpty()) {
            setDuration(null);
            setStartTime(null);
            endTime = null;
            return;
        }

        LocalDateTime earliestStart = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime latestEnd = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration totalDuration = subtasks.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        setStartTime(earliestStart);
        setDuration(totalDuration);
        endTime = latestEnd;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getStartTime() {
        return null;
    }

    @Override
    public Duration getDuration() {
        return null;
    }

    @Override
    public LocalDateTime getEndTime() {
        return null;
    }
}

