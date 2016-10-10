/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Folder;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author dmarcobo
 */
public class SendMail {
    
    private String from;
    private String to;
    private String server;
    private String htmlMessage;
    private String subject;
    private String username;
    private String password;
    private String smptPort;
    
    /**
     * 
     */
    public SendMail(){
        cargarProperties();
    }
    
    /**
     * 
     * @throws RuntimeException 
     */
    public void send() throws RuntimeException{
        Properties props = new Properties();
       
        try {
        
//            MailSSLSocketFactory sf = new MailSSLSocketFactory();
//            sf.setTrustAllHosts(true); 
//            //Si no metemos la siguiente linea tenemos que especificar los vmarguments
//            //-Djavax.net.ssl.trustStore="C:/cacerts" -Djavax.net.ssl.trustStorePassword="changeit"
//            props.put("mail.smtp.ssl.socketFactory", sf);
//            //Inicio
//            props.put("mail.smtp.starttls.enable", "false");
//            props.put("mail.smtp.ssl.enable", "true");
//            props.put("mail.smtp.ssl.checkserveridentity", "false");
//            props.put("mail.smtp.ssl.trust", "*");
//            props.put("mail.smtp.socketFactory.fallback", "false");
//            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", smptPort);
            props.put("mail.smtp.host", server);
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.connectiontimeout", "10000");

            Session session = Session.getInstance(props,
              new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                    }
              });

            session.setDebug(true);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(getFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getTo()));
            message.setSubject(getSubject());
            //message.setText(getHtmlMessage());
            message.setContent(getHtmlMessage(), "text/html; charset=utf-8");
            Transport.send(message);
            
            System.out.println("Done");
            
        //} catch (GeneralSecurityException | MessagingException e) {
        } catch (MessagingException e){
                throw new RuntimeException(e);
        }
    }
    
    /**
     * 
     */
    private void cargarProperties(){
        Properties prop = new Properties();
	InputStream input = null;
	try {
            input = new FileInputStream("config.properties");
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            server = prop.getProperty("server");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            smptPort = prop.getProperty("smptPort");
            from = prop.getProperty("from");
            
            System.out.println(server);
            System.out.println(username);
            System.out.println(password);
            System.out.println(smptPort);

	} catch (IOException ex) {
            ex.printStackTrace();
	} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	}
    }

    /**
     * 
     */
    public void Recibir(){
        final Properties props = System.getProperties();

        //XTrustProvider.install();

       // System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", true);

//        props.setProperty("mail.pop3s.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.pop3s.socketFactory.fallback", "false");
        props.setProperty("mail.pop3s.host", "SMAD01PMSCA05.usersad.everis.int");
        props.setProperty("mail.pop3s.ssl.enable", "true");
        props.setProperty("mail.pop3s.port", "995");
        props.setProperty("mail.pop3s.auth", "true" );      
//        props.setProperty("mail.pop3s.starttls.enable", "true");

        Session session  = Session.getInstance(props);
        session.setDebug(true);


        try{
            Store store = session.getStore("pop3s");
            store.connect(username, password);         
            Folder folder = store.getDefaultFolder();
            folder.open(Folder.READ_ONLY);
            Message message[] = folder.getMessages();
            for ( int i = 0; i < message.length; i++ )
            {
              Message m = message[i];
                  System.out.println( "-------------------------\nNachricht: " + i );
                  System.out.println( "From: " + m.getFrom().toString() );
                  System.out.println( "Topic: " + m.getSubject() );   


              if ( m.isMimeType("text/plain") )
                System.out.println( m.getContent() );
            }
            folder.close( false );
            store.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }
    
    /**
     * @return the htmlMessage
     */
    public String getHtmlMessage() {
        return htmlMessage;
    }

    /**
     * @param htmlMessage the htmlMessage to set
     */
    public void setHtmlMessage(String htmlMessage) {
        this.htmlMessage = htmlMessage;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
}
