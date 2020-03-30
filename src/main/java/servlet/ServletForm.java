package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import model.Assistant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.*;

//Da ne bismo u web.xml-u definisali servlet i servlet-mapping,
// jednostavnije je da to uradimo pomocu anotacije
@WebServlet(name = "MyServlet", urlPatterns = "/myServlet")
public class ServletForm extends HttpServlet {

    // Promenljive u servletu nisu thraed safe!
    private int counter = 0;

    private Gson gson;
    private BufferedReader in;
    private PrintWriter outSocket;

    public ServletForm() { }

    // Metoda koja rukuje get zahtevom
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Socket socket = new Socket("localhost", 8113);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        gson = new Gson();

        outSocket.println("GET");
        String allAssistants = in.readLine();

        List<Map> lista = gson.fromJson(allAssistants, ArrayList.class);
        List<Assistant> assistants = new ArrayList<>();
        for (Map map : lista) {
            JsonElement element = gson.toJsonTree(map);
            Assistant assistant = gson.fromJson(element,Assistant.class);
            assistants.add(assistant);
        }
        Set<Assistant> set = new HashSet<>();
        for (Assistant a : assistants) {
            set.add(a);
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<body><h1>Assistants :</h1><br>");
        String styleGrey = "<td style=\"background-color: grey\">";
        String styleWhite = "<td style=\"background-color: white\">";

        out.println("<table border=\"1\"><tr><th>Assistents</th><th style=\"background-color: grey\">Scores</th></tr>");

        for (Assistant a : set) {
            int count = 0;
            double sum = 0;
            for (Assistant a1 : assistants) {
                if (a.equals(a1)) {
                    count++;
                    sum += a1.getPoints();
                }
            }
            double avg = sum / count;
            DecimalFormat df = new DecimalFormat("##.##");
            avg = Double.parseDouble(df.format(avg));
            out.println("<tr><td>");
            out.println(a.getName() + " " + a.getLastName() + "</td>" + styleGrey + avg + "</td></tr>");
        }
        out.println("</table>");
        out.println("</body>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Socket socket = new Socket("localhost", 8113);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        gson = new Gson();

        String ime = req.getParameter("ime");
        String prezime = req.getParameter("prezime");
        String ocenaStr = req.getParameter("ocena");
        int ocena = Integer.parseInt(ocenaStr);

        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
        out.println("<body>");

        if (ocena >= 0 && ocena <= 10 && ime.length() > 0 && prezime.length() > 0) {
            outSocket.println("POST");
            outSocket.println(gson.toJson(new Assistant(ime,prezime,ocena)));
            out.println("<h1>Assistent with name : " + ime + " " + prezime + " successfully graded.</h1>");
        }else {
            out.println("<h1>Error. You didn't fill out the form correctly!</h1>");
        }

        out.println("</body>");
        out.close();
    }
}
