import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Process> processesList = new ArrayList<>();
    private static int DEFAULT_PROCESS_EXECUTION_TIME = 3; 

    public Scheduler() {
        super();
    }

    public void executeSimulation() {
        createProcesses(4);

        int iterationIndex = 0;
        Process previousProcessExecuted = null;
        while(!processesList.isEmpty() || previousProcessExecuted != null) {
            Process higherPriorityProcess = removeFirst();
            if(previousProcessExecuted != null) add(previousProcessExecuted);

            if(iterationIndex == 3) waitOneSecondToCreateMoreProcesses(8);
            if(iterationIndex == 6) waitOneSecondToCreateMoreProcesses(16);

            if(higherPriorityProcess == null) previousProcessExecuted = null;
            else {
                executeProcess(higherPriorityProcess);
                previousProcessExecuted = 
                    higherPriorityProcess.hasFullyExecuted() ? null : higherPriorityProcess;
    
                iterationIndex++;
            }
        }
    }

    private void createProcesses(int quantity) {
        for(int ind=0 ; ind<quantity ; ind++) {
            add(new Process());
        }
    }

    private void waitOneSecondToCreateMoreProcesses(int processesQuantity) {
        try {
            Thread.sleep(1000);
            createProcesses(processesQuantity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void executeProcess(Process process) {
        int processRemainingTime = process.getRemainingTime();
        int executionTime = 
            processRemainingTime < DEFAULT_PROCESS_EXECUTION_TIME ? 
            processRemainingTime : DEFAULT_PROCESS_EXECUTION_TIME;

        try {
            process.showHelloMessage();
            Thread.sleep(executionTime * 1000);
            process.run(executionTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void add(Process newProcess) {
        processesList.add(newProcess);
        riseProcess(processesList.size() - 1);
    }

    private Process removeFirst() {
        if(processesList.size() == 0) return null;

        int lowerPriorityProcessIndex = processesList.size() - 1;
        Process higherPriorityProcess = processesList.get(0);
        Process lowerPriorityProcess = processesList.get(
            lowerPriorityProcessIndex
        );

        processesList.set(0, lowerPriorityProcess);
        processesList.remove(lowerPriorityProcessIndex);
        descendProcess(0);

        return higherPriorityProcess;
    }

    private void exchangePositions(int firstIndex, int secondIndex) {
        Process firstProcess = processesList.get(firstIndex);
        Process secondProcess = processesList.get(secondIndex);

        processesList.set(firstIndex, secondProcess);
        processesList.set(secondIndex, firstProcess);
    }

    private void riseProcess(int processIndex) {
        if(processIndex == 0) return;

        Process process = processesList.get(processIndex);
        int parentIndex = processIndex/2;
        Process parentProcess = processesList.get(parentIndex);

        if(process.hasBiggerPriorityThan(parentProcess)) {
            exchangePositions(processIndex, parentIndex);
            riseProcess(parentIndex);
        }
    }

    private boolean exchangeChildAndParentPositionIfChildIsHigherPriority(
        int childIndex, int parentIndex
    ) {
        Process parentProcess = processesList.get(parentIndex);
        Process childProcess = processesList.get(childIndex);
        
        boolean needsExchange = childProcess.hasBiggerPriorityThan(parentProcess);
        if(needsExchange) {
            exchangePositions(parentIndex, childIndex);
            descendProcess(childIndex);
        }

        return needsExchange;
    }

    private void descendProcess(int processIndex) {
        int leftChildIndex = processIndex * 2;
        if(leftChildIndex > processesList.size() - 1) return;

        boolean exchangeWasExecuted = exchangeChildAndParentPositionIfChildIsHigherPriority(
            leftChildIndex, processIndex
        );
        if(exchangeWasExecuted) return;

        int rightChildIndex = leftChildIndex + 1;
        if(rightChildIndex > processesList.size() - 1) return;

        exchangeChildAndParentPositionIfChildIsHigherPriority(
            rightChildIndex, processIndex
        );
    }
}
