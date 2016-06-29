package com.mq.activity;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息消费者和接受者
 * @author hc-e40
 * 于spring集成时可以配置监听的形式及时发现消息队列数据
 *
 */
public class JMSConsumer {

	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址

    public static void main(String[] args) {
    	
    	System.out.println("用户名是："+USERNAME+"----密码是----"+PASSWORD+"地址："+BROKEURL);
        ConnectionFactory connectionFactory;//连接工厂
        Connection connection = null;//连接

        Session session;//会话 接受或者发送消息的线程
        Destination destination;//消息的目的地

        MessageConsumer messageConsumer;//消息的消费者

        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(JMSConsumer.USERNAME, JMSConsumer.PASSWORD, JMSConsumer.BROKEURL);

        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建一个连接HelloWorld的消息队列
            destination = session.createQueue("HelloWorld");
            //创建消息消费者
            messageConsumer = session.createConsumer(destination);

            int i = 0;
            while (true) {
                TextMessage textMessage = (TextMessage) messageConsumer.receive(100000);
                if(textMessage != null){
                    System.out.println("收到的消息:" + textMessage.getText());
                }else {
                    break;
                }
                i++;
                System.out.println(i);
            }


        } catch (JMSException e) {
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                    System.out.println("------------------");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
