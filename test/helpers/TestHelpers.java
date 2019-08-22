package helpers;

import play.libs.Json;
import play.mvc.Result;

import java.util.concurrent.Callable;

import static play.test.Helpers.contentAsString;

public class TestHelpers {

    public static <T> T contentAsTypeClass(Result result, Class<T> target) {
        return Json.fromJson(Json.parse(contentAsString(result)), target);
    }

    public static Exception exceptionOf(Callable<?> callable) {
        try {
            callable.call();
            return null;
        } catch (Exception e) {
            return e;
        }
    }
}
