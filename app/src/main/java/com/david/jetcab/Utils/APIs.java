package com.david.jetcab.Utils;

/**
 * Created by david on 01/12/2017.
 */

public class APIs {
    private static final String MAIN_URL = "http://159.203.133.173:8080/api/";
    public static final String SIGN_IN_API = MAIN_URL + "users/signin";
    public static final String SIGN_UP_API = MAIN_URL + "users/signup";
    public static final String UPDATE_CITES_API = MAIN_URL + "users/setFavoriteCities";
    public static final String UPDATE_PROFILE_API = MAIN_URL + "users/updateProfile";

    public static final String SEARCH_CITY_API = MAIN_URL + "city/searchCities";
    public static final String PLANES_LIST_API = MAIN_URL + "plane/get";
    public static final String FLIGHTS_LIST_API = MAIN_URL + "flight/getAvailableFlights";
    public static final String CREATE_FLIGHT_LIST_API = MAIN_URL + "flight/createByUser";
    public static final String GET_DEFAULT_PLANE_API = MAIN_URL + "config/getDefaultConfig";
    public static final String GET_MY_FLIGHT_API = MAIN_URL + "flight/getMyFlights";

    public static final String GET_FAQ_API = MAIN_URL + "faq/faqs";
    public static final String REGISTER_FAQ_API = MAIN_URL + "faq/register";

    public static final String RESET_PASSWORD_API = MAIN_URL + "reset_password_request";
    public static final String GIVE_REVIEW_API = MAIN_URL + "review/giveReview";
    public static final String OTHER_GET_API = MAIN_URL + "other/get";

    public static final String SAVE_FCM_TOKEN_API = MAIN_URL + "users/saveFCMToken";
}
