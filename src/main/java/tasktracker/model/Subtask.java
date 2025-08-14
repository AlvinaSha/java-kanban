package tasktracker.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int id, int epicId) {
        super(name, description, id);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }


    public Subtask(int id, String name, String description, int epicId) {
        this(id, name, description, Status.NEW, epicId);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public void setId(int id) {
        if(id == epicId){
            return;
        }
        super.setId(id);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
