package main

import (
    "fmt"
    "sort"
    "time"
)

type Process struct {
    id            int
    burstTime     int
    arrivalTime   int
    remainingTime int
    startTime     int
    endTime       int
    priority      int
}

func inputProcesses() []Process {
    var n int
    fmt.Print("Enter number of processes: ")
    fmt.Scan(&n)

    processes := make([]Process, n)

    for i := 0; i < n; i++ {
        fmt.Printf("Enter details for Process %d\n", i+1)
        fmt.Print("Burst Time: ")
        fmt.Scan(&processes[i].burstTime)
        fmt.Print("Arrival Time: ")
        fmt.Scan(&processes[i].arrivalTime)
        fmt.Print("Priority (for Priority Scheduling): ")
        fmt.Scan(&processes[i].priority)

        processes[i].id = i + 1
        processes[i].remainingTime = processes[i].burstTime
    }

    return processes
}

// Algorithm selection
func selectAlgorithm() string {
    fmt.Println("Select Scheduling Algorithm:")
    fmt.Println("1. First-Come-First-Served (FCFS)")
    fmt.Println("2. Round Robin")
    fmt.Println("3. Shortest Job First (SJF)")
    fmt.Println("4. Priority Scheduling")
    fmt.Print("Choice: ")

    var choice int
    fmt.Scan(&choice)

    switch choice {
    case 1:
        return "FCFS"
    case 2:
        return "RR"
    case 3:
        return "SJF"
    case 4:
        return "Priority"
    default:
        fmt.Println("Invalid choice, defaulting to FCFS.")
        return "FCFS"
    }
}

// First-Come-First-Served (FCFS)
func fcfs(processes []Process) {
    timeElapsed := 0
    fmt.Println("FCFS Scheduling:")
    for i := 0; i < len(processes); i++ {
        timeElapsed = max(timeElapsed, processes[i].arrivalTime)
        fmt.Printf("Process %d starts execution at time %d\n", processes[i].id, timeElapsed)
        processes[i].startTime = timeElapsed
        time.Sleep(1 * time.Second) // Simulate execution
        timeElapsed += processes[i].burstTime
        processes[i].endTime = timeElapsed
        fmt.Printf("Process %d finishes execution at time %d\n", processes[i].id, timeElapsed)
    }
  
}

// Round Robin (RR)
func roundRobin(processes []Process, timeQuantum int) {
    timeElapsed := 0
    fmt.Println("Round Robin Scheduling:")
    queue := processes
    for len(queue) > 0 {
        for i := 0; i < len(queue); i++ {
            proc := &queue[i]
            if proc.remainingTime > 0 {
                fmt.Printf("Process %d starts execution at time %d\n", proc.id, timeElapsed)
                startTime := timeElapsed
                if proc.remainingTime > timeQuantum {
                    timeElapsed += timeQuantum
                    proc.remainingTime -= timeQuantum
                } else {
                    timeElapsed += proc.remainingTime
                    proc.remainingTime = 0
                }
                fmt.Printf("Process %d executes from %d to %d\n", proc.id, startTime, timeElapsed)
                time.Sleep(1 * time.Second) // Simulate execution
            }
        }

        // Remove finished processes
        queue = filterProcesses(queue)
    }
    
}

// Shortest Job First (SJF)
func sjf(processes []Process) {
    timeElapsed := 0
    fmt.Println("Shortest Job First Scheduling:")

    // Sort processes by burst time, if arrival time is the same, pick the process with lower burst time
    sort.Slice(processes, func(i, j int) bool {
        if processes[i].arrivalTime == processes[j].arrivalTime {
            return processes[i].burstTime < processes[j].burstTime
        }
        return processes[i].arrivalTime < processes[j].arrivalTime
    })

    for i := 0; i < len(processes); i++ {
        timeElapsed = max(timeElapsed, processes[i].arrivalTime)
        fmt.Printf("Process %d starts execution at time %d\n", processes[i].id, timeElapsed)
        processes[i].startTime = timeElapsed
        time.Sleep(1 * time.Second) // Simulate execution
        timeElapsed += processes[i].burstTime
        processes[i].endTime = timeElapsed
        fmt.Printf("Process %d finishes execution at time %d\n", processes[i].id, timeElapsed)
    }
    
}

// Priority Scheduling
func priorityScheduling(processes []Process) {
    timeElapsed := 0
    fmt.Println("Priority Scheduling:")

    // Sort processes by priority (lower number means higher priority)
    sort.Slice(processes, func(i, j int) bool {
        return processes[i].priority < processes[j].priority
    })

    for i := 0; i < len(processes); i++ {
        timeElapsed = max(timeElapsed, processes[i].arrivalTime)
        fmt.Printf("Process %d with priority %d starts execution at time %d\n", processes[i].id, processes[i].priority, timeElapsed)
        processes[i].startTime = timeElapsed
        time.Sleep(1 * time.Second) // Simulate execution
        timeElapsed += processes[i].burstTime
        processes[i].endTime = timeElapsed
        fmt.Printf("Process %d finishes execution at time %d\n", processes[i].id, timeElapsed)
    }
    
}


// Remove finished processes from queue
func filterProcesses(queue []Process) []Process {
    var remaining []Process
    for _, proc := range queue {
        if proc.remainingTime > 0 {
            remaining = append(remaining, proc)
        }
    }
    return remaining
}

// Helper function to calculate maximum of two values
func max(a, b int) int {
    if a > b {
        return a
    }
    return b
}

func main() {
    processes := inputProcesses()
    algorithm := selectAlgorithm()

    switch algorithm {
    case "FCFS":
        fcfs(processes)
    case "RR":
        var timeQuantum int
        fmt.Print("Enter Time Quantum: ")
        fmt.Scan(&timeQuantum)
        roundRobin(processes, timeQuantum)
    case "SJF":
        sjf(processes)
    case "Priority":
        priorityScheduling(processes)
    default:
        fmt.Println("Algorithm not yet implemented.")
    }
}

