package com.tekrevol.arrowrecovery.constatnts;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by khanhamza on 09-Mar-17.
 */


public class WebServiceConstants {


    private static Map<String, String> headers;

    public static Map<String, String> getHeaders(String token) {
        if (headers == null) {
            headers = new HashMap<>();
            headers.put("_token", token);
        }
        return headers;
    }

    /**
     * URLs
     */

    /**
     * BEFORE LIVE DEPLOYMENT
     * Change URL Live/ UAT
     * Change Image URL
     * Check Version Code
     * BaseApplication Fabric enable
     */

    // STAGING
    public static final String BASE_URL = "http://app-arrow-recovery.servstaging.com/";
    public static final String IMAGE_BASE_URL = "http://app-arrow-recovery.servstaging.com/api/resize/";


    // DEV
//    public static final String BASE_URL = "http://papp.apps.fomarkmedia.com/";
//    public static final String IMAGE_BASE_URL = "http://arrow-recovery.apps.fomarkmedia.com/api/v1/";

    // LOCAL MACHINE
//    public static final String BASE_URL = "http://arrow-recovery.apps.fomarkmedia.com/";
//    public static final String IMAGE_BASE_URL = "http://arrow-recovery.apps.fomarkmedia.com/api/resize/";


    /**
     * API PATHS NAMES
     */

    public static final String PATH_REGISTER = "register";
    public static final String PATH_LOGIN = "login";
    public static final String PATH_GET_DEPARTMENTS = "departments";
    public static final String PATH_GET_SPECIALIZATIONS = "specializations";
    public static final String PATH_GET_USERS = "users";
    public static final String PATH_GET_USERS_SLASH = "users/";
    public static final String PATH_GET_PRODUCT = "products";
    public static final String PATH_GET_REFRESH = "refresh";
    public static final String PATH_SLOTS = "get-slots";
    public static final String PATH_PROFILE = "profile";
    public static final String PATH_GIFTS = "gifts";
    public static final String PATH_REDEEM_POINTS = "redeem-points";
    public static final String PATH_TASKS = "tasks";
    public static final String PATH_ACCEPT_TASK = "task-users";
    public static final String PATH_COMPLETE_TASK = "upload-completed-task";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_ADD_DEPENDENT = "addDependant";
    public static final String PATH_CANCEL_TASK = "cancel-task";
    public static final String PATH_CHANGE_DEPENDENT_PASSWORD = "change-dependant-password";
    public static final String PATH_SESSIONS = "sessions";
    public static final String PATH_ACCEPT_SESSION = "accept-session-request/";
    public static final String PATH_DECLINE_SESSION = "decline-session-request/";
    public static final String PATH_COMPLETE_SESSION = "complete-session/";
    public static final String PATH_START_SESSION = "start-session/";
    public static final String PATH_FORGET_PASSWORD = "forget-password";
    public static final String PATH_VERIFY_RESET_CODE = "verify-reset-code";
    public static final String PATH_RESET_PASSWORD = "reset-password";
    public static final String PATH_PAGES = "pages";
    public static final String PATH_SOCIAL_LOGIN = "social_login";
    public static final String PATH_VERIFY_COMPLETED_SESSION = "verify-completed-session/";
    public static final String PATH_CHANGE_PASSWORD = "change-password";
    public static final String PATH_RESENDOTP = "resend-otp";
    public static final String PATH_VERIFYOTP = "verify-otp";
    public static final String PATH_ORDERPRODUCTS = "order-products";
    public static final String PATH_ORDERS = "orders";
    public static final String PATH_COLLECTIONCENTER = "collection-centers";
    public static final String PATH_NOTIFICATIONS = "notifications";
    public static final String PATH_GETAVAILABILITY = "check-availabilty";
    public static final String PATH_NOTIFICATIONS_SLASH = "notifications/";


    /**
     * QUERY PARAMS
     */

    public static final String Q_PARAM_ROLE = "role";
    public static final String Q_PARAM_LIMIT = "limit";
    public static final String Q_PARAM_OFFSET = "offset";
    public static final String Q_PARAM_TOP_MENTOR = "top_mentor";
    public static final String Q_PARAM_MY_MENTOR = "my_mentor";
    public static final String Q_PARAM_SEARCH = "search";
    public static final String Q_PARAM_DEPT_ID = "department_id";
    public static final String Q_PARAM_TYPE = "type";
    public static final String Q_PARAM_AVAILABLE = "available";
    public static final String Q_PARAM_STATUS = "status";
    public static final String Q_PARAM_MENTOR_ID = "mentor_id";
    public static final String Q_PARAM_CURRENT_MENTOR = "current_mentor";
    public static final String Q_PARAM_SESSION_HISTORY = "session_history";
    public static final String Q_PARAM_SESSION_FROM = "session_from";
    public static final String Q_PARAM_SESSION_TO = "session_to";
    public static final String Q_PARAM_TYPE_FILTER = "type_filter";
    public static final String Q_PARAM_UPCOMING_SESSION_REQUEST = "upcoming_session_request";
    public static final String Q_PARAM_EMAIL = "email";
    public static final String Q_PARAM_SLUG = "slug";
    public static final String Q_PARAM_STATES = "states";
    public static final String Q_PARAM_VEHICLEMAKE = "vehicle-makes";
    public static final String Q_VEHICLE_MAKES = "vehicle-makes";
    public static final String Q_VEHICLE_MODEL = "vehicle-models";
    public static final String Q_MAKE_ID = "make_id";
    public static final String Q_QUERY = "query";
    public static final String Q_YEAR = "year";
    public static final String Q_SERIAL_NUMBER = "serial_number";
    public static final String Q_MODEL_ID = "model_id";
    public static final String Q_ORDER_ID = "order_id";
    public static final String Q_FEATURED = "is_featured";
    public static final String Q_LAT = "latitude";
    public static final String Q_LONG = "longitude";
    public static final String Q_COLLECTION_ID = "collection_center_id";
    public static final String Q_DATE = "date";
    public static final String Q_WITH_ORDER_PRODUCTS = "with_order_products";


    /**
     * STATUS
     */

    public static final int PARAMS_TOKEN_EXPIRE = 401;
    public static final int PARAMS_TOKEN_BLACKLIST = 402;


    /**
     * WSC KEYS
     */

    public static final String WSC_KEY_ATTACHMENT = "attachment[]";


    public static final String API_KEY = "46354312";
    public static final String SECRET = "d3c4046485d6ef92672123f7a9926f2967361d09";


}
