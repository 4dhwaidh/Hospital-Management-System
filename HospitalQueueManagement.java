import java.util.*;

class Patient {
    private String name;
    private int severity;
    private long arrivalTime;
    private String status;

    public Patient(String name, int severity) {
        this.name = name;
        this.severity = severity;
        this.arrivalTime = System.currentTimeMillis();
        this.status = "Waiting";
    }

    public String getName() { return name; }
    public int getSeverity() { return severity; }
    public long getArrivalTime() { return arrivalTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getWaitingTime() {
        return (System.currentTimeMillis() - arrivalTime) / 1000;
    }

    @Override
    public String toString() {
        String triageCategory = switch (severity) {
            case 1 -> "Critical";
            case 2 -> "High";
            case 3 -> "Moderate";
            case 4 -> "Low";
            default -> "Unknown";
        };
        long waitTime = getWaitingTime();
        return name + " (Triage: " + triageCategory + ", Wait Time: "
               + waitTime / 60 + " minutes, " + waitTime % 60 + " seconds, Status: " + status + ")";
    }
}

public class HospitalQueueManagement {
    private PriorityQueue<Patient> patientQueue;
    private Set<String> patientNames;
    private Scanner scanner;

    public HospitalQueueManagement() {
        patientQueue = new PriorityQueue<>(Comparator.comparingInt(Patient::getSeverity)
                .thenComparingLong(Patient::getArrivalTime));
        patientNames = new HashSet<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Hospital Queue Management ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Attend Patient");
            System.out.println("3. Display Queue");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addPatient();
                case "2" -> attendPatient();
                case "3" -> displayQueue();
                case "4" -> {
                    System.out.println("Exiting program!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addPatient() {
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine().trim();
        if (!isValidName(name) || patientNames.contains(name)) {
            System.out.println("Invalid name or duplicate entry. Please try again.");
            return;
        }

        System.out.print("Enter triage category (1=Critical, 2=High, 3=Moderate, 4=Low): ");
        String severityText = scanner.nextLine().trim();
        if (!isValidSeverity(severityText)) {
            System.out.println("Invalid severity. Enter a number between 1 and 4.");
            return;
        }

        int severity = Integer.parseInt(severityText);
        Patient newPatient = new Patient(name, severity);
        patientQueue.add(newPatient);
        patientNames.add(name);
        System.out.println("Patient added successfully.");
    }

    private void attendPatient() {
        if (patientQueue.isEmpty()) {
            System.out.println("No patients in the queue.");
        } else {
            Patient servedPatient = patientQueue.poll();
            patientNames.remove(servedPatient.getName());
            servedPatient.setStatus("Served");
            System.out.println("Attending: " + servedPatient);
        }
    }

    private void displayQueue() {
        if (patientQueue.isEmpty()) {
            System.out.println("No patients in the queue.");
        } else {
            System.out.println("\nCurrent Patient Queue (By Priority):");
            for (Patient patient : patientQueue) {
                System.out.println(patient);
            }
        }
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]+");
    }

    private boolean isValidSeverity(String severityText) {
        return severityText.matches("[1-4]");
    }

    public static void main(String[] args) {
        HospitalQueueManagement system = new HospitalQueueManagement();
        system.start();
    }
}

2