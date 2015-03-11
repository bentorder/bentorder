import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.util.Date;
import java.text.DateFormat;


public class Main extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    if (req.getRequestURI().endsWith("/db")) {
      showDatabase(req,resp);
    } else {
      		showHome(req,resp);
    }
  }

  private void showHome(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    
    resp.setContentType("text/html; charset=UTF-8");
    PrintWriter out = resp.getWriter();
    resp.setCharacterEncoding("UTF-8");
    
    Date date = new Date();
	//date.getYear()+1990：年
	//date.getMonth()+1:月
    //date.getDate()：日
    showMenu menu = new showMenu();

    out.print(date.getYear()+1990+ "年 " +(date.getMonth()+1)+"月" +date.getDate()+ "日\n");
    out.println("<html>");
    out.println("<head>");
    out.println("<title>サンプル1</title>");
    out.println("</head>");
    out.println("<body>");
    //out.println("<form method=\"\" >");
    out.println("<p>ID:<input type=\"text\" name=\"user\"</p>");
    out.println("<p>PW:<input type=\"password\" name=\"pass\"</p></br>");
    out.println("<input type=\"button\" action=\"menu.ShowMenu(req,resp)\" value=\"ログイン\" >");
    //out.println("</form>");
    out.println("</body>");
    out.println("</html>");
	
    out.close();
  }
  class showMenu{
  	private void ShowMenu(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		resp.setCharacterEncoding("UTF-8");
      	
	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>サンプル2</title>");
	    out.println("</head>");
	    out.println("<body>");
	    out.println("<p>ログイン後のページだよ</p></br>");
	    out.println("</body>");
	    out.println("</html>");
    }
  }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
  private void showDatabase(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Connection connection = null;
    try {
      connection = getConnection();

      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      String out = "Hello!\n";
      while (rs.next()) {
          out += "Read from DB: " + rs.getTimestamp("tick") + "\n";
      }

      resp.getWriter().print(out);
    } catch (Exception e) {
      resp.getWriter().print("There was an error: " + e.getMessage());
    } finally {
      if (connection != null) try{connection.close();} catch(SQLException e){}
    }
  }

  private Connection getConnection() throws URISyntaxException, SQLException {
    URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    int port = dbUri.getPort();

    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

    return DriverManager.getConnection(dbUrl, username, password);
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new Main()),"/*");
    server.start();
    server.join();
  }
}
