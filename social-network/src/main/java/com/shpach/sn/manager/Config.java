/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shpach.sn.manager;

import java.util.ResourceBundle;

/**
 *
 * @author KMM
 */
public class Config {

    private static Config instance;
    private ResourceBundle resource;
    public static final String DATASOURCE="DATASOURCE";
    private static final String BUNDLE_NAME = "com.shpach.sn.manager.config";
    public static final String DRIVER = "DRIVER";
    public static final String URL = "URL";
    public static final String MAIN = "MAIN";
    public static final String ERROR = "ERROR";
    public static final String LOGIN = "LOGIN";
    public static final String INDEX = "INDEX";
    public static final String AUTO_SUBMIT_LOGIN="AUTO_SUBMIT_LOGIN";
    public static final String TUTOR_TESTS="TUTOR_TESTS";
    public static final String TUTOR_CATEGORIES="TUTOR_CATEGORIES";
    public static final String TUTOR_COMMUNITIES="TUTOR_COMMUNITIES";
    public static final String TUTOR_QUESTIONS="TUTOR_QUESTIONS";
    public static final String REGISTRATION="REGISTRATION";
	public static final String TAKE_TEST = "TAKE_TEST";
	public static final String STUDENT_TESTS = "STUDENT_TESTS";
	public static final String STUDENT_STATISTIC = "STUDENT_STATISTIC";
	public static final String TUTOR_STATISTIC = "TUTOR_STATISTIC";
	public static final String HOME_PAGE = "HOME_PAGE";
	public static final String DEFAULT_AVATAR = "DEFAULT_AVATAR";
	public static final String USER_SETTINGS = "USER_SETTINGS";
	public static final String AVATAR_UPLOAD_DIR = "AVATAR_UPLOAD_DIR";
	public static final String FIND_FRIENDS = "FIND_FRIENDS";
	public static final String FRIENDS = "FRIENDS";
	public static final String TIMELINE = "TIMELINE";
	public static final String NEWS_FEED = "NEWS_FEED";
	public static final String ADMIN_PERMITIONS = "ADMIN_PERMITIONS";

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
            instance.resource = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String) resource.getObject(key);
    }
}
