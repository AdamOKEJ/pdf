import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RaportMagazynowy extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public RaportMagazynowy() {
        setTitle("System Raportowania Magazynowego");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID", "Nazwa produktu", "Ilość", "Cena"
        });

        model.addRow(new Object[]{1, "Mleko", 20, 3.50});
        model.addRow(new Object[]{2, "Chleb", 15, 2.80});
        model.addRow(new Object[]{3, "Masło", 10, 6.20});
        model.addRow(new Object[]{4, "Żółty ser", 8, 12.99});

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton pdfButton = new JButton("Generuj Raport PDF");
        pdfButton.addActionListener(e -> exportToPDF());

        add(scrollPane, BorderLayout.CENTER);
        add(pdfButton, BorderLayout.SOUTH);
    }

    private void exportToPDF() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            try {
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document,
                        new FileOutputStream(chooser.getSelectedFile() + ".pdf"));
                document.open();

              
                BaseFont baseFont = BaseFont.createFont(
                        BaseFont.HELVETICA,
                        BaseFont.IDENTITY_H,
                        BaseFont.EMBEDDED
                );
                Font headerFont = new Font(baseFont, 14, Font.BOLD);
                Font cellFont = new Font(baseFont, 11);

              
                String date = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
                Paragraph header = new Paragraph(
                        "Raport magazynowy\nData generowania: " + date,
                        headerFont
                );
                header.setAlignment(Element.ALIGN_CENTER);
                header.setSpacingAfter(20);
                document.add(header);

                PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
                pdfTable.setWidthPercentage(100);

               
                for (int i = 0; i < table.getColumnCount(); i++) {
                    PdfPCell cell = new PdfPCell(
                            new Phrase(table.getColumnName(i), cellFont)
                    );
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }

                for (int row = 0; row < table.getRowCount(); row++) {
                    for (int col = 0; col < table.getColumnCount(); col++) {
                        PdfPCell cell = new PdfPCell(
                                new Phrase(table.getValueAt(row, col).toString(), cellFont)
                        );
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfTable.addCell(cell);
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(this,
                        "Raport PDF wygenerowany poprawnie!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new RaportMagazynowy().setVisible(true)
        );
    }
}
