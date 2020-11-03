package edu.escuelaing.arep.hardapp;

import spark.Request;
import spark.Response;
import static spark.Spark.*;

import java.math.BigInteger;

/**
 * Calcula el factorial de un número de forma ineficiente para posteriormente poder escalarlo
 * @author Diego Puerto
 */
public class factorialIneficiente {
    
    public static void main( String[] args ) {
        port(getPort());
        get("/", (req, res) -> resultsPage(req, res));  
    }
    
    /**
      * Funcion que escanea el numero de ingreso y lo convierte en un entero
      * 
      * @param req Request de la pagina
      * @param res Response de la pagina
      * @return 
      */
    private static String resultsPage(Request req, Response res) {
        
        String data = req.queryParams("data");
        
        int number = Integer.parseInt(data);
        
        String result = getNthNumberOfFibonacci(number);
        
        return result;
    }
    
    private static String getNthNumberOfFibonacci(int nth) {
        System.out.println("La aplicación está calculando el numero \""+ nth +"\" de Fibonaccie.");        

        BigInteger nth_1 = new BigInteger("1");
        BigInteger nth_2 = new BigInteger("0");
        BigInteger answer = new BigInteger("0");

        if (nth < 0)
            answer = new BigInteger("0");
        else if (nth == 0)
            answer = nth_2;
        else if (nth == 1)
            answer = nth_1;
        else {
            for (int i = 0; i < nth - 1; i++) {
                answer = nth_2.add(nth_1);
                nth_2 = nth_1;
                nth_1 = answer;
            }
        }

        return answer.toString();
    }
    
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }    
           
        return 4567; //returns default port if heroku-port isn't set(i.e. on localhost)    }
    }
    
}
