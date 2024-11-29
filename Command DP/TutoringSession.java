import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Timeslot Class
class Timeslot {
    private LocalDateTime start;
    private LocalDateTime end;

    public Timeslot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public boolean fits(Timeslot other) {
        return (this.start.isBefore(other.end) && this.end.isAfter(other.start));
    }
}

// Command Interface
interface Command {
    void execute();
}

// Concrete Command
class SessionBooking implements Command {
    private Tutor tutor;
    private Timeslot requestedTimeslot;

    public SessionBooking(Tutor tutor, Timeslot requestedTimeslot) {
        this.tutor = tutor;
        this.requestedTimeslot = requestedTimeslot;
    }

    @Override
    public void execute() {
        tutor.manage(requestedTimeslot);
    }
}

// Receiver
class Tutor {
    private List<Timeslot> availability;

    public Tutor() {
        this.availability = new ArrayList<>();
    }

    public void addAvailability(Timeslot timeslot) {
        availability.add(timeslot);
    }

    public void manage(Timeslot requestedTimeslot) {
        boolean isAvailable = availability.stream().anyMatch(slot -> slot.fits(requestedTimeslot));
        if (isAvailable) {
            System.out.println("Session Accepted");
        } else {
            System.out.println("Session Rejected");
        }
    }
}

// Invoker
class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void receiveBookingRequest() {
        command.execute();
    }
}

// Client Code
public class TutoringSession {
    public static void main(String[] args) {
        Tutor tutor = new Tutor();

        // Adding tutor's availability
        tutor.addAvailability(new Timeslot(
                LocalDateTime.of(2024, 11, 29, 10, 0),
                LocalDateTime.of(2024, 11, 29, 12, 0)));
        tutor.addAvailability(new Timeslot(
                LocalDateTime.of(2024, 11, 29, 15, 0),
                LocalDateTime.of(2024, 11, 29, 17, 0)));

        Invoker invoker = new Invoker();

        // Requesting a session
        Timeslot requestedTimeslot = new Timeslot(
                LocalDateTime.of(2024, 11, 29, 10, 30),
                LocalDateTime.of(2024, 11, 29, 11, 30));
        Command session = new SessionBooking(tutor, requestedTimeslot);

        // Processing the booking
        invoker.setCommand(session);
        invoker.receiveBookingRequest(); // Output: Session Accepted
    }
}
