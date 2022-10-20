public class Process {
    private int id;
    private String name;
    private int remainingTime;

    private static int ID_OF_TO_NEW_PROCESS = 1;

    public Process() {
        id = ID_OF_TO_NEW_PROCESS++;
        name = "Processo número " + id;
        remainingTime = (int) Math.ceil(Math.random() * 20);
    }

    public void showHelloMessage() {
        System.out.println(
            "Oi, sou o processo " + id
            + ", e estou com tempo restante de: "
            + remainingTime + " segundos."
        );
    }

    private void showExecutedMessage() {
        System.out.println(
            "Oi, sou o processo " + id
            + ", fiz minha execução e fiquei com: "
            + remainingTime + " segundos de tempo restante."
        );
        System.out.println();
    }

    public void run(int runTime) {
        remainingTime -= runTime;
        showExecutedMessage();
    }

    public boolean hasFullyExecuted() {
        return remainingTime <= 0;
    }

    public boolean hasBiggerPriorityThan(Process process) {
        if(process == null) return true;
        if(this.remainingTime == process.getRemainingTime()) {
            return this.id < process.getId();
        }

        return this.remainingTime < process.getRemainingTime();
    }

    public double getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRemainingTime() {
        return remainingTime;
    } 
}
