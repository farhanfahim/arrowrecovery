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

    //PRICE BASEURL
    public static final String PRICE_BASE_URL = " https://www.quandl.com/api/v3/datasets/";

    // STAGING
//    public static final String BASE_URL = "http://arrow-recovery.demo.servstaging.com/";
//    public static final String IMAGE_BASE_URL = "http://arrow-recovery.demo.servstaging.com/api/resize/";

    //LIVE
   /* public static final String BASE_URL = "http://54.70.47.244/";
    public static final String IMAGE_BASE_URL = "http://54.70.47.244/api/resize/";*/

    //LIVE CLIENT
    public static final String BASE_URL = "http://arrowcatapp.com/";
    public static final String IMAGE_BASE_URL = "http://arrowcatapp.com/";

    // DEV

    // LOCAL MACHINE
   /* public static final String BASE_URL = "http://arrow-recovery.apps.fomarkmedia.com/";
    public static final String IMAGE_BASE_URL = "http://arrow-recovery.apps.fomarkmedia.com/api/resize/";
*/

    /**
     * API PATHS NAMES
     */

    public static final String PATH_REGISTER = "register";
    public static final String PATH_LOGIN = "login";
    public static final String PATH_GET_PRODUCT = "products";
    public static final String PATH_LOGOUT = "logout";
    public static final String PATH_GET_REFRESH = "refresh";
    public static final String PATH_SLOTS = "get-slots";
    public static final String PATH_PROFILE = "profile";
    public static final String PATH_FORGET_PASSWORD = "forget-password";
    public static final String PATH_VERIFY_RESET_CODE = "verify-reset-code";
    public static final String PATH_RESET_PASSWORD = "reset-password";
    public static final String PATH_PAGES = "pages";
    public static final String PATH_ME = "me";
    public static final String PATH_SOCIAL_LOGIN = "social_login";
    public static final String PATH_CHANGE_PASSWORD = "change-password";
    public static final String PATH_RESENDOTP = "resend-otp";
    public static final String PATH_VERIFYOTP = "verify-otp";
    public static final String PATH_ORDERPRODUCTS = "order-products";
    public static final String PATH_ORDERS = "orders";
    public static final String PATH_RHOD = "RHOD/data.json/";
    public static final String PATH_PLAT = "PLAT/data.json/";
    public static final String PATH_PALL = "PALL/data.json/";
    public static final String PATH_COLLECTIONCENTER = "collection-centers";
    public static final String PATH_NOTIFICATIONS = "notifications";
    public static final String PATH_USER_SLASH = "users/";
    public static final String PATH_GETAVAILABILITY = "check-availabilty";
    public static final String PATH_NOTIFICATIONS_SLASH = "notifications/";


    /**
     * QUERY PARAMS
     */

    public static final String Q_PARAM_LIMIT = "limit";
    public static final String Q_PARAM_OFFSET = "offset";
    public static final String Q_PARAM_STATUS = "status";
    public static final String Q_PARAM_EMAIL = "email";
    public static final String Q_PARAM_STATES = "states";
    public static final String Q_PARAM_COUNTRY = "countries";
    public static final String Q_PARAM_COUNTRY_ID = "country_id";
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
    public static final String Q_APIKEY = "api_key";
    public static final String Q_STARTDATE = "start_date";
    public static final String Q_ENDDATE = "end_date";
    public static final String Q_LAT = "latitude";
    public static final String Q_LONG = "longitude";
    public static final String Q_COLLECTION_ID = "collection_center_id";
    public static final String Q_DATE = "date";
    public static final String Q_WITH_ORDER_PRODUCTS = "with_order_products";
    public static final String Q_SORTED = "sortedBy";
    public static final String Q_ORDER_BY = "orderBy";


    /**
     * STATUS
     */

    public static final int PARAMS_TOKEN_EXPIRE = 401;
    public static final int PARAMS_TOKEN_BLACKLIST = 402;


    public static final String KEY_PRICE = "r5KZHsWqMZMWHdmHYtzi";
    public static final String KEY_MATERIAL_HISTORY = "material-histories";
    public static final String Q_KEY_TO = "to";
    public static final String Q_ASC = "asc";
    public static final String Q_DESC = "desc";
    public static final String Q_KEY_FROM = "from";
    public static final String Q_ORDER_BY_DATE = "date";
    public static final String Q_ORDER_BY_CREATED_DATE = "created_at";
    public static final String Q_ORDER_BY_NAME = "name";


    /**
     * WSC KEYS
     */

    public static final String WSC_KEY_ATTACHMENT = "attachment[]";


    public static final String API_KEY = "46354312";
    public static final String SECRET = "d3c4046485d6ef92672123f7a9926f2967361d09";


}
