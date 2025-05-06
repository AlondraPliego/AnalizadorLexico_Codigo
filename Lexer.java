/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CODIGO;

/**
 *
 * @author Hp
 */
public class Lexer {

    private String input;
    private int pos;
    private int linea;
    private int columna;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.linea = 1;
        this.columna = 1;
        this.currentChar = input.length() > 0 ? input.charAt(0) : '\0';
    }

    private void avanzar() {
        if (currentChar == '\n') {
            linea++;
            columna = 0;
        }
        pos++;
        columna++;
        if (pos < input.length()) {
            currentChar = input.charAt(pos);
        } else {
            currentChar = '\0';
        }
    }

    private void saltarEspacios() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            avanzar();
        }
    }

    private Token numero() {
        StringBuilder sb = new StringBuilder();
        int tokenLinea = linea;
        int tokenColumna = columna;

        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            sb.append(currentChar);
            avanzar();
        }

        return new Token(TokenType.NUMERO, sb.toString(), tokenLinea, tokenColumna);
    }

    private Token identificador() {
        StringBuilder sb = new StringBuilder();
        int tokenLinea = linea;
        int tokenColumna = columna;

        while (currentChar != '\0'
                && ((Character.isLetterOrDigit(currentChar)))) {
            sb.append(currentChar);
            avanzar();
        }

        String lexema = sb.toString();

        // Clasificación de tokens
        switch (lexema.toLowerCase()) {
            case "robot":
                return new Token(TokenType.PALABRA_R, lexema, tokenLinea, tokenColumna);
            case "iniciar":
            case "finalizar":
                return new Token(TokenType.ACCION, lexema, tokenLinea, tokenColumna);
            case "base":
            case "cuerpo":
            case "garra":
            case "velocidad":
                return new Token(TokenType.METODO, lexema, tokenLinea, tokenColumna);
            case "d+":
                return new Token(TokenType.NUMERO, lexema, tokenLinea, tokenColumna);
            default:
                if (lexema.matches("[a-zA-Z][a-zA-Z0-9_]*")) {
                    return new Token(TokenType.IDENTIFICADOR, lexema, tokenLinea, tokenColumna);
                } else {
                     return new Token(TokenType.NO_VALIDO, lexema, tokenLinea, tokenColumna);

                }
        }
    }

    public Token getSiguienteToken() {
        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                saltarEspacios();
                continue;
            }

            if (Character.isLetter(currentChar)) {
                return identificador();
            }

            if (Character.isDigit(currentChar)) {
                return numero();
            }

            if (currentChar == '=' || currentChar == '.') {
                avanzar(); // Simplemente los ignora
                continue;
            }

               Token token = new Token(TokenType.NO_VALIDO, String.valueOf(currentChar), linea, columna);
            avanzar();
            return token;

        }

        return new Token(TokenType.EOF, "", linea, columna);
    }
} 