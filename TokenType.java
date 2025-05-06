/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CODIGO;

/**
 *
 * @author Hp
 */
public enum TokenType {
    PALABRA_R,     // Robot
    IDENTIFICADOR, // r1
    PUNTO, // el punto entre r1 y base
    ACCION,        // iniciar, finalizar
    METODO,        // base, cuerpo, garra, velocidad
    NUMERO,        // 45, 100, etc.
    ASIGNACION, // =
    NO_VALIDO,
    EOF            // fin de entrada
}


