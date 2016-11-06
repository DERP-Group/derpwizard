package com.derpgroup.derpwizard.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.derpgroup.derpwizard.configuration.AccountLinkingDAOConfig;
import com.derpgroup.derpwizard.dao.AccountLinkingDAO;
import com.derpgroup.derpwizard.model.accountlinking.ExternalAccountLink;
import com.derpgroup.derpwizard.model.accountlinking.UserAccount;

public class H2EmbeddedAccountLinkingDAO implements AccountLinkingDAO {

  private static final Logger LOG = LoggerFactory.getLogger(H2EmbeddedAccountLinkingDAO.class);
  
  private Connection conn;
  private JdbcDataSource ds;
  
  public H2EmbeddedAccountLinkingDAO(AccountLinkingDAOConfig config){
    if(config == null || config.getProperties() == null){
      throw new RuntimeException("Could not initialize DAO due to missing configuration.");
    }
    Map<String,Object> daoConfiguration = config.getProperties();
    if(daoConfiguration.get("url") == null || daoConfiguration.get("user") == null || daoConfiguration.get("password") == null ){
      throw new RuntimeException("Could not initialize DAO due to missing property.");
    }
    ds = new JdbcDataSource();
    ds.setURL(String.valueOf(daoConfiguration.get("url")));
    ds.setUser(String.valueOf(daoConfiguration.get("user")));
    ds.setPassword(String.valueOf(daoConfiguration.get("password")));
    try {
      init();
    } catch (SQLException e) {
      LOG.error(e.getMessage());
    }
  }
  
  protected void init() throws SQLException {
    try {
      conn = ds.getConnection();
      setupFixtureData();
    } catch (SQLException e) {
      LOG.error(e.getMessage());
      throw e;
    }
  }
  
  public void shutdown() throws SQLException{
    try {
      conn.close();
    } catch (SQLException e) {
      LOG.error(e.getMessage());
      throw e;
    }
  }
  protected ResultSet executeStatement(String sql){
    return executeStatement(sql, null);
  }
  
  protected ResultSet executeStatement(String sql, ArrayList<? extends Object> parameters){

    CachedRowSet crs = null;
    
    try(
        PreparedStatement unpreparedStatement = conn.prepareStatement(sql);
        PreparedStatement statement = prepStatement(unpreparedStatement, parameters);
        ResultSet rs = statement.getResultSet();
        ){
      boolean resultSetRows = statement.execute();
      if(resultSetRows){
        crs = RowSetProvider.newFactory().createCachedRowSet();
        crs.populate(rs);
        return crs;
      }
    }catch(SQLException e){
      LOG.error(e.getMessage());
    }
    return null;
  }
  
  protected ResultSet executeQuery(String sql){
    return executeQuery(sql, null);
  }
  
  protected ResultSet executeQuery(String sql, ArrayList<? extends Object> parameters){

    CachedRowSet crs = null;
    
    try(
        PreparedStatement unpreparedStatement = conn.prepareStatement(sql);
        PreparedStatement statement = prepStatement(unpreparedStatement, parameters);
        ResultSet rs = statement.executeQuery();
        ){
      crs = RowSetProvider.newFactory().createCachedRowSet();
      crs.populate(rs);
      return crs;
    }catch(SQLException e){
      LOG.error(e.getMessage());
    }
    return null;
  }
  
  protected void setupFixtureData() throws SQLException{
    conn.setAutoCommit(false);
    String userTableCreation = "CREATE TABLE User(id varchar(255) PRIMARY KEY NOT NULL,"
        + "firstName varchar(255) NULL,"
        + "dateCreated TIMESTAMP NOT NULL DEFAULT(NOW()));";
    executeStatement(userTableCreation);
    
    String accountLinkTableCreation = "CREATE TABLE AccountLink("
        + "userId varchar(255) NOT NULL,"
        + "externalUserId varchar(255) NULL,"
        + "externalSystemName varchar(64) NOT NULL,"
        + "externalSystemToken varchar(255) NULL,"
        + "externalSystemRefreshToken varchar(255) NULL"
        + ");";
    executeStatement(accountLinkTableCreation);
    
    String linkingTokenTableCreation = "CREATE TABLE LinkingToken("
        + "token UUID NOT NULL DEFAULT(RANDOM_UUID()),"
        + "userId varchar(255) NOT NULL,"
        + "dateCreated TIMESTAMP NOT NULL DEFAULT(NOW())"
        + ");";
    executeStatement(linkingTokenTableCreation);
    
    String authorizationTableCreation = "CREATE TABLE Authorization("
        + "token UUID NOT NULL DEFAULT(RANDOM_UUID()),"
        + "userId varchar(255) NOT NULL,"
        + "dateCreated TIMESTAMP NOT NULL DEFAULT(NOW())"
        + ");";
    executeStatement(authorizationTableCreation);
    conn.commit();
    conn.setAutoCommit(true);
  }
  
