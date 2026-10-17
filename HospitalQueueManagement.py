import time
import heapq

class Patient:
    def __init__(self, name, severity):
        self.name = name
        self.severity = severity
        self.arrival_time = time.time()
        self.status = "Waiting"

    def get_waiting_time(self):
        return int(time.time() - self.arrival_time)

    def __lt__(self, other):
        return (self.severity, self.arrival_time) < (other.severity, other.arrival_time)

    def __str__(self):
        triage_category = {1: "Critical", 2: "High", 3: "Moderate", 4: "Low"}.get(self.severity, "Unknown")
        wait_time = self.get_waiting_time()
        return f"{self.name} (Triage: {triage_category}, Wait Time: {wait_time // 60} minutes, {wait_time % 60} seconds, Status: {self.status})"

class HospitalQueueManagement:
    def __init__(self):
        self.patient_queue = []
        self.patient_names = set()

    def start(self):
        while True:
            print("\n--- Hospital Queue Management ---")
            print("1. Add Patient")
            print("2. Attend Patient")
            print("3. Display Queue")
            print("4. Exit")
            choice = input("Choose an option: ").strip()

            if choice == "1":
                self.add_patient()
            elif choice == "2":
                self.attend_patient()
            elif choice == "3":
                self.display_queue()
            elif choice == "4":
                print("Exiting program!")
                break
            else:
                print("Invalid choice. Please try again.")

    def add_patient(self):
        name = input("Enter patient name: ").strip()
        if not self.is_valid_name(name) or name in self.patient_names:
            print("Invalid name or duplicate entry. Please try again.")
            return

        severity_text = input("Enter triage category (1=Critical, 2=High, 3=Moderate, 4=Low): ").strip()
        if not self.is_valid_severity(severity_text):
            print("Invalid severity. Enter a number between 1 and 4.")
            return

        severity = int(severity_text)
        new_patient = Patient(name, severity)
        heapq.heappush(self.patient_queue, new_patient)
        self.patient_names.add(name)
        print("Patient added successfully.")

    def attend_patient(self):
        if not self.patient_queue:
            print("No patients in the queue.")
        else:
            served_patient = heapq.heappop(self.patient_queue)
            self.patient_names.remove(served_patient.name)
            served_patient.status = "Served"
            print(f"Attending: {served_patient}")

    def display_queue(self):
        if not self.patient_queue:
            print("No patients in the queue.")
        else:
            print("\nCurrent Patient Queue (By Priority):")
            for patient in sorted(self.patient_queue):  # Display queue in sorted order
                print(patient)

    def is_valid_name(self, name):
        return name.replace(" ", "").isalpha()

    def is_valid_severity(self, severity_text):
        return severity_text in ["1", "2", "3", "4"]1

if __name__ == "__main__":
    system = HospitalQueueManagement()
    system.start()
