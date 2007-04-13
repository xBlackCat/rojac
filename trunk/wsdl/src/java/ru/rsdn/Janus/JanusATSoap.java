/**
 * JanusATSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.rsdn.Janus;

public interface JanusATSoap extends java.rmi.Remote {

    /**
     * Вытягивание топиков по ID сообщений
     */
    public ru.rsdn.Janus.TopicResponse getTopicByMessage(ru.rsdn.Janus.TopicRequest topicRequest) throws java.rmi.RemoteException;

    /**
     * Изменения в форуме с момента последней синхронизации
     */
    public ru.rsdn.Janus.ChangeResponse getNewData(ru.rsdn.Janus.ChangeRequest changeRequest) throws java.rmi.RemoteException;

    /**
     * Список форумов и групп форумов
     */
    public ru.rsdn.Janus.ForumResponse getForumList(ru.rsdn.Janus.ForumRequest forumRequest) throws java.rmi.RemoteException;

    /**
     * Получение новых пользователей с момента последней синхронизации
     */
    public ru.rsdn.Janus.UserResponse getNewUsers(ru.rsdn.Janus.UserRequest userRequest) throws java.rmi.RemoteException;

    /**
     * Добавление изменений в форумы
     */
    public void postChange(ru.rsdn.Janus.PostRequest postRequest) throws java.rmi.RemoteException;

    /**
     * Подтверждение добавления изменений в форумы
     */
    public ru.rsdn.Janus.PostResponse postChangeCommit() throws java.rmi.RemoteException;

    /**
     * Проверка активности
     */
    public void check() throws java.rmi.RemoteException;
}
