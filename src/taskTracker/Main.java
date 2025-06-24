package taskTracker;

import taskTracker.model.Status;
import taskTracker.model.Epic;
import taskTracker.model.Subtask;
import taskTracker.model.Task;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        testData();

        boolean running = true;
        while (running) {
            printMenu();
            int command = scanner.nextInt();
            scanner.nextLine();

            switch (command) {
                case 1:
                    createTask();
                    break;
                case 2:
                    createEpic();
                    break;
                case 3:
                    createSubtask();
                    break;
                case 4:
                    printAllTasks();
                    break;
                case 5:
                    printAllEpics();
                    break;
                case 6:
                    printAllSubtasks();
                    break;
                case 7:
                    updateTaskStatus();
                    break;
                case 8:
                    deleteTask();
                    break;
                case 9:
                    deleteEpic();
                    break;
                case 10:
                    deleteSubtask();
                    break;
                case 11:
                    printEpicSubtasks();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Неизвестная команда");
            }
        }
    }

    private static void testData() {
        // Создаем две задачи
        Task task1 = new Task("Покупки", "Купить продукты", 0);
        Task task2 = new Task("Уборка", "Убраться в квартире", 0);
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        // Создаем эпик с двумя подзадачами
        Epic epic1 = new Epic("Переезд", "Организация переезда в новый офис", 0);
        int epic1Id = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Упаковка", "Упаковать вещи", 0, epic1Id);
        Subtask subtask2 = new Subtask("Транспорт", "Заказать грузовик", 0, epic1Id);
        int subtask1Id = taskManager.createSubtask(subtask1);
        int subtask2Id = taskManager.createSubtask(subtask2);

        // Создаем эпик с одной подзадачей
        Epic epic2 = new Epic("Ремонт", "Ремонт в квартире", 0);
        int epic2Id = taskManager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Покраска", "Покрасить стены", 0, epic2Id);
        int subtask3Id = taskManager.createSubtask(subtask3);

        // Изменяем статусы
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);

        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);

        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask3);

        // Выводим тестовые данные
        System.out.println("Тестовые данные созданы:");
        System.out.println("\nЗадачи:");
        System.out.println(taskManager.getTaskById(task1Id));
        System.out.println(taskManager.getTaskById(task2Id));

        System.out.println("\nЭпики:");
        System.out.println(taskManager.getEpicById(epic1Id));
        System.out.println(taskManager.getEpicById(epic2Id));

        System.out.println("\nПодзадачи:");
        System.out.println(taskManager.getSubtaskById(subtask1Id));
        System.out.println(taskManager.getSubtaskById(subtask2Id));
        System.out.println(taskManager.getSubtaskById(subtask3Id));
    }

    private static void printMenu() {
        System.out.println("\nМенеджер задач");
        System.out.println("1. Создать задачу");
        System.out.println("2. Создать эпик");
        System.out.println("3. Создать подзадачу");
        System.out.println("4. Показать все задачи");
        System.out.println("5. Показать все эпики");
        System.out.println("6. Показать все подзадачи");
        System.out.println("7. Обновить статус задачи");
        System.out.println("8. Удалить задачу");
        System.out.println("9. Удалить эпик");
        System.out.println("10. Удалить подзадачу");
        System.out.println("11. Показать подзадачи эпика");
        System.out.println("0. Выход");
        System.out.print("Выберите команду: ");
    }

    private static void createTask() {
        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();

        Task task = new Task(name, description, 0);
        int id = taskManager.createTask(task);
        System.out.println("Задача создана с ID: " + id);
    }

    private static void createEpic() {
        System.out.print("Введите название эпика: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание эпика: ");
        String description = scanner.nextLine();

        Epic epic = new Epic(name, description, 0);
        int id = taskManager.createEpic(epic);
        System.out.println("Эпик создан с ID: " + id);
    }

    private static void createSubtask() {
        System.out.print("Введите ID эпика: ");
        int epicId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите название подзадачи: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание подзадачи: ");
        String description = scanner.nextLine();

        Subtask subtask = new Subtask(name, description, 0, epicId);
        int id = taskManager.createSubtask(subtask);
        if (id == -1) {
            System.out.println("Ошибка: эпик с ID " + epicId + " не существует");
        } else {
            System.out.println("Подзадача создана с ID: " + id);
        }
    }

    private static void printAllTasks() {
        List<Task> tasks = taskManager.getAllTasks();
        System.out.println("\nВсе задачи:");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    private static void printAllEpics() {
        List<Epic> epics = taskManager.getAllEpics();
        System.out.println("\nВсе эпики:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
    }

    private static void printAllSubtasks() {
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
    }

    private static void updateTaskStatus() {
        System.out.print("Введите ID задачи: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.println("Выберите статус:");
        System.out.println("1. NEW");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. DONE");
        System.out.print("Введите номер статуса: ");
        int statusNum = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Status status;
        switch (statusNum) {
            case 1:
                status = Status.NEW;
                break;
            case 2:
                status = Status.IN_PROGRESS;
                break;
            case 3:
                status = Status.DONE;
                break;
            default:
                System.out.println("Неверный номер статуса");
                return;
        }

        Task task = taskManager.getTaskById(id);
        if (task != null) {
            task.setStatus(status);
            taskManager.updateTask(task);
            System.out.println("Статус задачи обновлен");
            return;
        }

        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask != null) {
            subtask.setStatus(status);
            taskManager.updateSubtask(subtask);
            System.out.println("Статус подзадачи обновлен");
            return;
        }

        System.out.println("Задача с ID " + id + " не найдена");
    }

    private static void deleteTask() {
        System.out.print("Введите ID задачи: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        taskManager.deleteTaskById(id);
        System.out.println("Задача удалена");
    }

    private static void deleteEpic() {
        System.out.print("Введите ID эпика: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        taskManager.deleteEpicById(id);
        System.out.println("Эпик удален");
    }

    private static void deleteSubtask() {
        System.out.print("Введите ID подзадачи: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        taskManager.deleteSubtaskById(id);
        System.out.println("Подзадача удалена");
    }

    private static void printEpicSubtasks() {
        System.out.print("Введите ID эпика: ");
        int epicId = scanner.nextInt();
        scanner.nextLine();

        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);
        if (subtasks.isEmpty()) {
            System.out.println("Подзадачи не найдены или эпик не существует");
        } else {
            System.out.println("\nПодзадачи эпика " + epicId + ":");
            for (Subtask subtask : subtasks) {
                System.out.println(subtask);
            }
        }
    }
}
