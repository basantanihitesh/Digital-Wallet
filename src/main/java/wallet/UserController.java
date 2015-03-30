package wallet;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class UserController 

{
	ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Random r = new Random( System.currentTimeMillis() );
	
	List<User> userlist = new ArrayList<User>();
	List<IDCard> idlist = new ArrayList<IDCard>();
	List<WebLogin> weblist = new ArrayList<WebLogin>();
	List<BankAccount> banklist = new ArrayList<BankAccount>();
	
	//1. Create User
	@RequestMapping(value="/api/v1/users",method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public User createuser(@Valid @RequestBody User user)
	{	
		Calendar create = Calendar.getInstance();
		int userid =  (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
		user.setUser_id("u-"+userid);
		user.setCreated_at(dateFormat.format(create.getTime()));
		mongoOperation.save(user); 
		return user;
	}
	
	//2. View User
	@RequestMapping(value="/api/v1/users/{user_id}",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object getuser(@PathVariable String user_id)
	{	
		User user = mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), User.class);
		
	    return user;
	}
	
	//3. Update User
	@RequestMapping(value="/api/v1/users/{user_id}",method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Object updateuser(@Valid @RequestBody User user,@PathVariable String user_id)
	{
		Calendar update = Calendar.getInstance();
	
		User userl = mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), User.class);
		userl.setEmail(user.getEmail());
		userl.setPassword(user.getPassword());
		userl.setUpdated_at(dateFormat.format(update.getTime()));
		userl.setName(user.getName());
		
		mongoOperation.save(userl);
		return mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), User.class);
    
	}
	
	//4. Create ID Card
	@RequestMapping(value="/api/v1/users/{user_id}/idcards",method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Object createid(@Valid @RequestBody IDCard idcard,@PathVariable String user_id)
	{
		
		User userl = mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), User.class);
		if(userl != null)
		{
			int card_id =  (1 + r.nextInt(2) * 10000000 + r.nextInt(10000000));
			idcard.setCard_id("c-"+card_id);
			idcard.setUser_id(user_id);
			mongoOperation.save(idcard);
			return mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), IDCard.class);
		}
		else
			return "User not found";
	
	}
	
	//5. List All ID Cards
	@RequestMapping(value="/api/v1/users/{user_id}/idcards",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<IDCard> getidCards(@PathVariable String user_id)
	{
		
		List<IDCard> idl = mongoOperation.find(new Query(Criteria.where("user_id").is(user_id)),IDCard.class);
		return idl;
		
	}
	
	//6. Delete ID Card
	@RequestMapping(value="/api/v1/users/{user_id}/idcards/{card_id}",method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public Object deleteidCards(@PathVariable String user_id,@PathVariable String card_id)
	{
		
	
		IDCard id1 = mongoOperation.findOne(new Query(Criteria.where("card_id").is(card_id)), IDCard.class);
	
		if(id1 != null)
		{
			mongoOperation.remove(id1);
			return "Delete Sucessfull";
		}
		else
			return "Id Card not found";
			
	
		

		
	}
	
	//7. Create Web Login
	@RequestMapping(value="/api/v1/users/{user_id}/weblogins",method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Object createweblogin(@Valid @RequestBody WebLogin web,@PathVariable String user_id)
	{
	
		User userl = mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), User.class);
		if(userl != null)
		{
			int login_id =  (1 + r.nextInt(2) * 10000000 + r.nextInt(10000000));
			web.setLogin_id("l-"+login_id);
			web.setUser_id(user_id);
			mongoOperation.save(web);
			return mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), WebLogin.class);
		}
		else
			return "User not found";
		
	
	}
	
	//8. List All Web Logins
	@RequestMapping(value="/api/v1/users/{user_id}/weblogins",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<WebLogin> getweblogins(@PathVariable String user_id)
	{
	
		List<WebLogin> wbl = mongoOperation.find(new Query(Criteria.where("user_id").is(user_id)),WebLogin.class);
		return wbl;
		
	}
	
	//9. Delete Web Login
	@RequestMapping(value="/api/v1/users/{user_id}/weblogins/{login_id}",method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public Object deleteweblogins(@PathVariable String user_id,@PathVariable String login_id)
	{
		
	
		
		WebLogin wb = mongoOperation.findOne(new Query(Criteria.where("login_id").is(login_id)), WebLogin.class);
		
		if(wb != null)
		{
			mongoOperation.remove(wb);
			return "Delete Sucessfull";
		}
		else
			return "Web Login not found";

		
	}
	
	//10. Create Bank Account
	@RequestMapping(value="/api/v1/users/{user_id}/bankaccounts",method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Object createbankaccount(@Valid @RequestBody BankAccount bank,@PathVariable String user_id)
	{
		
		RestTemplate template = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverterList = template.getMessageConverters();
		MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(new MediaType("text", "plain"));
		supportedMediaTypes.add(new MediaType("application", "json"));
		jsonMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		messageConverterList.add(jsonMessageConverter);
		template.setMessageConverters(messageConverterList);
		String rn = bank.getRouting_number();
		String url = "https://routingnumbers.herokuapp.com/api/data.json?rn="+rn;
		Router bank_name_api = template.getForObject(url, Router.class);
			
		String custname = bank_name_api.getCustomer_name();
		if(bank_name_api.getCode()==200)
		{
			User userl = mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), User.class);
			if(userl != null)
			{
				int ba_id =  (1 + r.nextInt(2) * 10000000 + r.nextInt(10000000));
				bank.setBa_id("b-"+ba_id);
				bank.setUser_id(user_id);
				bank.setAccount_name(custname);
				mongoOperation.save(bank);
				return mongoOperation.findOne(new Query(Criteria.where("user_id").is(user_id)), BankAccount.class);
			}
			else
			{
				return "User not found";
			}
		}
		else 
		{
			return bank_name_api;
		}
	
	}
	
	//11. List All Web Logins
	@RequestMapping(value="/api/v1/users/{user_id}/bankaccounts",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<BankAccount> getbankaccounts(@PathVariable String user_id)
	{
	
		
		List<BankAccount> bal = mongoOperation.find(new Query(Criteria.where("user_id").is(user_id)), BankAccount.class);
		return bal;
		
		
	}
	
	//12. Delete a web login
	@RequestMapping(value="/api/v1/users/{user_id}/bankaccounts/{ba_id}",method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public Object deletebankaccount(@PathVariable String user_id,@PathVariable String ba_id)
	{
		
		BankAccount ba = mongoOperation.findOne(new Query(Criteria.where("ba_id").is(ba_id)), BankAccount.class);
		
		if(ba != null)
		{
			mongoOperation.remove(ba);
			return "Delete Sucessfull";
		}
		else
			return "Bank Account not found";

		
	}
	
	
}
	
