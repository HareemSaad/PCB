import java.util.*;

public class OS {
    // data feilds
    private int quantumSize;
    private Process PC;
    private int IR;
    private Queue pq = new Queue();
    // sub class - each object of Process represents a process
    static class Process {
        private String id;
        private int executionTime = 0; //time to finish execution -- here it is the same as instruction number
        private int instructionNumber; //size of process
        private int arrivalTime = -1; //when process enters the queue
        private int resumeIndex = 0; //holds the instruction from which the process is to be resumed
        private ArrayList<Integer> arr = new ArrayList<Integer>(); //a process is an array with above properties

        //constructor makes node and methodically sets their IDs
        public Process(int count) {
            this.id = "p" + count;
        }
    
        public String getId() {
            return id;
        }
    
        public int getExecutionTime() {
            return executionTime;
        }
    
        public void setExecutionTime(int executionTime) {
            this.executionTime = executionTime;
        }
    
        public int getArrivalTime() {
            return arrivalTime;
        }
    
        public void setArrivalTime(int arrivalTime) {
            this.arrivalTime = arrivalTime;
        }
    
        public ArrayList<Integer> getArray() {
            return arr;
        }
    
        public int getInstructionNumber() {
        return instructionNumber;
        }
    
        public void setInstructionNumber(int instructionNumber) {
        this.instructionNumber = instructionNumber;
        }
    
        public void setArray(ArrayList<Integer> ar) {
            System.out.println(id);
            this.arr = ar;
        }
    
        public void incResumeIndex() {
            this.resumeIndex = this.resumeIndex + 1;
        }

        public int getResumeIndex() {
            return resumeIndex;
        }

        public void setResumeIndex(int resumeIndex) {
            this.resumeIndex = resumeIndex;
        }
    }

    private void setQuantumSize(int quantumSize) {
        this.quantumSize = quantumSize;
    }
    
    private void createProcesses() {
        Scanner m = new Scanner(System.in);
        //set quantum time
        System.out.print("What quantum Time do you want to set: ");
        setQuantumSize(m.nextInt());
        //creation of n process objects
        System.out.print("How many processes do you want to create: ");
        int times = m.nextInt();
        for (int i = 0; i < times; i++) {
            //call process contructor 
            pq.EnQueue(new Process(i+1));
            // get size of process
            System.out.print("Enter size for process " + pq.getProcessBack().getId() + ": ");
            //set IN and ET
            pq.getProcessBack().setInstructionNumber(m.nextInt());
            pq.getProcessBack().setExecutionTime(pq.getProcessBack().getInstructionNumber());
            //enter array input
            for (int x = 0; x < pq.getProcessBack().getInstructionNumber(); x++) {
                System.out.print("enter element " + x + ": ");
                pq.getProcessBack().arr.add(m.nextInt());
            }
        }
        m.close();
    }
    //print all process arrays
    private void printarr() {
        Queue.QueueNode temp = pq.getFront();
        while (temp != null) {
            System.out.println(temp.getProcess().getId() + " ");
            for (int i = 0; i < temp.getProcess().arr.size(); i++) {
                System.out.print(temp.getProcess().arr.get(i) + " ");
            }
            System.out.println();
            temp = temp.getNext();
        }
    }
    //where everythjing runs
    public void run() {
        //make process print them
        createProcesses();
        printarr();
        //PC point to the front of the array
        PC = pq.getProcessFront();
        int count = -1, j = 0, t = quantumSize;
        //IR is the next executable instruction
        IR = PC.resumeIndex;
        Process pd;
        while (PC != null) {
            //set arrival time
            //if arrival time has already been set ignore
            count++;
            if (PC != null && PC.arrivalTime == -1) {
                PC.setArrivalTime(count);
            }
            //print PCB
            System.out.println("\nPCB of " + PC.getId());
            System.out.println("Execution time:\t\t" + PC.getExecutionTime());
            System.out.println("Arrival time:\t\t" + PC.getArrivalTime());
            System.out.println("No.of Instructions:\t" + PC.getInstructionNumber());
            System.out.println("Algorithm used:\t\tRound Robin");
            System.out.println("Quantum Size:\t\t" + quantumSize);
            //pd is the dispatched process
            //dequeue the front process to dispatch it
            pd = pq.Dequeue();
            PC = pq.getProcessFront();
            //in last iteration PC will be null and the process might have more instructions than the quantum time
            //so we set the for loop for the remaing instructions
            if (PC == null) {
                t = pd.instructionNumber - pd.resumeIndex;
            }
            //for loop to print array AND update resume index
            for (int i = 0; i < t; i++) {
                j = pd.getResumeIndex(); //point to the resume index
                pd.incResumeIndex();//increment resume index by 1
                //if the count is at t - 1 (last iteration) OR the array ends befor the last iteration of the quantum
                if (i == (t-1) || pd.resumeIndex == pd.instructionNumber) {
                    //at the end PC is null so we nullify IR
                    if (PC == null) {
                        IR = 0;
                    } else { //otherwise just give it the resume index of the next process
                        IR = PC.resumeIndex;
                    }
                } else { //if its not the last iteration or the end of array set IR to the next executable value
                    IR = j + 1;
                }
                //print array
                System.out.print(pd.arr.get(j) + " ");
                //if array ends break loop
                if (pd.resumeIndex == pd.instructionNumber) {
                    break;
                }
            }
            //print PC and IR
            if (PC == null) {
                System.out.println("\nPC: " + PC);
            } else { //else only because i wanted to print the id of process
                System.out.println("\nPC: " + PC + ", " + PC.getId());
            }
            System.out.println("IR: " + IR);
            //if the currently dispatched process is finished we DO NOT enqueue it otherwise we do
            if (pd.resumeIndex == pd.instructionNumber) {
                continue;
            } else {
                pq.EnQueue(pd);
            }
        }
    }
}