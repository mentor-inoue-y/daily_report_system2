package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actions.ActionBase;
import actions.UnknownAction;
import constants.ForwardConst;

@WebServlet(name="FrontController", urlPatterns={"/"})
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FrontController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        //パラメータに該当するActionクラスのインスタンス
        ActionBase action = getAction(request, response);

        //サーブレットコンテキスト、リクエスト、レスポンスをActionインスタンスのフィールドに設定
        action.init(getServletContext(), request, response);

        //Actionクラスの処理を呼び出し
        action.process();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //request.setCharacterEncoding("UTF-8");

        //out_list("doPost", request);

        doGet(request, response);
    }

    /**
     * リクエストパラメータの値から該当するActionクラスのインスタンスを作成し、返却する
     * (例:パラメータが action=Employee の場合、actions.EmployeeActionオブジェクト)
     * @param request リクエスト
     * @param response レスポンス
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) //コンパイラ警告を抑制
    private ActionBase getAction(HttpServletRequest request, HttpServletResponse response) {
        Class type = null;
        ActionBase action = null;
        try {

            //リクエストからパラメータ"action"の値を取得 (例:"Employee"、"Report")
            String actionString = request.getParameter(ForwardConst.ACT.getValue());

            //該当するActionオブジェクトを作成 (例:リクエストからパラメータ action=Employee の場合、actions.EmployeeActionオブジェクト)
            type = Class.forName(String.format("actions.%sAction", actionString));

            //ActionBaseのオブジェクトにキャスト(例:actions.EmployeeActionオブジェクト→actions.ActionBaseオブジェクト)
            action = (ActionBase) (type.asSubclass(ActionBase.class)
                    .getDeclaredConstructor()
                    .newInstance());

            //System.out.println("【FrontController】実行するタイプは＝" + actionString);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SecurityException
                | IllegalArgumentException | InvocationTargetException| NoSuchMethodException e) {

            //リクエストパラメータに設定されている"action"の値が不正の場合(例:action=xxxxx 等、該当するActionクラスがない場合)
            //エラー処理を行うActionオブジェクトを作成
            action = new UnknownAction();
        }
        return action;
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
