package controllers;

import java.util.Date;
import java.util.List;

import flexjson.JSONSerializer;

import models.Tweet;
import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Timeline extends Controller {

	public static void index(String user) {
	    User usuarioConsultado = null;
	    User usuarioConectado = Security.userConnected();

	    if(user != null) {
	        usuarioConsultado = User.find("byUsername", user).first();
	    }else {
	        usuarioConsultado = Security.userConnected();
	    }

	    //Información
	    long followings = User.count("select count(follow) from User user join user.follows follow where user = ?", usuarioConsultado);
	    long followers = User.count("select count(user) from User user where ? member of user.follows", usuarioConsultado);

	    render(user, usuarioConectado, usuarioConsultado, followings, followers);
	}
	
	
	public static void addTweet(@Valid Tweet tweet) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
		} else {
			tweet.author = Security.userConnected();
			tweet.date = new Date();
			tweet.save();
		}
		index(null);
	}
	
	public static void tweets(String user){
	    List<Object> tweets = Tweet.find("select tweet from Tweet tweet where tweet.author.username = ? order by tweet.date desc", user).fetch();
	    renderJSON(new JSONSerializer().include("msg", "date", "author.username").exclude("*").serialize(tweets));
	}

	public static void timeline(){
	    List<Object> tweets = Tweet.find("select tweet from Tweet tweet, User user where user = ? and (tweet.author = user or tweet.author member of user.follows) order by tweet.date desc", Security.userConnected()).fetch();
	    renderJSON(new JSONSerializer().include("msg", "date", "author.username").exclude("*").serialize(tweets));
	}
	
	public static void follow(String user){
	    User usuarioConectado = Security.userConnected();
	    User usuarioConsultado = User.find("byUsername", user).first();

	    usuarioConectado.follows.add(usuarioConsultado);
	    usuarioConectado.save();

	    index(null);
	}

	public static void unfollow(String user){
	    User usuarioConectado = Security.userConnected();
	    User usuarioConsultado = User.find("byUsername", user).first();

	    usuarioConectado.follows.remove(usuarioConsultado);
	    usuarioConectado.save();

	    index(null);
	}
	
}
