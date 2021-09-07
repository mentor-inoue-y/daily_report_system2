package filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    /**
     * Default constructor.
     */
    public EncodingFilter() {

    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {

    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //out_list("エンコード前", request);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        chain.doFilter(request, response);

        //out_list("エンコード後", request);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {

    }

    private void out_list(String mes, ServletRequest request) {
        Enumeration names = request.getParameterNames();

        System.out.println("");
        System.out.println(request.getCharacterEncoding());
        System.out.println("*************  "+mes+"*************  ");

        while (names.hasMoreElements()){
          String name = (String)names.nextElement();
          String vals[] = request.getParameterValues(name);
          if (vals != null){
            for (int i = 0 ; i < vals.length ; i++){
              System.out.print(name);
              System.out.print(":");
              System.out.println(vals[i]);
            }
          }
        }

        System.out.println("******************************  ");
        System.out.println("");
    }

}