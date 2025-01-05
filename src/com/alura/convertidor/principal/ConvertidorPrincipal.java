package com.alura.convertidor.principal;

import com.alura.convertidor.principal.Conversiones;
import com.alura.convertidor.principal.ConsultaConvertidor;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Scanner;

public class ConvertidorPrincipal {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ConsultaConvertidor consultaConvertidor =
            new ConsultaConvertidor(HttpClient.newHttpClient());

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = obtenerOpcionUsuario();

            try {
                switch (opcion) {
                    case 7 -> {
                        if (confirmarSalida()) {
                            System.out.println("¡Gracias por usar el convertidor!");
                            continuar = false;
                        }
                    }
                    case 1, 2, 3, 4, 5, 6 -> realizarConversion(opcion);
                    default -> System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static boolean confirmarSalida() {
        while (true) {
            System.out.println("\n¿Está seguro que desea salir del programa? (S/N):");
            String respuesta = scanner.next().trim().toUpperCase();
            if (respuesta.equals("S") || respuesta.equals("SI")) {
                return true;
            } else if (respuesta.equals("N") || respuesta.equals("NO")) {
                return false;
            } else {
                System.out.println("Por favor, responda con S o N.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("*".repeat(60));
        System.out.println("Sea bienvenido/a al Conversor de Moneda =]");
        System.out.println();
        System.out.println("1) Dólar =>> Peso argentino");
        System.out.println("2) Peso argentino =>> Dólar");
        System.out.println("3) Dólar =>> Real brasileño");
        System.out.println("4) Real brasileño =>> Dólar");
        System.out.println("5) Dólar =>> Peso colombiano");
        System.out.println("6) Peso colombiano =>> Dólar");
        System.out.println("7) Salir");
        System.out.println();
        System.out.println("Elija una opción válida:");
        System.out.println("*".repeat(60));
    }

    private static int obtenerOpcionUsuario() {
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un número válido.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void realizarConversion(int opcion) throws IOException, InterruptedException {
        String monedaOrigen, monedaDestino;

        switch (opcion) {
            case 1 -> {
                monedaOrigen = "USD";
                monedaDestino = "ARS";
            }
            case 2 -> {
                monedaOrigen = "ARS";
                monedaDestino = "USD";
            }
            case 3 -> {
                monedaOrigen = "USD";
                monedaDestino = "BRL";
            }
            case 4 -> {
                monedaOrigen = "BRL";
                monedaDestino = "USD";
            }
            case 5 -> {
                monedaOrigen = "USD";
                monedaDestino = "COP";
            }
            case 6 -> {
                monedaOrigen = "COP";
                monedaDestino = "USD";
            }
            default -> {
                System.out.println("Opción no válida");
                return;
            }
        }

        System.out.print("\nIngrese el valor a convertir: ");
        double cantidad = obtenerCantidad();

        Conversiones resultado = consultaConvertidor.tipoDeCambio(monedaOrigen, monedaDestino);
        double cantidadConvertida = cantidad * resultado.conversionRate();

        System.out.println("\nResultado de la conversión:");
        System.out.printf("%,.2f %s = %,.2f %s%n",
                cantidad, monedaOrigen, cantidadConvertida, monedaDestino);
        System.out.printf("Tasa de cambio: 1 %s = %,.4f %s%n",
                monedaOrigen, resultado.conversionRate(), monedaDestino);
        System.out.println("Última actualización: " + resultado.timeLastUpdateUtc());
        System.out.println("Próxima actualización: " + resultado.timeNextUpdateUtc());
    }

    private static double obtenerCantidad() {
        while (true) {
            try {
                while (!scanner.hasNextDouble()) {
                    System.out.println("Por favor, ingrese un valor numérico válido.");
                    scanner.next();
                }
                double cantidad = scanner.nextDouble();
                if (cantidad <= 0) {
                    System.out.println("Por favor, ingrese un valor mayor que cero.");
                    continue;
                }
                return cantidad;
            } catch (Exception e) {
                System.out.println("Error al leer el valor. Intente nuevamente.");
                scanner.nextLine();
            }
        }
    }
}