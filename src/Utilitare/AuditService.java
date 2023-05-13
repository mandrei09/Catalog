package Utilitare;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static final String FILE_PATH = "audit.csv";

    public static void logAction(Operatii operatie,Clase numeClasa) {
        LocalDateTime sysdate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(operatie + " " + numeClasa + ", " + formatter.format(sysdate));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing audit log: " + e.getMessage());
        }
    }
}