  protected Connection getConn(){
    return conn;
  }

  @Override
  public UserAccount getUserByUserId(String alexaUserId) {
    String userSelect = "SELECT id, firstName FROM User WHERE id = ?;";
    ArrayList<String> parameters = new ArrayList<String>();
    parameters.add(alexaUserId);
    ResultSet response = executeQuery(userSelect, parameters);
    
    UserAccount user = new UserAccount();
    try {
      if(!response.first()){
        return null;
      }
      user.setUserId(response.getString("id"));
      user.setFirstName(response.getString("firstName"));
    } catch (SQLException e) {
      LOG.error(e.getMessage());
    }
    return user;
  }

  @Override
  public UserAccount updateUser(UserAccount user) {

    String userCreate = "MERGE INTO User(id, firstName) KEY(id) VALUES(?,?);";
    
    ArrayList<String> parameters = new ArrayList<>();
    parameters.add(user.getUserId());
    parameters.add(user.getFirstName());

    executeStatement(userCreate, parameters);
    
    return getUserByUserId(user.getUserId());
  }

  @Override
  public String generateMappingTokenForUserId(String userId) {

    String linkingTokenCreate = "INSERT INTO LinkingToken(userId) VALUES(?);";

    ArrayList<String> parameters = new ArrayList<>();
    parameters.add(userId);
    executeStatement(linkingTokenCreate, parameters);
    
    String linkingTokenRetrieve = "SELECT TOP 1 token FROM LinkingToken WHERE userId = ? ORDER BY dateCreated DESC";
    try {
      ArrayList<String> retrievalParameters = new ArrayList<String>();
      retrievalParameters.add(userId);
      ResultSet response = executeQuery(linkingTokenRetrieve, retrievalParameters);
      if(!response.next()){
        return null;
      }
      return response.getString("token");
    } catch (SQLException e) {
      LOG.error(e.getMessage());
    }
    return null;
  }

  @Override
  public String getUserIdByMappingToken(String token) {

    String linkingTokenRetrieve = "SELECT TOP 1 userId FROM LinkingToken WHERE token = ? ORDER BY dateCreated DESC";
    try {
      ArrayList<String> parameters = new ArrayList<String>();
      parameters.add(token);
      ResultSet response = executeQuery(linkingTokenRetrieve, parameters);
      if(response == null || !response.next()){
        return null;
      }
      return response.getString("userId");
    } catch (SQLException e) {
      LOG.error(e.getMessage());
    }
    return null;
  }

  @Override
  public void expireMappingToken(String token) {
    String linkingTokenDelete = "DELETE FROM LinkingToken WHERE token = ?;";

      ArrayList<String> parameters = new ArrayList<String>();
      parameters.add(token);
      executeStatement(linkingTokenDelete, parameters);
  }

  @Override
  public String generateAuthToken(String userId) {

    String accessTokenCreate = "INSERT INTO Authorization(userId) VALUES(?);";

    ArrayList<String> parameters = new ArrayList<String>();
    parameters.add(userId);
    executeStatement(accessTokenCreate, parameters);

    String accessTokenRetrieve = "SELECT TOP 1 token FROM Authorization WHERE userId = ? ORDER BY dateCreated DESC";
    try {
      ArrayList<String> retrievalParameters = new ArrayList<String>();
      retrievalParameters.add(userId);
      ResultSet response = executeQuery(accessTokenRetrieve, retrievalParameters);
      response.first();
      return response.getString("token");
    } catch (SQLException e) {
      LOG.error(e.getMessage());
    }
    return null;
  }

  @Override
  public String getUserIdByAuthToken(String token) {

    String accessTokenRetrieve = "SELECT TOP 1 userId FROM Authorization WHERE token = ? ORDER BY dateCreated DESC";
    try {
      ArrayList<String> parameters = new ArrayList<String>();
      parameters.add(token);
      ResultSet response = executeQuery(accessTokenRetrieve, parameters);
      if(!response.next()){
        return null;
      }
      return response.getString("userId");
    } catch (SQLException e) {
      LOG.error(e.getMessage());
    }
    return null;
  }

