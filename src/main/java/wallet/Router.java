package wallet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Router
{

int code;
String customer_name;
public String getCustomer_name() {
	return customer_name;
}

public int getCode() {
	return code;
}

public void setCode(int code) {
	this.code = code;
}

}