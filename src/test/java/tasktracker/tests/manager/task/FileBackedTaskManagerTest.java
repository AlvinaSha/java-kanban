package tasktracker.tests.manager.task;

import tasktracker.manager.task.FileBackedTaskManager;
import tasktracker.model.Status;
import tasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        tempFile.deleteOnExit();
        manager = new FileBackedTaskManager(tempFile.getPath());
    }

    @Test
    void saveAndLoad_shouldHandleEmptyManager() {
        manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertAll(
                () -> assertTrue(loadedManager.getAllTasks().isEmpty()),
                () -> assertTrue(loadedManager.getAllEpics().isEmpty()),
                () -> assertTrue(loadedManager.getAllSubtasks().isEmpty())
        );
    }

    @Test
    void save_shouldStoreTaskWithCorrectFormat() throws IOException {
        Task task = new Task(1, "Test Task", "Description", Status.NEW);
        manager.createTask(task);

        String fileContent = Files.readString(tempFile.toPath());
        assertTrue(fileContent.contains("1,TASK,Test Task,NEW,Description,"));
    }

    @Test
    void loadFromFile_shouldHandleNonExistentFile() {
        File nonExistent = new File("non_existent.csv");
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(nonExistent);

        assertNotNull(loadedManager);
        assertTrue(loadedManager.getAllTasks().isEmpty());
    }

    
}