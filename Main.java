/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CODIGO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.*;

/**
 *
 * @author Hp
 */
public class Main {
    public static class TokenInfo {
        private String valor;
        private TokenType tipo;
        
        public TokenInfo(String valor, TokenType tipo, int linea) {
            this.valor = valor;
            this.tipo = tipo;
        }
        
        public String getValor() {
            return valor;
        }
        
        public TokenType getTipo() {
            return tipo;
        }
    }
    
    public static List<TokenInfo> analizarTextoCompleto(String codigo) {
        List<TokenInfo> tokens = new ArrayList<>();
        Lexer lexer = new Lexer(codigo);
        Token token;
        int lineaActual = 1;
        
        do {
            token = lexer.getSiguienteToken();
            if (token.getTipo() != TokenType.EOF) {
                tokens.add(new TokenInfo(
                        token.getValor(),
                        token.getTipo(),
                        lineaActual
                ));
            }
        } while (token.getTipo() != TokenType.EOF);
        
        return tokens;
    }
    
    public static String generarReporte(List<TokenInfo> tokens) {
        StringBuilder reporte = new StringBuilder();
        reporte.append(String.format("%-15s %-15s %n", "TOKEN", "TIPO"));
        reporte.append("----------------------------------------\n");
        
        for (TokenInfo token : tokens) {
            String colorInicio = "";
            String colorFin = "";
            if (token.getTipo() == TokenType.NO_VALIDO) {
                colorInicio = "\u001B[31m"; // Rojo
                colorFin = "\u001B[0m";     // Reset
            }
            reporte.append(String.format("%s%-15s %-15s %s%n",
                    colorInicio,
                    token.getValor(),
                    token.getTipo().toString(),
                    colorFin));
        }
        
        reporte.append("\nTotal tokens: ").append(tokens.size());
        
        return reporte.toString();
    }
    
    static class TokenTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                                                     boolean isSelected, boolean hasFocus, 
                                                     int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (column == 1 && "NO_VALIDO".equals(value.toString())) {
                c.setForeground(Color.RED);
            } else {
                c.setForeground(Color.BLACK);
            }
            
            return c;
        }
    }
    
    static class HeaderRenderer implements TableCellRenderer {
        private final TableCellRenderer defaultRenderer;

        public HeaderRenderer(TableCellRenderer defaultRenderer) {
            this.defaultRenderer = defaultRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                                                     boolean isSelected, boolean hasFocus, 
                                                     int row, int column) {
            JLabel header = (JLabel) defaultRenderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            
            header.setBackground(new Color(0, 102, 153));  // Color azul que coincide con el botón
            header.setForeground(Color.WHITE);
            header.setHorizontalAlignment(SwingConstants.CENTER);
            header.setBorder(BorderFactory.createEtchedBorder());
            header.setFont(header.getFont().deriveFont(Font.BOLD));
            
            return header;
        }
    }
    
    public static void generarReporteEnPane(List<TokenInfo> tokens, JTextPane textPane) {
       
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        model.addColumn("TOKEN");
        model.addColumn("TIPO");
        
        for (TokenInfo token : tokens) {
            model.addRow(new Object[]{token.getValor(), token.getTipo().toString()});
        }
        
        JTable table = new JTable(model);
        
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setRowSelectionAllowed(true);
        
        TokenTableCellRenderer cellRenderer = new TokenTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, cellRenderer);
        
        HeaderRenderer headerRenderer = new HeaderRenderer(table.getTableHeader().getDefaultRenderer());
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        
        JLabel totalLabel = new JLabel("Total tokens: " + tokens.size(), SwingConstants.CENTER);
        totalLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        totalLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        totalLabel.setForeground(new Color(0, 102, 153));
        totalLabel.setOpaque(true);
        totalLabel.setBackground(new Color(240, 240, 240));
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        
        JPanel tablePanel = new JPanel(new BorderLayout(0, 0));
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(totalLabel, BorderLayout.SOUTH);
        
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        
        try {
            StyledDocument doc = textPane.getStyledDocument();
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        textPane.insertComponent(mainPanel);
    }
}