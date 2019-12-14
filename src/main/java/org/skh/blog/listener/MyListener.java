package org.skh.blog.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @Created IntelliJ IDEA.
 * @Author L_SKH
 * @Date 2019/11/15 21:54
 */
@WebListener
public class MyListener implements HttpSessionListener {
    public static int online = 0 ;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        online++;

    }
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        online -- ;

    }
}
