package org.cishell.cibridge.cishell.graphql;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;


public class GraphiqlServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(GraphiqlServlet.graphiqlHTML);
            out.close();
    }

    private static final String graphiqlHTML = GraphiqlServlet.entryToString("/graphiql.html");

    private static String entryToString(String entryPath) {
        try {
            InputStream inputStream = GraphiqlServlet.class.getResourceAsStream(entryPath);
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inputStream, "UTF-8");
            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return out.toString();
        } catch(Exception e) {
            return "";
        }
    }
}

