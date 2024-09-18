import java.util.*;

class Task {
    int taskID;
    int execTime;  // Execution time (Burst time)
    int arrival;
    int remainingExecTime;
    int startAt = -1;
    int endAt = 0;
    int priority;
    boolean done = false;

    Task(int taskID, int execTime, int arrival, int priority) {
        this.taskID = taskID;
        this.execTime = execTime;
        this.arrival = arrival;
        this.remainingExecTime = execTime;
        this.priority = priority;
    }
}

public class TaskScheduler {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Gather task information
        System.out.print("Number of tasks to schedule: ");
        int numTasks = sc.nextInt();
        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < numTasks; i++) {
            System.out.println("Details for Task " + (i + 1));
            System.out.print("Execution Time: ");
            int execTime = sc.nextInt();
            System.out.print("Arrival Time: ");
            int arrival = sc.nextInt();
            System.out.print("Task Priority (for Priority Scheduling): ");
            int priority = sc.nextInt();
            tasks.add(new Task(i + 1, execTime, arrival, priority));
        }

        // Choose a scheduling algorithm
        System.out.println("Choose Scheduling Algorithm:");
        System.out.println("1. First Come First Serve (FCFS)");
        System.out.println("2. Round Robin");
        System.out.println("3. Shortest Task First (SJF)");
        System.out.println("4. Priority-based Scheduling");
        System.out.print("Choice: ");
        int option = sc.nextInt();

        switch (option) {
            case 1:
                firstComeFirstServe(tasks);
                break;
            case 2:
                System.out.print("Enter Time Slice for Round Robin: ");
                int timeSlice = sc.nextInt();
                roundRobin(tasks, timeSlice);
                break;
            case 3:
                shortestJobFirst(tasks);
                break;
            case 4:
                priorityBasedScheduling(tasks);
                break;
            default:
                System.out.println("Invalid choice, falling back to First Come First Serve.");
                firstComeFirstServe(tasks);
        }
    }

    // First Come First Serve scheduling algorithm
    public static void firstComeFirstServe(List<Task> tasks) {
        int elapsedTime = 0;
        System.out.println("First Come First Serve Execution:");

        while (!allTasksDone(tasks)) {
            for (Task task : tasks) {
                if (task.arrival <= elapsedTime && task.remainingExecTime > 0) {
                    if (task.startAt == -1) {
                        task.startAt = elapsedTime;
                    }

                    System.out.println("Task " + task.taskID + " begins at time " + elapsedTime);
                    for (int i = 0; i < task.execTime; i++) {
                        try {
                            Thread.sleep(1000);  // Simulate one time unit of execution
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        elapsedTime++;
                        task.remainingExecTime--;
                        displayGanttChart(tasks, elapsedTime);

                        if (task.remainingExecTime == 0) {
                            task.endAt = elapsedTime;
                            task.done = true;
                            System.out.println("Task " + task.taskID + " ends at time " + elapsedTime);
                        }
                    }
                }
            }
        }
    }

    // Round Robin scheduling algorithm
    public static void roundRobin(List<Task> tasks, int timeSlice) {
        int elapsedTime = 0;
        Queue<Task> queue = new LinkedList<>(tasks);
        System.out.println("Round Robin Scheduling:");

        while (!queue.isEmpty()) {
            Task task = queue.poll();
            if (task.remainingExecTime > 0) {
                if (task.startAt == -1) {
                    task.startAt = Math.max(elapsedTime, task.arrival);
                }

                System.out.println("Task " + task.taskID + " starts at time " + elapsedTime);
                int taskExecTime = Math.min(task.remainingExecTime, timeSlice);

                for (int i = 0; i < taskExecTime; i++) {
                    try {
                        Thread.sleep(1000);  // Simulate execution for one time unit
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    elapsedTime++;
                    task.remainingExecTime--;
                    displayGanttChart(tasks, elapsedTime);
                }

                if (task.remainingExecTime > 0) {
                    queue.add(task);  // Re-add unfinished task to queue
                } else {
                    task.endAt = elapsedTime;
                    task.done = true;
                    System.out.println("Task " + task.taskID + " finishes at time " + elapsedTime);
                }
            }
        }
    }

    // Shortest Job First scheduling algorithm
    public static void shortestJobFirst(List<Task> tasks) {
        int elapsedTime = 0;
        System.out.println("Shortest Job First Execution:");

        while (!allTasksDone(tasks)) {
            tasks.sort(Comparator.comparingInt(t -> t.remainingExecTime));

            for (Task task : tasks) {
                if (task.arrival <= elapsedTime && task.remainingExecTime > 0) {
                    if (task.startAt == -1) {
                        task.startAt = elapsedTime;
                    }

                    System.out.println("Task " + task.taskID + " starts at time " + elapsedTime);
                    for (int i = 0; i < task.execTime; i++) {
                        try {
                            Thread.sleep(1000);  // Simulate execution for one time unit
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        elapsedTime++;
                        task.remainingExecTime--;
                        displayGanttChart(tasks, elapsedTime);

                        if (task.remainingExecTime == 0) {
                            task.endAt = elapsedTime;
                            task.done = true;
                            System.out.println("Task " + task.taskID + " ends at time " + elapsedTime);
                        }
                    }
                }
            }
        }
    }

    // Priority-based scheduling algorithm
    public static void priorityBasedScheduling(List<Task> tasks) {
        int elapsedTime = 0;
        System.out.println("Priority-based Scheduling:");

        while (!allTasksDone(tasks)) {
            tasks.sort(Comparator.comparingInt(t -> t.priority));

            for (Task task : tasks) {
                if (task.arrival <= elapsedTime && task.remainingExecTime > 0) {
                    if (task.startAt == -1) {
                        task.startAt = elapsedTime;
                    }

                    System.out.println("Task " + task.taskID + " with priority " + task.priority + " starts at time " + elapsedTime);
                    for (int i = 0; i < task.execTime; i++) {
                        try {
                            Thread.sleep(1000);  // Simulate execution for one time unit
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        elapsedTime++;
                        task.remainingExecTime--;
                        displayGanttChart(tasks, elapsedTime);

                        if (task.remainingExecTime == 0) {
                            task.endAt = elapsedTime;
                            task.done = true;
                            System.out.println("Task " + task.taskID + " ends at time " + elapsedTime);
                        }
                    }
                }
            }
        }
    }

    // Display Gantt Chart in real-time based on task execution
    public static void displayGanttChart(List<Task> tasks, int currentTime) {
        System.out.print("Time " + currentTime + ": | ");
        for (Task task : tasks) {
            if (task.startAt != -1 && task.remainingExecTime < task.execTime) {
                System.out.print("T" + task.taskID + " ");
            } else {
                System.out.print("Idle ");
            }
        }
        System.out.println("|");
    }

    // Check if all tasks have been completed
    public static boolean allTasksDone(List<Task> tasks) {
        for (Task task : tasks) {
            if (!task.done) {
                return false;
            }
        }
        return true;
    }
}
