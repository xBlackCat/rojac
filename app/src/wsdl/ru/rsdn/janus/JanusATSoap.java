
package ru.rsdn.janus;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 *
 */
@WebService(name = "JanusATSoap", targetNamespace = "http://rsdn.ru/Janus/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface JanusATSoap {


    /**
     * Возвращает новые сообщения о нарушениях
     *
     * @param request
     * @return
     *     returns ru.rsdn.janus.ViolationResponse
     */
    @WebMethod(operationName = "GetNewViolations", action = "http://rsdn.ru/Janus/GetNewViolations")
    @WebResult(name = "GetNewViolationsResult", targetNamespace = "http://rsdn.ru/Janus/")
    @RequestWrapper(localName = "GetNewViolations", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetNewViolations")
    @ResponseWrapper(localName = "GetNewViolationsResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetNewViolationsResponse")
    public ViolationResponse getNewViolations(
        @WebParam(name = "request", targetNamespace = "http://rsdn.ru/Janus/")
        ViolationRequest request);

    /**
     * Вытягивание топиков по ID сообщений
     *
     * @param topicRequest
     * @return
     *     returns ru.rsdn.janus.TopicResponse
     */
    @WebMethod(operationName = "GetTopicByMessage", action = "http://rsdn.ru/Janus/GetTopicByMessage")
    @WebResult(name = "GetTopicByMessageResult", targetNamespace = "http://rsdn.ru/Janus/")
    @RequestWrapper(localName = "GetTopicByMessage", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetTopicByMessage")
    @ResponseWrapper(localName = "GetTopicByMessageResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetTopicByMessageResponse")
    public TopicResponse getTopicByMessage(
        @WebParam(name = "topicRequest", targetNamespace = "http://rsdn.ru/Janus/")
        TopicRequest topicRequest);

    /**
     * Изменения в форуме с момента последней синхронизации
     *
     * @param changeRequest
     * @return
     *     returns ru.rsdn.janus.ChangeResponse
     */
    @WebMethod(operationName = "GetNewData", action = "http://rsdn.ru/Janus/GetNewData")
    @WebResult(name = "GetNewDataResult", targetNamespace = "http://rsdn.ru/Janus/")
    @RequestWrapper(localName = "GetNewData", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetNewData")
    @ResponseWrapper(localName = "GetNewDataResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetNewDataResponse")
    public ChangeResponse getNewData(
        @WebParam(name = "changeRequest", targetNamespace = "http://rsdn.ru/Janus/")
        ChangeRequest changeRequest);

    /**
     * Список форумов и групп форумов
     *
     * @param forumRequest
     * @return
     *     returns ru.rsdn.janus.ForumResponse
     */
    @WebMethod(operationName = "GetForumList", action = "http://rsdn.ru/Janus/GetForumList")
    @WebResult(name = "GetForumListResult", targetNamespace = "http://rsdn.ru/Janus/")
    @RequestWrapper(localName = "GetForumList", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetForumList")
    @ResponseWrapper(localName = "GetForumListResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetForumListResponse")
    public ForumResponse getForumList(
        @WebParam(name = "forumRequest", targetNamespace = "http://rsdn.ru/Janus/")
        ForumRequest forumRequest);

    /**
     * Получение новых пользователей с момента последней синхронизации
     *
     * @param userRequest
     * @return
     *     returns ru.rsdn.janus.UserResponse
     */
    @WebMethod(operationName = "GetNewUsers", action = "http://rsdn.ru/Janus/GetNewUsers")
    @WebResult(name = "GetNewUsersResult", targetNamespace = "http://rsdn.ru/Janus/")
    @RequestWrapper(localName = "GetNewUsers", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetNewUsers")
    @ResponseWrapper(localName = "GetNewUsersResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetNewUsersResponse")
    public UserResponse getNewUsers(
        @WebParam(name = "userRequest", targetNamespace = "http://rsdn.ru/Janus/")
        UserRequest userRequest);

    /**
     * Получение пользователей по списку их идентификаторов
     *
     * @param request
     * @return
     *     returns ru.rsdn.janus.UserResponse
     */
    @WebMethod(operationName = "GetUserByIds", action = "http://rsdn.ru/Janus/GetUserByIds")
    @WebResult(name = "GetUserByIdsResult", targetNamespace = "http://rsdn.ru/Janus/")
    @RequestWrapper(localName = "GetUserByIds", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetUserByIds")
    @ResponseWrapper(localName = "GetUserByIdsResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.GetUserByIdsResponse")
    public UserResponse getUserByIds(
        @WebParam(name = "request", targetNamespace = "http://rsdn.ru/Janus/")
        UserByIdsRequest request);

    /**
     * Добавление изменений в форумы
     *
     * @param postRequest
     */
    @WebMethod(operationName = "PostChange", action = "http://rsdn.ru/Janus/PostChange")
    @RequestWrapper(localName = "PostChange", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.PostChange")
    @ResponseWrapper(localName = "PostChangeResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.PostChangeResponse")
    public void postChange(
        @WebParam(name = "postRequest", targetNamespace = "http://rsdn.ru/Janus/")
        PostRequest postRequest);

    /**
     * Подтверждение добавления изменений в форумы
     *
     * @return
     *     returns ru.rsdn.janus.PostResponse
     */
    @WebMethod(operationName = "PostChangeCommit", action = "http://rsdn.ru/Janus/PostChangeCommit")
    @WebResult(name = "PostChangeCommitResult", targetNamespace = "http://rsdn.ru/Janus/")
    @RequestWrapper(localName = "PostChangeCommit", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.PostChangeCommit")
    @ResponseWrapper(localName = "PostChangeCommitResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.PostChangeCommitResponse")
    public PostResponse postChangeCommit();

    /**
     * Проверка активности
     *
     */
    @WebMethod(operationName = "Check", action = "http://rsdn.ru/Janus/Check")
    @RequestWrapper(localName = "Check", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.Check")
    @ResponseWrapper(localName = "CheckResponse", targetNamespace = "http://rsdn.ru/Janus/", className = "ru.rsdn.janus.CheckResponse")
    public void check();

}