  @Override
  public void expireGrantedToken(String token) {
    String accessTokenDelete = "DELETE FROM Authorization WHERE token = ?;";

    ArrayList<String> parameters = new ArrayList<String>();
    parameters.add(token);
    executeStatement(accessTokenDelete, parameters);
  }
  
  protected PreparedStatement prepStatement(PreparedStatement statement, ArrayList<? extends Object> parameters) throws SQLException{
    if(parameters != null){
      for(int i = 0; i < parameters.size(); i++){
        statement.setObject(i + 1, parameters.get(i));
      }
    }
    return statement;
  }
  
  @Override
  public ExternalAccountLink createAccountLink(ExternalAccountLink link){
    ArrayList<Object> parameters = new ArrayList<Object>();
    parameters.add(link.getUserId());
    parameters.add(link.getExternalUserId());
    parameters.add(link.getExternalSystemName());
    parameters.add(link.getAuthToken());
    parameters.add(link.getRefreshToken());
    String createAccountLink = "MERGE INTO AccountLink(userId, externalUserId, externalSystemName, externalSystemToken, externalSystemRefreshToken)"
        + " KEY(userId, externalUserId, externalSystemName) VALUES(?,?,?,?,?)";
    //Should externalSystemToken be a different table with a FK relationship to this one?

    executeStatement(createAccountLink, parameters);
    
    return getAccountLinkByUserIdAndExternalSystemName(link.getUserId(), link.getExternalSystemName());
  }
  
  @Override
  public ExternalAccountLink getAccountLinkByUserIdAndExternalSystemName(String userId, String externalSystemName){
    ArrayList<Object> parameters = new ArrayList<Object>();
    parameters.add(userId);
    parameters.add(externalSystemName);
    String createAccountLink = "SELECT userId, externalUserId, externalSystemName, externalSystemToken, externalSystemRefreshToken"
        + " FROM AccountLink"
        + " WHERE userId = ?"
        + " AND externalSystemName = ?";

    ResultSet response;
    response = executeQuery(createAccountLink, parameters);
    try{
      if(!response.next()){
        return null;
      }
      
      return buildAccountLink(response);
    }catch(SQLException e){
      LOG.error(e.getMessage());
      return null;
    }
  }
  
  @Override
  public ExternalAccountLink getAccountLinkByExternalUserIdAndExternalSystemName(String externalUserId, String externalSystemName){
    ArrayList<Object> parameters = new ArrayList<Object>();
    parameters.add(externalUserId);
    parameters.add(externalSystemName);
    String createAccountLink = "SELECT userId, externalUserId, externalSystemName, externalSystemToken, externalSystemRefreshToken"
        + " FROM AccountLink"
        + " WHERE externalUserId = ?"
        + " AND externalSystemName = ?";

    ResultSet response = executeQuery(createAccountLink, parameters);
    if(response == null){
      LOG.error("Request to get account link by id returned a null response.");
      return null;
    }
    try{
      if(!response.next()){
        return null;
      }
      
      return buildAccountLink(response);
    }catch(SQLException e){
      LOG.error(e.getMessage());
      return null;
    }
  }
  
  @Override
  public List<ExternalAccountLink> getAccountLinksByUserId(String userId){
    ArrayList<Object> parameters = new ArrayList<Object>();
    parameters.add(userId);
    String createAccountLink = "SELECT userId, externalUserId, externalSystemName, externalSystemToken, externalSystemRefreshToken"
        + " FROM AccountLink"
        + " WHERE userId = ?";


    ResultSet response = executeQuery(createAccountLink, parameters);
    if(response == null){
      LOG.error("Request to get account links returned a null response.");
      return null;
    }
    List<ExternalAccountLink> externalAccountLinks = new LinkedList<ExternalAccountLink>();
    try{
      while(response.next()){
        externalAccountLinks.add(buildAccountLink(response));
      }
    }catch(SQLException e){
      LOG.error(e.getMessage());
      return null;
    }
    
    if(externalAccountLinks.size() < 1){
      return null;
    }
    return externalAccountLinks;
  }
  
  public ExternalAccountLink buildAccountLink(ResultSet rs) throws SQLException{
    ExternalAccountLink link = new ExternalAccountLink();
    link.setUserId(rs.getString("userId"));
    link.setExternalUserId(rs.getString("externalUserId"));
    link.setExternalSystemName(rs.getString("externalSystemName"));
    link.setAuthToken(rs.getString("externalSystemToken"));
    link.setRefreshToken(rs.getString("externalSystemRefreshToken"));
    return link;
  }
}
