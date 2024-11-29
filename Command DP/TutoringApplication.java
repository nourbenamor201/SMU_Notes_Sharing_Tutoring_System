import java.util.HashMap;
import java.util.Map;

// Command Interface
interface Command {
    void execute();
}

// Concrete Command
class TutorApplicationCommand implements Command {
    private SMUStaffAdmin admin;
    private TutorApplication application;

    public TutorApplicationCommand(SMUStaffAdmin admin, TutorApplication application) {
        this.admin = admin;
        this.application = application;
    }

    @Override
    public void execute() {
        admin.manageApplication(application);
    }
}

// Receiver
class SMUStaffAdmin {
    private Map<Integer, Float> studentGPAMap; // Map to store student GPA data

    public SMUStaffAdmin() {
        this.studentGPAMap = new HashMap<>();
    }

    // Add a student's GPA
    public void addStudentGPA(int studentId, float gpa) {
        studentGPAMap.put(studentId, gpa);
    }

    // Retrieve GPA for a specific student
    public float getStudentGPA(int studentId) {
        return studentGPAMap.getOrDefault(studentId, 0.0f); // Default GPA is 0.0 if not found
    }

    // Manage a tutor application
    public void manageApplication(TutorApplication application) {
        float studentGPA = getStudentGPA(application.getStudentId());
        if (studentGPA >= 3.5) { // Example criteria
            System.out.println("Application Accepted for Student ID: " + application.getStudentId());
        } else {
            System.out.println("Application Rejected for Student ID: " + application.getStudentId());
        }
    }
}

// Invoker
class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void submitApplication() {
        command.execute();
    }
}

// Abstract Application Class
abstract class AbstractTutorApplication {
    protected int courseId;
    protected int studentId;

    public AbstractTutorApplication(int courseId, int studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getStudentId() {
        return studentId;
    }
}

// Concrete Application Class
class TutorApplication extends AbstractTutorApplication {
    public TutorApplication(int courseId, int studentId) {
        super(courseId, studentId);
    }
}

// Client Code
public class TutoringApplication{
    public static void main(String[] args) {
        // SMUStaffAdmin instance
        SMUStaffAdmin admin = new SMUStaffAdmin();

        // Add GPA data for students
        admin.addStudentGPA(12345, 3.8f); // Student ID: 12345, GPA: 3.8
        admin.addStudentGPA(67890, 3.2f); // Student ID: 67890, GPA: 3.2

        // Create applications
        TutorApplication application1 = new TutorApplication(101, 12345);
        TutorApplication application2 = new TutorApplication(102, 67890);

        // Invoker instance
        Invoker invoker = new Invoker();

        // Process application 1
        Command command1 = new TutorApplicationCommand(admin, application1);
        invoker.setCommand(command1);
        invoker.submitApplication(); // Output: Application Accepted for Student ID: 12345

        // Process application 2
        Command command2 = new TutorApplicationCommand(admin, application2);
        invoker.setCommand(command2);
        invoker.submitApplication(); // Output: Application Rejected for Student ID: 67890
    }
}
