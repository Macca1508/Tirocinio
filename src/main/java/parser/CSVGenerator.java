package parser;

import java.io.*;
import java.util.Random;

public class CSVGenerator {
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        String fileName = "large_dataset_250000.csv";
        int numRows = 250000; // Cambia questo numero per più/meno righe
        
        try {
            generateCSV(fileName, numRows);
            System.out.println("File CSV generato: " + fileName);
            System.out.println("Numero di righe: " + numRows);
            System.out.println("Dimensione stimata: ~" + (numRows * 200 / 1024 / 1024) + " MB");
        } catch (IOException e) {
            System.err.println("Errore nella generazione del CSV: " + e.getMessage());
        }
    }
    
    public static void generateCSV(String fileName, int numRows) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Header
            writer.println("patient_id;body_mass_at_visit_kg;length_at_visit_cm;walking_experience_months;motor_risk_class;BSID_III_class;HRv;HRml;Hrap;SENv1;SENv2;SENv3;SENv4;SENv5;SENv6;SEN ml1;SEN ml2;SEN ml3;SEN ml4;SEN ml5;SEN ml 6;SEN ap1;SEN ap2;SEN ap 3;SEN ap 4;SEN ap 5;SEN ap 6;RRv;DETv;AVGLv;RRml;DETml;AVGLml;Rrap;DETap;AVGlap;Stride;nStride;Step;nStep;Stance%;DS%;stdStride;stdStep;stdStance;stdDS;Rstr_PSD1;Rstr_PSD2;Lstr_PSD1;Lstr_PSD2;step_PSD1;step_PSD2;Rstance_PSD1;Rstance_PSD2;Lstance_PSD1;Lstance_PSD2;DS_PSD1;DS_PSD2;simm_strT;simm_stance");
            
            // Genera righe di dati
            for (int i = 1; i <= numRows; i++) {
                StringBuilder row = new StringBuilder();
                
                // patient_id
                row.append("P").append(String.format("%06d", i)).append(";");
                
                // body_mass_at_visit_kg (3-25 kg per bambini)
                row.append(String.format("%.2f", 3.0 + random.nextDouble() * 22.0)).append(";");
                
                // length_at_visit_cm (45-120 cm)
                row.append(String.format("%.1f", 45.0 + random.nextDouble() * 75.0)).append(";");
                
                // walking_experience_months (0-36 mesi)
                row.append(random.nextInt(37)).append(";");
                
                // motor_risk_class (1-5)
                row.append(random.nextInt(5) + 1).append(";");
                
                // BSID_III_class (1-4)
                row.append(random.nextInt(4) + 1).append(";");
                
                // Valori HR (Heart Rate) - range realistico 80-180 bpm
                for (int j = 0; j < 3; j++) {
                    row.append(String.format("%.2f", 80.0 + random.nextDouble() * 100.0)).append(";");
                }
                
                // Valori SEN (Sensori) - range 0-100
                for (int j = 0; j < 18; j++) {
                    row.append(String.format("%.3f", random.nextDouble() * 100.0)).append(";");
                }
                
                // Valori RR, DET, AVGL - range variabile
                for (int j = 0; j < 9; j++) {
                    row.append(String.format("%.4f", random.nextDouble() * 10.0)).append(";");
                }
                
                // Parametri di camminata
                // Stride (lunghezza passo) - cm
                row.append(String.format("%.2f", 15.0 + random.nextDouble() * 45.0)).append(";");
                
                // nStride (numero di passi)
                row.append(random.nextInt(200) + 50).append(";");
                
                // Step (tempo passo) - secondi
                row.append(String.format("%.3f", 0.3 + random.nextDouble() * 1.2)).append(";");
                
                // nStep (numero di step)
                row.append(random.nextInt(400) + 100).append(";");
                
                // Stance% (percentuale appoggio)
                row.append(String.format("%.2f", 55.0 + random.nextDouble() * 25.0)).append(";");
                
                // DS% (Double Support %)
                row.append(String.format("%.2f", 10.0 + random.nextDouble() * 30.0)).append(";");
                
                // Deviazioni standard
                for (int j = 0; j < 4; j++) {
                    row.append(String.format("%.4f", random.nextDouble() * 5.0)).append(";");
                }
                
                // Valori PSD (Power Spectral Density)
                for (int j = 0; j < 12; j++) {
                    row.append(String.format("%.6f", random.nextDouble() * 0.001)).append(";");
                }
                
                // Valori simmetria (0-1)
                row.append(String.format("%.4f", random.nextDouble())).append(";");
                row.append(String.format("%.4f", random.nextDouble()));
                
                writer.println(row.toString());
                
                // Progress indicator
                if (i % 10000 == 0) {
                    System.out.println("Generato " + i + " righe...");
                }
            }
        }
    }
}
