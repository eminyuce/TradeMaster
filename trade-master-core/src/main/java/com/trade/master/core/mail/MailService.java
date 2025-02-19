package com.trade.master.core.mail;

import com.trade.master.core.entity.BotUser;

/**
 * Created by huseyina on 4/9/2017.
 */
public interface MailService {

    void sendMail(String to, String header, String body, boolean isHtml);

    void sendMail(BotUser user, String header, String body, boolean isHtml);


}
