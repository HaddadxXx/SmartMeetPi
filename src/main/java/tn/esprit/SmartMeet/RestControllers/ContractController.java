package tn.esprit.SmartMeet.RestControllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Contract;
import tn.esprit.SmartMeet.DAO.Repositories.ContractRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.core.io.ByteArrayResource;
import com.itextpdf.kernel.pdf.PdfDocument;


import java.io.ByteArrayOutputStream;


@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/api/contracts")
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;

    @GetMapping("/download/{contractId}")
    public ResponseEntity<ByteArrayResource> downloadContract(@PathVariable String contractId) {
        try {
            Contract contract = contractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("Contract not found"));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try (PdfWriter writer = new PdfWriter(baos);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                // Header
                Paragraph header = new Paragraph("SPONSORSHIP CONTRACT")
                        .setFontSize(20)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER);
                document.add(header);

                // Content
                document.add(new Paragraph("\nTitle: " + contract.getTitle()));
                document.add(new Paragraph("Event ID: " + contract.getEventId()));
                document.add(new Paragraph("Sponsoring Offer ID: " + contract.getSponsoringOfferId()));
                document.add(new Paragraph("Description: " + contract.getDescription()));
                document.add(new Paragraph("Amount: " + contract.getAmount() + " DT"));
                document.add(new Paragraph("Creation Date: " + contract.getCreationDate()));

                // Signature
                Paragraph signature = new Paragraph("\nDigital Signature: " + contract.getId())
                        .setItalic()
                        .setFontSize(12);
                document.add(signature);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract_" + contract.getId() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new ByteArrayResource(baos.toByteArray()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}